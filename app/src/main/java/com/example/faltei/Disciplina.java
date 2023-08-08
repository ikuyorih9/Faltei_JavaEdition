package com.example.faltei;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Disciplina implements Serializable {
    private String nomeDisciplina;
    private String nomeProfessor;
    private int corEscolhida;
    private int quantidadeAulas;

    private ArrayList <Date> faltas;

    public Disciplina(String nomeDisciplina, String nomeProfessor, int corEscolhida, int quantidadeAulas){
        this.nomeDisciplina = nomeDisciplina;
        this.nomeProfessor = nomeProfessor;
        this.corEscolhida = corEscolhida;
        this.quantidadeAulas = quantidadeAulas;
        faltas = new ArrayList<>();
    }

    public Disciplina(){
        faltas = new ArrayList<>();
    }

    public void setNomeDisciplina(String nomeDisciplina){
        this.nomeDisciplina = nomeDisciplina;
    }
    public void setNomeProfessor(String nomeProfessor){
        this.nomeProfessor = nomeProfessor;
    }
    public void setCorEscolhida(int corEscolhida){
        this.corEscolhida = corEscolhida;
    }

    public void setQuantidadeAulas(int quantidadeAulas){
        this.quantidadeAulas = quantidadeAulas;
    }

    public void setFaltas(ArrayList<Date> faltas){
        this.faltas = faltas;
    }
    public boolean iniciaGraficoFaltas(PieChart graficoFaltas, int corFalta){
        Log.d("HomeActivity", "INICIANDO GRAFICO!");
        if(graficoFaltas == null){
            return false;
        }

        int qtdFaltas = faltas.size();
        int restanteAulas = quantidadeAulas - qtdFaltas;

        ArrayList <PieEntry> dadosEntrada = new ArrayList<>();
        dadosEntrada.add(new PieEntry(qtdFaltas, "Faltas"));
        dadosEntrada.add(new PieEntry(restanteAulas, "Restante"));

        ArrayList<Integer> cores = new ArrayList<>();
        cores.add(corFalta);
        cores.add(Color.LTGRAY);

        PieDataSet dataSet = new PieDataSet(dadosEntrada, "Faltas");
        dataSet.setColors(cores);

        PieData data = new PieData(dataSet);

        graficoFaltas.setData(data);
        graficoFaltas.setRotationAngle(90);
        graficoFaltas.setHoleColor(Color.TRANSPARENT);
        graficoFaltas.getDescription().setEnabled(false);
        graficoFaltas.getLegend().setEnabled(false);
        graficoFaltas.getData().setValueTextSize(10);
        graficoFaltas.getData().setValueTextColor(Color.WHITE);
        graficoFaltas.setHoleRadius(40f);
        graficoFaltas.setTransparentCircleRadius(40f);
        graficoFaltas.setDrawEntryLabels(false);
        graficoFaltas.setUsePercentValues(true);
        graficoFaltas.invalidate();
        return true;
    }

    public void adicionarFaltaOrdenado(Date data){
        int size = faltas.size();
        for(int i = 0; i < size; i++){
            if(data.compareTo(faltas.get(i)) <= 0) {
                faltas.add(i, data);
                return;
            }
        }
        faltas.add(data);
    }
    public void adicionarFalta(Date data){
        faltas.add(data);
    }

    public void removerFalta(Date data){
        faltas.remove(data);
    }
    public void removerFalta(int index){
        faltas.remove(index);
    }

    public String getNomeDisciplina(){
        return nomeDisciplina;
    }

    public String getNomeProfessor(){
        return nomeProfessor;
    }

    public int getCorEscolhida(){
        return corEscolhida;
    }

    public int getQuantidadeAulas(){
        return quantidadeAulas;
    }

    public int getQuantidadeFaltas(){
        return faltas.size();
    }

    public Date getFalta(int i){
        return faltas.get(i);
    }

    public ArrayList <Date> getFaltas(){
        return faltas;
    }
    public double getPercentualFaltas(){
        return ((double) faltas.size())/quantidadeAulas;
    }

    public boolean equalDisciplina(Disciplina disciplina){
        boolean eqNome = this.nomeDisciplina == disciplina.getNomeDisciplina();
        boolean eqProf = this.nomeProfessor == disciplina.getNomeProfessor();
        boolean eqCor = this.corEscolhida == disciplina.getCorEscolhida();

        if(eqNome && eqProf && eqCor)
            return true;

        return false;
    }

    public ArrayList <String> listaDataFaltas(){
        Log.d("HomeActivity", "LISTA DE FALTAS:");
        ArrayList <String> dataFaltas = new ArrayList<>();
        for(int i = 0; i <faltas.size(); i++){
            Date data = faltas.get(i);
            Log.d("HomeActivity", "->" + i + ": " + data.toString());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dataFaltas.add(dateFormat.format(data));
        }

        return dataFaltas;
    }

    public void mostraFaltas(){
        for(int i = 0; i <faltas.size(); i++){
            Log.d("HomeActivity", "-----> DATA " + i + ": " + faltas.get(i).toString());
        }
    }

    public void mostraDadosDisciplina(){
        Log.d("HomeActivity", "---Nome: " +nomeDisciplina);
        Log.d("HomeActivity", "---Professor: " + nomeProfessor);
        Log.d("HomeActivity", "---Quantidade aulas: " + quantidadeAulas);
        Log.d("HomeActivity", "---Cor da disciplina: " + corEscolhida);
        //Log.d("HomeActivity", "---Faltas: " + getPercentualFaltas());
        mostraFaltas();
    }
}
