package amst4.t12020.espol.clasificadordepesos.fragments.entrenamiento;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import amst4.t12020.espol.clasificadordepesos.R;
import amst4.t12020.espol.clasificadordepesos.services.FirebaseInstanceService;
import amst4.t12020.espol.clasificadordepesos.utilitarios.Articulo;

public class entrenamientoFragment extends Fragment {

    //Variables de conexion con la base de Datos
    private FirebaseInstanceService servicioBase;
    private List<Articulo> listaBase;
    private float pesoBalanza;

    private TextView txtNombre;
    private TextView txtPeso;
    private TextView txtUbicacion;
    private Button btnSeleccionar;
    private Button btnAnterior;
    private Button btnSiguiente;
    private ImageView imgFotoArticulo;
    List<Articulo> listaRepetidos;

    private Articulo[] articulos;

    int posicionArticulo;

    boolean primeraVuelta=true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_entrenamiento, container, false);

        servicioBase = new FirebaseInstanceService();
        servicioBase.leerBalanza();
        servicioBase.leerArticulos();


        txtNombre = root.findViewById(R.id.txt_nombre);
        txtPeso = root.findViewById(R.id.txt_peso_articulo);
        txtUbicacion = root.findViewById(R.id.txt_num_articulo);

        btnSeleccionar = root.findViewById(R.id.btn_seleccionar);
        btnAnterior = root.findViewById(R.id.btn_anterior);
        btnSiguiente = root.findViewById(R.id.btn_siguiente);

        imgFotoArticulo = root.findViewById(R.id.img_foto_articulo);
        btnSiguiente.setVisibility(View.GONE);
        btnAnterior.setVisibility(View.GONE);

        btnSeleccionar.setText("Sensar");

        btnSeleccionar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sensarSeleccionar();
            }
        });
        btnAnterior.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                moverAnterior();
            }
        });
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                moverSiguiente();
            }
        });

        return root;
    }


    public void sensarSeleccionar(){
        if (primeraVuelta){
            posicionArticulo=0;
            btnSeleccionar.setText("Seleccionar");
            sensar();
            if(listaRepetidos.size()==0){
                Toast.makeText(getContext(),"No existe un objeto con ese peso",Toast.LENGTH_LONG).show();
            }else{
                primeraVuelta=false;
            }
        }else{

            txtNombre.setText("Nombre Articulo");
            Picasso.get().load(R.drawable.balanzaprueba)
                    .placeholder(R.drawable.ic_menu_camera)
                    .error(R.drawable.ic_menu_gallery)
                    .into(imgFotoArticulo);
            txtPeso.setText("Peso Articulo");
            txtUbicacion.setText("X/X");

            btnSeleccionar.setText("Sensar");
            btnSiguiente.setVisibility(View.GONE);
            btnAnterior.setVisibility(View.GONE);

            //Actualizar el numero de aciertos o fallos
            if(articulos[posicionArticulo-1].isRevisado()){
                servicioBase.getArticulosReference().child(articulos[posicionArticulo-1].getKey()).child("aciertos").setValue(String.valueOf(articulos[posicionArticulo-1].getAciertos()+1));
                servicioBase.getArticulosReference().child(articulos[posicionArticulo-1].getKey()).child("fallos").setValue(String.valueOf(articulos[posicionArticulo-1].getFallos()-1));
            }else{
                servicioBase.getArticulosReference().child(articulos[posicionArticulo-1].getKey()).child("aciertos").setValue(String.valueOf(articulos[posicionArticulo-1].getAciertos()+1));
            }


            servicioBase.leerBalanza();
            servicioBase.leerArticulos();
            Toast.makeText(getContext(),"Articulo Actualizado",Toast.LENGTH_LONG).show();

            primeraVuelta=true;
            posicionArticulo=0;
        }
    }


    public void sensar(){
        posicionArticulo=0;
        pesoBalanza = servicioBase.getPesoBalanza();
        listaBase = servicioBase.getListaArticulos();

        listaRepetidos = new ArrayList<Articulo>();
        Articulo mayorAciertos = null;
        int aciertosActuales=-1;


        int tamanioArreglo;

        for(Articulo actual: listaBase){
            if(pesoBalanza>=actual.getPeso()-0.01 && pesoBalanza<=actual.getPeso()+0.01 ){
                listaRepetidos.add(actual);
            }
        }

        if(listaRepetidos.size()==0){
            return;
        }

        tamanioArreglo =listaRepetidos.size();

        articulos = new Articulo[tamanioArreglo];

        int cont=0;

        for(Articulo actual: listaRepetidos){
            actual.setPosicion(cont);
            articulos[cont]=actual;
            cont++;
        }

        if(listaRepetidos.size()==1){
            txtNombre.setText(listaRepetidos.get(0).getCategoria());
            Picasso.get().load(listaRepetidos.get(0).getUrl())
                    .placeholder(R.drawable.ic_menu_camera)
                    .error(R.drawable.ic_menu_gallery)
                    .into(imgFotoArticulo);
            txtPeso.setText(String.valueOf(listaRepetidos.get(0).getPeso()));
            posicionArticulo=1;
        }else if(listaRepetidos.size() >1){
            for(Articulo actual: listaRepetidos){
                if(actual.getAciertos()>aciertosActuales){
                    aciertosActuales=actual.getAciertos();
                    mayorAciertos = actual;
                    posicionArticulo++;
                }
            }

            if(mayorAciertos!=null){
                txtNombre.setText(mayorAciertos.getCategoria());
                Picasso.get().load(mayorAciertos.getUrl())
                        .placeholder(R.drawable.ic_menu_camera)
                        .error(R.drawable.ic_menu_gallery)
                        .into(imgFotoArticulo);
                txtPeso.setText(String.valueOf(mayorAciertos.getPeso()));
                posicionArticulo=mayorAciertos.getPosicion()+1;
            }
        }

        txtUbicacion.setText(posicionArticulo+"/"+articulos.length);
        if(posicionArticulo==articulos.length  && articulos.length!=1){
            btnSiguiente.setVisibility(View.GONE);
            btnAnterior.setVisibility(View.VISIBLE);
        }else if(articulos.length==1){
            btnSiguiente.setVisibility(View.GONE);
            btnAnterior.setVisibility(View.GONE);
        }else if(posicionArticulo>1){
            btnSiguiente.setVisibility(View.VISIBLE);
            btnAnterior.setVisibility(View.VISIBLE);
        }else if(posicionArticulo==1){
            btnSiguiente.setVisibility(View.VISIBLE);
            btnAnterior.setVisibility(View.GONE);
        }
    }

    public void moverSiguiente(){
        if(posicionArticulo<=articulos.length-1){
            txtNombre.setText(articulos[posicionArticulo].getCategoria());
            Picasso.get().load(articulos[posicionArticulo].getUrl())
                    .placeholder(R.drawable.ic_menu_camera)
                    .error(R.drawable.ic_menu_gallery)
                    .into(imgFotoArticulo);
            txtPeso.setText(String.valueOf(articulos[posicionArticulo].getPeso()));
            txtUbicacion.setText(posicionArticulo+1+"/"+articulos.length);
            if(!articulos[posicionArticulo-1].isRevisado()){
                servicioBase.getArticulosReference().child(articulos[posicionArticulo-1].getKey()).child("fallos").setValue(String.valueOf(articulos[posicionArticulo-1].getFallos()+1));
                articulos[posicionArticulo-1].setFallos(articulos[posicionArticulo-1].getFallos()+1);
                articulos[posicionArticulo-1].setRevisado(true);
            }

            posicionArticulo++;
            if(posicionArticulo==articulos.length){
                btnSiguiente.setVisibility(View.GONE);
            }
            btnAnterior.setVisibility(View.VISIBLE);
        }

    }

    public void moverAnterior(){
        if(posicionArticulo-2>=0){
            if(!articulos[posicionArticulo-1].isRevisado()){
                servicioBase.getArticulosReference().child(articulos[posicionArticulo-1].getKey()).child("fallos").setValue(String.valueOf(articulos[posicionArticulo-1].getFallos()+1));
                articulos[posicionArticulo-1].setFallos(articulos[posicionArticulo-1].getFallos()+1);
                articulos[posicionArticulo-1].setRevisado(true);
            }
            posicionArticulo--;
            txtNombre.setText(articulos[posicionArticulo-1].getCategoria());
            Picasso.get().load(articulos[posicionArticulo-1].getUrl())
                    .placeholder(R.drawable.ic_menu_camera)
                    .error(R.drawable.ic_menu_gallery)
                    .into(imgFotoArticulo);
            txtPeso.setText(String.valueOf(articulos[posicionArticulo-1].getPeso()));
            txtUbicacion.setText(posicionArticulo+"/"+articulos.length);

            if(posicionArticulo-1==0){
                btnAnterior.setVisibility(View.GONE);
            }
            btnSiguiente.setVisibility(View.VISIBLE);

        }

    }







}