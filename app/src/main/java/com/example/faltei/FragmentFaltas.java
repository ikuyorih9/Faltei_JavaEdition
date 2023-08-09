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
                navControl.navigate(R.id.action_mostrarFaltas, bundleBannerFalta);
            }
        });

        //Obtém o gráfico do banner.
        PieChart grafico = bannerDisciplina.findViewById(R.id.chart_faltasDisciplina);
        disciplina.iniciaGraficoFaltas(grafico, getResources().getColor(R.color.faltaGrafico));

        return banner;
    }

    public void configuraGraficoGeral(){
        geralFaltas.setNoDataText(getString(R.string.MensagemSemFalta));
        geralFaltas.setNoDataTextColor(Color.BLACK);

        if(HomeActivity.disciplinasSalvas == null || HomeActivity.disciplinasSalvas.size()==0){
            geralFaltas.setData(null);
            geralFaltas.invalidate();
            return;
        }

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

        PieDataSet dataSet = new PieDataSet(dadosGeraisFaltas, "");
        dataSet.setColors(corDisciplinas);
        PieData data = new PieData(dataSet);
        geralFaltas.setData(data);
        geralFaltas.setRotationAngle(90);
        geralFaltas.setHoleColor(Color.TRANSPARENT);
        geralFaltas.getData().setValueTextSize(10);
        geralFaltas.setHoleRadius(40f);
        geralFaltas.setTransparentCircleRadius(45f);
        geralFaltas.getDescription().setEnabled(false);
        geralFaltas.setUsePercentValues(true);
        geralFaltas.getData().setValueTextColor(Color.WHITE);
        geralFaltas.invalidate();
    }
}