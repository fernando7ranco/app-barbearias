package com.app.barbearias;

import java.util.ArrayList;
import java.util.List;

public class HorariosSemana {
    public String id;
    public String diaSemana;
    public String horarioAbertura;
    public String horarioFechamento;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDiaSemana() {
        return Integer.parseInt(diaSemana);
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHorarioAbertura() {
        return horarioAbertura;
    }

    public void setHorarioAbertura(String horarioAbertura) {
        this.horarioAbertura = horarioAbertura;
    }

    public String getHorarioFechamento() {
        return horarioFechamento;
    }

    public void setHorarioFechamento(String horarioFechamento) {
        this.horarioFechamento = horarioFechamento;
    }

    public List<String> diasSemana() {
        List<String> lista = new ArrayList<>();
        lista.add("");
        lista.add("Segunda");
        lista.add("Terça");
        lista.add("Quarta");
        lista.add("Quinta");
        lista.add("Sexta");
        lista.add("Sabado");
        lista.add("Domingo");
        return  lista;
    }

    @Override
    public String toString() {
        String hi = this.getHorarioAbertura().isEmpty() ? "(não informado)" : this.getHorarioAbertura();
        String hf = this.getHorarioFechamento().isEmpty() ? "(não informado)" : this.getHorarioFechamento();
        return this.diasSemana().get(this.getDiaSemana()) + " das "+ hi + " até as "+ hf;
    }
}
