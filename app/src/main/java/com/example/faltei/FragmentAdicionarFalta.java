package com.example.faltei;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;

import com.example.faltei.databinding.FragmentAdicionarFaltaBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class FragmentAdicionarFalta extends Fragment {
    private FragmentAdicionarFaltaBinding binding;
    private Button bAdicionarFalta;
    private CalendarView calendarView;
    private Date selectedDate;

    private int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdicionarFaltaBinding.inflate(getLayoutInflater());
        bAdicionarFalta = binding.buttonAdicionarFalta;
        calendarView = binding.calendarView;

        id = getArguments().getInt("disciplinaIdKey");

        adicionarFaltaListener();
        calendarListener();
        return binding.getRoot();
    }

    public void adicionarFaltaListener(){
        bAdicionarFalta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(HomeActivity.disciplinasSalvas == null
                || HomeActivity.disciplinasSalvas.isEmpty()){
                    Snackbar.make(view, "Não há disciplinas para adicionar faltas.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }

                Log.d("HomeActivity", "ADICIONAR FALTA LISTENER - ID: " + id);
                Disciplina disciplinaSelecionada = HomeActivity.disciplinasSalvas.get(id);

                if(selectedDate == null){
                    Snackbar.make(view, "Selecione uma data!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }

                if(disciplinaSelecionada.getQuantidadeFaltas() >= disciplinaSelecionada.getQuantidadeAulas()){
                    Snackbar.make(view, "Você já atingiu o máximo de faltas possível!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    return;
                }

                Log.d("HomeActivity", "DATA SELECIONADA: " + selectedDate.toString());
                disciplinaSelecionada.adicionarFaltaOrdenado(selectedDate);

                ((HomeActivity)getActivity()).salvarDisciplinas();

                NavHostFragment navHostFragment = (NavHostFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.nav_contentMain);
                NavController navControl= navHostFragment.getNavController();
                navControl.popBackStack();
            }
        });
    }

    public void calendarListener(){
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                selectedDate = new GregorianCalendar(year, month, day).getTime();
                Log.d("HomeActivity", "DATA: " + selectedDate.toString());
            }
        });
    }
}