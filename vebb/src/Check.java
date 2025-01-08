public class Check {
    public Check() {
    }

    public static boolean checkCoordinates(float x, float y, float r) {
        return checkCircle(x, y, r) || checkRectangle(x, y, r) || checkTriangle(x, y, r);
    }

    public static boolean checkTriangle(float x, float y, float r){
        return (y <= -x + r / 2) && (y <= 0) && (x <= 0);
    }

    public static boolean checkRectangle(float x, float y, float r){
        return (x>=0) && (x<= r) && (y >= -r/2) && (y<=0);
    }

    public static boolean checkCircle(float x, float y, float r){
        return (x*x+y*y <= r*r) && (y<=0) && (x>=0);
    }

    public static boolean checkValues(float x, float y) {
        return -3.0 <= x && x <= 3.0 && -5.0 <= y && y <= 3.0;
    }
}