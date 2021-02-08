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

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import LuisCortes.AgroStore.Model.Publication;

public class Store extends AppCompatActivity {
    String title= "Tienda";
    private List<Publication> ListPublication = new ArrayList<Publication>();
    ArrayAdapter<Publication> arrayAdapterPublication;
    ListView List_PublicationP;
    Publication PublicationSelected;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        this.setTitle(title);
        List_PublicationP = findViewById(R.id.lv_PublicationData);
        inicializarFirebase();
        ListData();
        List_PublicationP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PublicationSelected = (Publication) parent.getItemAtPosition(position);
                String cod_prod = PublicationSelected.getUid();
                String cod_user = PublicationSelected.getUser();
                Intent Window = new Intent(Store.this, Presale.class);
                Window.putExtra("product", cod_prod);
                Window.putExtra("user", cod_user);
                startActivity(Window);
            }
        });
    }

    private void ListData() {
        databaseReference.child("Publication").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListPublication.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Publication u = objSnapshot.getValue(Publication.class);
                    ListPublication.add(u);
                    arrayAdapterPublication = new ArrayAdapter<Publication>(Store.this, android.R.layout.simple_list_item_1, ListPublication);
                    List_PublicationP.setAdapter(arrayAdapterPublication);
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
            case R.id.btnMenuStore:
                Intent Window = new Intent(Store.this, Home.class);
                startActivity(Window);
                break;

        }
    }
}