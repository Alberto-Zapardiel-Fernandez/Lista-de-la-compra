package com.azf.listadelacompra.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.azf.listadelacompra.R;
import com.azf.listadelacompra.item.Item;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final ArrayList<com.azf.listadelacompra.item.Item> Items;
    private final int resource;
    Item Item;
    private OnItemClickListener mListener;

    //Interfaz para asignar los métodos que tendrá
    public interface OnItemClickListener{
        void onDeleteClick(int position);
    }

    public void setOnClickListener(OnItemClickListener listener){mListener = listener;}

    public ItemAdapter(ArrayList<Item> Items, int resource){
        this.Items = Items;
        this.resource = resource;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtNombre2, txtTipo2;
        public View view;
        public ImageButton btnBorrar;
        public LinearLayout linearFondo;

        public ViewHolder(@NonNull View view, final OnItemClickListener listener) {
            super(view);
            this.view = view;
            this.txtNombre2 = view.findViewById(R.id.txtNombre2);
            this.txtTipo2 = view.findViewById(R.id.txtTipo2);
            this.linearFondo = view.findViewById(R.id.linearFondo);
            this.btnBorrar = view.findViewById(R.id.btnBorrar);
            this.btnBorrar. setOnClickListener(view1 -> {
                if (listener != null){
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listener.onDeleteClick(position);
                    }
                }
            });
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new ViewHolder(view,mListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Item = Items.get(position);
        holder.txtNombre2.setText(Item.getNombre());
        holder.txtTipo2.setText(Item.getTipo());
    }


    @Override
    public int getItemCount() {
        return Items.size();
    }
}
