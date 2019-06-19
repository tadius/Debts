package com.tadiuzzz.debts;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.tadiuzzz.debts.data.DebtRepository;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {

    NavController navController;

    @Inject
    DebtRepository debtRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Dagger injection
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        Toast.makeText(this, debtRepository.getStringTest(), Toast.LENGTH_LONG).show();

    }
}
