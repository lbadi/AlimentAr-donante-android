package proyectoalimentar.alimentardonanteapp.ui.donations;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.model.Item;
import proyectoalimentar.alimentardonanteapp.model.ProductType;
import proyectoalimentar.alimentardonanteapp.repository.RepoCallBack;
import proyectoalimentar.alimentardonanteapp.repository.UtilRepository;
import proyectoalimentar.alimentardonanteapp.ui.view.ItemView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{


    private final List<Item> items;
    private final List<ProductType> productTypes = new ArrayList<>();

    UtilRepository utilRepository;

    public ItemAdapter(UtilRepository utilRepository){
        items = new LinkedList<>();
        this.utilRepository = utilRepository;
        initProductTypes();
    }

    public void initProductTypes(){
        utilRepository.listProductTypes(new RepoCallBack<List<ProductType>>() {
            @Override
            public void onSuccess(List<ProductType> productTypes) {
                ItemAdapter.this.productTypes.clear();
                ItemAdapter.this.productTypes.addAll(productTypes);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void setItems(List<Item> items){
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(Item item){
        this.items.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(Item item){
        this.items.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fragmet, parent, false);
        return new ItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.populateWithItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item)
        public ItemView itemView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        void populateWithItem(Item item){
            itemView.setItem(item);
            itemView.setProductTypes(productTypes);
            itemView.setupRemoveItem(v -> removeItem(item));
        }
    }
}
