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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class ConfigActivity2 extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener{

    private ConfigActivity2.ViewHolder mViewHolder = new ConfigActivity2.ViewHolder();
    private SharedPrefences mSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config2);

        this.mSharedPreference = new SharedPrefences(this);

        //ID dos EditText
        this.mViewHolder.salariofixo2 = findViewById(R.id.valor_salario2);
        this.mViewHolder.poupafixo2 = findViewById(R.id.valor_poupancafixo2);
        this.mViewHolder.validade = findViewById(R.id.data);
        this.mViewHolder.nameconta = findViewById(R.id.nameconta);
        this.mViewHolder.totalfixo2 = findViewById(R.id.valor_fixo2);

        //ID dos Buttuns
        this.mViewHolder.salvasalario2 = findViewById(R.id.salvar_salario2);
        this.mViewHolder.salvafixo2 = findViewById(R.id.salvar_fixo2);
        this.mViewHolder.login2 = findViewById(R.id.logout);
        this.mViewHolder.salvarpoupafixo2 = findViewById(R.id.salvar_fixopoupanca2);

        //buttun
        this.mViewHolder.salvasalario2.setOnClickListener(this);
        this.mViewHolder.salvafixo2.setOnClickListener(this);
        this.mViewHolder.login2.setOnClickListener(this);
        this.mViewHolder.salvarpoupafixo2.setOnClickListener(this);

        //edittexts
        this.mViewHolder.salariofixo2.setOnFocusChangeListener(this);
        this.mViewHolder.totalfixo2.setOnFocusChangeListener(this);
        this.mViewHolder.poupafixo2.setOnFocusChangeListener(this);
        this.mViewHolder.validade.setOnFocusChangeListener(this);
        this.mViewHolder.nameconta.setOnFocusChangeListener(this);

        load();

    }
    private static class ViewHolder {
        EditText salariofixo2;
        EditText totalfixo2;
        EditText nameconta;
        EditText validade;
        Button salvasalario2;
        Button salvafixo2;
        Button login2;
        Button salvarpoupafixo2;
        EditText poupafixo2;


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.salvar_salario2) {
            String value5 = this.mViewHolder.salariofixo2.getText().toString().replace("R$","").replace("","").replace(",",".");
            if ("".equals(value5)) {
                Toast.makeText(this, "Valor não adicionado", Toast.LENGTH_LONG).show();
            } else {
                Double salario = Double.valueOf(value5);
                this.mSharedPreference.store(constant.KEY_SALARIO, salario);
                Toast.makeText(this, "Salvo", Toast.LENGTH_LONG).show();
                load();
                saldo();
            }

        } else if (view.getId() == R.id.salvar_fixo2) {
            String value3 = this.mViewHolder.totalfixo2.getText().toString().replace("R$","").replace("","").replace(",",".");
            String value4 = this.mViewHolder.validade.getText().toString().replace("/","").replace("","");
            String value2 = this.mViewHolder.nameconta.getText().toString();
            if (!value3.isEmpty() && !value4.isEmpty() && !value2.isEmpty()) {
                Double fixos = Double.valueOf(value3);
                this.mSharedPreference.store(value4, fixos);


                // adicionar Database


                Toast.makeText(this, "Salvo", Toast.LENGTH_LONG).show();
                saldo();
                load();
            } else {
                Toast.makeText(this, "Valor não adicionado", Toast.LENGTH_LONG).show();
            }

        } else if (view.getId() == R.id.salvar_fixopoupanca2) {
            String value3 = this.mViewHolder.poupafixo2.getText().toString().replace("R$","").replace("","").replace(",",".");
            if ("".equals(value3)) {
                Toast.makeText(this, "Valor não adicionado", Toast.LENGTH_LONG).show();
            } else {
                Double fixos = Double.valueOf(value3);
                this.mSharedPreference.store(constant.KEY_POUPANCA, fixos);
                Toast.makeText(this, "Salvo", Toast.LENGTH_LONG).show();
                load();
            }
        } else {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(ConfigActivity2.this, "Logout efetuado com sucesso", Toast.LENGTH_LONG).show();
            Intent cadastro = new Intent(this,ConfigActivity.class);
           startActivity(cadastro);
           finish();
        }

    }

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
        this.mViewHolder.salariofixo2.setText(getString(R.string.reais) + salario);

        Double poupa = this.mSharedPreference.getstore(constant.KEY_POUPANCA);
        this.mViewHolder.poupafixo2.setText(getString(R.string.reais) + poupa);
    }

    private void saldo(){
        Calendar mestest = Calendar.getInstance();
        int dias = mestest.get(Calendar.DAY_OF_MONTH);
        String sdias = String.valueOf(dias);
        Double saldoanterior = this.mSharedPreference.getstore(constant.KEY_SALDO_ANTERIOR);
        Double salario = this.mSharedPreference.getstore(constant.KEY_SALARIO);
        Double fixos = this.mSharedPreference.getstore(sdias);
        Double saldo1 = salario - fixos + saldoanterior;
        this.mSharedPreference.store(constant.KEY_SALDO, saldo1);

    }


}
