package com.example.faltei;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.faltei.databinding.FragmentConfiguracoesBinding;

public class FragmentConfiguracoes extends Fragment {
    FragmentConfiguracoesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConfiguracoesBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}