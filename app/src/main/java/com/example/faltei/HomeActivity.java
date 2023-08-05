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

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private AppBarConfiguration configuracaoToolbar;
    private ActivityHomeBinding binding;

    private Button b;

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
        if(disciplinasSalvas == null)
            return;

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        int size = disciplinasSalvas.size();
        editor.putInt("quantidadeDisciplinas", size);
        Log.d("HomeActivity", "Quantidade de disciplinas: " + size);

        for(int i = 0; i < size; i++){
            Disciplina disciplina = disciplinasSalvas.get(i);
            String nomeDisciplina = disciplina.getNomeDisciplina();
            String nomeProfessor = disciplina.getNomeProfessor();
            int cor = disciplina.getCorEscolhida();

            editor.putString("nomeDisciplina " + i, nomeDisciplina);
            editor.putString("nomeProfessor " + i, nomeProfessor);
            editor.putInt("corDisciplina " + i, cor);
            editor.apply();

            Log.d("HomeActivity", "Disciplina " + nomeDisciplina + " salva!");
        }
    }

    public void carregarDisciplinas(){
        Log.d("HomeActivity", "Recuperando disciplinas...");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        int quantidadeDisciplinas = sharedPref.getInt("quantidadeDisciplinas", -1);

        Log.d("HomeActivity", "Quantidade de disciplinas: " + quantidadeDisciplinas);

        if(quantidadeDisciplinas <= 0)
            return;

        if(disciplinasSalvas == null)
            disciplinasSalvas = new ArrayList<Disciplina>();
        else
            disciplinasSalvas.clear();

        for(int i = 0; i < quantidadeDisciplinas; i++){
            Log.d("HomeActivity", "Carregando disciplina " + i);
            String nomeDisciplina = sharedPref.getString("nomeDisciplina " + i, "Disciplina -");
            String nomeProfessor = sharedPref.getString("nomeProfessor " + i, "Professor -");
            int cor = sharedPref.getInt("corDisciplina " + i, -1);

            Disciplina disciplina = new Disciplina(nomeDisciplina, nomeProfessor, cor);
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
        sharedPref.edit().remove("nomeDisciplina " + iDisciplina);

        disciplinasSalvas.remove(iDisciplina);
    }
}