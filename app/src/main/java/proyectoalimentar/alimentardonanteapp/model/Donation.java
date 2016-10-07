package proyectoalimentar.alimentardonanteapp.model;


import java.util.Calendar;

public class Donation {
    private State states;
    private String description;
    private Calendar pickupTimeFrom;
    private Calendar pickupTimeTo;


    public Donation(State states, String description, Calendar pickupTimeFrom, Calendar pickupTimeTo){
        this.states = states;
        this.description = description;
        this.pickupTimeFrom = pickupTimeFrom;
        this.pickupTimeTo = pickupTimeTo;
    }


    public State getState() {
        return states;
    }

    public String getDescription() {
        return description;
    }

    public Calendar getPickupTimeFrom() {
        return pickupTimeFrom;
    }

    public Calendar getPickupTimeTo() {
        return pickupTimeTo;
    }
}
