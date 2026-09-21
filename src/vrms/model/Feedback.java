package vrms.model;

import vrms.exception.RentalException;
import vrms.contract.Identifiable;

public class Feedback implements Identifiable{

    private String id;
    private String rentalId;
    private int grade;
    private String comment;

    public Feedback (String id, String rentalId, int grade, String comment) throws RentalException {

        if (id == null || id.isBlank()) {
            
            throw new RentalException("Error : Feedback Id is empty or null");
        }

        if (rentalId == null || rentalId.isEmpty())
            {
            throw new RentalException("Error : Rental Id is empty or null");
        }

        if (grade < 1 || grade > 5){

            throw new IllegalArgumentException("Error : Grade must be between 1 and 5, Here grade = " + grade);
        }

        this.id = id;
        this.rentalId = rentalId;
        this.grade = grade;
        this.comment = comment;
    }
    
    @Override
    public String getId() {
        return this.id;
    }

    public String getRentalId() {
        return this.rentalId;
    }

    public int getGrade() {
        return this.grade;
    }

    public String getComment() {
        return this.comment;
    }

}