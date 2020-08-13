package amst4.t12020.espol.clasificadordepesos.ui.identObjeto;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IdentObjetoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public IdentObjetoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Este es el fragmento de identificar objeto");
    }

    public LiveData<String> getText() {
        return mText;
    }
}