package com.example.faltei;

import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.faltei.databinding.FragmentHomeBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class FragmentHome extends Fragment {

    private FragmentHomeBinding binding;
    private PieChart graficoHome;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        graficoHome = binding.chartHome;
        criaBannersDisciplinas();
        criaGraficoGeral();
        return binding.getRoot();
    }

    public void iniciaGrafico(){

    }

    public ArrayList<Disciplina> ordenaListaDisciplinas(){
        ArrayList <Disciplina> disciplinasOrdemPiorMelhor = new ArrayList<>();

        //ORDENA DO PIOR PARA O MELHOR
        for(int i = 0; i < HomeActivity.disciplinasSalvas.size(); i++){
            Disciplina disciplina = HomeActivity.disciplinasSalvas.get(i);
            if(disciplinasOrdemPiorMelhor.size()==0){
                disciplinasOrdemPiorMelhor.add(disciplina);
                Log.d("HomeActivity", "DISCIPLINAS GERAL: (" + i + ") " + disciplina.getNomeDisciplina());
                Log.d("HomeActivity", "DISCIPLINAS LOCAL: (" + i + ") " + disciplinasOrdemPiorMelhor.get(i).getNomeDisciplina());
                Log.d("HomeActivity", "");
                continue;
            }

            boolean ignorar = false;
            for(int j = 0; j < disciplinasOrdemPiorMelhor.size(); j++){
                Log.d("HomeActivity", "DISCIPLINAS GERAL: (" + i + ") " + disciplina.getNomeDisciplina());
                Log.d("HomeActivity", "DISCIPLINAS LOCAL: (" + j + ") " + disciplinasOrdemPiorMelhor.get(j).getNomeDisciplina());
                double percentualLocal = disciplinasOrdemPiorMelhor.get(j).getPercentualFaltas();
                double percentualGeral = disciplina.getPercentualFaltas();
                Log.d("HomeActivity", "PERCENTUAIS:");
                Log.d("HomeActivity", "--- Geral ("+disciplina.getNomeDisciplina() + "): " + percentualGeral);
                Log.d("HomeActivity", "--- LocaL ("+disciplinasOrdemPiorMelhor.get(j).getNomeDisciplina() + "): " + percentualLocal);
                Log.d("HomeActivity", "");
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
            Log.d("HomeActivity", "");
        }

        return disciplinasOrdemPiorMelhor;
    }

    public void criaBannersDisciplinas(){
        if(HomeActivity.disciplinasSalvas == null || HomeActivity.disciplinasSalvas.size()==0){
            adicionaTextCard();
            return;
        }
        else
            retiraTextCard();

        LinearLayout layout_listaDisciplinas = binding.layoutListaDisciplinas;
        ArrayList<Disciplina> disciplinasOrdenadas = ordenaListaDisciplinas();

        //APRESENTA OS BANNERS
        for(int i = 0; i < disciplinasOrdenadas.size(); i++){
            Log.d("HomeActivity", "BANNER DA DISCIPLINA: " + disciplinasOrdenadas.get(i).getNomeDisciplina());
            String nomeDisciplina = disciplinasOrdenadas.get(i).getNomeDisciplina();
            Double percentualFaltas = disciplinasOrdenadas.get(i).getPercentualFaltas();
            Log.d("HomeActivity", "---Percentual de faltas: " + percentualFaltas);
            View bannerHome = getLayoutInflater().inflate(R.layout.banner_home, null, false);
            CardView cardViewBanner = bannerHome.findViewById(R.id.cardView_bannerHome);
            if(percentualFaltas <= 0.06)
                cardViewBanner.setCardBackgroundColor(getResources().getColor(R.color.otimo));
            else if(percentualFaltas <= 0.12)
                cardViewBanner.setCardBackgroundColor(getResources().getColor(R.color.tranquilo));
            else if (percentualFaltas <= 0.18)
                cardViewBanner.setCardBackgroundColor(getResources().getColor(R.color.cuidado));
            else if (percentualFaltas <= 0.24)
                cardViewBanner.setCardBackgroundColor(getResources().getColor(R.color.perigo));
            else if (percentualFaltas <= 0.3)
                cardViewBanner.setCardBackgroundColor(getResources().getColor(R.color.emergencial));
            else
                cardViewBanner.setCardBackgroundColor(getResources().getColor(R.color.desistencia));

            TextView txtView_nomeDisciplinaBanner = bannerHome.findViewById(R.id.txtView_nomeDisciplina_bannerHome);
            txtView_nomeDisciplinaBanner.setText(nomeDisciplina);

            PieChart chart_disciplinaBanner = (PieChart) bannerHome.findViewById(R.id.chartDisciplina_bannerHome);

            disciplinasOrdenadas.get(i).iniciaGraficoFaltas(chart_disciplinaBanner, getResources().getColor(R.color.faltaGrafico));

            layout_listaDisciplinas.addView(bannerHome);
        }
    }

    public void criaGraficoGeral(){
        graficoHome.setNoDataText(getString(R.string.MensagemSemFalta));
        graficoHome.setNoDataTextColor(Color.BLACK);

        if(HomeActivity.disciplinasSalvas == null || HomeActivity.disciplinasSalvas.size()==0){
            graficoHome.setData(null);
            graficoHome.invalidate();
            return;
        }

        ArrayList <PieEntry> dadosEntrada = new ArrayList<>();
        ArrayList<Integer> cores = new ArrayList<>();

        for(int i = 0; i < HomeActivity.disciplinasSalvas.size(); i++){
            Disciplina disciplina = HomeActivity.disciplinasSalvas.get(i);
            if(disciplina.getQuantidadeFaltas() <= 0)
                continue;
            int quantidadeFaltas = disciplina.getQuantidadeFaltas();
            dadosEntrada.add(new PieEntry(quantidadeFaltas, disciplina.getNomeDisciplina()));
            cores.add(disciplina.getCorEscolhida());
        }

        if(dadosEntrada.size() == 0)
            return;

        PieDataSet dataSet = new PieDataSet(dadosEntrada, "");
        dataSet.setColors(cores);

        PieData data = new PieData(dataSet);

        graficoHome.setNoDataText(getString(R.string.MensagemSemFalta));
        graficoHome.setNoDataTextColor(Color.BLACK);

        graficoHome.setData(data);
        graficoHome.setRotationAngle(90);
        graficoHome.setHoleColor(Color.TRANSPARENT);
        graficoHome.getData().setValueTextSize(10);
        graficoHome.setHoleRadius(40f);
        graficoHome.setTransparentCircleRadius(45f);
        graficoHome.getDescription().setEnabled(false);
        graficoHome.setUsePercentValues(true);
        graficoHome.getData().setValueTextColor(Color.WHITE);
        graficoHome.invalidate();
    }

    public void retiraTextCard(){
        LinearLayout layout_listaDisciplinas = binding.layoutListaDisciplinas;
        for(int i = 0; i < layout_listaDisciplinas.getChildCount(); i++){
            View view = layout_listaDisciplinas.getChildAt(i);
            if(view instanceof TextView){
                layout_listaDisciplinas.removeView(view);
            }
        }
    }

    public void adicionaTextCard(){
        LinearLayout layout_listaDisciplinas = binding.layoutListaDisciplinas;
        TextView txtView = new TextView(getContext());
        txtView.setText(getString(R.string.MensagemInfoCards));
        float sp = getResources().getDimension(R.dimen.tamanhoTexto)/getResources().getDisplayMetrics().scaledDensity;
        txtView.setTextSize(sp);
        txtView.setGravity(Gravity.CENTER);
        txtView.setPadding(10,10,10,10);

        layout_listaDisciplinas.addView(txtView);
    }
}