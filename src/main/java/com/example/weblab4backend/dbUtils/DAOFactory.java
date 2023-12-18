package com.example.weblab4backend.dbUtils;

public class DAOFactory {
    private static CheckAreaDAO resultDAO;
    private static CheckUserDAO userDAO;

    private static DAOFactory instance;

    public static DAOFactory getInstance() {
        if (instance == null)
            instance = new DAOFactory();
        return instance;
    }

    public CheckAreaDAO getResultDAO() {
        if (resultDAO == null)
            resultDAO = new CheckAreaDAOImpl();
        return resultDAO;
    }

    public CheckUserDAO getUserDAO() {
        if (userDAO == null)
            userDAO = new CheckUserDAOImpl();
        return userDAO;
    }
}