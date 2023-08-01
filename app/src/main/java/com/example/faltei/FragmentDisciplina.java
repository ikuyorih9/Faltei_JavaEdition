package com.example.faltei;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.faltei.databinding.FragmentDisciplinaBinding;
import com.google.android.material.snackbar.Snackbar;



public class FragmentDisciplina extends Fragment {

    private FragmentDisciplinaBinding binding;
    private LinearLayout suporteLinLay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDisciplinaBinding.inflate(inflater, container, false);
        binding.addDisciplina.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                NavHostFragment navHostFragment = (NavHostFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.nav_contentMain);
                NavController navControl= navHostFragment.getNavController();
                navControl.navigate(R.id.action_addDisciplina);

            }
        });
        return binding.getRoot();
    }

    protected void criaDisciplina(){
        LinearLayout suporte = (LinearLayout) getView().findViewById(R.id.suporte_LinLay);
        View banner = getLayoutInflater().inflate(R.layout.banner_disciplina, null, false);
        suporte.addView(banner);

    }
}