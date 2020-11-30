package com.feehales.controlederenda.classes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;

public class Usuario {


    private String email;
    private String password;
    private String name;
    private String sexo;
    private String idade;


    public String getIdade() {
        return idade; }

    public void setIdade(String idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Exclude
    public String getPassword() {
        return password;
    }
    @Exclude
    public void setPassword(String password) {
        this.password = password;
    }

    @Exclude
    public Boolean loadlogin(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            return true;
        }else {
            return false;
        }
    }
}
