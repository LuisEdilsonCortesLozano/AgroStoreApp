package LuisCortes.AgroStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

import LuisCortes.AgroStore.Model.BuyProduct;
import LuisCortes.AgroStore.Model.Publication;

public class Market extends AppCompatActivity {
    private List<BuyProduct> ListBuyProduct = new ArrayList<BuyProduct>();
    ArrayAdapter<BuyProduct> arrayAdapterBuyProduct;
    ListView List_BuyProductP;
    TextView MarketTittleP;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        List_BuyProductP = findViewById(R.id.lv_marketData);
        MarketTittleP = findViewById(R.id.Matket_tittle);
        inicializarFirebase();
        GetData();
    }

    private void GetData() {
        Intent Window = getIntent();
        String var = Window.getStringExtra("var");
        String tittle = Window.getStringExtra("tittle");
        Market.this.setTitle(tittle);
        MarketTittleP.setText(tittle);
        FirebaseUser User = firebaseauth.getCurrentUser();
        String uid = User.getUid();
        ListData(var,uid);
    }

    private void ListData(String service, String uid) {
        databaseReference.child(service).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListBuyProduct.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    BuyProduct u = objSnapshot.getValue(BuyProduct.class);
                    ListBuyProduct.add(u);
                    arrayAdapterBuyProduct = new ArrayAdapter<BuyProduct>(Market.this, android.R.layout.simple_list_item_1, ListBuyProduct);
                    List_BuyProductP.setAdapter(arrayAdapterBuyProduct);
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
            case R.id.btnMarketBack:
                MarketBack();
                break;
        }
    }

    private void MarketBack() {
        Intent Window = new Intent(Market.this, Home.class);
        startActivity(Window);
    }
}