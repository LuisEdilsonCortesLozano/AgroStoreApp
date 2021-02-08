package LuisCortes.AgroStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

import LuisCortes.AgroStore.Model.BuyProduct;
import LuisCortes.AgroStore.Model.Comment;

public class ShoppingCart extends AppCompatActivity {
    TextView DescripcionP, PresentacionP, PrecioP, ProductP, UserPublicadorP, CantP, ObservacionP;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        DescripcionP = findViewById(R.id.descripcion_shopping_cart);
        PresentacionP = findViewById(R.id.presentacion_shopping_cart);
        PrecioP = findViewById(R.id.precio_shopping_cart);
        ProductP = findViewById(R.id.txt_product_shopping_cart);
        UserPublicadorP = findViewById(R.id.txt_user_shopping_cart);
        CantP = findViewById(R.id.txt_cant_shopping_cart);
        ObservacionP = findViewById(R.id.txt_observacion_shopping_cart);
        inicializarFirebase();
        GetData();
    }

    private void GetData() {
        Intent Window = getIntent();
        String product = Window.getStringExtra("product");
        String user = Window.getStringExtra("user");
        String tittle = Window.getStringExtra("tittle");
        String descripcion = Window.getStringExtra("descripcion");
        String presentacion = Window.getStringExtra("presentacion");
        String precio = Window.getStringExtra("precio");
        ShoppingCart.this.setTitle(tittle);

        if((product != null) && (user != null)){
            DescripcionP.setText(descripcion);
            PresentacionP.setText(presentacion);
            PrecioP.setText(precio);
            ProductP.setText(product);
            UserPublicadorP.setText(user);
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
            case R.id.btnShoppingCartBack:
                ShoppingCartBack();
                break;

            case R.id.btnShoppingCartSave:
                BoxValidation();
                break;
        }
    }

    private void BoxValidation() {
        String cant = CantP.getText().toString().trim();
        String observacion = ObservacionP.getText().toString().trim();
        if (cant.equals("")){
            CantP.setError("Campo Requerido");
            CantP.requestFocus();
        }else if (observacion.equals("")){
            ObservacionP.setError("Campo Requerido");
            ObservacionP.requestFocus();
        }else if (ObservacionP.length()<6){
            ObservacionP.setError("Minimo 6 Caracteres");
            ObservacionP.requestFocus();
        }else{
            ShoppingCartSave();
        }
    }

    private void ShoppingCartSave() {
        String userpublicador = UserPublicadorP.getText().toString().trim();
        String producto = ProductP.getText().toString().trim();
        FirebaseUser User = firebaseauth.getCurrentUser();
        String cantidad = CantP.getText().toString().trim();
        String observacion = ObservacionP.getText().toString().trim();

        if((User != null) && (userpublicador != null) && (producto != null)){

            databaseReference.child("Publication").child(producto).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String ProductoDescripcion = dataSnapshot.child("descripcion").getValue().toString().trim();
                        String ProductoUnidad = dataSnapshot.child("unidad").getValue().toString().trim();
                        String ProductoPrecio = dataSnapshot.child("precio").getValue().toString().trim();

                        databaseReference.child("User").child(userpublicador).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String PublicadorNombre = dataSnapshot.child("nombre").getValue().toString().trim();
                                    String PublicadorNumero = dataSnapshot.child("numero").getValue().toString().trim();
                                    String PublicadorCiudad = dataSnapshot.child("ciudad").getValue().toString().trim();
                                    String PublicadorDireccion = dataSnapshot.child("direccion").getValue().toString().trim();

                                    String Compradoruid = User.getUid();
                                    databaseReference.child("User").child(Compradoruid).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                String CompradorNombre = dataSnapshot.child("nombre").getValue().toString().trim();
                                                String CompradorNumero = dataSnapshot.child("numero").getValue().toString().trim();
                                                String CompradorCiudad = dataSnapshot.child("ciudad").getValue().toString().trim();
                                                String CompradorDireccion = dataSnapshot.child("direccion").getValue().toString().trim();

                                                BuyProduct u = new BuyProduct();
                                                u.setCompraId(UUID.randomUUID().toString());
                                                u.setCompraCantidad(cantidad);
                                                u.setCompraObservacion(observacion);
                                                u.setCompradoruid(Compradoruid);
                                                u.setCompradorNombre(CompradorNombre);
                                                u.setCompradorNumero(CompradorNumero);
                                                u.setCompradorCiudad(CompradorCiudad);
                                                u.setCompradorDireccion(CompradorDireccion);
                                                u.setPublicadoruid(userpublicador);
                                                u.setPublicadorNombre(PublicadorNombre);
                                                u.setPublicadorNumero(PublicadorNumero);
                                                u.setPublicadorCiudad(PublicadorCiudad);
                                                u.setPublicadorDireccion(PublicadorDireccion);
                                                u.setProductouid(producto);
                                                u.setProductoDescripcion(ProductoDescripcion);
                                                u.setProductoUnidad(ProductoUnidad);
                                                u.setProductoPrecio(ProductoPrecio);

                                                databaseReference.child("Cart").child(u.getCompradoruid()).child(u.getCompraId()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            databaseReference.child("CartDetail").child(u.getPublicadoruid()).child(u.getCompraId()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        ShowCart();
                                                                    }else{
                                                                        databaseReference.child("Cart").child(u.getCompradoruid()).child(u.getCompraId()).removeValue();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                                
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            
        }
    }

    private void ShowCart() {
        Intent Window = new Intent(ShoppingCart.this, Market.class);
        Window.putExtra("var", "Cart");
        Window.putExtra("tittle", "Compras");
        startActivity(Window);
    }

    private void ShoppingCartBack() {
        String product = ProductP.getText().toString().trim();
        String user = UserPublicadorP.getText().toString().trim();
        Intent Window = new Intent(ShoppingCart.this, Presale.class);
        Window.putExtra("product", product);
        Window.putExtra("user", user);
        startActivity(Window);
    }
}