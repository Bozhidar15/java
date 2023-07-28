package bg.sofia.uni.fmi.mjt.flightscanner;

import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.flight.Flight;

import java.util.*;

public class FlightScanner implements FlightScannerAPI {

    List<Flight> flights = new ArrayList<>();
    List<Airport> airportList = new ArrayList<>();

    private ArrayList<Flight> onlyOneAirport(Airport airport) {
        ArrayList<Flight> copy = new ArrayList<>(flights); //list.ofCopy
        for (int i = 0; i < copy.size(); i++) {
            if (!copy.get(i).getFrom().equals(airport)) {
                copy.remove(i);
            }
        }
        return copy;
    }

    private HashMap<Airport, HashSet<Flight>> mapOfFlights() {
        HashMap<Airport, HashSet<Flight>> map = new HashMap<>();
        for (Flight flight : flights) {
            if (!airportList.contains(flight.getFrom())) {
                airportList.add(flight.getFrom());
            }
            if (!airportList.contains(flight.getTo())) {
                airportList.add(flight.getTo());
            }
        }
        for (Airport airport : airportList) {
            HashSet<Flight> setOfCurrentAirport = new HashSet<>();

            for (Flight flight : flights) {
                if (flight.getFrom().equals(airport)) {
                    setOfCurrentAirport.add(flight);
                }
            }
            map.put(airport, setOfCurrentAirport);
        }
        return map;
    }

    @Override
    public void add(Flight flight) {
        if (flight == null) {
            throw new IllegalArgumentException("flight is null");
        } else if (!flights.contains(flight)) {
            flights.add(flight);
        }
    }

    @Override
    public void addAll(Collection<Flight> flights) {
        List<Flight> temp = new ArrayList<>(flights);
        for (Flight flight : temp) {
            add(flight);
        }
    }

    @Override
    public List<Flight> searchFlights(Airport from, Airport to) {
        if (from == null || to == null || from.equals(to)) {
            throw new IllegalArgumentException("from or to are is invalid");
        }
        
        LinkedList<Airport> listForSearch = new LinkedList<>();
        Map<Airport, HashSet<Flight>> mapOfAllFlights = mapOfFlights();
        Set<Airport> visited = new HashSet<>();
        Map<Airport, Flight> travel = new HashMap<>();
        visited.add(from);
        listForSearch.add(from);

        listForSearch.push(from);
        visited.add(from);
        travel.put(from, null);
        while (!listForSearch.isEmpty()) {
            Airport airport = listForSearch.pop();
            for (Flight flight : mapOfAllFlights.get(airport)) {
                if (!visited.contains(flight.getTo())) {
                    visited.add(flight.getTo());
                    listForSearch.push(flight.getTo());
                    travel.put(flight.getTo(), flight);
                }
            }
        }
        if (!visited.contains(to)) {
            return new ArrayList<>();
        } else {
            ArrayList<Flight> shortestTravel = new ArrayList<>();
            for (Airport airport = to; airport != from; airport = travel.get(airport).getFrom())
                shortestTravel.add(travel.get(airport));
            Collections.reverse(shortestTravel);
            return shortestTravel;
        }
    }

    @Override
    public List<Flight> getFlightsSortedByFreeSeats(Airport from) {
        if (from == null) {
            throw new IllegalArgumentException("from is null");
        }
        List<Flight> copy = onlyOneAirport(from);
        copy.sort(new CustomComparatorSeats());
        //List<Flight> forSort = Collections.unmodifiableList(copy);
        return Collections.unmodifiableList(copy);
    }

    @Override
    public List<Flight> getFlightsSortedByDestination(Airport from) {
        if (from == null) {
            throw new IllegalArgumentException("from is null");
        }
        List<Flight> copy = onlyOneAirport(from);
        copy.sort(new CustomComparatorTo());
        //List<Flight> forSort = Collections.unmodifiableList(copy);
        return Collections.unmodifiableList(copy);
    }
}
