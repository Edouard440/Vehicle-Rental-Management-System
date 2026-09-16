package vrms.model;

import vrms.contract.Identifiable;
import java.lang.Comparable;

public abstract class Vehicle implements Identifiable, Comparable<Vehicle> {

    private String id;
    private String model;
    private double mileage;
    private double baseRate;

    public enum VehicleStatus {
        AVAILABLE,
        RENTED,
        MAINTENANCE
    }

    private VehicleStatus status;

    public Vehicle(String id, String model, double mileage,
                   double baseRate, VehicleStatus status) {
        this.id = id;
        this.model = model;
        this.mileage = mileage;
        this.baseRate = baseRate;
        this.status = status;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public String getModel() {
        return this.model;
    }

    public double getMileage() {
        return this.mileage;
    }

    public double getBaseRate() {
        return this.baseRate;
    }

    public VehicleStatus getStatus() {
        return this.status;
    }

    @Override
    public int compareTo(Vehicle other){
        return this.getId().compareTo(other.getId());
    }
}