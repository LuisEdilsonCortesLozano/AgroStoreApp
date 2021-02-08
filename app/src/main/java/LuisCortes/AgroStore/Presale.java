package LuisCortes.AgroStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Presale extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseauth;
    TextView NombreP, NumeroP, CiudadP, DireccionP, DescripcionP, PresentacionP, PrecioP;
    EditText ProductP, DescriptionP, UserP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presale);
        NombreP = findViewById(R.id.nombre_presale);
        NumeroP = findViewById(R.id.numero_presale);
        CiudadP = findViewById(R.id.ciudad_presale);
        DireccionP = findViewById(R.id.direccion_presale);
        DescripcionP = findViewById(R.id.descripcion_presale);
        PresentacionP = findViewById(R.id.presentacion_presale);
        PrecioP = findViewById(R.id.precio_presale);
        ProductP = findViewById(R.id.txt_produc_presale);
        DescriptionP = findViewById(R.id.txt_description_presale);
        UserP = findViewById(R.id.txt_user_presale);
        inicializarFirebase();
        GetData();
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        // Instancia base de datos
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        // Instancia a autenticacion
        firebaseauth = FirebaseAuth.getInstance();
    }

    private void GetData() {
        Intent Window = getIntent();
        String product = Window.getStringExtra("product");
        String user = Window.getStringExtra("user");

        if((product != null) && (user != null)){
            Publicador(user);
            Producto(product);
        }
    }

    private void Producto(String product) {
        databaseReference.child("Publication").child(product).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String descripcion = dataSnapshot.child("descripcion").getValue().toString().trim();
                    String presentacion = dataSnapshot.child("unidad").getValue().toString().trim();
                    String precio = dataSnapshot.child("precio").getValue().toString().trim();
                    DescripcionP.setText("Descripción: "+descripcion);
                    PresentacionP.setText("Presentación: "+presentacion);
                    PrecioP.setText("Precio: "+precio);
                    ProductP.setText(product);
                    DescriptionP.setText(descripcion);
                    Presale.this.setTitle(descripcion);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Publicador(String user) {
        databaseReference.child("User").child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String nombre = dataSnapshot.child("nombre").getValue().toString().trim();
                    String numero = dataSnapshot.child("numero").getValue().toString().trim();
                    String ciudad = dataSnapshot.child("ciudad").getValue().toString().trim();
                    String direccion = dataSnapshot.child("direccion").getValue().toString().trim();
                    NombreP.setText("Nombre: "+nombre);
                    NumeroP.setText("Número: "+numero);
                    CiudadP.setText("Ciudad: "+ciudad);
                    DireccionP.setText("Dirección: "+direccion);
                    UserP.setText(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btnPresaleCancel:
                PresaleBack();
                break;
                
            case R.id.btnPresaleComment:
                ShowCommentary();
                break;

            case R.id.btnPresaleBuy:
                PresaleBuy();
                break;
        }
    }

    private void PresaleBuy() {
        String product = ProductP.getText().toString().trim();
        String user = UserP.getText().toString().trim();
        String tittle = DescriptionP.getText().toString().trim();
        String descripcion = DescripcionP.getText().toString().trim();
        String presentacion = PresentacionP.getText().toString().trim();
        String precio = PrecioP.getText().toString().trim();


        if((product != null) && (user != null) && (descripcion != null)){
            Intent Window = new Intent(Presale.this, ShoppingCart.class);
            Window.putExtra("product", product);
            Window.putExtra("user", user);
            Window.putExtra("tittle", tittle);
            Window.putExtra("descripcion", descripcion);
            Window.putExtra("presentacion", presentacion);
            Window.putExtra("precio", precio);
            startActivity(Window);
        }
    }

    private void ShowCommentary() {
        String product = ProductP.getText().toString().trim();
        String user = UserP.getText().toString().trim();
        String descripcion = DescriptionP.getText().toString().trim();


        if((product != null) && (user != null) && (descripcion != null)){
            Intent Window = new Intent(Presale.this, Commentary.class);
            Window.putExtra("product", product);
            Window.putExtra("user", user);
            Window.putExtra("descripcion", descripcion);
            startActivity(Window);
        }
    }

    private void PresaleBack() {
        Intent Window = new Intent(Presale.this, Store.class);
        startActivity(Window);
    }
}