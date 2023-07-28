package bg.sofia.uni.fmi.mjt.airbnb.accommodation.location;

import static java.lang.Math.sqrt;

public class Location {
    private static double MyX,MyY;
    public Location(double x, double y){
        MyX=x;
        MyY=y;
    }
    public double getDist(Location other) {
        double Px = MyX - other.MyX;
        double Py = MyY - other.MyY;

        return sqrt(Px*Px + Py*Py);
    }

}
