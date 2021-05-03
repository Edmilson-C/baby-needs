package com.example.babyneeds;

import android.content.Intent;
import android.os.Bundle;

import com.example.babyneeds.data.DatabaseHandler;
import com.example.babyneeds.model.BabyNeeds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private AlertDialog dialog;
    private AlertDialog.Builder builder;
    private EditText etItem, etQuantity, etColor, etSize;
    private Button butSave;
    private List<BabyNeeds> needsList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        db = new DatabaseHandler(MainActivity.this);

        if(db.getCount() > 0) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }

        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
            }
        });
    }

    private void createPopupDialog() {
        builder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);

        etItem = view.findViewById(R.id.etItem);
        etQuantity = view.findViewById(R.id.etQuantity);
        etColor = view.findViewById(R.id.etColor);
        etSize = view.findViewById(R.id.etSize);
        butSave = view.findViewById(R.id.butSave);

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        butSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etItem.getText().toString().trim().equals("") || etQuantity.getText().toString().trim().equals("") ||
                        etColor.getText().toString().trim().equals("") || etSize.getText().toString().trim().equals(""))
                    Snackbar.make(v, "Preencha o(s) espaço(s) em branco", Snackbar.LENGTH_SHORT).show();
                else {
                    BabyNeeds needs = new BabyNeeds();
                    needs.setItem(etItem.getText().toString().trim());
                    needs.setQuantity(Integer.parseInt(etQuantity.getText().toString().trim()));
                    needs.setColor(etColor.getText().toString().trim());
                    needs.setSize(Integer.parseInt(etSize.getText().toString().trim()));

                    db.addNeed(needs);
                    Snackbar.make(v, "Dados adicionados com sucesso!", Snackbar.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            startActivity(new Intent(MainActivity.this, ListActivity.class));
                        }
                    }, 1200);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
