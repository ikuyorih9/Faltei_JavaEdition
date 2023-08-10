package com.example.faltei;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.IDNA;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
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
    private Button bEditar;
    private Button bApagar;
    private EditText etNomeDisciplina;
    private EditText etProfessor;
    private EditText etQtdCreditos;
    private ArrayList <ImageView> cores;
    private LinearLayout amostraCor;
    private LinearLayout botoesAlternativos;
    private FragmentAdicionarDisciplinaBinding binding;
    private int corEscolhida;
    private Disciplina disciplina;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("HomeActivity", "AAAA!");
        binding = FragmentAdicionarDisciplinaBinding.inflate(inflater, container, false);
        bCriarDisciplina = binding.buttonCriarDisciplina;
        bEditar = binding.buttonEditarDisciplina;
        bApagar = binding.buttonApagarDisciplina;

        etNomeDisciplina = binding.editTextNomeDisciplina;
        etProfessor = binding.editTextProfessor;
        etQtdCreditos = binding.editTextQtdAulas;

        amostraCor = binding.layAmostraCor;
        botoesAlternativos = binding.layoutBotoesAlternativos;

        getParentFragmentManager().setFragmentResultListener("bundleDisciplina", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Disciplina disciplinaRecebida = ((Disciplina) result.getSerializable("bundleBannerDisciplina"));
                etNomeDisciplina.setText(disciplinaRecebida.getNomeDisciplina());
                etProfessor.setText(disciplinaRecebida.getNomeProfessor());
                etQtdCreditos.setText("" + disciplinaRecebida.getQuantidadeCreditos());

                corEscolhida = disciplinaRecebida.getCorEscolhida();
                amostraCor.setBackgroundColor(corEscolhida);

                apagarDisciplinaListener(disciplinaRecebida);
                editarDisciplinaListener(disciplinaRecebida);

                bCriarDisciplina.setVisibility(View.INVISIBLE);
                botoesAlternativos.setVisibility(View.VISIBLE);
            }
        });

        corEscolhida = -1;
        cores = new ArrayList<ImageView>();
        criaVetorCores();

        disciplina = new Disciplina();
        disciplina.setCorEscolhida(-1);

        for(int i = 0; i < cores.size(); i++){
            selecionarCorListener(i);
        }

        adicionarDisciplinaListener();

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

    private void editarDisciplinaListener(Disciplina disciplinaRecebida){
        bEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeDisciplina = etNomeDisciplina.getText().toString();
                if(nomeDisciplina.isEmpty()){
                    Snackbar.make(view, "Insira o nome da disciplina.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }
                disciplina.setNomeDisciplina(nomeDisciplina);

                String nomeProfessor = etProfessor.getText().toString();
                if(nomeProfessor.isEmpty()){
                    Snackbar.make(view, "Insira o nome do professor.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }
                disciplina.setNomeProfessor(nomeProfessor);

                Log.d("HomeActivity", "QUANTIDADE AULAS: " + etQtdCreditos.getText() + etQtdCreditos.getText());

                if(etQtdCreditos.getText().toString().isEmpty()
                        || Integer.parseInt(etQtdCreditos.getText().toString()) <= 0){
                    Snackbar.make(view, "Insira uma quantidade de aulas válida.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }
                int qtdCreditos = Integer.parseInt(etQtdCreditos.getText().toString());
                disciplina.setQuantidadeCreditos(qtdCreditos);
                disciplina.calculaQuantidadeAulas();

                disciplina.setCorEscolhida(corEscolhida);
                if(disciplina.getCorEscolhida() == -1){
                    Snackbar.make(view, "Escolha uma cor para a disciplina.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }

                Snackbar.make(view, "Disciplina criada!", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                disciplina.setFaltas(disciplinaRecebida.getFaltas());

                int iDisciplina = HomeActivity.disciplinasSalvas.indexOf(disciplinaRecebida);
                HomeActivity.disciplinasSalvas.remove(iDisciplina);
                HomeActivity.disciplinasSalvas.add(iDisciplina, disciplina);

                ((HomeActivity) getActivity()).salvarDisciplinas();

                bCriarDisciplina.setVisibility(View.VISIBLE);
                botoesAlternativos.setVisibility(View.INVISIBLE);

                NavHostFragment navHostFragment = (NavHostFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.nav_contentMain);
                NavController navControl= navHostFragment.getNavController();
                navControl.popBackStack();
            }
        });
    }

    private void selecionarCorListener(int i){
        cores.get(i).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable backgroud = view.getBackground();
                if(backgroud instanceof ColorDrawable)
                    corEscolhida = ((ColorDrawable) backgroud).getColor();
                    //disciplina.setCorEscolhida(((ColorDrawable) backgroud).getColor());
                else
                    disciplina.setCorEscolhida(-1);
                Log.d("HomeActivity", "Cor clicada: " + corEscolhida);
                amostraCor.setBackgroundColor(corEscolhida);
            }
        });
    }

    private void apagarDisciplinaListener(Disciplina disciplinaRecebida){
        bApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) getActivity()).apagarDisciplina(disciplinaRecebida);
                ((HomeActivity) getActivity()).salvarDisciplinas();

                bCriarDisciplina.setVisibility(View.VISIBLE);
                botoesAlternativos.setVisibility(View.INVISIBLE);

                Snackbar.make(view, "Disciplina apagada!", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                NavHostFragment navHostFragment = (NavHostFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.nav_contentMain);
                NavController navControl= navHostFragment.getNavController();
                navControl.popBackStack();
            }
        });
    }

    private void adicionarDisciplinaListener(){
        bCriarDisciplina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeDisciplina = etNomeDisciplina.getText().toString();
                if(nomeDisciplina.isEmpty()){
                    Snackbar.make(view, "Insira o nome da disciplina.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }
                disciplina.setNomeDisciplina(nomeDisciplina);

                String nomeProfessor = etProfessor.getText().toString();
                if(nomeProfessor.isEmpty()){
                    Snackbar.make(view, "Insira o nome do professor.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }
                disciplina.setNomeProfessor(nomeProfessor);

                if(etQtdCreditos.getText().toString().isEmpty()
                        || Integer.parseInt(etQtdCreditos.getText().toString()) <= 0){
                    Snackbar.make(view, "Insira uma quantidade de aulas válida.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }
                int qtdCreditos = Integer.parseInt(etQtdCreditos.getText().toString());
                disciplina.setQuantidadeCreditos(qtdCreditos);
                disciplina.calculaQuantidadeAulas();

                disciplina.setCorEscolhida(corEscolhida);
                if(disciplina.getCorEscolhida() == -1){
                    Snackbar.make(view, "Escolha uma cor para a disciplina.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }

                Snackbar.make(view, "Disciplina criada!", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                if(HomeActivity.disciplinasSalvas == null)
                    HomeActivity.disciplinasSalvas = new ArrayList<Disciplina>();

                if(HomeActivity.disciplinasSalvas!=null)
                    HomeActivity.disciplinasSalvas.add(disciplina);

                ((HomeActivity) getActivity()).salvarDisciplinas();

                NavHostFragment navHostFragment = (NavHostFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.nav_contentMain);
                NavController navControl= navHostFragment.getNavController();
                navControl.popBackStack();
            }
        });
    }
}