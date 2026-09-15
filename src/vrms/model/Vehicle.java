package vrms.model;

import vrms.contract.Identifiable;

public abstract class Vehicle implements Identifiable {

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
}