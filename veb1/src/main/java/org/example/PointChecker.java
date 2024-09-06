package org.example;

public class PointChecker {
    public static boolean checkPointInPryamougolnik(int x, int y, int r) {
        if (x >= 0 && y <= 0) {
            if (x <= r && Math.abs(y) <= r / 2) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkPointInTreugolnik(int x, int y, int r) {
        if (x <= 0 && y <= 0) {
            if (x >= -r && y >= -r && x + y >= -r) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkPointInKrug(int x, int y, int r) {
        if (x >= 0 && y >= 0) {
            if (Math.sqrt(x * x + y * y) <= r) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkPointInAnyArea(int x, int y, int r) {
        return checkPointInPryamougolnik(x, y, r)
                || checkPointInTreugolnik(x, y, r)
                || checkPointInKrug(x, y, r);
    }
}