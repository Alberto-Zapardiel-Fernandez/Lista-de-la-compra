package com.azf.listadelacompra.vista;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azf.listadelacompra.FBSync.FBSync;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Item> items = new ArrayList<>();
    private EditText edtNombre, edtTipo;
    private Button btnInsertar;
    private ItemAdapter mAdapter;
    private Context mContext;
    private RecyclerView recyclerView;
    private TextView txtNada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initViews();
        guardarItems();
        if (items.size() == 0) {
            txtNada.setVisibility(View.VISIBLE);
        } else {
            txtNada.setVisibility(View.GONE);
        }
    }

    private void initRecycler(ArrayList<Item> items) {
        if (items.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            txtNada.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            txtNada.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mAdapter = new ItemAdapter(items, R.layout.fila_view);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setOnClickListener(position -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(R.string.quieres_borrar)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> removeItem(position))
                        .setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
            });
        }
    }

    private void removeItem(int position) {
        Item item = items.get(position);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Utils.PATH);
        ref.child(item.getKey()).removeValue().addOnSuccessListener(aVoid -> {
            items.remove(position);
            if (items.size() > 0) {
                guardarItems();
            } else {
                showRecycler();
            }
            Toast.makeText(mContext, R.string.borrado, Toast.LENGTH_SHORT).show();
        });
    }

    private void initViews() {
        edtNombre = findViewById(R.id.edtNombre);
        edtTipo = findViewById(R.id.edtTipo);
        recyclerView = findViewById(R.id.recycler);
        txtNada = findViewById(R.id.txtNada);
        btnInsertar = findViewById(R.id.btnInsertar);
        btnInsertar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnInsertar.getId()) {
            String nombre = edtNombre.getText().toString();
            String tipo = edtTipo.getText().toString();
            if (nombre.length() > 0 && tipo.length() > 0) {
                FBSync.insertar(mContext, nombre, tipo);
                edtTipo.setText("");
                edtNombre.setText("");
                edtNombre.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtTipo.getWindowToken(), 0);
            } else {
                Toast.makeText(this, getString(R.string.campos_vacios), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void guardarItems() {
        clearData();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Utils.PATH);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        addItem(dataSnapshot);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void addItem(DataSnapshot dataSnapshot) {
        String nombre = dataSnapshot.child(Utils.NOMBRE).getValue().toString();
        String tipo = dataSnapshot.child(Utils.TIPO).getValue().toString();
        String key = dataSnapshot.getKey();
        items.add(new Item(nombre, tipo, key));
        showRecycler();
    }

    private void showRecycler() {
        initRecycler(items);
        if (items.size() == 0) {
            txtNada.setVisibility(View.VISIBLE);
        } else {
            txtNada.setVisibility(View.GONE);
        }
    }


    private void clearData() {
        if (items.size() > 0 && mAdapter != null) {
            items.clear();
            mAdapter.notifyDataSetChanged();
        }
    }
}