package com.feehales.controlederenda.layout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.feehales.controlederenda.R;
import com.feehales.controlederenda.classes.ConfiguraçãoFirebase;
import com.feehales.controlederenda.classes.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroActivity extends AppCompatActivity implements View.OnClickListener{

    private CadastroActivity.ViewHolder mViewHolder = new CadastroActivity.ViewHolder();
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Usuario usuario = new Usuario();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //ID do Edittext
        this.mViewHolder.email = findViewById(R.id.cdemail);
        this.mViewHolder.password = findViewById(R.id.cdpassword);
        this.mViewHolder.password2 = findViewById(R.id.cdpasswordconfimation);
        this.mViewHolder.username = findViewById(R.id.cdUsername);
        this.mViewHolder.idade = findViewById(R.id.cdidade);
        this.mViewHolder.sexo = findViewById(R.id.sexo);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.sexo, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mViewHolder.sexo.setAdapter(adapter);

        //ID do button
        this.mViewHolder.cadastro = findViewById(R.id.cdcadastrar);


        this.mViewHolder.cadastro.setOnClickListener(this);

    }



    private static class ViewHolder {
        EditText email;
        EditText password;
        EditText password2;
        EditText username;
        EditText idade;
        Spinner sexo;

        Button cadastro;
    }


    @Override
    public void onClick(View view) {
        String senha = this.mViewHolder.password.getText().toString();
        String email = this.mViewHolder.email.getText().toString();
        String senha2 = this.mViewHolder.password2.getText().toString();
        String sexo = this.mViewHolder.sexo.getSelectedItem().toString();
        String nome = this.mViewHolder.username.getText().toString();
        String idade = this.mViewHolder.idade.getText().toString();
        if(email.equals("") | senha.equals("") | senha2.equals("") | nome.equals("") | idade.equals("") | sexo.equals("")  ){
           Toast test =  Toast.makeText(this, "Por favor preencher todos os campos", Toast.LENGTH_LONG);
           test.setGravity(Gravity.CENTER,0,0);
           test.show();
        }else {
            if (senha.equals(senha2)) {
                usuario.setEmail(email);
                usuario.setSexo(sexo);
                Toast.makeText(this, sexo, Toast.LENGTH_LONG).show();
                usuario.setPassword(senha);
                usuario.setName(nome);
                usuario.setIdade(idade);

                cadastrarUser();

            } else {
                Toast.makeText(this, "As senhas não Correspondem", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void cadastrarUser(){

        auth = ConfiguraçãoFirebase.getAutenticar();
        auth.createUserWithEmailAndPassword(usuario.getEmail(),usuario.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    iserirusuario(usuario);

                }else{
                    String erroExcecao ="";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = "Digite senha mais forte com mínimo 8 caracteres";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "O e-mail digitado é invalido, digite um novo e-mail";
                    }catch (FirebaseAuthUserCollisionException e){
                        erroExcecao = "Esse e-mail já está cadastrado";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro";
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this,"Erro: " + erroExcecao,Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private boolean iserirusuario(Usuario usuario){
        try {
            reference = ConfiguraçãoFirebase.getdatabase().child("Usuarios");
            reference.push().setValue(usuario);
            Toast.makeText(this,"Usuario cadastrado com sucesso ",Toast.LENGTH_LONG).show();
            apagar();
            return true;
        }catch (Exception e){
            Toast.makeText(this,"Erro ao gravar o usuario ",Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false;
        }
    }

    private void apagar(){
        this.mViewHolder.username.setText("");
        this.mViewHolder.password.setText("");
        this.mViewHolder.password2.setText("");
        this.mViewHolder.idade.setText("");
        this.mViewHolder.email.setText("");
    }



}