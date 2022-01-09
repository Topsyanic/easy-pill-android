package com.cb008264.easy_pill_android.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cb008264.easy_pill_android.R;
import com.cb008264.easy_pill_android.model.Medicine;
import java.util.ArrayList;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder>{
        LayoutInflater inflater;
        List<Medicine> medicines;
        MedicineAdapter.MedicineClickListener medicineClickListener;

public MedicineAdapter(Context context, List<Medicine> medicines, MedicineAdapter.MedicineClickListener medicineClickListener)
        {
        this.inflater =  LayoutInflater.from(context);
        this.medicines = medicines;
        this.medicineClickListener = medicineClickListener;
        }



public interface MedicineClickListener
{
    void selectedMedicine(Medicine medicine);

}

    @NonNull
    @Override
    public MedicineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.medicine_list_layout,parent,false);
        return new MedicineAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineAdapter.ViewHolder holder, int position) {
        Medicine medicine = medicines.get(position);
        holder.medicineName.setText(medicines.get(position).getName());
        holder.medicinePrice.setText(medicines.get(position).getPrice());
        holder.medicineQuantity.setText(medicines.get(position).getQuantity());
        holder.arrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medicineClickListener.selectedMedicine(medicine);
            }
        });
    }

    @Override
    public int getItemCount() {
        return  medicines.size();
    }
    public void filterList(ArrayList<Medicine> filteredList){
        medicines = filteredList;
        notifyDataSetChanged();
    }
    public void addItem(Medicine medicine) {
        medicines.add(medicine);
        notifyDataSetChanged();
    }
    public void removeItem(int position) {
        medicines.remove(position);
        notifyDataSetChanged();
    }


public class ViewHolder extends RecyclerView.ViewHolder{
    TextView medicineName,medicinePrice,medicineQuantity;
    ImageButton arrowBtn;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        medicineName = itemView.findViewById(R.id.medicineName);
        medicinePrice = itemView.findViewById(R.id.medicinePrice);
        medicineQuantity = itemView.findViewById(R.id.medicineQuantity);
        arrowBtn = itemView.findViewById(R.id.medicine_btn_arrow);
    }
}
}
