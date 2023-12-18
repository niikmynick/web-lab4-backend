package com.example.weblab4backend.beans;

import com.example.weblab4backend.dbUtils.DAOFactory;
import com.example.weblab4backend.utils.AreaChecker;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

@Path("/data")
public class ResultManager {
    private LinkedList<AreaCheckerBean> results = new LinkedList<>();

    public LinkedList<AreaCheckerBean> getResults() {
        return results;
    }

    @POST
    @Path("/get")
    @Produces("application/json")
    public Response getStringResults(@QueryParam("owner_id") int owner_id) {

        System.out.println("Get results for user with id = " + owner_id);

        try {
            results = new LinkedList<>(DAOFactory.getInstance().getResultDAO().getUserResults(owner_id));

            return Response.ok(results).build();

        } catch (SQLException ex) {
            System.err.println("Something went wrong when trying to get result to DB: " + ex);
            return Response.status(503).build();
        }
    }

    public void setResults(LinkedList<AreaCheckerBean> results) {
        this.results = results;
    }

    @Transactional
    @POST
    @Path("/add")
    @Produces("application/json")
    public Response addResults(@QueryParam("x") double x, @QueryParam("y") double y, @QueryParam("r") double r, @QueryParam("owner_id") int owner_id) {

        System.out.println("Add results for user with id = " + owner_id);

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        String requestTime = dateFormat.format(new Date(System.currentTimeMillis()));
        long startTime = System.nanoTime();

        Hit hit = new Hit(x, y, r);

        AreaCheckerBean currentResult = new AreaCheckerBean();

        try {
            results = new LinkedList<>(DAOFactory.getInstance().getResultDAO().getUserResults(owner_id));
            operateHit(requestTime, startTime, currentResult, hit, owner_id);

            return Response.ok(currentResult).build();

        } catch (SQLException ex) {
            System.err.println("Something went wrong when trying to get result to DB: " + ex);
            return Response.status(503).build();
        }
    }

    private void operateHit(String requestTime, long startTime, AreaCheckerBean currentResult, Hit hit, int owner_id) {
        currentResult.setX( hit.getX() );
        currentResult.setY( hit.getY() );
        currentResult.setR( hit.getR() );

        currentResult.setOwnerid(owner_id);

        currentResult.setStatus( AreaChecker.isHit(hit.getX(), hit.getY(), hit.getR()) );

        currentResult.setRequestTime( requestTime );
        currentResult.setScriptTime( System.nanoTime() - startTime );

        System.out.println(currentResult);

        try {
            DAOFactory.getInstance().getResultDAO().addNewResult( currentResult );
            results.addFirst( currentResult );

        } catch (SQLException ex) {
            System.err.println("Something went wrong when trying add new result to DB: " + ex);
        }
    }

//    @Transactional
//    @POST
//    @Path("/clear")
//    public JsonArray clearResults() {
//        System.out.println("clearResults");
//        results.clear();
//        try {
//            DAOFactory.getInstance().getResultDAO().clearResults();
//        } catch (SQLException ex) {
//            System.err.println("Something went wrong when trying to add new result to DB: " + ex);
//        }
//
//        JsonArrayBuilder builder = Json.createArrayBuilder();
//        results.forEach(result -> builder.add(result.toJsonArray()));
//        return builder.build();
//    }

    @Transactional
    @POST
    @Path("/clear")
    public Response clearUserResults(@QueryParam("owner_id") int owner_id) {

        System.out.println("Clear results for user with id = " + owner_id);

        try {
            DAOFactory.getInstance().getResultDAO().clearUserResults(owner_id);
            results.clear();

            return Response.ok().build();

        } catch (SQLException ex) {
            System.err.println("Something went wrong when trying add new result to DB: " + ex);
            return Response.status(503).build();
        }
    }

}
