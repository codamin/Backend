package repository;

import static java.lang.StrictMath.sqrt;

public class Location {
    private int x;
    private int y;

    public Location() {

    }

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getDistance(Location other) {
        return (float) sqrt(Math.pow(other.x - this.x, 2) + Math.pow(other.y - this.y, 2));
    }

    public String StringFormat() {
        String stringFormat = new String("(" + String.valueOf(x) + "," + String.valueOf(y) + ")");
        return stringFormat;
    }
}
