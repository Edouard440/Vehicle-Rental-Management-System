package vrms.model;
import vrms.contract.Identifiable;

public abstract class Vehicle implements Identifiable {
    private String ID;
    private String model;
    private double mileage;
    private double baseRate;
    public enum VehicleStatus {
        AVAILABLE,
        RENTED,
        MAINTENANCE
    }// bc we need inenr/nester type
    private VehicleStatus status;

    public Vehicle (String ID, String model, double mileage, double baseRate,VehicleStatus status){
        this.ID=ID;
        this.model=model;
        this.mileage=mileage;
        this.baseRate=baseRate;
        this.status=status;
    }

    @Override 
    public String getID(){
        return this.ID;
    }
    
}

