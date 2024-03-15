package com.example.hannappqrcodescanner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hannappqrcodescanner.Model.HistoryModel;
import com.example.hannappqrcodescanner.R;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    Context context;
    ArrayList<HistoryModel> list;
    
    public HistoryAdapter(Context context, ArrayList<HistoryModel> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_list_layout, parent, false);
        
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {
        HistoryModel model = list.get(position);

        holder.itemTV.setText(model.getItem());
        holder.amountTV.setText(model.getAmount());
        holder.brandTV.setText(model.getBrand());
        holder.categoryTV.setText(model.getCategory());
        holder.conditionTV.setText(model.getCondition());
        holder.dateTV.setText(model.getDate());
        holder.employeeTV.setText(model.getEmployee());
        holder.propertyTV.setText(model.getProperty());
        holder.serialTV.setText(model.getSerial());
        holder.remarksTV.setText(model.getRemarks());
        holder.scannedDate.setText(model.getScannedDate());
        holder.remarksDateTV.setText(model.getRemarksDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemTV, amountTV, brandTV,
                categoryTV, conditionTV, dateTV,
                employeeTV, propertyTV,serialTV, remarksTV,
                scannedDate, remarksDateTV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemTV = itemView.findViewById(R.id.item_Textview);
            amountTV = itemView.findViewById(R.id.amount_Textview);
            brandTV = itemView.findViewById(R.id.brand_Textview);
            categoryTV = itemView.findViewById(R.id.category_Textview);
            conditionTV = itemView.findViewById(R.id.condition_Textview);
            employeeTV = itemView.findViewById(R.id.employee_Textview);
            propertyTV = itemView.findViewById(R.id.property_Textview);
            serialTV = itemView.findViewById(R.id.serial_Textview);
            dateTV = itemView.findViewById(R.id.date_Textview);
            remarksTV = itemView.findViewById(R.id.remarks_Textview);
            scannedDate = itemView.findViewById(R.id.scanDateAndTime_Textview);
            remarksDateTV = itemView.findViewById(R.id.remarksDate_Textview);
        }
    }
}
