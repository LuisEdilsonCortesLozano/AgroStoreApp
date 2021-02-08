package LuisCortes.AgroStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import LuisCortes.AgroStore.Model.Publication;

public class MyPublication extends AppCompatActivity {
    String title= "Mis Productos";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseauth;
    private List<Publication> ListMyPublication = new ArrayList<Publication>();
    ArrayAdapter<Publication> arrayAdapterMyPublication;
    ListView List_MyPublicationP;
    Publication MyPublicationSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publication);
        this.setTitle(title);
        inicializarFirebase();
        List_MyPublicationP = findViewById(R.id.lv_MyPublicationData);
        ListData();

        List_MyPublicationP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyPublicationSelected = (Publication) parent.getItemAtPosition(position);
                String cod_prod = MyPublicationSelected.getUid();
                Intent Window = new Intent(MyPublication.this, LuisCortes.AgroStore.Publication.class);
                Window.putExtra("product", cod_prod);
                startActivity(Window);
            }
        });
    }

    private void ListData() {
        FirebaseUser User = firebaseauth.getCurrentUser();
        if(User != null){
            String uid = User.getUid();
            databaseReference.child("PublicationDetail").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ListMyPublication.clear();
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                        Publication u = objSnapshot.getValue(Publication.class);
                        ListMyPublication.add(u);
                        arrayAdapterMyPublication = new ArrayAdapter<Publication>(MyPublication.this, android.R.layout.simple_list_item_1, ListMyPublication);
                        List_MyPublicationP.setAdapter(arrayAdapterMyPublication);
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
        switch (view.getId()) {
            case R.id.btnMenuMyPublication:
                Intent Window = new Intent(MyPublication.this, Home.class);
                startActivity(Window);
                break;
        }
    }
}