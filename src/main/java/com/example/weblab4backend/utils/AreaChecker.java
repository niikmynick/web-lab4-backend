package com.example.weblab4backend.utils;

public final class AreaChecker {
    public static boolean isHit(double x, double y, double r) {
        if (x >= 0) {
            if (y >= 0) {
                return Math.pow(x, 2) + Math.pow(y, 2) <= Math.pow((r/2), 2);
            } else {
                return (x <= r) && (y >= -r);
            }
        } else {
            if (y >= 0) {
                return (-x + y) <= (r / 2);
            } else {
                return false;
            }
        }
    }

}
