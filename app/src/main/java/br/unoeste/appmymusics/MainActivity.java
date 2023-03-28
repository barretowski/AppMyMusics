package br.unoeste.appmymusics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1,menu);
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