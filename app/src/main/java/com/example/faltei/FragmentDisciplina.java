package com.example.faltei;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.faltei.databinding.FragmentDisciplinaBinding;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class FragmentDisciplina extends Fragment {

    private FragmentDisciplinaBinding binding;
    private LinearLayout suporteLinLay;

    @Override
    public void onResume() {
        super.onResume();
        limpaBanners();
        displayBanners();
    }

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

    protected void criaBannerDisciplina(@NonNull Disciplina disciplina){
        String nomeDisciplina = disciplina.getNomeDisciplina();
        String nomeProfessor = disciplina.getNomeProfessor();
        int qtdCreditos = disciplina.getQuantidadeCreditos();
        int corEscolhida = disciplina.getCorEscolhida();

        LinearLayout suporte = (LinearLayout) getView().findViewById(R.id.suporte_LinLay);

        View banner = getLayoutInflater().inflate(R.layout.banner_disciplina, null, false);
        CardView bannerDisciplina = banner.findViewById(R.id.cardView_banner);
        LinearLayout layout_bannerDisciplina = banner.findViewById(R.id.layout_banner);

        layout_bannerDisciplina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundleBannerDisciplina = new Bundle();
                bundleBannerDisciplina.putSerializable("bundleBannerDisciplina", disciplina);
                getParentFragmentManager().setFragmentResult("bundleDisciplina", bundleBannerDisciplina);

                NavHostFragment navHostFragment = (NavHostFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.nav_contentMain);
                NavController navControl= navHostFragment.getNavController();
                navControl.navigate(R.id.action_addDisciplina);
            }
        });

        bannerDisciplina.setCardBackgroundColor(corEscolhida);
        TextView txtView_disciplina = bannerDisciplina.findViewById(R.id.txtView_nomeDisciplina);
        txtView_disciplina.setText(nomeDisciplina);

        LinearLayout layout_prof_aula = bannerDisciplina.findViewById(R.id.layout_prof_aula);

        TextView txtView_professor = layout_prof_aula.findViewById(R.id.txtView_nomeProfessor);
        txtView_professor.setText("Prof: " + nomeProfessor);

        TextView txtView_aulas = layout_prof_aula.findViewById(R.id.txtView_qtdAulas);
        String qtdAulasText = "Créditos: " + qtdCreditos;
        txtView_aulas.setText(qtdAulasText);

        suporte.addView(banner);

    }

    private void displayBanners(){
        if(HomeActivity.disciplinasSalvas != null)
            for(int i = 0; i < HomeActivity.disciplinasSalvas.size(); i++){
                Disciplina disciplina = HomeActivity.disciplinasSalvas.get(i);
                Log.d("HomeActivity", "DISCIPLINA " + i + ":");
                disciplina.mostraDadosDisciplina();
                criaBannerDisciplina(disciplina);
            }
    }

    private void limpaBanners(){
        Log.d("HomeActivity", "Disciplinas: Limpando banners!");
        LinearLayout suporte = (LinearLayout) getView().findViewById(R.id.suporte_LinLay);
        suporte.removeAllViews();
    }
}