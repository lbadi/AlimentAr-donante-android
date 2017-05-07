package proyectoalimentar.alimentardonanteapp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.Configuration;
import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.model.Item;
import proyectoalimentar.alimentardonanteapp.model.ProductType;

public class ItemView extends FrameLayout{

    @BindView(R.id.quantity_item)
    public Spinner quantitySpinner;
    @BindView(R.id.category_item)
    public Spinner categorySpinner;
    @BindView(R.id.measure_item)
    public TextView measureItem;
    @BindView(R.id.remove_item)
    public ImageView removeItem;

    private ArrayAdapter<ProductType> productTypeArrayAdapter;
    private ArrayAdapter<Integer> quantityArrayAdapter;


    private Item item;
    private List<ProductType> productTypes = new ArrayList<>();

    public ItemView(Context context) {
        super(context);
        init();
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        AlimentarApp.inject(this);
        View view = inflate(getContext(), R.layout.item, this);
        ButterKnife.bind(this,view);

//        setupProductTypeSpinner();
    }


    public void setupQuantitySpinner(){

        quantityArrayAdapter= new ArrayAdapter<>(
                this.getContext(), android.R.layout.simple_spinner_item, generateIntList(Configuration.MAX_QUANTITY,
                Configuration.MIN_QUANTITY));
        quantityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        quantitySpinner.setAdapter(quantityArrayAdapter);
        quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer quantity = (Integer) parent.getItemAtPosition(position);
                item.setQuantity(quantity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                item.setQuantity(null);
            }
        });

    }

    private void setupProductTypeSpinner(){

        productTypeArrayAdapter = new ArrayAdapter<>(
                this.getContext(), android.R.layout.simple_spinner_item, productTypes);
        productTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(productTypeArrayAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProductType productType = (ProductType) parent.getItemAtPosition(position);
                item.setProductType(productType);
                measureItem.setText(productType.getMeasurementUnit());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                item.setProductType(null);
            }
        });

    }

    /**
     * Add a listener to the remove image.
     * @param onClickListener
     */
    public void setupRemoveItem(OnClickListener onClickListener){
        removeItem.setOnClickListener(onClickListener);
    }

    public List<String> getName(List<ProductType> productTypes){
        List<String> names = new ArrayList<>();
        for(ProductType productType : productTypes){
            names.add(productType.getName());
        }
        return names;
    }

    public void setItem(Item item){
        this.item = item;
        refreshView();
    }

    public Item getItem() {
        return item;
    }

    /**
     * Refresh the item view.
     */
    public void refreshView(){
        if(item == null){
            return;
        }
        //Set quantity spinner to the actual position.
        if(item.getQuantity() == null){
            quantitySpinner.setSelection(0);

        }else{
            quantitySpinner.setSelection(item.getQuantity() - 1);
        }
    }

    public void setProductTypes(List<ProductType> productTypes) {
        this.productTypes = productTypes;
        setupProductTypeSpinner();
        setupQuantitySpinner();
    }

    /**
     * Utlity Method. Generate an List of integers. In asc order.
     * @param maxInt the max int on the list
     * @param minInt the min int on the list.
     * @return a list of int in asc order.
     */
    private List<Integer> generateIntList(int maxInt, int minInt){
        List<Integer> intList = new ArrayList<>();
        for (int i = minInt; i <= maxInt; i++)
        {
            intList.add(i);
        }
        return intList;
    }
}
