package LuisCortes.AgroStore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {
    String title= "Inicio";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.setTitle(title);
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
            case R.id.btnBackHome:
                SesionClose();
                break;

            case R.id.btnNewPublication:
                Window = new Intent(Home.this, NewPublication.class);
                startActivity(Window);
                break;

            case R.id.btnUserProfileShow:
                Window = new Intent(Home.this, UserProfile.class);
                startActivity(Window);
                break;

            case R.id.btnStore:
                Window = new Intent(Home.this, Store.class);
                startActivity(Window);
                break;

            case R.id.btnSearchPublication:
                Window = new Intent(Home.this, MyPublication.class);
                startActivity(Window);
                break;

            case R.id.btnCart:
                Window = new Intent(Home.this, Market.class);
                Window.putExtra("var", "Cart");
                Window.putExtra("tittle", "Compras");
                startActivity(Window);
                break;

            case R.id.btnSale:
                Window = new Intent(Home.this, Market.class);
                Window.putExtra("var", "CartDetail");
                Window.putExtra("tittle", "Ventas");
                startActivity(Window);
                break;
        }
    }

    private void SesionClose() {
        firebaseauth.signOut();
        Intent Window = new Intent(Home.this, Index.class);
        startActivity(Window);
        this.finish();
    }
}