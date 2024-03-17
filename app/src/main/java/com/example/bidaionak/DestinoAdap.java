package com.example.bidaionak;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class DestinoAdap extends RecyclerView.Adapter<DestinoAdap.DestinoViewHolder> {

    private Context context;
    private ArrayList<Destino> destinosList;

    public DestinoAdap(Context context, ArrayList<Destino> destinosList) {
        this.context = context;
        this.destinosList = destinosList;
    }

    @NonNull
    @Override
    public DestinoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.destino_item, parent, false);
        return new DestinoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinoViewHolder holder, int position) {
        final Destino destino = destinosList.get(position);
        holder.textViewNombre.setText(destino.getNombre());
        holder.textViewCoste.setText(String.valueOf(destino.getCosteTotal()));
        holder.textViewTransporte.setText(destino.getTransporte());

        // Manejar clics en elementos individuales
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoDetalle(destino);
            }
        });
    }

    @Override
    public int getItemCount() {
        return destinosList.size();
    }

    private void mostrarDialogoDetalle(final Destino destino) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.diag_editar_destino, null);

        final EditText editTextNombre = dialogView.findViewById(R.id.editTextNombre);
        final EditText editTextCoste = dialogView.findViewById(R.id.editTextCoste);
        final EditText editTextTransporte = dialogView.findViewById(R.id.editTextTransporte);

        editTextNombre.setText(destino.getNombre());
        editTextCoste.setText(String.valueOf(destino.getCosteTotal()));
        editTextTransporte.setText(destino.getTransporte());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setTitle("Detalles del Destino");

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombre = editTextNombre.getText().toString();
                String coste = editTextCoste.getText().toString();
                String transporte = editTextTransporte.getText().toString();



                // Actualizar el destino
                Bd bd = new Bd(context);
                destino.setNombre(nombre);
                destino.setCosteTotal(coste);
                destino.setTransporte(transporte);
                bd.editarDestino(destino);
                notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancelar", null);

        // Manejar clic en el botón "Eliminar"
        builder.setNeutralButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Eliminar destino
                Bd bd = new Bd(context);
                bd.deleteTask(destino.getId());
                destinosList.remove(destino);
                notifyDataSetChanged();
            }
        });

        builder.create().show();

    }

    public class DestinoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombre;
        TextView textViewCoste;
        TextView textViewTransporte;

        public DestinoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewDestino);
            textViewCoste = itemView.findViewById(R.id.textViewCoste);
            textViewTransporte = itemView.findViewById(R.id.textViewTransporte);
        }
    }
}
