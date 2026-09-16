package vrms.contract;

import vrms.model.Vehicle;

public interface PricingPolicy {
    public double calculatePrice(int duration, Vehicle vehicle);
}
