package com.example.babyneeds;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.babyneeds.data.DatabaseHandler;
import com.example.babyneeds.model.BabyNeeds;
import com.example.babyneeds.ui.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private DatabaseHandler db;
    private List<BabyNeeds> needsList;
    private FloatingActionButton fab;
    private EditText etItem, etQty, etSize, etColor;
    private Button butSave;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        db = new DatabaseHandler(ListActivity.this);
        needsList = db.getAllNeeds();
        recyclerViewAdapter = new RecyclerViewAdapter(ListActivity.this, needsList);

        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopDialog();
            }
        });
    }

    private void createPopDialog() {
        builder = new AlertDialog.Builder(ListActivity.this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        etItem = view.findViewById(R.id.etItem);
        etQty = view.findViewById(R.id.etQuantity);
        etColor = view.findViewById(R.id.etColor);
        etSize = view.findViewById(R.id.etSize);
        butSave = view.findViewById(R.id.butSave);

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        butSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etItem.getText().toString().trim().equals("") || etQty.getText().toString().trim().equals("")
                        || etColor.getText().toString().trim().equals("") || etSize.getText().toString().trim().equals(""))
                    Snackbar.make(v, "Preencha o(s) espaço(s) em branco", Snackbar.LENGTH_SHORT).show();
                else {
                    BabyNeeds needs = new BabyNeeds();
                    needs.setItem(etItem.getText().toString().trim());
                    needs.setQuantity(Integer.parseInt(etQty.getText().toString().trim()));
                    needs.setColor(etColor.getText().toString().trim());
                    needs.setSize(Integer.parseInt(etSize.getText().toString().trim()));

                    db.addNeed(needs);
                    Snackbar.make(v, "Dados adicionados com sucesso!", Snackbar.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            startActivity(new Intent(ListActivity.this, ListActivity.class));
                            finish();
                        }
                    }, 1200);

                }
            }
        });
    }
}
