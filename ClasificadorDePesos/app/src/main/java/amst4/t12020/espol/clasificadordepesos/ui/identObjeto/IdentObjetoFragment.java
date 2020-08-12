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

import amst4.t12020.espol.clasificadordepesos.R;

public class IdentObjetoFragment extends Fragment {

    private IdentObjetoViewModel identObjetoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        identObjetoViewModel =
                ViewModelProviders.of(this).get(IdentObjetoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_ident_objeto, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        identObjetoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}