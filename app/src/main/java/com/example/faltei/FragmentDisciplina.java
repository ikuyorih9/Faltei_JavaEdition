package com.example.faltei;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.faltei.databinding.FragmentDisciplinaBinding;
import com.example.faltei.databinding.FragmentHomeBinding;

public class FragmentDisciplina extends Fragment {

    private FragmentDisciplinaBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDisciplinaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}