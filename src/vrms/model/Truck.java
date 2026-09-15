package vrms.model;

public class Truck extends Vehicle{

    private double loadCapacity;

    public Truck(String id, String model, double mileage,double baseRate, VehicleStatus status, double loadCapacity){
        super(id, model, mileage, baseRate, status);
        this.loadCapacity=loadCapacity;
    }
    public double getLoadCapacity(){
        return this.loadCapacity;
    }

}