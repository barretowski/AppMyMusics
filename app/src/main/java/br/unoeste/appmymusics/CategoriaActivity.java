package br.unoeste.appmymusics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import br.unoeste.appmymusics.db.bean.Genero;
import br.unoeste.appmymusics.db.dal.GeneroDAL;

public class CategoriaActivity extends AppCompatActivity {
    private ListView lvCategoria;
    private Button btConfirmar;
    private EditText etGenero;
    private LinearLayout linearLayout;
    private FloatingActionButton fabNovaCategoria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        lvCategoria = findViewById(R.id.lvCategoria);
        linearLayout = findViewById(R.id.linearLayout);
        btConfirmar=findViewById(R.id.btConfirmar);
        etGenero=findViewById(R.id.etGenero);
        fabNovaCategoria=findViewById(R.id.fabNovaCategoria);
        ArrayList<Genero> generos=new GeneroDAL(this).get("");
        ArrayAdapter<Genero> adapter=new ArrayAdapter<Genero>(this,
                android.R.layout.simple_list_item_1,generos);
        lvCategoria.setAdapter(adapter);
        linearLayout.setVisibility(View.GONE);
        btConfirmar.setOnClickListener(e->{
            cadastrarGenero();
            linearLayout.setVisibility(View.GONE);
        });
        fabNovaCategoria.setOnClickListener(e->{
            linearLayout.setVisibility(View.VISIBLE);
            etGenero.setText("");
            etGenero.requestFocus();
        });
        lvCategoria.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Snackbar sbar;
                sbar=Snackbar.make(view,"Qual sua ação?",Snackbar.LENGTH_LONG);
                sbar.show();
                sbar.setAction("Apagar ?", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // apagar o item do listview
                    }
                });
                return true;
            }
        });
    }

    private void cadastrarGenero() {

        GeneroDAL dal = new GeneroDAL(this);
        Genero genero=new Genero(etGenero.getText().toString());
        dal.salvar(genero);
        ArrayList<Genero> generos=new GeneroDAL(this).get("");
        ArrayAdapter<Genero> adapter=new ArrayAdapter<Genero>(this,
                android.R.layout.simple_list_item_1,generos);
        lvCategoria.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.itncategoria:
                linearLayout.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearLayout.setVisibility(View.VISIBLE);
                final int targetHeight = linearLayout.getMeasuredHeight();
                /**/
                Animation a = new Animation()
                {
                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        linearLayout.getLayoutParams().height = interpolatedTime == 1
                                ? LinearLayout.LayoutParams.WRAP_CONTENT
                                : (int)(targetHeight * interpolatedTime);
                        linearLayout.requestLayout();
                    }

                    @Override
                    public boolean willChangeBounds() {
                        return true;
                    }
                };

                // 1dp/ms
                a.setDuration((int)(targetHeight / linearLayout.getContext().getResources().getDisplayMetrics().density));
                linearLayout.startAnimation(a);
                /**/
                etGenero.setText("");
                etGenero.requestFocus();
                // cadastrar nova categoria
                break;
            case R.id.itvoltar:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}