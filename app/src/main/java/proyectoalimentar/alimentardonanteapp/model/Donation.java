package proyectoalimentar.alimentardonanteapp.model;


import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class Donation {

    Integer id;
    Status status;
    String description;
    @SerializedName("pickup_time_from")
    DateTime pickupTimeFrom;
    @SerializedName("pickup_time_to")
    DateTime pickupTimeTo;
    Donator donator;

    public Donation(Integer id,Status status, String description, DateTime pickupTimeFrom, DateTime pickupTimeTo,
                    Donator donator){
        this.id = id;
        this.status = status;
        this.description = description;
        this.pickupTimeFrom = pickupTimeFrom;
        this.pickupTimeTo = pickupTimeTo;
        this.donator = donator;
    }

    public Integer getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public DateTime getPickupTimeFrom() {
        return pickupTimeFrom;
    }

    public DateTime getPickupTimeTo() {
        return pickupTimeTo;
    }

    public Donator getDonator() {
        return donator;
    }

}
