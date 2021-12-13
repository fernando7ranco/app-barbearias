package com.app.barbearias;

import java.util.ArrayList;

public class Barbeiros {
    public String id;
    public String nome;
    public String whatsapp;
    public ArrayList<HorariosSemana> horarios;

    public String getId() {
        return id;
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

    @Override
    public String toString() {
        return "Barbeiros{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", whatsapp='" + whatsapp + '\'' +
                ", horarios=" + horarios +
                '}';
    }
}
