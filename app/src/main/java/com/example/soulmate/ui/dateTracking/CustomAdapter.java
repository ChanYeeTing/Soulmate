package com.example.soulmate.ui.dateTracking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.soulmate.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DataModel> dataList;

    // Constructor to initialize context and data list
    public CustomAdapter(Context context, ArrayList<DataModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    // Return the size of your data set
    @Override
    public int getCount() {
        return dataList.size();
    }

    // Return an item from your data set at a specified position
    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    // Return the unique identifier of an item at a specified position
    @Override
    public long getItemId(int position) {
        return position;
    }


    // Create the view for each item in your data set
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder, holder2;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity, parent, false);
            holder = new ViewHolder();


            holder.appointment = convertView.findViewById(R.id.appointment1);
            holder.date = convertView.findViewById(R.id.date1);
            holder.time = convertView.findViewById(R.id.time1);
            holder.hospital = convertView.findViewById(R.id.hospital1);
            holder.detail = convertView.findViewById(R.id.detail1);
            holder.detail.setTextIsSelectable(true);
            holder.cancel= convertView.findViewById(R.id.cancel);



            // Add more TextViews as needed

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DataModel currentItem = (DataModel) getItem(position);
        holder.appointment.setText(currentItem.getText1());
        holder.date.setText(currentItem.getText2());
        holder.time.setText(currentItem.getText3());
        holder.hospital.setText(currentItem.getText4());
        holder.detail.setText(currentItem.getText5());

        holder.cancel.setEnabled(currentItem.getUid()!=null);

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Identify the selected item
                DataModel selectedItem = (DataModel) getItem(position);



                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Cancel Appointment");
                builder.setMessage("Are you sure you want to cancel this appointment?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the item from the ListView
                        dataList.remove(position);
                        notifyDataSetChanged();
                        // Delete the corresponding data from the database
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Activity");
                        databaseReference.child(currentItem.getUid()).child(currentItem.getKey()).removeValue();

                        if(currentItem.getText4()=="-") {
                            DatabaseReference appointment = FirebaseDatabase.getInstance().getReference("Appointment").child(currentItem.getText1());
                            appointment.child(currentItem.getText2()).child(currentItem.getText3()).child(currentItem.getUid()).removeValue();
                        }
                        else if(currentItem.getText1().equals("Clinic and Hospital"))
                        {
                            DatabaseReference appointment = FirebaseDatabase.getInstance().getReference("Appointment").child("ClinicHospital");
                            appointment.child(currentItem.getText4()).child(currentItem.getText2()).child(currentItem.getText3()).child(currentItem.getUid()).removeValue();
                        }

                        else {
                            DatabaseReference appointment = FirebaseDatabase.getInstance().getReference("Appointment").child(currentItem.getText1());
                            appointment.child(currentItem.getText4()).child(currentItem.getText2()).child(currentItem.getText3()).child(currentItem.getUid()).removeValue();
                        }
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
//
            }
        });



        return convertView;
    }


    static class ViewHolder {
        TextView hospital;
        TextView detail;
        TextView appointment;
        TextView date;
        TextView time;
        Button cancel;
        // Add more TextViews as needed
    }
}

