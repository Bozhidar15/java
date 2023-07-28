package bg.sofia.uni.fmi.mjt.airbnb;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.filter.Criterion;


public class Airbnb implements AirbnbAPI {
    private final Bookable[] array;

    public Airbnb(Bookable[] accommodations) {
        array = accommodations.clone();//only = ?
    }

    public Bookable findAccommodationById(String id) {
        if (id == null)
            return null;
        for (Bookable bookable : array) {
            if (id.equalsIgnoreCase(bookable.getId())) {
                return bookable;
            }
        }
        return null;
    }

    public double estimateTotalRevenue() {
        double sum = 0;
        for (Bookable bookable : array) {
            if (bookable.isBooked())
                sum += bookable.getTotalPriceOfStay();
        }
        return sum;
    }

    public long countBookings() {
        long counter = 0;
        for (Bookable bookable : array) {
            if (bookable.isBooked()) {
                counter++;
            }
        }
        return counter;
    }

    public Bookable[] filterAccommodations(Criterion... criteria) {
        boolean len = false;
        if (criteria.length == 0)
            return array;
        if (criteria.length == 2)
            len = true;
//        for (Criterion value : criteria) {
//            if (!(value instanceof LocationCriterion || value instanceof PriceCriterion)) {
//                return new Bookable[0];
//            }
//        }
        int CounterOfCriteria = 0, z = 0, p = 0;
        for (Bookable bookable : array) {
            if (criteria[p].check(bookable)) {
                if (!len) {
                    CounterOfCriteria++;
                } else if (criteria[1].check(bookable)) {
                    CounterOfCriteria++;
                }
            }
        }
        if (CounterOfCriteria == 0)
            return new Bookable[CounterOfCriteria];
        Bookable[] newArray = new Bookable[CounterOfCriteria];
        for (Bookable bookable : array) {
            if (len) {
                if (criteria[0].check(bookable) && criteria[1].check(bookable)) {
                    newArray[z++] = bookable;
                }
            } else if (criteria[0].check(bookable)) {
                newArray[z++] = bookable;
            }

        }
        return newArray;
    }
}
