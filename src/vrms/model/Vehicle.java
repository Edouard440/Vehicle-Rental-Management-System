package vrms.model;

import vrms.contract.Identifiable;
import vrms.exception.RentalException;

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

    public void setMileage(double mileage) throws RentalException {
    if (!Double.isFinite(mileage) || mileage < 0
                || mileage < this.mileage) {
            throw new RentalException(
                "Mileage must be finite, non-negative and cannot decrease"
            );
        }

        this.mileage = mileage;
    }

    public void setStatus(VehicleStatus status) throws RentalException {
        if (status == null) {
            throw new RentalException("Vehicle status is required");
        }

        this.status = status;
    }

    @Override
    public int compareTo(Vehicle other){
        return this.getId().compareTo(other.getId());
    }
}