package com.feehales.controlederenda.layout;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.feehales.controlederenda.R;
import com.feehales.controlederenda.constant.constant;
import com.feehales.controlederenda.datas.SharedPrefences;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewHolder mViewHolder = new ViewHolder();
    private SharedPrefences mSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mSharedPreference = new SharedPrefences(this);

        this.mViewHolder.config = findViewById(R.id.config);
        this.mViewHolder.editvalue = findViewById(R.id.valor);
        this.mViewHolder.saldoatual = findViewById(R.id.saldo);
        this.mViewHolder.Saldopoupanca = findViewById(R.id.saldo_poupanca);
        this.mViewHolder.salvargasto = findViewById(R.id.button_gasto);
        this.mViewHolder.guarda = findViewById(R.id.guardar);
        this.mViewHolder.poupanca = findViewById(R.id.valor_poupanca);
        this.mViewHolder.tirar = findViewById(R.id.button_tirar);
        this.mViewHolder.salvardeposito = findViewById(R.id.button_deposito);
        this.mViewHolder.valordeposito = findViewById(R.id.valor_deposito);


        this.mViewHolder.config.setOnClickListener(this);
        this.mViewHolder.salvargasto.setOnClickListener(this);
        this.mViewHolder.guarda.setOnClickListener(this);
        this.mViewHolder.tirar.setOnClickListener(this);
        this.mViewHolder.salvardeposito.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizacao();
        finaldemes();
        poupancaview();
        inicodemes();
        saldoatual();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.config){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null){
                //abrir a janela de configuração
                Intent config2 = new Intent(this, ConfigActivity2.class);
                startActivity(config2);
            }else {
                //abrir a janela de configuração
                Intent config = new Intent(this, ConfigActivity.class);
                startActivity(config);
            }

        }else if (view.getId() == R.id.button_gasto){
            //salvar valor de gasto
            String value = this.mViewHolder.editvalue.getText().toString();
            if("".equals(value) ){
                Toast.makeText(this,"Valor não adicionado",Toast.LENGTH_LONG).show();
            }else{
                Double real = Double.valueOf(value);
                gastocartao(real);
                this.mViewHolder.editvalue.setText("");
                Toast.makeText(this,"Valor tirado",Toast.LENGTH_LONG).show();
            }
        }else if (view.getId() == R.id.guardar ){
            // salvar poupança
            String value2 = this.mViewHolder.poupanca.getText().toString();
            if("".equals(value2)){
                Toast.makeText(this,"Valor não adicionado",Toast.LENGTH_LONG).show();
            }else{
                Double real1 = Double.valueOf(value2);
                poupanca(real1);
                this.mViewHolder.poupanca.setText("");
                Toast.makeText(this,"Valor Adicionado",Toast.LENGTH_LONG).show();

            }
        }else if (view.getId() == R.id.button_deposito){
            String valuereais = this.mViewHolder.valordeposito.getText().toString();
            if("".equals(valuereais)){
                Toast.makeText(this,"Valor não adicionado",Toast.LENGTH_LONG).show();
            }else{
                Double real1 = Double.valueOf(valuereais);
                Double saldoantes = this.mSharedPreference.getstore(constant.KEY_SALDO);

                Double saldo = real1 + saldoantes;

                this.mSharedPreference.store(constant.KEY_SALDO,saldo);

                this.mViewHolder.valordeposito.setText("");
                saldoatual();
                Toast.makeText(this,"Valor Depositado",Toast.LENGTH_LONG).show();

            }

        }else {
            String value2 = this.mViewHolder.poupanca.getText().toString();
            if("".equals(value2)){
                Toast.makeText(this,"Valor não adicionado",Toast.LENGTH_LONG).show();
            }else{
                Double real1 = Double.valueOf(value2);
                poupancatira(real1);
                this.mViewHolder.poupanca.setText("");
                Toast.makeText(this,"Valor retirado",Toast.LENGTH_LONG).show();

            }
        }

    }

    private static class ViewHolder {
        EditText editvalue;
        EditText valordeposito;
        EditText poupanca;
        TextView saldoatual;
        TextView Saldopoupanca;
        Button config;
        Button salvargasto;
        Button guarda;
        Button tirar;
        Button salvardeposito;
    }


// logica


    // mostrar saldo disponivel
    @SuppressLint("SetTextI18n")
    private void saldoatual(){
        Double saldo1 = this.mSharedPreference.getstore(constant.KEY_SALDO);
        NumberFormat nf = new DecimalFormat("##.##");
        String saldo = String.valueOf(nf.format(saldo1));
        this.mViewHolder.saldoatual.setText(getString(R.string.reais) + saldo);
    }

    //mostrar saldo da poupança disponivel
    @SuppressLint("SetTextI18n")
    private void poupancaview(){
        Double poupa = this.mSharedPreference.getstore(constant.KEY_POUPANCA);
        NumberFormat nf = new DecimalFormat("##.##");
        String spoupa = String.valueOf(nf.format(poupa));
        this.mViewHolder.Saldopoupanca.setText(getString(R.string.reais) + spoupa);
    }


    private void poupanca(Double pou){
        Double saldo1 = this.mSharedPreference.getstore(constant.KEY_SALDO);
        Double vpoupanca = this.mSharedPreference.getstore(constant.KEY_POUPANCA);
        Double saldo = saldo1 - pou;
        Double poupa = vpoupanca + pou;
        this.mSharedPreference.store(constant.KEY_POUPANCA,poupa);
        this.mSharedPreference.store(constant.KEY_SALDO,saldo);
        poupancaview();
        saldoatual();

    }
    private void poupancatira(Double pou){
        Double poup = this.mSharedPreference.getstore(constant.KEY_POUPANCA);
        Double saldo1 = this.mSharedPreference.getstore(constant.KEY_SALDO);
        Double poup2 = poup - pou;
        Double saldo2 = pou + saldo1;
        this.mSharedPreference.store(constant.KEY_SALDO,saldo2);
        this.mSharedPreference.store(constant.KEY_POUPANCA,poup2);
        poupancaview();
        saldoatual();
    }

    private void gastocartao(Double cartao){
        Double saldo1 = this.mSharedPreference.getstore(constant.KEY_SALDO);
        Double saldo = saldo1 - cartao;
        this.mSharedPreference.store(constant.KEY_SALDO,saldo);
        saldoatual();
    }

    private void finaldemes(){
        Double saldo1 = this.mSharedPreference.getstore(constant.KEY_SALDO);
        this.mSharedPreference.store(constant.KEY_SALDO_ANTERIOR,saldo1);
    }

    private void inicodemes(){

        if(Calendar.getInstance().get(Calendar.MONTH) != this.mSharedPreference.getstoremes(constant.KEY_MES) ){

            Calendar mestest = Calendar.getInstance();
            int mes = mestest.get(Calendar.MONTH);
            int dias = mestest.get(Calendar.DAY_OF_MONTH);
            String sdias = String.valueOf(dias);
            this.mSharedPreference.storemes(constant.KEY_MES,mes);

             if (loadlogin()){
                 Double saldoanterior = this.mSharedPreference.getstore(constant.KEY_SALDO_ANTERIOR);
                 Double salario = this.mSharedPreference.getstore(constant.KEY_SALARIO);
                 Double fixos = this.mSharedPreference.getstore(sdias);

                 Double saldo1 = salario - fixos + saldoanterior;
                 this.mSharedPreference.store(constant.KEY_SALDO, saldo1);
             }else {

                 Double saldoanterior = this.mSharedPreference.getstore(constant.KEY_SALDO_ANTERIOR);
                 Double salario = this.mSharedPreference.getstore(constant.KEY_SALARIO);
                 Double fixos = this.mSharedPreference.getstore(constant.KEY_FIXA);

                 Double saldo1 = salario - fixos + saldoanterior;

                 this.mSharedPreference.store(constant.KEY_SALDO, saldo1);
             }
        }

    }

    private void atualizacao(){
        if(this.mSharedPreference.getstore(constant.KEY_ATIVADO) == 0.00) {
            Calendar mestest = Calendar.getInstance();
            int mes = mestest.get(Calendar.MONTH);
            this.mSharedPreference.storemes(constant.KEY_MES, mes);
        }
    }
    private Boolean loadlogin(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            return true;
        }else {
            return false;
        }
    }



}