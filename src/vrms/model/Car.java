package vrms.model;

public class Car extends Vehicle{

    private int numberOfSeats;

    public Car(int numberOfSeats,String id, String model, double mileage,double baseRate, VehicleStatus status){
        super(id, model, mileage, baseRate, status);
        this.numberOfSeats=numberOfSeats;
    }
    public int getNumberOfSeats(){
        return this.numberOfSeats;
    }

}