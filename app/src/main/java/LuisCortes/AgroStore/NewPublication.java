package LuisCortes.AgroStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import LuisCortes.AgroStore.Model.Publication;

public class NewPublication extends AppCompatActivity {
    String title= "Nueva Publicación";
    EditText uidP, DescripcionP, MedidaP, PrecioP;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_publication);
        this.setTitle(title);
        DescripcionP = findViewById(R.id.txt_descripcion_new_publicacion);
        MedidaP = findViewById(R.id.txt_medida_new_publicacion);
        PrecioP = findViewById(R.id.txt_precio_new_publicacion);
        inicializarFirebase();
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
            case R.id.btnNewPlicationCancel:
                Window = new Intent(NewPublication.this, Home.class);
                startActivity(Window);
                break;

            case R.id.btnNewPlicationSave:
                BoxValidation();
                break;
        }
    }

    private void BoxValidation() {
        String descripcion = DescripcionP.getText().toString().trim();
        String medida = MedidaP.getText().toString().trim();
        String precio = PrecioP.getText().toString().trim();
        if (descripcion.equals("")){
            DescripcionP.setError("Campo Requerido");
            DescripcionP.requestFocus();
        }else if (descripcion.length()<6){
            DescripcionP.setError("Minimo 6 Caracteres");
            DescripcionP.requestFocus();
        }else if (medida.equals("")){
            MedidaP.setError("Campo Requerido");
            MedidaP.requestFocus();
        }else if (medida.length()<6){
            MedidaP.setError("Minimo 6 Caracteres");
            MedidaP.requestFocus();
        }else if (precio.equals("")){
            PrecioP.setError("Campo Requerido");
            PrecioP.requestFocus();
        }else{
            NewPublicationSave();
        }
    }

    private void NewPublicationSave() {
        String descripcion = DescripcionP.getText().toString().trim();
        String medida = MedidaP.getText().toString().trim();
        String precio = PrecioP.getText().toString().trim();
        FirebaseUser user = firebaseauth.getCurrentUser();
        String uid = user.getUid();
        Publication u = new Publication();
        u.setUid(UUID.randomUUID().toString());
        u.setDescripcion(descripcion);
        u.setUnidad(medida);
        u.setPrecio(precio);
        u.setUser(uid);
        databaseReference.child("Publication").child(u.getUid()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    databaseReference.child("PublicationDetail").child(u.getUser()).child(u.getUid()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(NewPublication.this,"Tu publicación ha sido compartida", Toast.LENGTH_LONG).show();
                                ShowHome();
                            } else {
                                databaseReference.child("Publication").child(u.getUid()).removeValue();
                                Toast.makeText(NewPublication.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(NewPublication.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void ShowHome() {
        Intent Window = new Intent(NewPublication.this, Home.class);
        startActivity(Window);
        CleanBoxes();
    }

    private void CleanBoxes() {
        DescripcionP.setText("");
        MedidaP.setText("");
        PrecioP.setText("");
        DescripcionP.requestFocus();
    }
}