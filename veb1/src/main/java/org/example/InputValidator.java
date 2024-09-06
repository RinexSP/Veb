package org.example;

public class InputValidator {

    private int x;
    private int y;
    private int r;

    public boolean validateInput() {
        String xStr = System.getenv("X");
        String yStr = System.getenv("Y");
        String rStr = System.getenv("R");

        if (xStr == null || yStr == null || rStr == null) {
            System.out.println("Не все переменные окружения заданы. " +
                    "Требуются X, Y и R.");
            return false;
        }

        try {
            x = Integer.parseInt(xStr);
            y = Integer.parseInt(yStr);
            r = Integer.parseInt(rStr);

            if (x <= -3 || x >= 3 || y < -3 || y >= 5) {
                System.out.println("Неверные значения для X или Y. " +
                        "X должен быть от -3 до 3 (не включая +-3), " +
                        "Y должен быть от -3 до 5 (не включая -3 и 5).");
                return false;
            }

            if (r != 1 && r != 1.5 && r != 2 && r != 2.5 && r != 3) {
                System.out.println("Неверное значение для R. " +
                        "R должен быть 1, 1.5, 2, 2.5 или 3.");
                return false;
            }

        } catch (NumberFormatException e) {
            System.out.println("Неверный формат для значений переменных окружения.");
            return false;
        }

        System.out.println("Все переменные окружения заданы верно!");
        System.out.println("X: " + x);
        System.out.println("Y: " + y);
        System.out.println("R: " + r);

        return true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getR() {
        return r;
    }
}
