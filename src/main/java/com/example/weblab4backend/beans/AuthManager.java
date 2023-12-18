package com.example.weblab4backend.beans;

import com.example.weblab4backend.dbUtils.DAOFactory;
import com.example.weblab4backend.utils.JWTManager;
import com.example.weblab4backend.utils.PasswordManager;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.LinkedList;

@Path("/auth")
public class AuthManager {

    private LinkedList<User> users = new LinkedList<>();

    public AuthManager() {
        try {
            users = new LinkedList<>(DAOFactory.getInstance().getUserDAO().getAllUsers());
        } catch (SQLException ex) {
            System.err.println("Something went wrong when trying add new result to DB: " + ex);
        }
    }

    public LinkedList<User> getUsers() {
        return users;
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(AuthRequest loginRequest) {

        System.out.println("Logging in user with username: " + loginRequest.getUsername());

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        JsonArrayBuilder builder = Json.createArrayBuilder();

        User foundUser = null;

        try {
            foundUser = DAOFactory.getInstance().getUserDAO().getUserByUsername(username);
        } catch (SQLException ex) {
            System.err.println("Something went wrong when trying to get user by username: " + ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        if (foundUser == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (!PasswordManager.checkPassword(password, foundUser.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        builder.add(Json.createObjectBuilder()
                .add("access", true)
                .add("token", JWTManager.generateToken(foundUser.getId()))
                .add("user_id", String.valueOf(foundUser.getId()))
        );

        return Response.ok(builder.build()).build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(AuthRequest loginRequest) {

        System.out.println("Registering new user with username: " + loginRequest.getUsername());

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        JsonArrayBuilder builder = Json.createArrayBuilder();

        User newUser = new User(username, PasswordManager.hashPassword(password));

        try {
            DAOFactory.getInstance().getUserDAO().addUser(newUser);
        } catch (SQLException ex) {
            System.err.println("Something went wrong when trying add new user to DB: " + ex);
            if (ex.getMessage().contains("ConstraintViolationException")) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        users.add(newUser);

        builder.add(Json.createObjectBuilder()
                .add("access", true)
                .add("token", JWTManager.generateToken(newUser.getId()))
                .add("user_id", newUser.getId())
        );

        return Response.ok(builder.build()).build();
    }
}
