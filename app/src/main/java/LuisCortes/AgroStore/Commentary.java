package LuisCortes.AgroStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import LuisCortes.AgroStore.Model.Comment;
import LuisCortes.AgroStore.Model.Publication;

public class Commentary extends AppCompatActivity {
    private List<Comment> ListComment = new ArrayList<Comment>();
    ArrayAdapter<Comment> arrayAdapterComment;
    ListView List_CommentP;
    EditText ProductP, UserP, CommentaryP;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentary);
        ProductP = findViewById(R.id.txt_product_commentary);
        UserP = findViewById(R.id.txt_user_commentary);
        CommentaryP = findViewById(R.id.txt_description_commentary);
        inicializarFirebase();
        GetData();
        List_CommentP = findViewById(R.id.lv_CommentaryData);
        ListData();
    }

    private void ListData() {
        String product = ProductP.getText().toString().trim();
        databaseReference.child("Commentary").child(product).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListComment.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Comment u = objSnapshot.getValue(Comment.class);
                    ListComment.add(u);
                    arrayAdapterComment = new ArrayAdapter<Comment>(Commentary.this, android.R.layout.simple_list_item_1, ListComment);
                    List_CommentP.setAdapter(arrayAdapterComment);
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

    private void GetData() {
        Intent Window = getIntent();
        String product = Window.getStringExtra("product");
        String user = Window.getStringExtra("user");
        String descripcion = Window.getStringExtra("descripcion");
        String nombrepublicador = Window.getStringExtra("nombrepublicador");
        Commentary.this.setTitle(descripcion);

        if((product != null) && (user != null)){
            ProductP.setText(product);
            UserP.setText(user);
        }

    }

    public void OnClick(View view) {
           switch (view.getId()){
                case R.id.btnCommentaryBack:
                    CommentaryBack();
                break;

            case R.id.btnCommentarySave:
                    BoxValidation();
                break;
        }
    }

    private void CommentaryBack() {
        String product = ProductP.getText().toString().trim();
        String user = UserP.getText().toString().trim();
        Intent Window = new Intent(Commentary.this, Presale.class);
        Window.putExtra("product", product);
        Window.putExtra("user", user);
        startActivity(Window);
    }

    private void BoxValidation() {
        String comment = CommentaryP.getText().toString().trim();
        if (comment.equals("")){
            CommentaryP.setError("Campo Requerido");
            CommentaryP.requestFocus();
        }else if (CommentaryP.length()<6){
            CommentaryP.setError("Minimo 6 Caracteres");
            CommentaryP.requestFocus();
        }else{
            CommnentSave();
        }
    }

    private void CommnentSave() {
        String comment = CommentaryP.getText().toString().trim();
        String product = ProductP.getText().toString().trim();
        FirebaseUser User = firebaseauth.getCurrentUser();

        if(User != null){
            String uid = User.getUid();
            databaseReference.child("User").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String nombre = dataSnapshot.child("nombre").getValue().toString().trim();
                        Comment u = new Comment();
                        u.setUid(UUID.randomUUID().toString());
                        u.setProduct(product);
                        u.setDescripcion(comment);
                        u.setNombreUser(nombre);
                        databaseReference.child("Commentary").child(u.getProduct()).child(u.getUid()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Commentary.this,"Tu comentario ha sido publicado", Toast.LENGTH_LONG).show();
                                    CommentaryP.setText("");
                                    CommentaryP.requestFocus();
                                }else{
                                    Toast.makeText(Commentary.this,task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
}