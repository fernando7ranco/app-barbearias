package com.app.barbearias;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class FormDadosBarbeariaActivity extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private String idBarbearia;
    private ArrayList<Barbeiros> listaBarbeiros;
    private loadingActivity loadingActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.auth = FirebaseAuth.getInstance();
        this.user = this.auth.getCurrentUser();

        if (FormDadosBarbeariaActivity.this.user == null) {
            Intent intent = new Intent(FormDadosBarbeariaActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user == null) {
                        Intent intent = new Intent(FormDadosBarbeariaActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            };
            this.auth.addAuthStateListener(authStateListener);

            setContentView(R.layout.activity_form_dados_barbearia);

            this.loadingActivity = new loadingActivity();
            this.loadingActivity.init(this);

            this.listaBarbeiros = new ArrayList<Barbeiros>();

            FormDadosBarbeariaActivity.this.load();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if ( this.user == null ) {
            Intent intent = new Intent(FormDadosBarbeariaActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    public void deslogarUsuario(View v) {
        this.auth.signOut();
        this.user = null;
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(v);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void salvar(View v) {
        this.loadingActivity.show();

        ArrayList<HorariosSemana> HorariosSemana;

        EditText nome = findViewById(R.id.nomeBarbeariaCadastro);
        EditText descricao = findViewById(R.id.descricaoBarbeariaCadastro);
        EditText endereco = findViewById(R.id.enderecoBarbeariaCadastro);
        EditText numero = findViewById(R.id.numeroBarbeariaCadastro);
        EditText bairro = findViewById(R.id.bairroBarbeariaCadastro);
        EditText whatsapp = findViewById(R.id.whatsappBarbeariaCadastro);

        Barbearias barbearia = new Barbearias();

        barbearia.setId(FormDadosBarbeariaActivity.this.idBarbearia);
        barbearia.setIdUsuario(this.user.getUid());
        barbearia.setNome(nome.getText().toString().trim());
        barbearia.setDescricao(descricao.getText().toString().trim());
        barbearia.setEndereco(endereco.getText().toString().trim());
        barbearia.setNumero(Integer.valueOf(numero.getText().toString().trim()));
        barbearia.setBairro(bairro.getText().toString().trim());
        barbearia.setWhatsapp(whatsapp.getText().toString().trim());

        ArrayList<HorariosSemana> horarios = new ArrayList<>();
        ViewGroup diasSemanaHoras = findViewById(R.id.diasSemanaHoras);
        for (int i = 0; i  < diasSemanaHoras.getChildCount(); i++) {
            ViewGroup dia = (ViewGroup)diasSemanaHoras.getChildAt(i);
            Button btI = (Button)dia.getChildAt(1);
            Button btF = (Button)dia.getChildAt(2);

            String hi, hf;
            hi = (btI.getText().toString().toUpperCase().equals("INICIO") == false) ? btI.getText().toString() : "";
            hf = (btF.getText().toString().toUpperCase().equals("FIM") == false) ? btF.getText().toString() : "";

            HorariosSemana horario = new HorariosSemana();
            horario.setDiaSemana(String.valueOf(i+1));
            horario.setHorarioAbertura(hi);
            horario.setHorarioFechamento(hf);

            horarios.add(horario);
        }

        barbearia.setHorarios(horarios);
        barbearia.setBarbeiros(FormDadosBarbeariaActivity.this.listaBarbeiros);

        barbearia.save();

        Toast.makeText(this, "Informações salvas", Toast.LENGTH_LONG).show();

        this.loadingActivity.hide();
    }

    public void load() {

        ArrayList<HorariosSemana> HorariosSemana;

        EditText nome = findViewById(R.id.nomeBarbeariaCadastro);
        EditText descricao = findViewById(R.id.descricaoBarbeariaCadastro);
        EditText endereco = findViewById(R.id.enderecoBarbeariaCadastro);
        EditText numero = findViewById(R.id.numeroBarbeariaCadastro);
        EditText bairro = findViewById(R.id.bairroBarbeariaCadastro);
        EditText whatsapp = findViewById(R.id.whatsappBarbeariaCadastro);

        Barbearias barbearia = new Barbearias();

        this.loadingActivity.show();

        barbearia.getByIdUser(this.user.getUid(), new Regras() {
            @Override
            public void execute(ArrayList l) {

                FormDadosBarbeariaActivity.this.loadingActivity.hide();

                Barbearias b = (Barbearias)l.get(0);

                FormDadosBarbeariaActivity.this.idBarbearia = b.getId();

                nome.setText(b.getNome());
                descricao.setText(b.getDescricao());
                endereco.setText(b.getEndereco());
                numero.setText(String.valueOf(b.getNumero()));
                bairro.setText(b.getBairro());
                whatsapp.setText(b.getWhatsapp());

                ArrayList<HorariosSemana> horarios = b.getHorarios();
                if (horarios.size() > 0 ) {
                    ViewGroup diasSemanaHoras = findViewById(R.id.diasSemanaHoras);
                    for (int i = 0; i < diasSemanaHoras.getChildCount(); i++) {
                        ViewGroup dia = (ViewGroup) diasSemanaHoras.getChildAt(i);
                        Button btI = (Button) dia.getChildAt(1);
                        Button btF = (Button) dia.getChildAt(2);

                        String hi, hf;
                        hi = (horarios.get(i).getHorarioAbertura().isEmpty()) ? "INICIO" : horarios.get(i).getHorarioAbertura();
                        hf = (horarios.get(i).getHorarioFechamento().isEmpty()) ? "FIM" : horarios.get(i).getHorarioFechamento();

                        btI.setText(hi);
                        btF.setText(hf);
                    }
                }

                FormDadosBarbeariaActivity.this.listaBarbeiros = b.getBarbeiros();
                FormDadosBarbeariaActivity.this.mostrarBarbeiros();
            }
        });
    }

    public void adicionarBarbeiro(View v) {

        EditText nome = findViewById(R.id.nomeBarbeiroCadastro);
        EditText whatsapp = findViewById(R.id.whatsappBarbeiroCadastro);

        if (nome.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Informe um nome para o novo barbeiro", Toast.LENGTH_LONG).show();
        } else {

            Barbeiros b = new Barbeiros();

            b.setNome(nome.getText().toString().trim());
            b.setWhatsapp(whatsapp.getText().toString().trim());

            ArrayList<HorariosSemana> horarios = new ArrayList<>();
            ViewGroup diasSemanaHoras = findViewById(R.id.diasSemanaHorasBarbeiro);
            for (int i = 0; i < diasSemanaHoras.getChildCount(); i++) {
                ViewGroup dia = (ViewGroup) diasSemanaHoras.getChildAt(i);
                Button btI = (Button) dia.getChildAt(1);
                Button btF = (Button) dia.getChildAt(2);

                String hi, hf;
                hi = (btI.getText().toString().toUpperCase().equals("INICIO") == false) ? btI.getText().toString() : "";
                hf = (btF.getText().toString().toUpperCase().equals("FIM") == false) ? btF.getText().toString() : "";

                HorariosSemana horario = new HorariosSemana();
                horario.setDiaSemana(String.valueOf(i + 1));
                horario.setHorarioAbertura(hi);
                horario.setHorarioFechamento(hf);

                horarios.add(horario);
            }
            b.setHorarios(horarios);

            this.listaBarbeiros.add(b);

            this.mostrarBarbeiros();
        }
    }

    private void mostrarBarbeiros() {

        if (this.listaBarbeiros.size() > 0) {

            ViewGroup.LayoutParams params;

            ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.barbeirosBarbeariaCadastro);

            params = expandableListView.getLayoutParams();
            params.height = this.listaBarbeiros.size() * 140;
            if (params.height < (140 * 7)) {
                params.height = 140 * 7;
            }
            expandableListView.setLayoutParams(params);
            expandableListView.requestLayout();

            expandableListView.setAdapter(new ExpandableAdpter(FormDadosBarbeariaActivity.this, this.listaBarbeiros));

            expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    String nome = FormDadosBarbeariaActivity.this.listaBarbeiros.get(arg2).getNome();

                    AlertDialog.Builder alert = new AlertDialog.Builder(FormDadosBarbeariaActivity.this);
                    alert.setTitle("Excluir barbeiro");
                    alert.setIcon(android.R.drawable.ic_delete);
                    alert.setMessage("Você deseja excluir permanentemente o barbeirp: " + nome + "?");
                    alert.setNeutralButton("Cancelar", null);
                    alert.setPositiveButton("Sim excluir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FormDadosBarbeariaActivity.this.listaBarbeiros.remove(arg2);
                            FormDadosBarbeariaActivity.this.mostrarBarbeiros();
                        }
                    });
                    alert.show();

                    return false;
                }
            });

            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                int lastGroup = -1;

                @Override
                public void onGroupExpand(int groupPosition) {
                    Toast.makeText(FormDadosBarbeariaActivity.this, "Horários do barbeiro(a) : " + FormDadosBarbeariaActivity.this.listaBarbeiros.get(groupPosition).getNome(), Toast.LENGTH_SHORT).show();
                    if (lastGroup > -1 && lastGroup != groupPosition) {
                        expandableListView.collapseGroup(lastGroup);
                    }
                    lastGroup = groupPosition;
                }
            });
        }
    }
}