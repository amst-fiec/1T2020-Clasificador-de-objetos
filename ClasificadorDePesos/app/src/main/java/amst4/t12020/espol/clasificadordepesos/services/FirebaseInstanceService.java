package amst4.t12020.espol.clasificadordepesos.services;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import amst4.t12020.espol.clasificadordepesos.utilitarios.Articulo;

public class FirebaseInstanceService {

    private DatabaseReference db_referencesensor; //Variable publica de referencia a la base de datos Balanza
    private DatabaseReference db_reference; //Variable publica de referencia a la base de datos Articulo

    private List<Articulo> listaBase ;
    private Float pesoBalanza = Float.valueOf(0);

    public FirebaseInstanceService() {
        iniciarBaseDeDatos();
    }

    private void iniciarBaseDeDatos(){
        db_reference = FirebaseDatabase.getInstance().getReference().child("objeto");
        db_referencesensor = FirebaseDatabase.getInstance().getReference().child("Balanza");
    }

    public void leerArticulos(){

        db_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaBase = new ArrayList<Articulo>();
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                    Articulo datoActual = new Articulo();
                    datoActual.setKey(snapshot.getKey());
                    datoActual.setCategoria(String.valueOf(snapshot.child("nombre").getValue()));
                    datoActual.setPeso(Float.valueOf(String.valueOf(snapshot.child("peso").getValue())));
                    datoActual.setUrl(String.valueOf(snapshot.child("imagen").getValue()));
                    datoActual.setAciertos(Integer.valueOf(String.valueOf(snapshot.child("aciertos").getValue())));
                    datoActual.setFallos(Integer.valueOf(String.valueOf(snapshot.child("fallos").getValue())));
                    listaBase.add(datoActual);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.toException());

            }
        });
    }

    public void leerBalanza(){
        db_referencesensor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                    pesoBalanza=Float.valueOf(String.valueOf(snapshot.child("valor").getValue()));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.toException());
            }
        });
    }


    public List<Articulo> getListaArticulos() {
        return listaBase;
    }

    public Float getPesoBalanza() {
        return pesoBalanza;

    }

    public DatabaseReference getArticulosReference() {
        return db_reference;
    }
}
