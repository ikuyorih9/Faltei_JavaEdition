package com.example.faltei;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private LinearLayout bannerSelecionado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        graficoHome = binding.chartHome;
        criaBannersDisciplinas();
        criaGraficoGeral();

        return binding.getRoot();
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
            int limiteFaltas = disciplinasOrdenadas.get(i).getQuantidadeLimiteFaltas();

            Log.d("HomeActivity", "---Percentual de faltas: " + percentualFaltas);
            View bannerHome = getLayoutInflater().inflate(R.layout.banner_home, null, false);
            CardView cardViewBanner = bannerHome.findViewById(R.id.cardView_bannerHome);
            ImageView src = bannerHome.findViewById(R.id.imageView_situacao);

            String texto;
            if(percentualFaltas == 0.0){
                texto = getString(R.string.MensagemSemFalta);
                cardViewBanner.setCardBackgroundColor(getResources().getColor(R.color.otimo));
                src.setImageResource(R.drawable.otimo);
            }
            else if(percentualFaltas <= 0.06){
                texto = getString(R.string.MensagemTranquilo);
                cardViewBanner.setCardBackgroundColor(getResources().getColor(R.color.otimo));
                src.setImageResource(R.drawable.otimo);
            }
            else if(percentualFaltas <= 0.12) {
                texto = getString(R.string.MensagemOk);
                cardViewBanner.setCardBackgroundColor(getResources().getColor(R.color.tranquilo));
                src.setImageResource(R.drawable.tranquilo);
            }
            else if (percentualFaltas <= 0.18) {
                texto = getString(R.string.MensagemCuidado);
                cardViewBanner.setCardBackgroundColor(getResources().getColor(R.color.cuidado));
                src.setImageResource(R.drawable.ok);
            }
            else if (percentualFaltas <= 0.24) {
                texto = getString(R.string.MensagemPerigo);
                cardViewBanner.setCardBackgroundColor(getResources().getColor(R.color.perigo));
                src.setImageResource(R.drawable.cuidado);
            }
            else if (percentualFaltas <= 0.3) {
                texto = getString(R.string.MensagemEmergente);
                cardViewBanner.setCardBackgroundColor(getResources().getColor(R.color.emergencial));
                src.setImageResource(R.drawable.emergente);
            }
            else {
                texto = getString(R.string.MensagemReprovado);
                cardViewBanner.setCardBackgroundColor(getResources().getColor(R.color.desistencia));
                src.setImageResource(R.drawable.skull);
                cardViewBanner.setAlpha(0.3f);
            }

            TextView txtView_nomeDisciplinaBanner = bannerHome.findViewById(R.id.txtView_nomeDisciplina_bannerHome);
            txtView_nomeDisciplinaBanner.setText(nomeDisciplina);

            TextView txtView_percetual = bannerHome.findViewById(R.id.txtView_percentual);
            txtView_percetual.setText(String.format("%.1f",percentualFaltas*100) + "%");

            TextView txtView_infoDisciplina = bannerHome.findViewById(R.id.txtView_infoDisciplina_home);
            txtView_infoDisciplina.setText(trocaXporNumero(texto,limiteFaltas));

            PieChart chart_disciplinaBanner = (PieChart) bannerHome.findViewById(R.id.chart_disciplina_home);
            disciplinasOrdenadas.get(i).iniciaGraficoFaltas(chart_disciplinaBanner, getResources().getColor(R.color.faltaGrafico));

            LinearLayout layout_info = bannerHome.findViewById(R.id.layout_info_home);

            AnimatorSet setAnim = new AnimatorSet();
            setAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            ValueAnimator anim = ValueAnimator.ofInt(0,250).setDuration(200);

            anim.addUpdateListener(animation->{
                int value = (int) animation.getAnimatedValue();
                bannerSelecionado.getLayoutParams().height = value;
                bannerSelecionado.requestLayout();
                Log.d("HomeActivity", "HEIGHT = " + bannerSelecionado.getHeight());
            });

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    Log.d("HomeActivity", "HEIGHT FINAL: " + bannerSelecionado.getHeight());
                    if(bannerSelecionado.getHeight() >= 5) {
                        ViewGroup.LayoutParams layoutParams = bannerSelecionado.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    }
                    else
                        bannerSelecionado.getLayoutParams().height = 0;
                }
            });

            setAnim.play(anim);
            Log.d("HomeActivity", "HEIGHT: " + layout_info.getHeight());
            LinearLayout layout_card = bannerHome.findViewById(R.id.layout_internoCardView);
            layout_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bannerSelecionado = layout_info;
                    Log.d("HomeActivity", "TAMANHO CERTO: " + bannerSelecionado.getMeasuredHeight());
                    Log.d("HomeActivity", "BANNER SELECIONADO: " + bannerSelecionado.toString());
                    if(layout_info.getHeight() == 0){
                        Log.d("HomeActivity", "HEIGHT INICIAL: " + layout_info.getHeight());
                        anim.setIntValues(0,250);
                    }
                    else{
                        Log.d("HomeActivity", "HEIGHT INICIAL: " + layout_info.getHeight());
                        anim.setIntValues(250,0);
                    }
                    setAnim.start();
                }
            });
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

    public String trocaXporNumero(String mensagem, int numero){
        String texto;
        texto = mensagem.replace("X", Integer.toString(numero));
        return texto;
    }
}