package vrms.model;

public class Car extends Vehicle{

    private int numberOfSeats;

    public Car(String id, String model, double mileage,double baseRate, VehicleStatus status, int numberOfSeats){
        super(id, model, mileage, baseRate, status);
        this.numberOfSeats=numberOfSeats;
    }
    public int getNumberOfSeats(){
        return this.numberOfSeats;
    }

}