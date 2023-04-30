package tk.meceap.sos.adapters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import tk.meceap.sos.constants.Core;
import tk.meceap.sos.models.Occurency;

public class OccurenceViewModel extends ViewModel {

    private MutableLiveData<List<Occurency>> data;

    public OccurenceViewModel() {
        data = new MutableLiveData<>();
        data.setValue(Core.getInstance().getOccurencies());
    }

    public LiveData<List<Occurency>> getOccurences() {
        return data;
    }
}