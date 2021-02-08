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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import LuisCortes.AgroStore.Model.User;

public class Publication extends AppCompatActivity {
    String title= "Detalle Producto";
    EditText uidP, DescripcionP, MedidaP, PrecioP;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publication);
        this.setTitle(title);
        DescripcionP = findViewById(R.id.txt_descripcion_publicacion);
        MedidaP = findViewById(R.id.txt_medida_publicacion);
        PrecioP = findViewById(R.id.txt_precio_publicacion);
        uidP = findViewById(R.id.txt_product_publicacion);
        inicializarFirebase();
        GetData();
    }

    private void GetData() {
        Intent Window = getIntent();
        String product = Window.getStringExtra("product");
        uidP.setText(product);
        databaseReference.child("Publication").child(product).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String descripcion = dataSnapshot.child("descripcion").getValue().toString().trim();
                    String medida = dataSnapshot.child("unidad").getValue().toString().trim();
                    String precio = dataSnapshot.child("precio").getValue().toString().trim();
                    DescripcionP.setText(descripcion);
                    MedidaP.setText(medida);
                    PrecioP.setText(precio);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
            case R.id.btnPublicationCancel:
                PublicationBack();
                break;

            case R.id.btnPlicationSave:
                BoxValidation();
                break;

            case R.id.btnPublicationDelete:
                PublicationDelete();
                break;
        }

    }

    private void PublicationDelete() {
        String product = uidP.getText().toString().trim();
        FirebaseUser user = firebaseauth.getCurrentUser();
        String uid = user.getUid();
        databaseReference.child("Publication").child(product).removeValue();
        databaseReference.child("PublicationDetail").child(uid).child(product).removeValue();
        databaseReference.child("Commentary").child(product).removeValue();
        Toast.makeText(Publication.this,"Producto Inactivado", Toast.LENGTH_LONG).show();
        PublicationBack();
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
            PublicationUpdate();
        }
    }

    private void PublicationUpdate() {
        String descripcion = DescripcionP.getText().toString().trim();
        String medida = MedidaP.getText().toString().trim();
        String precio = PrecioP.getText().toString().trim();
        String product = uidP.getText().toString().trim();
        FirebaseUser user = firebaseauth.getCurrentUser();
        String uid = user.getUid();
        LuisCortes.AgroStore.Model.Publication u = new LuisCortes.AgroStore.Model.Publication();
        u.setUid(product);
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
                                Toast.makeText(Publication.this,"Producto actualizado", Toast.LENGTH_LONG).show();
                                PublicationBack();
                            }
                        }
                    });
                }
            }
        });


    }

    private void PublicationBack() {
        DescripcionP.setText("");
        MedidaP.setText("");
        PrecioP.setText("");
        DescripcionP.requestFocus();
        Intent Window = new Intent(Publication.this, MyPublication.class);
        startActivity(Window);
    }


}