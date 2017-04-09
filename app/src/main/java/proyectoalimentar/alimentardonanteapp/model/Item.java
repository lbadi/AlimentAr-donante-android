package proyectoalimentar.alimentardonanteapp.model;



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
