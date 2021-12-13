package com.app.barbearias;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import android.content.Context;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ExpandableAdpter extends BaseExpandableListAdapter {
    private ArrayList<Barbeiros> barbeiros;
    private LayoutInflater inflater;
    private Context context;

    public ExpandableAdpter(Context context, ArrayList<Barbeiros> b) {
        this.barbeiros = b;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return this.barbeiros.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.barbeiros.get(i).getHorarios().size();
    }

    @Override
    public Object getGroup(int i) {
        return this.barbeiros.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return  this.barbeiros.get(i).getHorarios().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ViewBarbeiros vBarbeiros;

        if (view == null) {
            view = inflater.inflate(R.layout.linhas_barbeiros, null);
            vBarbeiros = new ViewBarbeiros();
            view.setTag(vBarbeiros);

            vBarbeiros.nome = view.findViewById(R.id.nomeBarbeiro);
        } else {
            vBarbeiros = (ViewBarbeiros) view.getTag();
        }

        vBarbeiros.nome.setText(this.barbeiros.get(i).getNome());

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ViewHorariosBarbeiros vHorarios;

        if (view == null) {
            view = inflater.inflate(R.layout.linhas_horarios_barbeiros_view, null);
            vHorarios = new ViewHorariosBarbeiros();
            view.setTag(vHorarios);

            vHorarios.horarios = (TextView) view.findViewById(R.id.horariosBarbeiros);
        } else {
            vHorarios = (ViewHorariosBarbeiros) view.getTag();
        }

        vHorarios.horarios.setText(this.getChild(i, i1).toString());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}

class ViewBarbeiros {
    TextView nome;
    Button whats;
}

class ViewHorariosBarbeiros {
    TextView horarios;
}