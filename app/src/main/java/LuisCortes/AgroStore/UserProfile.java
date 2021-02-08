package LuisCortes.AgroStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import LuisCortes.AgroStore.Model.User;

public class UserProfile extends AppCompatActivity {
    String title= "Mi Cuenta";
    EditText uidP, NombreP, NumeroP, CiudadP, DireccionP;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        this.setTitle(title);
        NombreP = findViewById(R.id.txt_nombre_perfil);
        NumeroP = findViewById(R.id.txt_numero_perfil);
        CiudadP = findViewById(R.id.txt_ciudad_perfil);
        DireccionP = findViewById(R.id.txt_direccion_perfil);
        inicializarFirebase();
      }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser User = firebaseauth.getCurrentUser();
        if(User != null){
            String uid = User.getUid();
            databaseReference.child("User").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String nombre = dataSnapshot.child("nombre").getValue().toString().trim();
                        String numero = dataSnapshot.child("numero").getValue().toString().trim();
                        String ciudad = dataSnapshot.child("ciudad").getValue().toString().trim();
                        String direccion = dataSnapshot.child("direccion").getValue().toString().trim();
                        NombreP.setText(nombre);
                        NumeroP.setText(numero);
                        CiudadP.setText(ciudad);
                        DireccionP.setText(direccion);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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
        switch (view.getId()){
            case R.id.btnUserProfileCancel:
                UserProfileBack();
                break;

            case R.id.btnUserProfileSave:
                UserProfileSave();
                break;
        }
    }

    private void UserProfileBack() {
        Intent Window = new Intent(UserProfile.this, Home.class);
        startActivity(Window);
    }

    private void UserProfileSave() {
        FirebaseUser User = firebaseauth.getCurrentUser();
        if(User != null){
            String uid = User.getUid();
            String nombre = NombreP.getText().toString().trim();
            String numero = NumeroP.getText().toString().trim();
            String ciudad = CiudadP.getText().toString().trim();
            String direccion = DireccionP.getText().toString().trim();
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
                        Toast.makeText(UserProfile.this,"Datos actualizados", Toast.LENGTH_LONG).show();
                        UserProfileBack();
                    }
                }
            });
        }
    }
}