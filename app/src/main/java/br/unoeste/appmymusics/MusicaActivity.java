package br.unoeste.appmymusics;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import br.unoeste.appmymusics.db.bean.Genero;
import br.unoeste.appmymusics.db.bean.Musica;
import br.unoeste.appmymusics.db.dal.GeneroDAL;
import br.unoeste.appmymusics.db.dal.MusicaDAL;

public class MusicaActivity extends AppCompatActivity{
    private ListView lvMusica;
    private Button btConfirmar;
    private EditText etTitulo;
    private EditText etAno;
    private EditText etInterprete;
    private EditText etDuracao;

    private Genero generoSelect;
    private Spinner spGenero;
    private ArrayAdapter<Genero> adapterGeneros;

    private AlertDialog alerta;
    private ArrayList<Musica> musicas;
    private Musica musica;
    private LinearLayout linearLayoutMusica;
    private FloatingActionButton fabNovaMusica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musica);
        lvMusica = findViewById(R.id.lvMusica);
        linearLayoutMusica = findViewById(R.id.linearLayoutMusica);
        btConfirmar = findViewById(R.id.btConfirmarMusica);
        btConfirmar.setEnabled(false);
        etTitulo = findViewById(R.id.etTitulo);
        etAno = findViewById(R.id.etAno);
        etDuracao = findViewById(R.id.etDuracao);
        etInterprete = findViewById(R.id.etInterprete);

        etAno.addTextChangedListener(textWatcher);
        etInterprete.addTextChangedListener(textWatcher);
        etDuracao.addTextChangedListener(textWatcher);
        etTitulo.addTextChangedListener(textWatcher);


        fabNovaMusica = findViewById(R.id.fabNovaMusica);

        fabNovaMusica.setOnClickListener(e->{
            linearLayoutMusica.setVisibility(View.VISIBLE);
            etTitulo.setText("");
            etTitulo.requestFocus();
        });

        atualizarTela();
        loadCombos();
        linearLayoutMusica.setVisibility(View.GONE);

        btConfirmar.setOnClickListener(
                e->{
                    //verifica se musica já existe
                    if(musica==null)
                        cadastrarMusica();
                    else
                        atualizarMusica();

                    linearLayoutMusica.setVisibility(View.GONE);
                    musica = null;
                }
        );
        lvMusica.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialogAlert(i);
                return true;
            }
        });
        lvMusica.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final long DELAY = 300;//ajusta o atraso para que ao chamar um clique longo, não acione o click simples
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        generoSelect = new Genero();
                        linearLayoutMusica.setVisibility(View.VISIBLE);
                        musica = musicas.get(i);

                        etTitulo.setText(musica.getTitulo().toString());
                        etAno.setText(musica.getAno() + "");
                        etInterprete.setText(musica.getInterprete());
                        etDuracao.setText(musica.getDuracao() + "");
                        generoSelect = musica.getGenero();
                        int pos = adapterGeneros.getPosition(generoSelect);
                        spGenero.setSelection(pos);
                    }
                }, DELAY);


            }
        });
    }
    private void loadCombos()
    {
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
                generoSelect = genero;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                generoSelect = null;
            }
        });

    }
    private void cadastrarMusica()
    {
        MusicaDAL dal = new MusicaDAL(this);
        Musica musica = new Musica(etTitulo.getText().toString());

        musica.setAno(Integer.parseInt(etAno.getText().toString()));
        musica.setDuracao(Double.parseDouble(etDuracao.getText().toString()));
        musica.setGenero(generoSelect);
        musica.setInterprete(etInterprete.getText().toString());
        musica.setTitulo(etTitulo.getText().toString());

        dal.salvar(musica);

        musicas = new MusicaDAL(this).get("");
        atualizarTela();
    }
    private void atualizarMusica()
    {
        musica.setGenero(generoSelect);
        musica.setInterprete(etInterprete.getText().toString());
        musica.setTitulo(etTitulo.getText().toString());
        musica.setAno(Integer.parseInt(etAno.getText().toString()));
        musica.setDuracao(Double.parseDouble(etDuracao.getText().toString()));
        MusicaDAL dal = new MusicaDAL(this);
        dal.alterar(musica);
        atualizarTela();
    }
    private void excluirMusica(Musica musica)
    {
        MusicaDAL dal = new MusicaDAL(this);
        dal.apagar(musica.getId());
        atualizarTela();
    }
    private boolean atualizarTela()
    {
        this.musicas=new MusicaDAL(this).get("");
        ArrayAdapter<Musica> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,musicas);
        lvMusica.setAdapter(adapter);
        musica = null;
        generoSelect = null;

        etInterprete.setText("");
        etInterprete.requestFocus();
        etTitulo.setText("");
        etTitulo.requestFocus();
        etAno.setText("");
        etAno.requestFocus();
        etDuracao.setText("");
        etDuracao.requestFocus();

        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu3,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.itNovaMusica:
                linearLayoutMusica.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearLayoutMusica.setVisibility(View.VISIBLE);
                final int targetHeight = linearLayoutMusica.getMeasuredHeight();
                /**/
                Animation a = new Animation()
                {
                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        linearLayoutMusica.getLayoutParams().height = interpolatedTime == 1
                                ? LinearLayout.LayoutParams.WRAP_CONTENT
                                : (int)(targetHeight * interpolatedTime);
                        linearLayoutMusica.requestLayout();
                    }

                    @Override
                    public boolean willChangeBounds() {
                        return true;
                    }
                };

                // 1dp/ms
                a.setDuration((int)(targetHeight / linearLayoutMusica.getContext().getResources().getDisplayMetrics().density));
                linearLayoutMusica.startAnimation(a);
                /**/
                etTitulo.setText("");
                etTitulo.requestFocus();
                break;

            case R.id.itVoltarMusica:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogAlert(int i)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confrimar exclusão");
        builder.setMessage("Deseja excluir essa música?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Musica musica = musicas.get(i);
                if(musica.getId() > 0)
                    excluirMusica(musica);
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alerta = builder.create();
        alerta.show();
    }
    // Cria um TextWatcher para verificar quando o texto for alterado
    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Verifica se todos os campos estão preenchidos
            if (!TextUtils.isEmpty(etAno.getText()) &&
                    !TextUtils.isEmpty(etInterprete.getText()) &&
                    !TextUtils.isEmpty(etDuracao.getText()) &&
                    !TextUtils.isEmpty(etTitulo.getText()) &&
                    generoSelect!=null
            ){
                // Habilita o botão se todos os campos estiverem preenchidos
                btConfirmar.setEnabled(true);
            } else {
                // Desabilita o botão se algum campo estiver vazio
                btConfirmar.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }


    };
}
