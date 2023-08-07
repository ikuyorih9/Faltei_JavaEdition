package com.example.faltei;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

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

    public int getPercentualFaltas(){
        int qtdFaltas = faltas.size();
        int qtdAulas = quantidadeAulas;
        return qtdFaltas/qtdAulas;
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
