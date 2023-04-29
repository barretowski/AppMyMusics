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
import br.unoeste.appmymusics.db.bean.Musica;

import java.util.List;

import static android.os.Build.VERSION_CODES.R;

public class MusicaAdapter extends ArrayAdapter<Musica> {
    private int layout;
    private TextView cod;
    private TextView titulo;
    private TextView ano;
    private TextView duracao;
    private TextView genero;
    private TextView interprete;

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

        cod = (TextView) convertView.findViewById(R.id.tvCod);
        titulo = (TextView) convertView.findViewById(R.id.tvTitulo);
        ano = (TextView) convertView.findViewById(R.id.tvAno);
        duracao = (TextView) convertView.findViewById(R.id.tvDuracao);
        genero = (TextView) convertView.findViewById(R.id.tvGenero);
        interprete = (TextView) convertView.findViewById(R.id.tvInterprete);

        Musica musica = this.getItem(position);
        cod.setText("" + musica.getId());
        titulo.setText(musica.getTitulo());
        ano.setText("" + musica.getAno());
        duracao.setText("" + musica.getDuracao());
        genero.setText("" + musica.getGenero().getNome());
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
