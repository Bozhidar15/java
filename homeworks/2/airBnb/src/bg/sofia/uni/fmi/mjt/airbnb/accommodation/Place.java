package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Place implements Bookable {
    public static int VillaCounter = 0, ApartamentsCounter = 0, HotelCounter = 0, AllReserved = 0,AllPrice=0;
    protected double price=0.0;
    protected Location MyLocation=null;
    protected LocalDateTime start, end;
    //protected Period between;
    protected long durationDays;
    protected boolean booked = false;

    @Override
    public String getId() {

        if (this instanceof Villa) {
            //AllReserved++;
            String temp= "VIL-" + VillaCounter;
            VillaCounter++;
            return temp;
        } else if (this instanceof Apartment) {
            String temp= "APA-" + ApartamentsCounter;
            ApartamentsCounter++;
            return temp;
        } else if (this instanceof Hotel) {
            //AllReserved++;
            String temp= "HOT-" + HotelCounter;
            HotelCounter++;
            return temp;
        }
        return null;
    }

    @Override
    public boolean isBooked() {
        return booked;
    }

    @Override
    public boolean book(LocalDateTime checkIn, LocalDateTime checkOut) {

        if (checkOut == null ||checkIn== null || checkIn.isAfter(checkOut) || booked || checkIn.isBefore(LocalDateTime.now()))
            return false;
        durationDays = checkIn.until(checkOut, ChronoUnit.DAYS);
        if (durationDays<1)
            return false;
        else {
            start = checkIn;
            end = checkOut;
            booked = true;
            AllReserved++;
            return true;
        }

    }

    @Override
    public double getTotalPriceOfStay() {
        return durationDays * price;
    }

    @Override
    public double getPricePerNight() {
        return price;
    }

    @Override
    public Location getLocation() {
        return MyLocation;
    }
}
