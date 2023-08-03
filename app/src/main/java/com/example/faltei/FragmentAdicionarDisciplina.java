package com.example.faltei;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.IDNA;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.faltei.databinding.FragmentAdicionarDisciplinaBinding;
import com.example.faltei.databinding.FragmentDisciplinaBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;


public class FragmentAdicionarDisciplina extends Fragment {
    private Button bCriarDisciplina;
    private EditText etNomeDisciplina;
    private EditText etProfessor;
    private ArrayList <ImageView> cores;
    private LinearLayout amostraCor;
    private FragmentAdicionarDisciplinaBinding binding;
    private int corEscolhida;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("HomeActivity", "AAAA!");
        binding = FragmentAdicionarDisciplinaBinding.inflate(inflater, container, false);
        bCriarDisciplina = binding.buttonCriarDisciplina;
        etNomeDisciplina = binding.editTextNomeDisciplina;
        etProfessor = binding.editTextProfessor;

        amostraCor = binding.layAmostraCor;
        corEscolhida = -1;
        cores = new ArrayList<ImageView>();
        criaVetorCores();

        for(int i = 0; i < cores.size(); i++){
            cores.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Drawable backgroud = view.getBackground();
                    if(backgroud instanceof ColorDrawable)
                        corEscolhida = ((ColorDrawable) backgroud).getColor();
                    Log.d("HomeActivity", "Cor clicada: " + corEscolhida);
                    amostraCor.setBackgroundColor(corEscolhida);
                }
            });
        }

        bCriarDisciplina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String disciplina = etNomeDisciplina.getText().toString();
                if(disciplina.isEmpty()){
                    Snackbar.make(view, "Insira o nome da disciplina.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                Bundle disciplinaBundle = new Bundle();
                disciplinaBundle.putString("disciplina", disciplina);

                String prof = etProfessor.getText().toString();
                if(prof.isEmpty()){
                    Snackbar.make(view, "Insira o nome do professor.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                Bundle professorBundle = new Bundle();
                disciplinaBundle.putString("professor", prof);

                if(corEscolhida == -1){
                    Snackbar.make(view, "Escolha uma cor para a disciplina.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                Bundle corBundle = new Bundle();
                disciplinaBundle.putInt("cor", corEscolhida);

                Snackbar.make(view, disciplina + " " + prof, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Disciplina infoD = new Disciplina(disciplina, prof, corEscolhida);
                Bundle infoClass = new Bundle();
                infoClass.putSerializable("infoKey", infoD);
                getParentFragmentManager().setFragmentResult("requestKey", infoClass);

                NavHostFragment navHostFragment = (NavHostFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.nav_contentMain);
                NavController navControl= navHostFragment.getNavController();
                navControl.popBackStack();
            }
        });

        return binding.getRoot();
    }

    private void criaVetorCores(){
        if(binding != null){
            cores.add(binding.corDisciplinaVermelho);
            cores.add(binding.corDisciplinaLaranja);
            cores.add(binding.corDisciplinaAmarelo);
            cores.add(binding.corDisciplinaVerde);
            cores.add(binding.corDisciplinaAzul);
            cores.add(binding.corDisciplinaRoxo);
        }
    }

    public class Disciplina implements Serializable {
        private String nomeDisciplina;
        private String nomeProfessor;
        private int corEscolhida;

        public Disciplina(String nomeDisciplina, String nomeProfessor, int corEscolhida){
            this.nomeDisciplina = nomeDisciplina;
            this.nomeProfessor = nomeProfessor;
            this.corEscolhida = corEscolhida;
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
    }
}