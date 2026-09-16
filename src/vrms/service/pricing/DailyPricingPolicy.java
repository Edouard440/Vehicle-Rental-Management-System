package vrms.service.pricing;

import vrms.contract.PricingPolicy;
import vrms.model.Bike;
import vrms.model.Car;
import vrms.model.Truck;
import vrms.model.Vehicle;

public class DailyPricingPolicy implements PricingPolicy{
    @Override 
    public double calculatePrice(int rentalDurationInHours, Vehicle vehicle){
        int days = rentalDurationInHours/24;
        double coefficient =1.0;
        if(vehicle instanceof Car){
            coefficient = 1.2;
        }else if(vehicle instanceof Bike){
            coefficient = 0.7;
        }else if (vehicle instanceof Truck){
            coefficient = 2.0;
        }
        return coefficient * days * vehicle.getBaseRate();
    }

}