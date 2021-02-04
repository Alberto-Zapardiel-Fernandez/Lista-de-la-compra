package com.azf.listadelacompra.vista;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.azf.listadelacompra.usuario.Usuario;
import com.azf.listadelacompra.utils.Constantes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Item> items = new ArrayList<>();
    private EditText edtNombre;
    private Button btnInsertar;
    private ItemAdapter mAdapter;
    private Context mContext;
    private RecyclerView recyclerView;
    private TextView txtNada;
    private Spinner spinner;
    private static int contador;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = getSharedPreferences(Constantes.DATOS, Context.MODE_PRIVATE);
        if (getIntent().getExtras()!=null){
            Usuario usuario = (Usuario) getIntent().getSerializableExtra(Constantes.USUARIO);
            email = usuario.getEmail().replaceAll("[^a-zA-Z0-9]","");
            SharedPreferences.Editor objEditor = preferences.edit();
            objEditor.putString(Constantes.EMAILPATH, email);
            objEditor.putString(Constantes.NOMBREUSUARIO, usuario.getNombre());
            objEditor.apply();
        }else{
            email = preferences.getString(Constantes.EMAILPATH,null);
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.bienvenido)+""+ preferences.getString(Constantes.NOMBREUSUARIO,Constantes.INVITADO)+"!");
        contador = 0;
        mContext = this;
        initViews();
        guardarItems();
        if (items.size() == 0) {
            txtNada.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtNada.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void removeItem(int position) {
        Item item = items.get(position);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constantes.PATH).child(email);
        ref.child(item.getKey()).removeValue().addOnSuccessListener(aVoid -> {
            items.remove(position);
            guardarItems();
            Toast.makeText(mContext, R.string.borrado, Toast.LENGTH_SHORT).show();
        });
    }

    private void initViews() {
        edtNombre = findViewById(R.id.edtNombre);
        recyclerView = findViewById(R.id.recycler);
        spinner = findViewById(R.id.spinnerTipo);
        txtNada = findViewById(R.id.txtNada);
        btnInsertar = findViewById(R.id.btnInsertar);
        btnInsertar.setOnClickListener(this);
        items = new ArrayList<>();
        iniciarYRellenarSpinner();
    }

    private void iniciarYRellenarSpinner() {
        String[] tipos = new String[]{Constantes.CARNE, Constantes.BEBIDA, Constantes.LACTEOS,
                Constantes.CHARCUTERIA, Constantes.FRUTAVERDURA, Constantes.VARIOS, Constantes.PASTA,
                Constantes.CONGELADO, Constantes.LIMPIEZA};
        List<String> tipos2 = new ArrayList<>(Arrays.asList(tipos));
        Collections.sort(tipos2);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tipos2);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btnInsertar.getId()) {
            String nombre = edtNombre.getText().toString();
            String tipo = spinner.getSelectedItem().toString();
            if (nombre.length() > 0 && tipo.length() > 0) {
                FBSync.insertar(mContext, nombre, tipo, email);
                edtNombre.setText("");
                edtNombre.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtNombre.getWindowToken(), 0);
                startActivity(new Intent(MainActivity.this,MainActivity.class));
            } else {
                Toast.makeText(this, getString(R.string.campos_vacios), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void guardarItems() {
        clearData();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constantes.PATH).child(email);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        addItem(dataSnapshot);
                    }
                }
                initRecycler(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void initRecycler(ArrayList<Item> items) {
        if (items.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            txtNada.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            txtNada.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            Collections.sort(items,Item.ordenarPorTipo);
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

    private void addItem(DataSnapshot dataSnapshot) {
        String nombre = dataSnapshot.child(Constantes.NOMBRE).getValue().toString();
        String tipo = dataSnapshot.child(Constantes.TIPO).getValue().toString();
        String key = dataSnapshot.getKey();
        items.add(new Item(nombre, tipo, key));
    }


    private void clearData() {
        if (items.size() > 0 && mAdapter != null) {
            items.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        if (contador == 0){
            Toast.makeText(mContext, getString(R.string.pulsa_de_nuevo_para_salir), Toast.LENGTH_SHORT).show();
            contador++;
        }else {
            finishAffinity();
        }
    }
}