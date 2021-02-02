package com.azf.listadelacompra.FBSync;

import android.content.Context;
import android.widget.Toast;

import com.azf.listadelacompra.R;
import com.azf.listadelacompra.utils.Utils;
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
        databaseReference = FirebaseDatabase.getInstance().getReference();
        map.put("nombre",nombre);
        map.put("tipo",tipo);
        databaseReference.child(Utils.PATH).setValue(map).addOnSuccessListener(aVoid -> Toast.makeText(context, context.getString(R.string.insertado), Toast.LENGTH_SHORT).show());
    }
}
