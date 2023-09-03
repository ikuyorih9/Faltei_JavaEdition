package com.example.faltei;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.faltei.databinding.FragmentConfiguracoesBinding;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FragmentConfiguracoes extends Fragment{
    FragmentConfiguracoesBinding binding;
    EditText editText_limitePresenca;

    EditText editText_horasPorCredito;

    Button button_salvar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConfiguracoesBinding.inflate(getLayoutInflater());
        editText_limitePresenca = binding.editTextLimitePresenca;
        editText_limitePresenca.setText(Double.toString(HomeActivity.mediaFaltas*100));

        editText_horasPorCredito = binding.editTextHorasPorCredito;
        editText_horasPorCredito.setText(Integer.toString(HomeActivity.horasPorCredito));

        button_salvar = binding.buttonSalvar;
        salvarListener();

        return binding.getRoot();
    }

    private void salvarListener(){
        button_salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double frequencia = Double.parseDouble(editText_limitePresenca.getText().toString())/100;
                int horasPorCredito = Integer.parseInt(editText_horasPorCredito.getText().toString());
                Log.d("HomeActivity", "-------------------------FREQUENCIA: " + frequencia);
                Log.d("HomeActivity", "-------------------------HORAS POR CREDITO: " + horasPorCredito);
                HomeActivity.mediaFaltas = frequencia;
                HomeActivity.horasPorCredito = horasPorCredito;
                ((HomeActivity) getActivity()).salvarConfiguracoes();

                Log.e("HomeActivity", getContext().getFilesDir().list()[0]);

                try{
                    FileOutputStream output = getContext().openFileOutput("sample.txt", Context.MODE_APPEND);
                    output.write("Olá eu sou um animal generico".getBytes());
                    output.close();

                    FileInputStream input = getContext().openFileInput("sample.txt");
                    String s = "";
                    int a = 0;
                    do{
                        a = input.read();
                        if(a == -1)
                            break;
                        Log.e("HomeActivity", Character.toString((char)a));
                        s += Character.toString((char)a);
                    }
                    while(a >= 0);
                    Log.e("HomeActivity", s);
                    input.close();

                }
                catch(IOException e){
                    Log.e("HomeActivity", e.getMessage());
                }



                NavHostFragment navHostFragment = (NavHostFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.nav_contentMain);
                NavController navControl= navHostFragment.getNavController();
                navControl.popBackStack();
            }
        });
    }

}