package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class Villa extends Place {
    public Villa(Location location, double pricePerNight){
        if (location==null)
            pricePerNight=0.0;
        MyLocation=location;
        price=pricePerNight;
    }
}
