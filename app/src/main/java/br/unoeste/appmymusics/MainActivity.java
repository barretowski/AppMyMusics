package br.unoeste.appmymusics;

import static android.R.layout.simple_list_item_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Locale;

import br.unoeste.appmymusics.db.bean.Genero;
import br.unoeste.appmymusics.db.bean.Musica;
import br.unoeste.appmymusics.db.dal.GeneroDAL;
import br.unoeste.appmymusics.db.dal.MusicaDAL;

public class MainActivity extends AppCompatActivity {
    private ListView lvPrincipal;
    private MusicaDAL dal;
    private Musica musica;
    private ArrayAdapter<Genero> adapterGeneros;
    private ArrayList<Musica> musicas;
    private ArrayList<Musica> musicasEncontradas;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvPrincipal = findViewById(R.id.lvPrincipal);
        ArrayList<Genero> generos = new GeneroDAL(this).get("");
        adapterGeneros = new ArrayAdapter<Genero>(this, R.layout.list_item, generos);
        this.musicas=new MusicaDAL(this).get("");
        MusicaAdapter adapter = new MusicaAdapter(this, R.layout.item,musicas);
        lvPrincipal.setAdapter(adapter);

        searchList("");
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