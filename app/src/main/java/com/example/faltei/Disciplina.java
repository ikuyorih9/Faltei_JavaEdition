package com.example.faltei;

import android.app.Activity;
import android.content.SharedPreferences;

import java.io.Serializable;

public class Disciplina implements Serializable {
    private String nomeDisciplina;
    private String nomeProfessor;
    private int corEscolhida;

    public Disciplina(String nomeDisciplina, String nomeProfessor, int corEscolhida){
        this.nomeDisciplina = nomeDisciplina;
        this.nomeProfessor = nomeProfessor;
        this.corEscolhida = corEscolhida;
    }

    public Disciplina(){

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

    public String getNomeDisciplina(){
        return nomeDisciplina;
    }

    public String getNomeProfessor(){
        return nomeProfessor;
    }

    public int getCorEscolhida(){
        return corEscolhida;
    }

    public boolean equalDisciplina(Disciplina disciplina){
        boolean eqNome = this.nomeDisciplina == disciplina.getNomeDisciplina();
        boolean eqProf = this.nomeProfessor == disciplina.getNomeProfessor();
        boolean eqCor = this.corEscolhida == disciplina.getCorEscolhida();

        if(eqNome && eqProf && eqCor)
            return true;

        return false;
    }
}
