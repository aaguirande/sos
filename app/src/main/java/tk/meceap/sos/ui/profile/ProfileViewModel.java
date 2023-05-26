package tk.meceap.sos.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import tk.meceap.sos.constants.Core;
import tk.meceap.sos.models.Agent;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<Agent> mText;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(Core.getInstance().getUserLogged());
    }

    public LiveData<Agent> getText() {
        return mText;
    }
}