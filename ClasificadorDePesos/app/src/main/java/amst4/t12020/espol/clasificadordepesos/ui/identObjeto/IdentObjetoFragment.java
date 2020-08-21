package amst4.t12020.espol.clasificadordepesos.ui.identObjeto;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import amst4.t12020.espol.clasificadordepesos.R;

public class IdentObjetoFragment extends Fragment {

    private IdentObjetoViewModel identObjetoViewModel;

    public DatabaseReference db_reference; //Variable publica de referencia a la base de datos


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        identObjetoViewModel =
                ViewModelProviders.of(this).get(IdentObjetoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_ident_objeto, container, false);
        iniciarBaseDeDatos();
        leerObjetos();
        return root;
    }



    //Iniciar base de datos
    public void iniciarBaseDeDatos(){
        db_reference = FirebaseDatabase.getInstance().getReference().child("objeto");
    }

    public void leerObjetos(){
        db_reference.child("1").child("imagen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    System.out.println(snapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError Error) {
                System.out.println(Error.toException());
            }
        });
    }

}