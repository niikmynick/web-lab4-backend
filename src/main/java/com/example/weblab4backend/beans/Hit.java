package com.example.weblab4backend.beans;


public class Hit implements java.io.Serializable {
    private double x;
    private double y;
    private double r;
    private boolean status;
    private String requestTime;
    private String scriptTime;


    public Hit() {
        this.x = 0;
        this.y = 0;
        this.r = 0;
        this.status = false;
        this.requestTime = "";
        this.scriptTime = "";
    }

    public Hit(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.status = false;
        this.requestTime = "";
        this.scriptTime = "";
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public boolean getStatus() {
        return status;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public String getScriptTime() {
        return scriptTime;
    }


    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setR(double r) {
        this.r = r;
    }

    public void setStatus(boolean status) { this.status = status; };

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public void setScriptTime(String scriptTime) {
        this.scriptTime = scriptTime;
    }

    @Override
    public String toString() {
        return "Hit{" + "x=" + x + ", y=" + y + ", r=" + r +
                ", hit=" + status +
                ", requestTime='" + requestTime + '\'' + ", scriptTime='" + scriptTime + '\'' +
                '}';
    }
}
