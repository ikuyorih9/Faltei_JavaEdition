package com.example.faltei;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.example.faltei.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {
    private AppBarConfiguration configuracaoToolbar;
    private ActivityHomeBinding binding;

    private Button b;

    public static double mediaFaltas = 0.7;

    public static int horasPorCredito = 15;
    public static ArrayList<Disciplina> disciplinasSalvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("HomeActivity", "onCreate");
        carregarDisciplinas();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //Desativa o modo escuro.
        binding = ActivityHomeBinding.inflate(getLayoutInflater()); //Cria um objeto de vinculação.
        setContentView(binding.getRoot()); //Obtém a visualização e a ativa na tela.
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar)); //Configura a barra de ferramentas.
        //setSupportActionBar(binding.appBarHome.toolbar); //Configura a barra de ferramentas.

        DrawerLayout drawer = binding.drawerLayout; //Cria um layout Drawer, que permite a sobreposição do menu lateral.
        NavigationView navigationView = binding.navView; //Cria um menu de navegação padrão (menu lateral).
        configuracaoToolbar = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_disciplinas, R.id.nav_faltas).setOpenableLayout(drawer).build();

        //Gerencia a navegação do aplicativo.
        // Navigation flows and destinations are determined by the navigation graph owned by the controller
        NavController navController = Navigation.findNavController(this, R.id.nav_contentMain);
        NavigationUI.setupActionBarWithNavController(this, navController, configuracaoToolbar);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_contentMain);
        return NavigationUI.navigateUp(navController, configuracaoToolbar) || super.onSupportNavigateUp();
    }


    public void salvarDisciplinas(){
        Log.d("HomeActivity", "Salvando disciplinas!");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        int size = disciplinasSalvas.size();
        if(disciplinasSalvas == null)
            size = 0;
        Log.d("HomeActivity", "Salvando" + size + " disciplinas!");
        editor.putInt(getString(R.string.qtdDisciplinasKey), size);

        for(int i = 0; i < size; i++){
            Disciplina disciplina = disciplinasSalvas.get(i);
            String nomeDisciplina = disciplina.getNomeDisciplina();
            String nomeProfessor = disciplina.getNomeProfessor();
            int cor = disciplina.getCorEscolhida();
            int qtdCreditos = disciplina.getQuantidadeCreditos();
            int qtdFaltas = disciplina.getQuantidadeFaltas();

            editor.putString(getString(R.string.nomeDisciplinaKey) + i, nomeDisciplina);
            editor.putString(getString(R.string.nomeProfessorKey) + i, nomeProfessor);
            editor.putInt(getString(R.string.corDisciplinaKey) + i, cor);
            editor.putInt(getString(R.string.qtdAulasKey) + i, qtdCreditos);

            editor.putInt(getString(R.string.qtdFaltasKey) + i, qtdFaltas);
            for(int j = 0; j < qtdFaltas; j++){
                Date date = disciplina.getFalta(j);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String dateString = dateFormat.format(date);
                String prefixo = getString(R.string.nomeDisciplinaKey) + i;
                editor.putString(prefixo + getString(R.string.idFalta) + j, dateString);
            }
            Log.d("HomeActivity", "Disciplina " + nomeDisciplina + " salva!");
        }
        editor.apply();
    }

    public void carregarDisciplinas(){
        Log.d("HomeActivity", "Recuperando disciplinas...");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        int quantidadeDisciplinas = sharedPref.getInt(getString(R.string.qtdDisciplinasKey), -1);

        Log.d("HomeActivity", "Quantidade de disciplinas: " + quantidadeDisciplinas);

        if(quantidadeDisciplinas <= 0)
            return;

        if(disciplinasSalvas == null)
            disciplinasSalvas = new ArrayList<Disciplina>();
        else
            disciplinasSalvas.clear();

        for(int i = 0; i < quantidadeDisciplinas; i++){
            Log.d("HomeActivity", "Carregando disciplina " + i);
            String nomeDisciplina = sharedPref.getString(getString(R.string.nomeDisciplinaKey) + i, "Disciplina -");
            String nomeProfessor = sharedPref.getString(getString(R.string.nomeProfessorKey) + i, "Professor -");
            int cor = sharedPref.getInt(getString(R.string.corDisciplinaKey) + i, -1);
            int qtdCreditos = sharedPref.getInt(getString(R.string.qtdAulasKey) + i, -1);

            Disciplina disciplina = new Disciplina(nomeDisciplina, nomeProfessor, cor, qtdCreditos);

            int qtdFaltas = sharedPref.getInt(getString(R.string.qtdFaltasKey) + i, 0);
            for(int j = 0; j < qtdFaltas; j++){
                String key = getString(R.string.nomeDisciplinaKey) + i + getString(R.string.idFalta) + j;
                String dateString = sharedPref.getString(key, "dd/MM/yyyy");
                if(dateString != null){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        Date date = dateFormat.parse(dateString);
                        disciplina.adicionarFalta(date);
                    }
                    catch (ParseException e){
                        Log.e("HomeActivity", e.getMessage());
                    }

                }
            }
            disciplinasSalvas.add(disciplina);
        }
    }

    public void apagarDisciplina(Disciplina disciplina){
        int iDisciplina = disciplinasSalvas.indexOf(disciplina);
        Log.d("HomeActivity", "APAGAR DISCIPLINA!");
        Log.d("HomeActivity", "Apagar a disciplina: " + iDisciplina);

        if(iDisciplina == -1)
            return;

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        sharedPref.edit().remove(getString(R.string.nomeDisciplinaKey) + iDisciplina);
        sharedPref.edit().remove(getString(R.string.nomeProfessorKey) + iDisciplina);
        sharedPref.edit().remove(getString(R.string.corDisciplinaKey) + iDisciplina);
        sharedPref.edit().remove(getString(R.string.qtdAulasKey) + iDisciplina);

        int qtdFaltas = disciplina.getQuantidadeFaltas();
        for(int i = 0; i < qtdFaltas; i++){
            String prefixo = getString(R.string.nomeDisciplinaKey) + iDisciplina + getString(R.string.idFalta) + i;
            sharedPref.edit().remove(prefixo);
            disciplina.removerFalta(0);
        }
        sharedPref.edit().remove(getString(R.string.qtdFaltasKey) + iDisciplina);

        disciplinasSalvas.remove(iDisciplina);
        int quantidadeDisciplinas = disciplinasSalvas.size();
        sharedPref.edit().putInt(getString(R.string.qtdDisciplinasKey), quantidadeDisciplinas);
        sharedPref.edit().apply();
        Log.d("HomeActivity", "Agora há somente " + quantidadeDisciplinas + " disciplinas.");
    }
}