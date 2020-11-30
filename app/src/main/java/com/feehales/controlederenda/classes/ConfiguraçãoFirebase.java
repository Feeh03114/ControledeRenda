package com.feehales.controlederenda.classes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguraçãoFirebase {

    private static DatabaseReference ReferenciaFirebase;
    private static FirebaseAuth autenticacao;

    public static DatabaseReference getdatabase(){
        if(ReferenciaFirebase == null){
            ReferenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return ReferenciaFirebase;
    }

    public static FirebaseAuth getAutenticar(){
        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }
}
