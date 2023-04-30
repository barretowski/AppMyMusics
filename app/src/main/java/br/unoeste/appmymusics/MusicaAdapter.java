package br.unoeste.appmymusics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import br.unoeste.appmymusics.db.bean.Musica;

public class MusicaAdapter extends ArrayAdapter {
    private int layout;
    public MusicaAdapter(@NonNull Context context, int resource, @NonNull List<Musica> objects) {
        super(context, resource, objects);
        layout=resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){{
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.layout, parent, false);
        }}

        TextView titulo = (TextView) convertView.findViewById(R.id.tvTitulo);
        TextView interprete = (TextView) convertView.findViewById(R.id.tvInterprete);

        Musica musica = (Musica) this.getItem(position);
        titulo.setText(musica.getTitulo());
        interprete.setText("" + musica.getInterprete());

        Button btnLetra = convertView.findViewById(R.id.btnLetra);
        btnLetra.setOnClickListener(e -> {

            Intent letraActivity = new Intent(getContext(), LetraActivity.class);
            letraActivity.putExtra("artista", musica.getInterprete());
            letraActivity.putExtra("musica", musica.getTitulo());
            getContext().startActivity(letraActivity);
        });

        return convertView;
    }
}
