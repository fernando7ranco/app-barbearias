package com.app.barbearias;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class DadosBarbeariasActivity extends AppCompatActivity {
    private String idBarbearia;
    private Barbearias barbearias = new Barbearias();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_barbearias);

        this.idBarbearia = getIntent().getStringExtra("idBarbearia");

        this.mostrarDados();
    }

    private void mostrarDados() {

       this.barbearias.getById(this.idBarbearia, new Regras(){
           public void execute(ArrayList l) {
               Barbearias b = (Barbearias) l.get(0);
               TextView nome = findViewById(R.id.nomeBarbearia);
               TextView descricao = findViewById(R.id.descricaoBarbearia);
               TextView enderecoCompleto = findViewById(R.id.enderecoCompletoBarbearia);
               TextView whatsapp = findViewById(R.id.whatsappBarbearia);
               ListView horarios = findViewById(R.id.horariosBarbearias);

               nome.setText(b.getNome());
               descricao.setText(b.getDescricao());
               enderecoCompleto.setText("Local: " + b.getEndereco() +", "+ b.getNumero() +", "+ b.getBairro());
               whatsapp.setText("Whatsapp: " + b.getWhatsapp());

               ViewGroup.LayoutParams params;

               if ( b.getHorarios().size() > 0 ) {
                   params = horarios.getLayoutParams();
                   params.height =  b.getHorarios().size() * 175;
                   horarios.setLayoutParams(params);
                   horarios.requestLayout();

                   ArrayAdapter adapter = new ArrayAdapter(DadosBarbeariasActivity.this, android.R.layout.simple_list_item_1, b.getHorarios());
                   horarios.setEnabled(false);
                   horarios.setAdapter(adapter);
               }
               if ( b.getBarbeiros().size() > 0 ) {
                   ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.barbeirosBarbearia);

                   params = expandableListView.getLayoutParams();
                   params.height = b.getBarbeiros().size() * 140 ;
                   if (params.height < ( 140 * 7)) {
                       params.height =  140 * 7;
                   }
                   expandableListView.setLayoutParams(params);
                   expandableListView.requestLayout();

                   expandableListView.setAdapter(new ExpandableAdpter(DadosBarbeariasActivity.this, b.getBarbeiros()));

                   expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                       @Override
                       public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                           //Toast.makeText(DadosBarbeariasActivity.this, "Group: "+groupPosition+"| Item: "+childPosition, Toast.LENGTH_SHORT).show();
                           return false;
                       }
                   });

                   expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                       int lastGroup = -1;

                       @Override
                       public void onGroupExpand(int groupPosition) {
                           Toast.makeText(DadosBarbeariasActivity.this, "Horários do barbeiro(a) : " + b.getBarbeiros().get(groupPosition).getNome(), Toast.LENGTH_SHORT).show();
                           if (lastGroup > -1 && lastGroup != groupPosition) {
                               expandableListView.collapseGroup(lastGroup);
                           }
                           lastGroup = groupPosition;
                       }
                   });
               }
           }
        });

    }
}
