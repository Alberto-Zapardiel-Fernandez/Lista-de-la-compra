package com.azf.listadelacompra.FBSync;

import android.content.Context;
import android.widget.Toast;

import com.azf.listadelacompra.R;
import com.azf.listadelacompra.utils.Constantes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FBSync {

    public static DatabaseReference databaseReference;

    public FBSync(){
    }
    public static void insertar(Context context, String nombre, String tipo) {
        Map<String,Object> map = new HashMap<>();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constantes.PATH);
        map.put("nombre",nombre);
        map.put("tipo",tipo);
        String key = databaseReference.push().getKey();
        map.put("key",key);
        databaseReference.child(key).setValue(map).addOnSuccessListener(aVoid -> Toast.makeText(context, context.getString(R.string.insertado), Toast.LENGTH_SHORT).show());
    }
}
