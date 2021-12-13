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

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.auth = FirebaseAuth.getInstance();
        this.user = this.auth.getCurrentUser();

        if (  this.user != null) {
            Intent intent = new Intent(LoginActivity.this, FormDadosBarbeariaActivity.class);
            startActivity(intent);
        } else {

            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        Intent intent = new Intent(LoginActivity.this, FormDadosBarbeariaActivity.class);
                        startActivity(intent);
                    }
                }
            };
            this.auth.addAuthStateListener(authStateListener);

            setContentView(R.layout.activity_login);

            EditText inEmail = findViewById(R.id.inEmailLogin);
            EditText inSenha = findViewById(R.id.inSenhaLogin);
            Button btEntrar = findViewById(R.id.btCriarConta);

            btEntrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = inEmail.getText().toString().trim();
                    String senha = inSenha.getText().toString().trim();
                    Log.d("dados logins:", "email=" + email + " senha=" + senha);
                    LoginActivity.this.login(email, senha);
                }
            });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private void login(String email, String senha) {
        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Por favor preencha e-mail e senha corretamente", Toast.LENGTH_LONG).show();
        } else if(!this.isValidEmailAddress(email)) {
            Toast.makeText(this, "Por favor preencha e-mail valido", Toast.LENGTH_LONG).show();
        } else if (this.validPassword(senha)) {
            Toast.makeText(this, "Por favor informe sua senha com pelos 6 caracteres", Toast.LENGTH_LONG).show();
        } else {
            this.auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        LoginActivity.this.user = LoginActivity.this.auth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, "Login efetuado", Toast.LENGTH_LONG).show();
                        Log.d("corretos logins firebase:", "email="+ email +" senha="+senha);
                    } else {
                        Log.d("corretos logins firebase:", "email="+ email +" senha="+senha);
                        String message = task.getException().getMessage();
                        Log.d("corretos logins firebase erro:", message);
                        Toast.makeText(LoginActivity.this, "Erro de login de dados. Por favor preencha e-mail e senha corretamente", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public boolean validPassword(String password) {
        return password.length() < 6;
    }
}