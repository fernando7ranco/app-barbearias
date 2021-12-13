package com.app.barbearias;

import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract class BarbeariasDAO {
    private ConexaoBanco conexao;
    private DatabaseReference barbearias;

    public BarbeariasDAO() {
        this.conexao = new ConexaoBanco();
        this.barbearias = this.conexao.getReference().child("barbearias");
    }

    public boolean save(){
        Barbearias b = this.referenciaDadosBarbearias();
        String key;
        if (b.getId() == null) {
            key = this.barbearias.push().getKey();
        } else {
            key = b.getId();
            b.setId(null);
        }

        this.barbearias.child(key).setValue(b);
        b.setId(key);

        return  true;
    }

    private Barbearias estrururacao(DataSnapshot snapshot) {
        Log.d("Barbearias dados:", String.valueOf(snapshot.getValue()));

        Barbearias b = new Barbearias();

        b.setId(snapshot.getKey());
        b.setNome(snapshot.child("nome").getValue().toString());
        b.setDescricao(snapshot.child("descricao").getValue().toString());
        b.setEndereco(snapshot.child("endereco").getValue().toString());
        b.setNumero(Integer.parseInt(snapshot.child("numero").getValue().toString()));
        b.setBairro(snapshot.child("bairro").getValue().toString());
        b.setWhatsapp(snapshot.child("whatsapp").getValue().toString());

        ArrayList<HorariosSemana> horarios = new ArrayList<>();

        for (DataSnapshot snapshotH: snapshot.child("horarios").getChildren()) {
            Log.d("Barbearias horarios dados:", String.valueOf(snapshotH.getValue()));
            HorariosSemana h = new HorariosSemana();
            h.setId(snapshotH.getKey());
            h.setDiaSemana(snapshotH.child("diaSemana").getValue().toString());
            h.setHorarioAbertura(snapshotH.child("horarioAbertura").getValue().toString());
            h.setHorarioFechamento(snapshotH.child("horarioFechamento").getValue().toString());
            horarios.add(h);
        }

        b.setHorarios(horarios);

        ArrayList<Barbeiros> barbeiros = new ArrayList<>();

        for (DataSnapshot snapshotB: snapshot.child("barbeiros").getChildren()) {

            Barbeiros barbeiro = new Barbeiros();

            barbeiro.setId(snapshotB.getKey());
            barbeiro.setNome(snapshotB.child("nome").getValue().toString());
            barbeiro.setWhatsapp(snapshotB.child("whatsapp").getValue().toString());

            ArrayList<HorariosSemana> horariosB = new ArrayList<>();

            for (DataSnapshot snapshotH: snapshotB.child("horarios").getChildren()) {
                Log.d("Barbearias barbeiros horarios dados:", String.valueOf(snapshotH.getValue()));

                HorariosSemana h = new HorariosSemana();
                h.setId(snapshotH.getKey());
                h.setDiaSemana(snapshotH.child("diaSemana").getValue().toString());
                h.setHorarioAbertura(snapshotH.child("horarioAbertura").getValue().toString());
                h.setHorarioFechamento(snapshotH.child("horarioFechamento").getValue().toString());
                horariosB.add(h);
            }

            barbeiro.setHorarios(horariosB);

            Log.d("Barbearias barbeiros dados:", String.valueOf(barbeiro));

            barbeiros.add(barbeiro);
        }

        b.setBarbeiros(barbeiros);

        return b;
    }

    public void list(Regras r){
        ArrayList<Barbearias> lista = new ArrayList<>();

        this.barbearias.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                   Barbearias b = estrururacao(snapshot);
                    lista.add(b);
                }
                r.execute(lista);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void getById(String id, Regras r) {

        this.barbearias.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Barbearias b = estrururacao(snapshot);
                ArrayList<Barbearias> lista = new ArrayList<>();
                lista.add(b);
                r.execute(lista);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void getByIdUser(String idUsuario, Regras r) {
        Log.d("getByIdUser:", String.valueOf(idUsuario));

        this.barbearias.orderByChild("idUsuario").equalTo(idUsuario).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Barbearias b = estrururacao(dataSnapshot);
                ArrayList<Barbearias> lista = new ArrayList<>();
                lista.add(b);
                r.execute(lista);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    protected abstract Barbearias referenciaDadosBarbearias();
}