package vrms.model;

public class Bike extends Vehicle{

    private int engineCapacity;

    public Bike(String id, String model, double mileage,double baseRate, VehicleStatus status, int engineCapacity){
        super(id, model, mileage, baseRate, status);
        this.engineCapacity=engineCapacity;
    }
    public int getEngineCapacity(){
        return this.engineCapacity;
    }

}