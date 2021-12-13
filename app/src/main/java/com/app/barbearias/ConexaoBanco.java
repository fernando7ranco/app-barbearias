package com.app.barbearias;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConexaoBanco {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;

    public ConexaoBanco() {
        this.firebaseDatabase = firebaseDatabase.getInstance();
        this.reference = this.firebaseDatabase.getReference();
    }

    public DatabaseReference getReference() {
        return this.reference;
    }
}
