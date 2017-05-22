package proyectoalimentar.alimentardonanteapp.model;


import com.google.gson.annotations.SerializedName;

public class Item {

    private Integer quantity;

    ProductType productType;

    public Integer getQuantity() {
        return quantity;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
