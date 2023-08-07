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
    FragmentAdicionarFaltaBinding binding;
    Button bAdicionarFalta;
    Spinner spinnerDisciplina;
    CalendarView calendarView;

    Date selectedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdicionarFaltaBinding.inflate(getLayoutInflater());
        bAdicionarFalta = binding.buttonAdicionarFalta;
        spinnerDisciplina = binding.spinnerDisciplina;
        calendarView = binding.calendarView;

        adicionarFaltaListener();
        configuraSpinnerDisciplinas();
        calendarListener();;

        return binding.getRoot();
    }

    public void configuraSpinnerDisciplinas(){
        ArrayList<String> nomeDisciplinas = new ArrayList<String>();
        for(int i = 0; i <HomeActivity.disciplinasSalvas.size(); i++){
            Disciplina disciplina = HomeActivity.disciplinasSalvas.get(i);
            nomeDisciplinas.add(disciplina.getNomeDisciplina());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,nomeDisciplinas);
        spinnerDisciplina.setAdapter(adapter);
    }

    public void adicionarFaltaListener(){
        bAdicionarFalta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iDisciplina = (int) spinnerDisciplina.getSelectedItemId();
                Disciplina disciplinaSelecionada = HomeActivity.disciplinasSalvas.get(iDisciplina);

                if(selectedDate == null){
                    Snackbar.make(view, "Selecione uma data!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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