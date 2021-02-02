package com.azf.listadelacompra.vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.azf.listadelacompra.R;
import com.azf.listadelacompra.adapter.ItemAdapter;
import com.azf.listadelacompra.item.Item;
import com.azf.listadelacompra.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Item> movimientos = new ArrayList<>();
    private EditText edtNombre, edtTipo;
    private Button btnInsertar;
    private ItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        guardarItems();
    }

    private void initViews() {
        edtNombre = findViewById(R.id.edtNombre);
        edtTipo = findViewById(R.id.edtTipo);
        btnInsertar = findViewById(R.id.btnInsertar);
        btnInsertar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnInsertar.getId()){
            String nombre = edtNombre.getText().toString();
            String tipo = edtTipo.getText().toString();
            if (nombre.length()>0 && tipo.length()>0){
                insertar(nombre,tipo);
            }
        }
    }

    private void insertar(String nombre, String tipo) {

    }
    private void guardarItems() {
        clearData();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Utils.PATH);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            for (DataSnapshot d : ds.getChildren()) {
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }


    private void clearData() {
        if (movimientos.size() > 0 && mAdapter != null) {
            movimientos.clear();
            mAdapter.notifyDataSetChanged();
        }
    }
}