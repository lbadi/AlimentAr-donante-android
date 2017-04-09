package proyectoalimentar.alimentardonanteapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by leonelbadi on 6/4/17.
 */

public class ProductType {

    String name;
    @SerializedName("measurement_unit")
    String measurementUnit;

    public ProductType(String name, String measurementUnit){
        this.name = name;
        this.measurementUnit = measurementUnit;
    }

    public String getName() {
        return name;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    @Override
    public String toString() {
        return name;
    }
}
