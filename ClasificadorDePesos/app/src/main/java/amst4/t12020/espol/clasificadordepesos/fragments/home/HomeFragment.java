package amst4.t12020.espol.clasificadordepesos.fragments.home;

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

public class HomeFragment extends Fragment {

    //Variables de mapeo con la vista
    private TextView nombreObjeto;
    private TextView txtPrecision;
    private TextView pesoObjeto;
    private ImageView imagenObjeto;
    private Button botonAcierto;

    //Variables de conexion con la base de Datos
    private FirebaseInstanceService servicioBase;
    private List<Articulo> listaBase = null;
    private float pesoBalanza = 0;
    float acumuladorAciertos=0;
    float acumuladorFallos=0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        nombreObjeto = root.findViewById(R.id.text_home);
        imagenObjeto = (ImageView) root.findViewById(R.id.imgFotoObj);
        pesoObjeto = root.findViewById(R.id.txt_peso_objeto);
        botonAcierto = root.findViewById(R.id.btn_sensar_home);
        txtPrecision = root.findViewById(R.id.txt_precision);

        servicioBase = new FirebaseInstanceService();
        servicioBase.leerBalanza();
        servicioBase.leerArticulos();

        txtPrecision.setVisibility(View.GONE);

        botonAcierto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                actualizarPorPeso();
            }
        });

        nombreObjeto.setText("Pese un objeto!");
        Picasso.get().load(R.drawable.bot_final)
                .placeholder(R.drawable.ic_menu_camera)
                .error(R.drawable.ic_menu_gallery)
                .into(imagenObjeto);
        pesoObjeto.setText("El peso de su objeto aparecera aqui!");

        return root;
    }

    public void actualizarPorPeso(){
        pesoBalanza = servicioBase.getPesoBalanza();
        listaBase = servicioBase.getListaArticulos();

        float precision = 0;

        List<Articulo> listaRepetidos = new ArrayList<Articulo>();
        Articulo mayorAciertos = null;
        int aciertosActuales=0;

        for(Articulo actual: listaBase){
            acumuladorAciertos= acumuladorAciertos + actual.getAciertos();
            acumuladorFallos = acumuladorFallos + actual.getFallos();
            if(pesoBalanza>=actual.getPeso()-0.01 && pesoBalanza<=actual.getPeso()+0.01 ){
                listaRepetidos.add(actual);
            }
        }

        double preci=0;

        if(acumuladorAciertos+acumuladorFallos!=0){
            float denominador = acumuladorFallos + acumuladorAciertos;
            precision= acumuladorAciertos / denominador;
            precision=precision*100;
        }

        txtPrecision.setVisibility(View.VISIBLE);
        txtPrecision.setText("La precision actual del sistema es: "+String.format ("%.2f", Double.valueOf(precision))+"%");

        if(listaRepetidos.size()==0){
            nombreObjeto.setText("Sense un objeto!");
            Picasso.get().load(R.drawable.bot_final)
                    .placeholder(R.drawable.ic_menu_camera)
                    .error(R.drawable.ic_menu_gallery)
                    .into(imagenObjeto);
            pesoObjeto.setText("El peso de su objeto aparecera aqui!");
            Toast.makeText(getContext(),"No existe un objeto con ese peso",Toast.LENGTH_LONG).show();
        }

        if(listaRepetidos.size()==1){
            nombreObjeto.setText(listaRepetidos.get(0).getCategoria());
            Picasso.get().load(listaRepetidos.get(0).getUrl())
                    .placeholder(R.drawable.ic_menu_camera)
                    .error(R.drawable.ic_menu_gallery)
                    .into(imagenObjeto);
            pesoObjeto.setText(String.valueOf(listaRepetidos.get(0).getPeso()));
        }else if(listaRepetidos.size() >1){
            for(Articulo actual: listaRepetidos){
                if(actual.getAciertos()>aciertosActuales){
                    aciertosActuales=actual.getAciertos();
                    mayorAciertos = actual;
                }
            }

            if(mayorAciertos!=null){
                nombreObjeto.setText(mayorAciertos.getCategoria());
                Picasso.get().load(mayorAciertos.getUrl())
                        .placeholder(R.drawable.ic_menu_camera)
                        .error(R.drawable.ic_menu_gallery)
                        .into(imagenObjeto);
                pesoObjeto.setText(String.valueOf(mayorAciertos.getPeso()));
            }
        }
    }





}