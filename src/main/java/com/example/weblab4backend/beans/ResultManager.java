package com.example.weblab4backend.beans;

import com.example.weblab4backend.dbUtils.DAOFactory;
import com.example.weblab4backend.utils.AreaChecker;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.stream.Stream;

@Path("/data")
public class ResultManager {
    private LinkedList<AreaCheckerBean> results = new LinkedList<>();

    public ResultManager() {
        try {
            results = new LinkedList<>(DAOFactory.getInstance().getResultDAO().getAllResults());
        } catch (SQLException ex) {
            System.err.println("Something went wrong when trying add new result to DB: " + ex);
        }
    }

    public LinkedList<AreaCheckerBean> getResults() {
        return results;
    }

    @GET
    @Path("/get")
    public JsonArray getStringResults() {
        System.out.println("getResults");

        JsonArrayBuilder builder = Json.createArrayBuilder();
        results.forEach(result -> builder.add(result.toJsonArray()));
        return builder.build();
    }

    public void setResults(LinkedList<AreaCheckerBean> results) {
        this.results = results;
    }

    @Transactional
    @POST
    @Path("/add")
    public JsonArray addResults(@QueryParam("x") double x, @QueryParam("y") double y, @QueryParam("r") double r) {
        System.out.println("addResults");

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        String requestTime = dateFormat.format(new Date(System.currentTimeMillis()));
        long startTime = System.nanoTime();

        Hit hit = new Hit(x, y, r);

        AreaCheckerBean currentResult = new AreaCheckerBean();

        operateHit(requestTime, startTime, currentResult, hit);

        JsonArrayBuilder builder = Json.createArrayBuilder();
        results.forEach(result -> builder.add(result.toJsonArray()));
        return builder.build();
    }

    private void operateHit(String requestTime, long startTime, AreaCheckerBean currentResult, Hit hit) {
        currentResult.setX( hit.getX() );
        currentResult.setY( hit.getY() );
        currentResult.setR( hit.getR() );

        currentResult.setStatus( AreaChecker.isHit(hit.getX(), hit.getY(), hit.getR()) );

        currentResult.setRequestTime( requestTime );
        currentResult.setScriptTime( System.nanoTime() - startTime );

        System.out.println(currentResult);
        try {
            DAOFactory.getInstance().getResultDAO().addNewResult( currentResult );
        } catch (SQLException ex) {
            System.err.println("Something went wrong when trying add new result to DB: " + ex);
        }

        results.addFirst( currentResult );
    }

    @Transactional
    @DELETE
    @Path("/clear")
    public JsonArray clearResults() {
        System.out.println("clearResults");
        results.clear();
        try {
            DAOFactory.getInstance().getResultDAO().clearResults();
        } catch (SQLException ex) {
            System.err.println("Something went wrong when trying add new result to DB: " + ex);
        }

        JsonArrayBuilder builder = Json.createArrayBuilder();
        results.forEach(result -> builder.add(result.toJsonArray()));
        return builder.build();
    }

}
