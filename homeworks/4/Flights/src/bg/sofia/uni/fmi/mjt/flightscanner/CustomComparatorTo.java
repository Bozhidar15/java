package bg.sofia.uni.fmi.mjt.flightscanner;

import java.util.Comparator;

import bg.sofia.uni.fmi.mjt.flightscanner.flight.RegularFlight;

public class CustomComparatorTo implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        RegularFlight flight1 = (RegularFlight) o1;
        RegularFlight flight2 = (RegularFlight) o2;
        return flight1.getTo().ID().compareTo(flight2.getTo().ID());
    }
}
