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
    private int quantidadeLimiteFaltas;
    private int quantidadeCreditos;
    private ArrayList <Date> faltas;

    public Disciplina(String nomeDisciplina, String nomeProfessor, int corEscolhida, int quantidadeCreditos){
        this.nomeDisciplina = nomeDisciplina;
        this.nomeProfessor = nomeProfessor;
        this.corEscolhida = corEscolhida;
        this.quantidadeCreditos = quantidadeCreditos;
        this.quantidadeAulas = (quantidadeCreditos*HomeActivity.horasPorCredito*60)/100;
        faltas = new ArrayList<>();
        quantidadeLimiteFaltas = quantidadeAulas - ((int)((HomeActivity.mediaFaltas) * quantidadeAulas)+1);
    }

    public Disciplina(){
        faltas = new ArrayList<>();
        quantidadeLimiteFaltas = 0;
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
    public void setQuantidadeCreditos(int quantidadeCreditos){
        this.quantidadeCreditos = quantidadeCreditos;
        this.quantidadeAulas = (quantidadeCreditos*HomeActivity.horasPorCredito*60)/100;
    }
    public void setLimiteFaltas(int quantidadeLimiteFaltas){
        this.quantidadeLimiteFaltas = quantidadeLimiteFaltas;
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
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);

        graficoFaltas.setData(data);
        graficoFaltas.setRotationAngle(90);
        graficoFaltas.setHoleColor(Color.TRANSPARENT);
        graficoFaltas.getDescription().setEnabled(false);
        graficoFaltas.getLegend().setEnabled(false);
        graficoFaltas.getData().setDrawValues(false);
        graficoFaltas.setCenterTextSize(10);

        graficoFaltas.setCenterText(String.format("%.1f",getPercentualFaltas()*100) + "%");
        graficoFaltas.setHoleRadius(35f);
        graficoFaltas.setTransparentCircleRadius(35f);
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
        quantidadeLimiteFaltas--;
    }
    public void adicionarFalta(Date data){
        faltas.add(data);
        quantidadeLimiteFaltas --;
    }
    public void removerFalta(Date data){
        Log.d("HomeActivity", "removerFalta: " + faltas.contains(data));
        for(int i = 0; i < faltas.size(); i++){
            Log.d("HomeActvity", "-----FALTAAAA: "+faltas.get(i).toString());
        }
        faltas.remove(data);
        quantidadeLimiteFaltas++;
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
        calculaQuantidadeAulas();
        return quantidadeAulas;
    }
    public int getQuantidadeFaltas(){
        return faltas.size();
    }
    public Date getFalta(int i){
        return faltas.get(i);
    }
    public int getQuantidadeLimiteFaltas(){
        return quantidadeLimiteFaltas;
    }
    public ArrayList <Date> getFaltas(){
        return faltas;
    }
    public double getPercentualFaltas(){
        return ((double) faltas.size())/quantidadeAulas;
    }
    public int getQuantidadeCreditos(){
        return quantidadeCreditos;
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

    public void calculaQuantidadeAulas(){
        quantidadeAulas = (quantidadeCreditos*HomeActivity.horasPorCredito*60)/100;
    }
}
