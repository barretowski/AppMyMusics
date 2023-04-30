package br.unoeste.appmymusics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import br.unoeste.appmymusics.api.Letra;
import br.unoeste.appmymusics.api.Musica;
import br.unoeste.appmymusics.api.RetrofitConfig;
import br.unoeste.appmymusics.api.Traducao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LetraActivity extends AppCompatActivity {
    private Traducao traducao = null;
    private Letra letra;
    private Musica m;
    private TextView tvLetra;

    private Button btTraduzir;
    private int idioma = 1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letra);
        tvLetra = findViewById(R.id.tvLetra);
        btTraduzir = findViewById(R.id.btTraduzir);

        Intent intent = getIntent();
        String musica = intent.getStringExtra("musica");
        String artista = intent.getStringExtra("artista");

        btTraduzir.setOnClickListener(e -> {
            if(letra != null && musica != null){
                if(idioma == 1){
                    idioma = 2;
                    if(m.translate != null){
                        tvLetra.setText(m.translate.get(0).getText());
                    }else{
                        tvLetra.setText("Falha ao realizar tradução");
                    }
                }else{
                    idioma = 1;
                    tvLetra.setText(m.getText());
                }
            }
        });

        reqApi(artista, musica);
    }
    private void reqApi(String artista, String musica) {
        String apiKey = "660a4395f992ff67786584e238f501aa";
        Call<Letra> call = new RetrofitConfig().getLetraService().buscarLetra(apiKey, artista, musica);
        call.enqueue(new Callback<Letra>() {
            @Override
            public void onResponse(Call<Letra> call, Response<Letra> response) {
                letra = response.body();
                if(letra.type.compareToIgnoreCase("notfound") == 0){
                    tvLetra.setText("Não encontrado");
                }else{
                    if(letra.music != null){
                        m = letra.music.get(0);
                        tvLetra.setText(letra.music.get(0).getText());
                    }else{
                        tvLetra.setText("Musica não encontrada");
                    }
                }
                Log.i("resposta: ",""+letra);
            }

            @Override
            public void onFailure(Call<Letra> call, Throwable t) {

            }
        });
    }

}
