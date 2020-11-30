package com.feehales.controlederenda.layout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.feehales.controlederenda.R;
import com.feehales.controlederenda.classes.ConfiguraçãoFirebase;
import com.feehales.controlederenda.classes.Usuario;
import com.feehales.controlederenda.constant.constant;
import com.feehales.controlederenda.datas.SharedPrefences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginActivity.ViewHolder mViewHolder = new LoginActivity.ViewHolder();
    private FirebaseAuth mAuth;
    private Usuario usuario = new Usuario();
    private SharedPrefences mSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(usuario.loadlogin()){
            Intent longado = new Intent(this,ConfigActivity2.class);
            abriractivis(longado);
        }else {
            setContentView(R.layout.activity_login);

            this.mSharedPreference = new SharedPrefences(this);

            //ID dos EditText
            this.mViewHolder.email = findViewById(R.id.email);
            this.mViewHolder.password = findViewById(R.id.password);
            this.mViewHolder.esqueci = findViewById(R.id.esqueci);

            //ID dos Buttuns
            this.mViewHolder.login = findViewById(R.id.loginsalvar);
            this.mViewHolder.cadastro = findViewById(R.id.cadastro);

            //ID do CheckBox
            this.mViewHolder.lembrar = findViewById(R.id.lembrar);

            //click nos botoes
            this.mViewHolder.login.setOnClickListener(this);
            this.mViewHolder.cadastro.setOnClickListener(this);

           load();
        }

    }

    private static class ViewHolder {
        EditText email;
        EditText password;
        TextView esqueci;

        CheckBox lembrar;

        Button login;
        Button cadastro;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.loginsalvar){
            String email = this.mViewHolder.email.getText().toString();
            String password = this.mViewHolder.password.getText().toString();

            if("".equals(email) | "".equals(password)){
                Toast.makeText(this, "Usarname e password não colocados", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Fazer Cadastro", Toast.LENGTH_LONG).show();
            }else{
                usuario.setEmail(email);
                usuario.setPassword(password);
                boolean checking =  this.mViewHolder.lembrar.isChecked();
                if(checking){
                    this.mSharedPreference.user(constant.KEY_USER,email);
                    this.mSharedPreference.user(constant.KEY_PASSWORD,password);
                    this.mSharedPreference.check(constant.KEY_CHECK,checking);

                    // fazer a autenticação
                    autenticar();

                }else{ this.mSharedPreference.check(constant.KEY_CHECK,checking);}

                //fazer a autenticação
                autenticar();
            }
        }else if(view.getId() == R.id.esqueci){
            esquecisenha();
        } else{
            Intent cadastro = new Intent(this,CadastroActivity.class);
            abriractivis(cadastro);

        }
    }

    private void load(){
        Boolean check = this.mSharedPreference.getcheck(constant.KEY_CHECK);
        if (check) {
            String use = this.mSharedPreference.getuser(constant.KEY_USER);
            String password = this.mSharedPreference.getuser(constant.KEY_PASSWORD);

            this.mViewHolder.email.setText(use);
            this.mViewHolder.password.setText(password);
            this.mViewHolder.lembrar.setChecked(check);
        } else {
            this.mViewHolder.email.setText("");
            this.mViewHolder.password.setText("");
            this.mViewHolder.lembrar.setChecked(check);
        }
    }

    private void autenticar(){
        mAuth = ConfiguraçãoFirebase.getAutenticar();
        mAuth.signInWithEmailAndPassword(usuario.getEmail(),usuario.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent longado = new Intent(LoginActivity.this,ConfigActivity2.class);
                    abriractivis(longado);
                    Toast.makeText(LoginActivity.this, "Login efetuado Sucesso", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "E-mail/Username ou Password invalidos", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void abriractivis(Intent abrir){
        startActivity(abrir);
    }

    private void esquecisenha() {
        if (usuario.getEmail().equals("")) {
            Toast.makeText(LoginActivity.this, "Adicionar o E-mail no campo do e-mail", Toast.LENGTH_LONG).show();
        } else {
            mAuth = ConfiguraçãoFirebase.getAutenticar();
            mAuth.sendPasswordResetEmail(usuario.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "E-mail enviado", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


}