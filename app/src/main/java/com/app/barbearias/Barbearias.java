package com.app.barbearias;

import java.util.ArrayList;

public class Barbearias extends BarbeariasDAO{
    private String id;
    public String idUsuario = "";
    public String nome = "";
    public String descricao = "";
    public String endereco = "";
    public int numero = 0;
    public String bairro = "";
    public String whatsapp = "";
    public ArrayList<HorariosSemana> horarios = new ArrayList<HorariosSemana>();
    public ArrayList<Barbeiros> barbeiros = new  ArrayList<Barbeiros>();

    @Override
    protected Barbearias referenciaDadosBarbearias() {
        return this;
    }

    public String getId() {
        return id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public ArrayList<HorariosSemana> getHorarios() {
        return horarios;
    }

    public void setHorarios(ArrayList<HorariosSemana> horarios) {
        this.horarios = horarios;
    }

    public ArrayList<Barbeiros> getBarbeiros() {
        return barbeiros;
    }

    public void setBarbeiros(ArrayList<Barbeiros> barbeiros) {
        this.barbeiros = barbeiros;
    }

    public Barbearias() {
        super();
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
