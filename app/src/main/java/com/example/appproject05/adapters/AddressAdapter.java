package com.example.appproject05.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appproject05.R;
import com.example.appproject05.models.Address;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private List<Address> addresses;
    private AddressClickListener listener;

    public interface AddressClickListener {
        void onAddressSelected(Address address);
        void onAddressEdit(Address address);
        void onAddressDelete(Address address);
    }

    public AddressAdapter(List<Address> addresses, AddressClickListener listener) {
        this.addresses = addresses;
        this.listener = listener;
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {
        Address address = addresses.get(position);
        holder.bind(address);
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView label, street, neighborhood, cityState, complement;
        Chip chipDefault;
        MaterialButton btnEdit, btnDelete;

        AddressViewHolder(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.address_label);
            street = itemView.findViewById(R.id.address_street);
            neighborhood = itemView.findViewById(R.id.address_neighborhood);
            cityState = itemView.findViewById(R.id.address_city_state);
            complement = itemView.findViewById(R.id.address_complement);
            chipDefault = itemView.findViewById(R.id.chip_default);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        void bind(Address address) {
            label.setText(address.getLabel());
            street.setText(address.getStreet());
            neighborhood.setText(address.getNeighborhood());
            cityState.setText(address.getCityState());
            complement.setText(address.getComplement());
            chipDefault.setVisibility(address.isDefault() ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(v -> listener.onAddressSelected(address));
            btnEdit.setOnClickListener(v -> listener.onAddressEdit(address));
            btnDelete.setOnClickListener(v -> listener.onAddressDelete(address));
        }
    }
}