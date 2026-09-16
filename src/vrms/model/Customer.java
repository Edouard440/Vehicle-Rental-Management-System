package vrms.model;

import vrms.contract.Identifiable;

public class Customer implements Identifiable {

    private String id;
    private String name;
    private String email;

    public Customer(String id, String name, String email){
        this.id=id;
        this.name=name;
        this.email=email;
    }
    @Override 
    public String getId(){
        return this.id;
    }
    public String getEmail(){
        return this.email;
    }
    public String getName(){
        return this.name;
    }
}