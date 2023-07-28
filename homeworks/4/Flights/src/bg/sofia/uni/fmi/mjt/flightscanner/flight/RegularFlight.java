package bg.sofia.uni.fmi.mjt.flightscanner.flight;

import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.exception.FlightCapacityExceededException;
import bg.sofia.uni.fmi.mjt.flightscanner.exception.InvalidFlightException;
import bg.sofia.uni.fmi.mjt.flightscanner.passenger.Passenger;

import java.util.*;

public class RegularFlight implements Flight /*, Comparator<RegularFlight>*/ {
    String flightId;
    Airport from;
    Airport to;
    int capacity;
    int takenSeats = 0;
    List<Passenger> plainPassengers;

    private RegularFlight(String flightId, Airport from, Airport to, int totalCapacity) {
        this.flightId = flightId;
        this.from = from;
        this.capacity = totalCapacity;
        this.to = to;
        plainPassengers = new ArrayList<>();
    }

    public static RegularFlight of(String flightId, Airport from, Airport to, int totalCapacity) {
        if (flightId == null || flightId.isBlank() || flightId.isEmpty()) {
            throw new IllegalArgumentException("flight ID is not correct");
        } else if (from == null || to == null) {
            throw new IllegalArgumentException("departure city or destination is null or departure city " +
                    "equals destination city");
        } else if (from.equals(to)) {
            throw new InvalidFlightException("from and to are equals");
        } else if (totalCapacity < 0) {
            throw new IllegalArgumentException("Capacity of the flight is not correct");
        }
        return new RegularFlight(flightId, from, to, totalCapacity);
    }

    @Override
    public Airport getFrom() {
        return from;
    }

    @Override
    public Airport getTo() {
        return to;
    }

    @Override
    public void addPassenger(Passenger passenger) throws FlightCapacityExceededException {

        if (takenSeats == capacity) {
            throw new FlightCapacityExceededException("There is no available seats");
        } else {
            takenSeats++;
            plainPassengers.add(passenger);
        }

    }

    @Override
    public void addPassengers(Collection<Passenger> passengers) throws FlightCapacityExceededException {
        if (passengers.size() > capacity - takenSeats) {
            throw new FlightCapacityExceededException("the flight cannot accommodate that many passengers");
        } else {
            takenSeats += passengers.size();
            plainPassengers.addAll(passengers);
        }
    }

    @Override
    public Collection<Passenger> getAllPassengers() {
        return Collections.unmodifiableList(plainPassengers);
    }

    @Override
    public int getFreeSeatsCount() {
        return capacity - takenSeats;
    }
}
