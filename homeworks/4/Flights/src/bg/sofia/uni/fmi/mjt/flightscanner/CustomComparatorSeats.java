package bg.sofia.uni.fmi.mjt.flightscanner;

import java.util.Comparator;

import bg.sofia.uni.fmi.mjt.flightscanner.flight.RegularFlight;

class CustomComparatorSeats implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        RegularFlight c2 = (RegularFlight) o2;
        RegularFlight c1 = (RegularFlight) o1;
        return c2.getFreeSeatsCount() - c1.getFreeSeatsCount();
    }
}
