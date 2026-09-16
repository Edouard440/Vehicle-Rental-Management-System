package vrms.ordering;
import java.util.Comparator;
import vrms.model.Vehicle;

public class VehicleMileageComparator implements Comparator<Vehicle>{
    @Override 
    public int compare(Vehicle first, Vehicle second){
        int resultComparison = Double.compare(first.getMileage(), second.getMileage());
    

    if (resultComparison !=0){
        return resultComparison;
    }

    return first.compareTo(second);
    
    }
}

