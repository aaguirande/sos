package tk.meceap.sos.ui.calendar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import tk.meceap.sos.constants.Core;
import tk.meceap.sos.models.Calendar;

public class CalendarViewModel extends ViewModel {
    private MutableLiveData<List<Calendar>> data;

    public CalendarViewModel() {
        data = new MutableLiveData<>();
        data.setValue(Core.getInstance().getCalendars());
    }

    public LiveData<List<Calendar>> getCalendars() {
        return data;
    }
}