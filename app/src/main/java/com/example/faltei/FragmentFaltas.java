package com.example.faltei;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.faltei.databinding.ActivityHomeBinding;
import com.example.faltei.databinding.FragmentDisciplinaBinding;
import com.example.faltei.databinding.FragmentFaltasBinding;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FragmentFaltas extends Fragment {
    private FragmentFaltasBinding binding;

    private PieChart geralFaltas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFaltasBinding.inflate(getLayoutInflater()); //Cria um objeto de vinculação.

        binding.addFalta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment navHostFragment = (NavHostFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.nav_contentMain);
                NavController navControl= navHostFragment.getNavController();
                navControl.navigate(R.id.action_addFaltas);
            }
        });

        geralFaltas = binding.chartGeral;

        configuraGraficoGeral();
        displayBannerDisciplinas();

        return binding.getRoot();
    }

    public void displayBannerDisciplinas(){
        if(HomeActivity.disciplinasSalvas == null)
            return;

        LinearLayout layoutGeral = (LinearLayout) binding.layoutBannersFalta;
        for(int i = 0; i < HomeActivity.disciplinasSalvas.size(); i++){
            Log.d("HomeActivity", "Disciplina: " + HomeActivity.disciplinasSalvas.get(i).getNomeDisciplina());
            Disciplina disciplina = HomeActivity.disciplinasSalvas.get(i);
            View banner = criaBannerDisciplina(disciplina);
            layoutGeral.addView(banner);
        }
    }

    public View criaBannerDisciplina(Disciplina disciplina){
        //Obtém o fragmento do banner de uma disciplina.
        View banner = getLayoutInflater().inflate(R.layout.banner_falta, null, false);
        //Obtém o layout do banner de uma disciplina.
        LinearLayout bannerDisciplina = banner.findViewById(R.id.layout_banner_falta);

        bannerDisciplina.setBackgroundColor(disciplina.getCorEscolhida());

        //Obtém o textView do banner.
        TextView txtView_nomeDisciplina = bannerDisciplina.findViewById(R.id.txtView_nomeDisciplina_falta);
        txtView_nomeDisciplina.setText(disciplina.getNomeDisciplina());

        bannerDisciplina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iDisciplina = HomeActivity.disciplinasSalvas.indexOf(disciplina);
                Bundle bundleBannerFalta = new Bundle();
                bundleBannerFalta.putInt("bundleBannerFalta", iDisciplina);
                getParentFragmentManager().setFragmentResult("bundleFragment", bundleBannerFalta);

                NavHostFragment navHostFragment = (NavHostFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.nav_contentMain);
                NavController navControl= navHostFragment.getNavController();
                navControl.navigate(R.id.action_mostrarFaltas);
            }
        });

        //Obtém o gráfico do banner.
        PieChart grafico = bannerDisciplina.findViewById(R.id.chart_faltasDisciplina);

        int qtdFaltas = disciplina.getQuantidadeFaltas();
        //int qtdFaltas = 2;
        int qtdAulas = disciplina.getQuantidadeAulas();
        int qtdRestante = qtdAulas - qtdFaltas;

        Log.d("HomeActivity","Quantidade de aulas: " + qtdAulas);
        Log.d("HomeActivity","Quantidade de faltas: " + qtdFaltas);
        Log.d("HomeActivity","Quantidade restante: " + qtdRestante);

        ArrayList<PieEntry> dados = new ArrayList<>();
        dados.add(new PieEntry(qtdFaltas, "Faltas"));
        dados.add(new PieEntry(qtdRestante, "Restante"));

        ArrayList<Integer> cores = new ArrayList<>();
        cores.add(getResources().getColor(R.color.faltaGrafico));
        cores.add(getResources().getColor(R.color.restanteGrafico));

        PieDataSet dataSett = new PieDataSet(dados, "");
        dataSett.setColors(cores);
        PieData datta = new PieData(dataSett);

        grafico.setData(datta);
        grafico.setRotationAngle(90);
        grafico.setHoleColor(Color.TRANSPARENT);
        grafico.getDescription().setEnabled(false);
        grafico.getLegend().setEnabled(false);
        grafico.getData().setDrawValues(false);
        grafico.setDrawEntryLabels(false);
        grafico.setUsePercentValues(true);
        grafico.invalidate();

        return banner;
    }

    public void configuraGraficoGeral(){
        ArrayList<PieEntry> dadosGeraisFaltas = new ArrayList<>();
        ArrayList<Integer> corDisciplinas = new ArrayList<>();
        int qtdFaltasTotal = 0;
        int qtdAulasTotal = 0;
        for(int i = 0; i < HomeActivity.disciplinasSalvas.size(); i++){
            Disciplina disciplina = HomeActivity.disciplinasSalvas.get(i);
            qtdFaltasTotal += disciplina.getQuantidadeFaltas();
            qtdAulasTotal += disciplina.getQuantidadeAulas();
            if(HomeActivity.disciplinasSalvas.get(i).getQuantidadeFaltas() != 0) {
                dadosGeraisFaltas.add(new PieEntry(disciplina.getQuantidadeFaltas(), disciplina.getNomeDisciplina()));
                corDisciplinas.add(disciplina.getCorEscolhida());
            }
        }

        int qtdAulasRestante = qtdAulasTotal - qtdFaltasTotal;
        dadosGeraisFaltas.add(new PieEntry(qtdAulasRestante, "Aulas restantes"));
        corDisciplinas.add(getResources().getColor(R.color.restanteGrafico));

        PieDataSet dataSet = new PieDataSet(dadosGeraisFaltas, "Faltas");
        dataSet.setColors(corDisciplinas);
        PieData data = new PieData(dataSet);
        geralFaltas.setData(data);
        geralFaltas.setRotationAngle(90);
        geralFaltas.setHoleColor(Color.TRANSPARENT);
        geralFaltas.getDescription().setEnabled(false);
        geralFaltas.setUsePercentValues(true);
        geralFaltas.getData().setValueTextColor(Color.WHITE);
        geralFaltas.invalidate();
    }
}