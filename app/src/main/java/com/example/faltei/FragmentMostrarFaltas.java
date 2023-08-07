package com.example.faltei;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.faltei.databinding.FragmentMostrarFaltasBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FragmentMostrarFaltas extends Fragment {

    private FragmentMostrarFaltasBinding binding;
    private Disciplina disciplina;
    private PieChart graficoDisciplina;

    private TextView txtView_numFaltas;
    private TextView txtView_numAulas;
    private TextView txtView_faltasRestantes;

    private ListView listView_listaFaltas;

    private ArrayList<Date> datasSelecionadas;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("bundleFragment", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                int i = result.getInt("bundleBannerFalta",-1);
                if(i == -1)
                    Log.e("HomeActivity", "Erro ao receber disciplina em FragmentMostrarFaltas");
                else
                    disciplina = (Disciplina) HomeActivity.disciplinasSalvas.get(i);
                criarGrafico();
                configuraInfo();
                configuraLista();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMostrarFaltasBinding.inflate(getLayoutInflater());
        graficoDisciplina = binding.chartEstatisticaDisciplina;
        txtView_numAulas = binding.txtViewNumAulas;
        txtView_numFaltas = binding.txtViewNumFaltas;
        txtView_faltasRestantes = binding.txtViewFaltasRestantes;
        listView_listaFaltas = binding.listViewLista;
        removerFalta();
        itemSelecionadoListener();

        return binding.getRoot();
    }

    public void criarGrafico(){
        if(disciplina == null){
            Log.d("HomeActivity", "FragmentMostrarFaltas: disciplina não inicializada!");
            return;
        }

        Log.d("HomeActivity", disciplina.toString());
        int qtdFaltas = disciplina.getQuantidadeFaltas();
        int qtdAulasRestantes = disciplina.getQuantidadeAulas() - qtdFaltas;
        ArrayList <PieEntry> dadosEntrada = new ArrayList<>();
        dadosEntrada.add(new PieEntry(qtdFaltas, "Faltas"));
        dadosEntrada.add(new PieEntry(qtdAulasRestantes, "Aulas restantes"));

        ArrayList<Integer> cores = new ArrayList<>();
        cores.add(disciplina.getCorEscolhida());
        cores.add(getResources().getColor(R.color.restanteGrafico));

        PieDataSet dataSet = new PieDataSet(dadosEntrada, "A");
        dataSet.setColors(cores);

        PieData data = new PieData(dataSet);

        graficoDisciplina.setData(data);
        graficoDisciplina.setRotationAngle(90);
        graficoDisciplina.setHoleColor(Color.TRANSPARENT);
        graficoDisciplina.getDescription().setEnabled(false);
        graficoDisciplina.setUsePercentValues(true);
        graficoDisciplina.invalidate();
    }

    public void configuraInfo(){
        int numAulas = disciplina.getQuantidadeAulas();
        int numFaltas = disciplina.getQuantidadeFaltas();
        int numPresencasLimite = (int)((HomeActivity.mediaFaltas) * numAulas) + 1;
        int numFaltasRestantes = numAulas - numPresencasLimite;

        Log.d("HomeActivity", "Número de aulas: " + numAulas);
        Log.d("HomeActivity", "Número de faltas: " + numFaltas);
        Log.d("HomeActivity", "Número de faltas restantes: " + numFaltasRestantes);

        txtView_numAulas.setText(getString(R.string.numAulas) + numAulas);
        txtView_numFaltas.setText(getString(R.string.numFaltas) + numFaltas);
        txtView_faltasRestantes.setText(getString(R.string.numFaltasRestantes) + numFaltasRestantes);
    }

    public void configuraLista(){
        ArrayList<String> datasFaltas = disciplina.listaDataFaltas();
        ArrayAdapter <String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice, datasFaltas);
        listView_listaFaltas.setAdapter(adapter);
    }

    public void removerFalta(){
        binding.removerFalta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(datasSelecionadas == null || datasSelecionadas.size() == 0){
                    Snackbar.make(view, "Selecione um registro de falta!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }
                int qtdDatas = datasSelecionadas.size();
                for(int i = 0; i < qtdDatas; i++){
                    Date data = datasSelecionadas.get(0);
                    Log.d("HomeActivity", "DATA SELECIONADA PARA APAGAR: " + data.toString());
                    disciplina.removerFalta(data);
                    datasSelecionadas.remove(0);
                }
                criarGrafico();
                configuraInfo();
                configuraLista();
                Snackbar.make(view, "Registro(s) de falta removido(s)!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    public void itemSelecionadoListener(){
        listView_listaFaltas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFromList =(String) (listView_listaFaltas.getItemAtPosition(i));
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = format.parse(selectedFromList);
                    if(datasSelecionadas == null)
                        datasSelecionadas = new ArrayList<>();
                    if(listView_listaFaltas.isItemChecked(i))
                        datasSelecionadas.add(date);
                    else
                        datasSelecionadas.remove(date);
                }
                catch (ParseException e){
                    Log.e("HomeActivity", e.getMessage());
                }
            }
        });


    }
}