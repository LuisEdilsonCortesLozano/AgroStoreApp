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

import LuisCortes.AgroStore.Model.User;

public class NewUser extends AppCompatActivity {
    String title= "Registro";
    EditText uidP, NombreP, NumeroP, CiudadP, DireccionP, CorreoP, PasswordP;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        this.setTitle(title);
        NombreP = findViewById(R.id.txt_nombre);
        NumeroP = findViewById(R.id.txt_numero);
        CiudadP = findViewById(R.id.txt_ciudad);
        DireccionP = findViewById(R.id.txt_direccion);
        CorreoP = findViewById(R.id.txt_correo);
        PasswordP = findViewById(R.id.txt_password);
        inicializarFirebase();
        inicializarCorreoLogin();
    }

    private void inicializarCorreoLogin() {
        Intent Window = getIntent();
        String CorreoLogin = Window.getStringExtra("CorreoLogin");
        CorreoP.setText(CorreoLogin);
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
            case R.id.btnNewUserCancel:
                Window = new Intent(NewUser.this, Index.class);
                startActivity(Window);
                break;

            case R.id.btnSave:
                BoxValidation();
                break;
        }

    }

    private void BoxValidation() {
        String nombre = NombreP.getText().toString().trim();
        String numero = NumeroP.getText().toString().trim();
        String ciudad = CiudadP.getText().toString().trim();
        String direccion = DireccionP.getText().toString().trim();
        String correo = CorreoP.getText().toString().trim();
        String password = PasswordP.getText().toString().trim();
        if (nombre.equals("")){
            NombreP.setError("Campo Requerido");
            NombreP.requestFocus();
        }else if (numero.equals("")){
            NumeroP.setError("Campo Requerido");
            NumeroP.requestFocus();
        }else if (ciudad.equals("")){
            CiudadP.setError("Campo Requerido");
            CiudadP.requestFocus();
        }else if (direccion.equals("")) {
            DireccionP.setError("Campo Requerido");
            DireccionP.requestFocus();
        }else if (correo.equals("")){
            CorreoP.setError("Campo Requerido");
            CorreoP.requestFocus();
        }else if (!ValidateEmail(correo)){
            CorreoP.setError("Correo Invalido");
            CorreoP.requestFocus();
        }else if (password.equals("")){
            PasswordP.setError("Campo Requerido");
            PasswordP.requestFocus();
        }else if (PasswordP.length()<6){
            PasswordP.setError("Minimo 6 Caracteres");
            PasswordP.requestFocus();
        }
        else{
            UserSave();
        }
    }

    private boolean ValidateEmail (String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void UserSave() {
        String nombre = NombreP.getText().toString().trim();
        String numero = NumeroP.getText().toString().trim();
        String ciudad = CiudadP.getText().toString().trim();
        String direccion = DireccionP.getText().toString().trim();
        String correo = CorreoP.getText().toString().trim();
        String password = PasswordP.getText().toString().trim();

        firebaseauth.createUserWithEmailAndPassword(correo, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseauth.getCurrentUser();
                    String uid = user.getUid();
                    User u = new User();
                    u.setUid(uid);
                    u.setNombre(nombre);
                    u.setNumero(numero);
                    u.setCiudad(ciudad);
                    u.setDireccion(direccion);
                    databaseReference.child("User").child(u.getUid()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                ShowHome();
                            }
                        }
                    });

                } else {
                    Toast.makeText(NewUser.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void ShowHome() {
        Intent Window = new Intent(NewUser.this, Home.class);
        startActivity(Window);
        CleanBoxes();
    }

    private void CleanBoxes() {
        NombreP.setText("");
        NumeroP.setText("");
        CiudadP.setText("");
        DireccionP.setText("");
        CorreoP.setText("");
        PasswordP.setText("");
        NombreP.requestFocus();
    }
}