package bg.sofia.uni.fmi.mjt.airbnb.filter;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class LocationCriterion implements Criterion {
    private final Location MyLocation;
    private final double Distance;

    public LocationCriterion(Location currentLocation, double maxDistance) {
        MyLocation = currentLocation;
        Distance = maxDistance;
    }

    public boolean check(Bookable bookable) {
        if (MyLocation == null)
            return true;
        if (bookable == null)
            return false;
        return Distance >= MyLocation.getDist(bookable.getLocation());
    }
}
