package com.example.faltei;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DisciplinaViewModel extends ViewModel {
    private final MutableLiveData<String> nomeDisciplina = new MutableLiveData<>();
    private final MutableLiveData<String> nomeProfessor = new MutableLiveData<>();
    private final MutableLiveData<Integer> corEscolhida = new MutableLiveData<>();

    public DisciplinaViewModel(){

    }

    public void setNomeDisciplina(String nomeDisciplina){
        this.nomeDisciplina.setValue(nomeDisciplina);
    }

    public void setNomeProfessor(String nomeProfessor){
        this.nomeProfessor.setValue(nomeProfessor);
    }

    public void setCorEscolhida(int corEscolhida){
        this.corEscolhida.setValue(corEscolhida);
    }

    public LiveData<String> getNomeDisciplina(){
        return nomeDisciplina;
    }

    public LiveData<String> getNomeProfessor(){
        return nomeProfessor;
    }

    public LiveData<Integer> getCorEscolhida(){
        return corEscolhida;
    }

    public boolean isVazio(){
        if(nomeDisciplina != null && nomeProfessor != null && corEscolhida != null){
            Log.d("HomeActivity", "VALORES NÃO VAZIOS!");
            Log.d("HomeActivity", "Vazio: " + nomeDisciplina.getValue().isEmpty());
            Log.d("HomeActivity", "Vazio: " + nomeProfessor.getValue().isEmpty());
            return false;
        }
        Log.d("HomeActivity", "VALORES VAZIOS!");
        return true;
    }
}
