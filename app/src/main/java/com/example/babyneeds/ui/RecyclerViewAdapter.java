package com.example.babyneeds.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.babyneeds.ListActivity;
import com.example.babyneeds.MainActivity;
import com.example.babyneeds.R;
import com.example.babyneeds.data.DatabaseHandler;
import com.example.babyneeds.model.BabyNeeds;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<BabyNeeds> needsList;

    public RecyclerViewAdapter(Context context, List<BabyNeeds> needsList) {
        this.context = context;
        this.needsList = needsList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        BabyNeeds needs = needsList.get(position);
        holder.tvItem.setText("Item: " + needs.getItem());
        holder.tvQty.setText("Quantity: " + needs.getQuantity());
        holder.tvColor.setText("Color: " + needs.getColor());
        holder.tvSize.setText("Size: " + needs.getSize());
        holder.tvDate.setText("Added date: " + needs.getDate());
    }

    @Override
    public int getItemCount() {
        return needsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvItem, tvQty, tvColor, tvSize, tvDate, tvTitle;
        public EditText etItem, etQty, etColor, etSize;
        public Button butEdit, butDelete, butYes, butNo, butSave;
        public int id;
        public View view;
        public AlertDialog.Builder builder;
        public AlertDialog dialog;
        public DatabaseHandler db;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvItem);
            tvQty = itemView.findViewById(R.id.tvQuantity);
            tvColor = itemView.findViewById(R.id.tvColor);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvDate = itemView.findViewById(R.id.tvDate);
            butEdit = itemView.findViewById(R.id.butEdit);
            butDelete = itemView.findViewById(R.id.butDelete);
            db = new DatabaseHandler(context);

            butEdit.setOnClickListener(this);
            butDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            BabyNeeds needs;
            switch (v.getId()) {
                case R.id.butEdit :
                    needs = needsList.get(getAdapterPosition());
                    createEditPopUp(needs);
                    break;
                case R.id.butDelete:
                    createConfirmPopUp();
                    break;
                case R.id.butYes:
                    needs = needsList.get(getAdapterPosition());
                    deleteItem(needs.getId());
                    dialog.dismiss();
                    break;
                case R.id.butNo:
                    dialog.dismiss();
                    break;
            }
        }

        private void deleteItem(int id) {
            db.deleteNeed(id);
            needsList.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
        }

        private void createConfirmPopUp() {
            view = LayoutInflater.from(context).inflate(R.layout.confirmation_popup, null);
            builder = new AlertDialog.Builder(context);
            builder.setView(view);

            dialog = builder.create();
            dialog.show();

            butNo = view.findViewById(R.id.butNo);
            butYes = view.findViewById(R.id.butYes);

            butNo.setOnClickListener(this);
            butYes.setOnClickListener(this);
        }

        private void createEditPopUp(final BabyNeeds needs) {
            view = LayoutInflater.from(context).inflate(R.layout.popup, null);
            builder = new AlertDialog.Builder(context);
            builder.setView(view);

            dialog = builder.create();
            dialog.show();

            tvTitle = view.findViewById(R.id.tvTitle);
            etItem = view.findViewById(R.id.etItem);
            etQty = view.findViewById(R.id.etQuantity);
            etColor = view.findViewById(R.id.etColor);
            etSize = view.findViewById(R.id.etSize);
            butSave = view.findViewById(R.id.butSave);

            tvTitle.setText("Edit Item");
            etItem.setText(needs.getItem());
            etQty.setText(needs.getQuantity() + "");
            etColor.setText(needs.getColor());
            etSize.setText(needs.getSize() + "");
            butSave.setText("Update");

            butSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(etItem.getText().toString().trim().equals("") || etQty.getText().toString().trim().equals("")
                    || etColor.getText().toString().trim().equals("") || etSize.getText().toString().trim().equals(""))
                        Snackbar.make(v, "Preencha o(s) espaço(s) em branco", Snackbar.LENGTH_SHORT).show();
                    else {
                        needs.setItem(etItem.getText().toString().trim());
                        needs.setQuantity(Integer.parseInt(etQty.getText().toString().trim()));
                        needs.setColor(etColor.getText().toString().trim());
                        needs.setSize(Integer.parseInt(etSize.getText().toString().trim()));

                        db.updateNeeds(needs);
                        Snackbar.make(v,"Dados actualizados com sucesso!", Snackbar.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                notifyItemChanged(getAdapterPosition(), needs);
                            }
                        }, 1200);
                    }
                }
            });
        }
    }
}
