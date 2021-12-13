package com.app.barbearias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lvBarbearias;
    private List<Barbearias> listaBarbearias;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.lvBarbearias = findViewById(R.id.lvBarbearias);

        this.lvBarbearias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DadosBarbeariasActivity.class);
                intent.putExtra("idBarbearia",  MainActivity.this.listaBarbearias.get(i).getId());
                startActivity(intent);
            }
        });
        this.carregarListaDeBarbearias();
    }

    private void carregarListaDeBarbearias() {
        Barbearias b = new Barbearias();
        b.list(new Regras(){
            @Override
            public void execute(ArrayList l) {
                MainActivity.this.listaBarbearias = l;
                MainActivity.this.adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,  MainActivity.this.listaBarbearias);
                MainActivity.this.lvBarbearias.setAdapter(MainActivity.this.adapter);
            }
        });

    }
}