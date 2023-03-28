package br.unoeste.appmymusics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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
import br.unoeste.appmymusics.db.dal.MusicaDAL;

public class CategoriaActivity extends AppCompatActivity {
    private ListView lvCategoria;
    private Button btConfirmar;
    private EditText etGenero;
    private AlertDialog alerta;
    private ArrayList<Genero> generos;
    private Genero genero;
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
        generos = new GeneroDAL(this).get("");
        ArrayAdapter<Genero> adapter=new ArrayAdapter<Genero>(this,
                android.R.layout.simple_list_item_1,generos);

        lvCategoria.setAdapter(adapter);
        linearLayout.setVisibility(View.GONE);

        btConfirmar.setOnClickListener(e->{
            if(genero==null)
                cadastrarGenero();
            else
                editarGenero();
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
                dialogAlert(i);
                return true;
            }
        });
        lvCategoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final long DELAY = 300;//ajusta o atraso para que ao chamar um clique longo, não acione o click simples
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        linearLayout.setVisibility(View.VISIBLE);
                        genero = generos.get(i);
                        etGenero.setText(genero.getNome());
                    }
                }, DELAY);


            }
        });

    }
    private void dialogAlert(int i) //Cria o gerador do AlertDialog
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confrimar exclusão"); //define o titulo
        builder.setMessage("Deseja excluir essa categoria?"); //define a mensagem
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface arg0, int arg1) {
                genero = generos.get(i);
                if(genero.getId() > 0)
                    excluirGenero(genero);
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // código a ser executado
            }
        });
        alerta = builder.create(); //cria o AlertDialog
        alerta.show(); //Exibe
    }

    private void excluirGenero(Genero genero)
    {
        GeneroDAL dal = new GeneroDAL(this);
        dal.apagar(genero.getId());
        atualizarTela();
    }
    private void editarGenero()
    {
        GeneroDAL dal = new GeneroDAL(this);
        genero.setNome(etGenero.getText().toString());
        dal.alterar(genero);
        atualizarTela();
    }
    private void cadastrarGenero() {

        GeneroDAL dal = new GeneroDAL(this);
        genero =new Genero(etGenero.getText().toString());
        dal.salvar(genero);

        generos=new GeneroDAL(this).get("");

        atualizarTela();
    }
    public boolean atualizarTela()
    {
        this.generos=new GeneroDAL(this).get("");
        ArrayAdapter<Genero> adapter=new ArrayAdapter<Genero>(this,
                android.R.layout.simple_list_item_1,generos);
        lvCategoria.setAdapter(adapter);
        genero = null;
        etGenero.setText("");
        return true;
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