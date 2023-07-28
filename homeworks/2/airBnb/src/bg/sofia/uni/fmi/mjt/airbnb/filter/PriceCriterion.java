package bg.sofia.uni.fmi.mjt.airbnb.filter;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;

public class PriceCriterion implements Criterion {
    private double min,max;
    public PriceCriterion(double minPrice, double maxPrice){
        min=minPrice;
        max=maxPrice;
    }
    public boolean check(Bookable bookable){
        if (bookable==null)
            return false;
        return max>=bookable.getPricePerNight()&&min<=bookable.getPricePerNight();
    }
}
