package vrms.ordering;
import vrms.model.Vehicle;
import java.util.Comparator;

public class VehicleRateComparator implements Comparator<Vehicle>{
    @Override
    public int compare(Vehicle first, Vehicle second){
        int resultComparison = Double.compare(first.getBaseRate(), second.getBaseRate());
        if (resultComparison!=0){
            return resultComparison;
        }

        return first.compareTo(second);
    }

    
}