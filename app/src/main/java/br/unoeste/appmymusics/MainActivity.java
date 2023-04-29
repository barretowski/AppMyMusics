package br.unoeste.appmymusics;

import static android.R.layout.simple_list_item_1;

import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Locale;

import br.unoeste.appmymusics.db.bean.Genero;
import br.unoeste.appmymusics.db.bean.Musica;
import br.unoeste.appmymusics.db.dal.GeneroDAL;
import br.unoeste.appmymusics.db.dal.MusicaDAL;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private ListView lvPrincipal;
    private MusicaDAL dal;
    private Musica musica;
    private EditText etTitulo;
    private EditText etAno;
    private EditText etInterprete;
    private EditText etDuracao;

    private ArrayList<Musica> musicas;
    private ArrayList<Musica> musicasEncontradas;
    private Spinner spGenero;
    private Genero generoSelected;
    ArrayAdapter<Genero> adapterGeneros;
    private LinearLayout linearLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvPrincipal = findViewById(R.id.lvPrincipal);
        linearLayout = findViewById(R.id.linearLayoutMusica);
        this.musicas=new MusicaDAL(this).get("");

        ArrayList<Genero> generos = new GeneroDAL(this).get("");
        adapterGeneros = new ArrayAdapter<Genero>(this,
                android.R.layout.simple_list_item_1,generos);
        adapterGeneros.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenero = findViewById(R.id.spGenero);
        spGenero.setAdapter(adapterGeneros);
        spGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Genero genero = (Genero) adapterGeneros.getItem(position);
                generoSelected = genero;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                generoSelected = null;
            }
        });
        spGenero.setSelection(adapterGeneros.getPosition(generoSelected));
        this.musicas= new MusicaDAL(this).get("");
        MusicaAdapter adapter = new MusicaAdapter(this, R.layout.fragment_list_item,musicas);
        lvPrincipal.setAdapter(adapter);
        lvPrincipal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                linearLayout.setVisibility(View.VISIBLE);
                Musica mscEncontrada = musicas.get(i);
                etTitulo.setText(mscEncontrada.getTitulo().toString());
                etAno.setText(mscEncontrada.getAno() + "");
                etInterprete.setText(mscEncontrada.getInterprete());
                etDuracao.setText(mscEncontrada.getDuracao() + "");
                generoSelected = mscEncontrada.getGenero();
                musica = mscEncontrada;

                int posItem;
                for(posItem = 0;
                    posItem < adapterGeneros.getCount()
                            && adapterGeneros.getItem(posItem).getNome().compareToIgnoreCase(generoSelected.getNome()) != 0; posItem++){}

                spGenero.setSelection(adapterGeneros.getPosition(generoSelected));
                generoSelected = adapterGeneros.getItem(posItem);
            }
        });

        lvPrincipal.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Snackbar sbar;
                sbar = Snackbar.make(view, "Deseja apagar a musica?", Snackbar.LENGTH_LONG);
                sbar.show();
                sbar.setAction("Apagar ?", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Musica musica =  musicas.get(i);
                        if(musica.getId() > 0){
                            apagarMusica(musica);
                        }
                    }
                });
                return true;
            }
        });
    }
    public void searchList(String query)
    {
        musicasEncontradas = new ArrayList<Musica>();
        for(Musica item: musicas)
        {
            if(item.getInterprete().toLowerCase().contains(query.toLowerCase())
               || item.getTitulo().toLowerCase().contains(query.toLowerCase()) )
            {
                musicasEncontradas.add(item);
            }
        }
        if(!musicasEncontradas.isEmpty())
        {
            ArrayAdapter<Musica> adapterEncontradas = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1,musicasEncontradas);
            lvPrincipal.setAdapter(adapterEncontradas);
        }
    }
    private void apagarMusica(Musica musica){
        try{
            MusicaDAL dal = new MusicaDAL(this);
            dal.apagar(musica.getId());
            this.atualizarDados();

        }catch (Exception e){
            System.out.println(e);
        }
    }
    private void atualizarDados(){
        this.musicas= new MusicaDAL(this).get("");
        MusicaAdapter adapter = new MusicaAdapter(this, R.layout.fragment_list_item,musicas);
        lvPrincipal.setAdapter(adapter);
        this.musica = null;
        this.limparCampos();
    }
    private void limparCampos(){
        etTitulo.setText("");
        etTitulo.requestFocus();
        etAno.setText("");
        etAno.requestFocus();
        etDuracao.setText("");
        etDuracao.requestFocus();
        etInterprete.setText("");
        etInterprete.requestFocus();
        generoSelected = adapterGeneros.getItem(0);
        musica = null;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
        MenuItem menu1 = menu.findItem(R.id.itSearch);
        SearchView searchview = (SearchView) menu1.getActionView();
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //searchList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.itmusica:
                Intent intent2 = new Intent(this, MusicaActivity.class);
                startActivity(intent2);
                break;
            case R.id.itcategoria:
                Intent intent = new Intent(this,CategoriaActivity.class);
                startActivity(intent);
                break;
            case R.id.itfechar:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}