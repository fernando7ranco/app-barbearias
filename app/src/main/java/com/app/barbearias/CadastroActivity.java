package com.app.barbearias;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.auth = FirebaseAuth.getInstance();
        this.user = this.auth.getCurrentUser();

        if (  this.user != null) {
            Intent intent = new Intent(CadastroActivity.this, FormDadosBarbeariaActivity.class);
            startActivity(intent);
        } else {
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        Intent intent = new Intent(CadastroActivity.this, FormDadosBarbeariaActivity.class);
                        startActivity(intent);
                    }
                }
            };
            this.auth.addAuthStateListener(authStateListener);

            setContentView(R.layout.activity_cadastro);

            EditText inNome = findViewById(R.id.inNomeCastro);
            EditText inEmail = findViewById(R.id.inEmailCadastro);
            EditText inSenha = findViewById(R.id.inSenhaCadastro);
            EditText inSenhaConfirme = findViewById(R.id.inConfirmeSenhaCadastro);
            Button btCadastrar = findViewById(R.id.btCadastrarConta);

            btCadastrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nome = inNome.getText().toString().trim();
                    String email = inEmail.getText().toString().trim();
                    String senha = inSenha.getText().toString().trim();
                    String senhaConfirme = inSenhaConfirme.getText().toString().trim();

                    if (CadastroActivity.this.validate(nome, email, senha, senhaConfirme)) {
                        CadastroActivity.this.cadastrar(nome, email, senha);
                    }
                }
            });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(CadastroActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private void cadastrar(String nome, String email, String senha){

        this.auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                    if (isNewUser) {
                        CadastroActivity.this.auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    CadastroActivity.this.user = CadastroActivity.this.auth.getCurrentUser();
                                    Barbearias b = new Barbearias();
                                    b.setIdUsuario(CadastroActivity.this.user.getUid());
                                    b.setNome(nome);
                                    b.save();
                                    Toast.makeText(CadastroActivity.this, "Conta criada com sucesso!", Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(CadastroActivity.this, "Ocorreram erros ao criar o usuario. Por favor preencha e-mail e senha corretamente", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(CadastroActivity.this, "Ocorreram erros ao criar o usuario. E-mail já cadastrado", Toast.LENGTH_LONG).show();
                    }

                }
            });
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public boolean validPassword(String password) {
        return password.length() >= 6;
    }

    public boolean confirmPassword(String password1, String password2) {
        return password1.equals(password2);
    }

    public boolean validName(String name) {
        return name.trim().length() > 0;
    }

    public boolean validate(String name, String email, String password1, String password2) {
        if (!this.validName(name)){
            Toast.makeText(this, "Ocorreram erros preecha corretamente o nome da barbearia", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!this.isValidEmailAddress(email)) {
            Toast.makeText(this, "Ocorreram erros preecha corretamente o e-mail", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!this.validPassword(password1)) {
            Toast.makeText(this, "Ocorreram erros preecha corretamente a senha com no minimo 6 caracteres", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!this.confirmPassword(password1, password2)) {
            Toast.makeText(this, "Ocorreram erros campo senha não esta confirmado", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}