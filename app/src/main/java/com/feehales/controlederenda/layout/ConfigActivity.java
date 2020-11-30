package com.feehales.controlederenda.layout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.feehales.controlederenda.R;
import com.feehales.controlederenda.constant.constant;
import com.feehales.controlederenda.datas.SharedPrefences;

public class ConfigActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private ConfigActivity.ViewHolder mViewHolder = new ConfigActivity.ViewHolder();
    private SharedPrefences mSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        this.mSharedPreference = new SharedPrefences(this);

        //ID dos EditText
        this.mViewHolder.salariofixo = findViewById(R.id.valor_salario);
        this.mViewHolder.poupafixo = findViewById(R.id.valor_poupancafixo);
        this.mViewHolder.totalfixo = findViewById(R.id.valor_fixo);

        //ID dos Buttuns
        this.mViewHolder.salvasalario = findViewById(R.id.salvar_salario);
        this.mViewHolder.salvafixo = findViewById(R.id.salvar_fixo);
        this.mViewHolder.login = findViewById(R.id.login);
        this.mViewHolder.salvarpoupafixo = findViewById(R.id.salvar_fixopoupanca);

        //buttun
        this.mViewHolder.salvasalario.setOnClickListener(this);
        this.mViewHolder.salvafixo.setOnClickListener(this);
        this.mViewHolder.login.setOnClickListener(this);
        this.mViewHolder.salvarpoupafixo.setOnClickListener(this);

        //edittexts
        this.mViewHolder.salariofixo.setOnFocusChangeListener(this);
        this.mViewHolder.totalfixo.setOnFocusChangeListener(this);
        this.mViewHolder.poupafixo.setOnFocusChangeListener(this);


        load();
    }

    private static class ViewHolder {
        EditText salariofixo;
        EditText totalfixo;
        Button salvasalario;
        Button salvafixo;
        Button login;
        Button salvarpoupafixo;
        EditText poupafixo;


    }


    // OnClick
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.salvar_salario) {
            String value5 = this.mViewHolder.salariofixo.getText().toString().replace("R$","").replace("","").replace(",",".");
            if ("".equals(value5)) {
                Toast.makeText(this, "Valor não adicionado", Toast.LENGTH_LONG).show();
            } else {
                Double salario = Double.valueOf(value5);
                this.mSharedPreference.store(constant.KEY_SALARIO, salario);
                Toast.makeText(this, "Salvo", Toast.LENGTH_LONG).show();
                load();
                saldo();
            }

        } else if (view.getId() == R.id.salvar_fixo) {
            String value3 = this.mViewHolder.totalfixo.getText().toString().replace("R$","").replace("","").replace(",",".");
            if ("".equals(value3)) {
                Toast.makeText(this, "Valor não adicionado", Toast.LENGTH_LONG).show();
            } else {
                Double fixos = Double.valueOf(value3);
                this.mSharedPreference.store(constant.KEY_FIXA, fixos);
                Toast.makeText(this, "Salvo", Toast.LENGTH_LONG).show();
                saldo();
                load();
            }

        } else if (view.getId() == R.id.salvar_fixopoupanca) {
            String value3 = this.mViewHolder.poupafixo.getText().toString().replace("R$","").replace("","").replace(",",".");
            if ("".equals(value3)) {
                Toast.makeText(this, "Valor não adicionado", Toast.LENGTH_LONG).show();
            } else {
                Double fixos = Double.valueOf(value3);
                this.mSharedPreference.store(constant.KEY_POUPANCA, fixos);
                Toast.makeText(this, "Salvo", Toast.LENGTH_LONG).show();
                load();
            }
        } else {

            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
            finish();
        }

    }


    // Focus Change
    @Override
    public void onFocusChange(View view, boolean b) {

        EditText et = (EditText) findViewById(view.getId());

        if (b) {
            et.setText("");
        }
    }


    //Carregando dados
    private void load() {
        Double salario = this.mSharedPreference.getstore(constant.KEY_SALARIO);
        this.mViewHolder.salariofixo.setText(getString(R.string.reais) + salario);

        Double fixo = this.mSharedPreference.getstore(constant.KEY_FIXA);
        this.mViewHolder.totalfixo.setText(getString(R.string.reais) + fixo);

        Double poupa = this.mSharedPreference.getstore(constant.KEY_POUPANCA);
        this.mViewHolder.poupafixo.setText(getString(R.string.reais) + poupa);
    }

    //Caluculo do saldo
    private void saldo() {
        Double saldoanterior = this.mSharedPreference.getstore(constant.KEY_SALDO_ANTERIOR);
        Double salario = this.mSharedPreference.getstore(constant.KEY_SALARIO);
        Double fixos = this.mSharedPreference.getstore(constant.KEY_FIXA);
        Double saldo1 = salario - fixos + saldoanterior;
        this.mSharedPreference.store(constant.KEY_SALDO, saldo1);
    }

}