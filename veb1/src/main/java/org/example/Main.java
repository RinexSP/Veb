package org.example;


public class Main {
    public static void main(String[] args) {
        InputValidator validator = new InputValidator();
        if (validator.validateInput()) {
            int x = validator.getX();
            int y = validator.getY();
            int r = validator.getR();

            boolean isInside = PointChecker.checkPointInAnyArea(x, y, r);
            System.out.println("Результат проверки: " + isInside);
        }
    }
}