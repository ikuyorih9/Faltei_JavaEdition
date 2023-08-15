package com.example.faltei;

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

                NavHostFragment navHostFragment = (NavHostFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.nav_contentMain);
                NavController navControl= navHostFragment.getNavController();
                navControl.popBackStack();
            }
        });
    }

}