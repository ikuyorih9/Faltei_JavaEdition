package com.example.faltei;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.faltei.databinding.FragmentHomeBinding;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;

public class FragmentHome extends Fragment {

    private FragmentHomeBinding binding;
    private PieChart graficoHome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        graficoHome = binding.chartHome;
        criaListaDisciplinas();
        return binding.getRoot();
    }

    public void iniciaGrafico(){

    }

    public void criaListaDisciplinas(){
        if(HomeActivity.disciplinasSalvas == null || HomeActivity.disciplinasSalvas.size()==0)
            return;

        View bannerHome = getLayoutInflater().inflate(R.layout.banner_home, null, false);
        CardView cardViewBanner = bannerHome.findViewById(R.id.cardView_bannerHome);

        LinearLayout layout_listaDisciplinas = binding.layoutListaDisciplinas;
        ArrayList <Disciplina> disciplinasOrdemPiorMelhor = new ArrayList<>();

        //ORDENA DO PIOR PARA O MELHOR
        for(int i = 0; i < HomeActivity.disciplinasSalvas.size(); i++){
            Disciplina disciplina = HomeActivity.disciplinasSalvas.get(i);
            boolean ignorar = false;
            for(int j = 0; j < HomeActivity.disciplinasSalvas.size(); j++){
                if(disciplinasOrdemPiorMelhor.size()==0){
                    disciplinasOrdemPiorMelhor.add(disciplina);
                    ignorar = true;
                    break;
                }
                double percentualLocal = disciplinasOrdemPiorMelhor.get(j).getPercentualFaltas();
                double percentualGeral = disciplina.getPercentualFaltas();
                if(percentualGeral >= percentualLocal){
                    disciplinasOrdemPiorMelhor.add(j,disciplina);
                    ignorar = true;
                    break;
                }
            }
            if(ignorar)
                continue;
            disciplinasOrdemPiorMelhor.add(disciplina);
        }

        //MOSTRA ORDENAÇÃO.
        for(int i = 0; i < HomeActivity.disciplinasSalvas.size(); i++){
            disciplinasOrdemPiorMelhor.get(i).mostraDadosDisciplina();
        }
    }
}