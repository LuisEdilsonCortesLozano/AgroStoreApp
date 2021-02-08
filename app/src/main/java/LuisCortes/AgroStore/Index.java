package LuisCortes.AgroStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class Index extends AppCompatActivity {
    String title= "Autenticación";
    EditText correoP,passwordP;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_main);
        correoP = findViewById(R.id.txt_CorreoLogin);
        passwordP = findViewById(R.id.txt_PasswordLogin);
        this.setTitle(title);
        inicializarFirebase();
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser User = firebaseauth.getCurrentUser();
        if(User != null){
            ShowHome();
        }
    }
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        // Instancia base de datos
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        // Instancia a autenticacion
        firebaseauth = FirebaseAuth.getInstance();
    }

    public void OnClick(View view) {
        Intent Window =  null;
        switch (view.getId()){
            case R.id.btnEnter:
                BoxValidation();
                break;

            case R.id.btnNewUser:
                ShowNewUser();
                break;
        }

    }

    private void ShowNewUser() {
        String correo = correoP.getText().toString().trim();
        Intent Window = new Intent(Index.this, NewUser.class);
        Window.putExtra("CorreoLogin", correo);
        startActivity(Window);
    }

    private void BoxValidation() {
        String correo = correoP.getText().toString().trim();
        String password = passwordP.getText().toString().trim();
        if (correo.equals("")){
            correoP.setError("Campo Requerido");
            correoP.requestFocus();
        }else if (!ValidateEmail(correo)){
            correoP.setError("Correo Invalido");
            correoP.requestFocus();
        }else if (password.equals("")){
            passwordP.setError("Campo Requerido");
            passwordP.requestFocus();
        }else if (passwordP.length()<6){
            passwordP.setError("Minimo 6 Caracteres");
            passwordP.requestFocus();
        }else{
            Enter();
        }
    }

    private boolean ValidateEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public void Enter() {
        String correo = correoP.getText().toString().trim();
        String password = passwordP.getText().toString().trim();
        firebaseauth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    ShowHome();
                } else {
                    //Toast.makeText(Index.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(Index.this,"Registrate primero", Toast.LENGTH_LONG).show();
                    ShowNewUser();
                }
            }
        });
    }

    private void CleanBoxes() {
        correoP.setText("");
        passwordP.setText("");
        correoP.requestFocus();
    }

    public void ShowHome() {
        Intent Window = new Intent(Index.this, Home.class);
        startActivity(Window);
        CleanBoxes();
    }
}