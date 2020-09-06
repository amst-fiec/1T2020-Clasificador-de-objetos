package amst4.t12020.espol.clasificadordepesos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class mostrarResultados extends AppCompatActivity {
    String pesoValor;
    TextView txMostrar;
    String ValorSensor;
    TextView txNombre;


    private ImageView imv_photo;
    public DatabaseReference db_referencesensor; //Variable publica de referencia a la base de datos Balanza
    public DatabaseReference db_reference; //Variable publica de referencia a la base de datos Articulo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_resultados);
        imv_photo = findViewById(R.id.imv_foto);
        txMostrar=(TextView)findViewById(R.id.textView);
        txNombre=(TextView)findViewById(R.id.txnombre);
        iniciarBaseDeDatos();
        leerObjects();
        leerSensor();


    }

    //Iniciar base de datos
    public void iniciarBaseDeDatos(){
        db_reference = FirebaseDatabase.getInstance().getReference().child("Articulo");
        db_referencesensor = FirebaseDatabase.getInstance().getReference().child("Balanza");
    }

    //FUNCION QUE LEE LOS OBJETOS DE NUSTRA BASE DE DATOS
    public void leerObjects(){
        db_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                    //IMPLEMANTCION DE LA FUNCION MOSTRAR RESULTADOS
                    mostrarRegistrosPorPantalla(snapshot);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.toException());

            }
        });
    }


    //Funcion que lee el sensor y retornar el valor del mismo
    public String leerSensor(){
        db_referencesensor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ValorSensor= String.valueOf(dataSnapshot.child("valor").getValue());
                System.out.println(dataSnapshot.child("valor").getValue());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return ValorSensor;
    }

    //Funcion que trar los valores los compara y muestra  por pantalla
    //Recibe un snapchot

    public void mostrarRegistrosPorPantalla(DataSnapshot snapshot){
        String valorSensor=leerSensor();
        System.out.println(valorSensor);
        pesoValor = String.valueOf(snapshot.child("peso").getValue());
        String url= String.valueOf(snapshot.child("imagen").getValue());
        String nombre= String.valueOf(snapshot.child("nombre").getValue());


        //Comparacion de las variables Sensor y peso del  Articulo
        //MUESTRA DE DATOS  POR PANTALLA

        while(pesoValor.compareTo(valorSensor)==0){
            txMostrar.setText(pesoValor);
            txNombre.setText(nombre);
            Picasso.get().load(url)
                    .placeholder(R.drawable.ic_menu_camera)
                    .error(R.drawable.ic_menu_gallery)
                    .into(imv_photo);

            break;
        }
    }

    //Cierre de sesion
    public void cerrarSesion(View view){
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("msg","cerrarSesion");
        startActivity(intent);
    }
}