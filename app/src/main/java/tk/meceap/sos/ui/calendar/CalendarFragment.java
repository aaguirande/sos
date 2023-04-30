package tk.meceap.sos.ui.calendar;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tk.meceap.sos.R;
import tk.meceap.sos.adapters.CalendarAdapter;
import tk.meceap.sos.constants.Core;
import tk.meceap.sos.databinding.FragmentCalendarBinding;
import tk.meceap.sos.databinding.FragmentSlideshowBinding;
import tk.meceap.sos.models.Calendar;

public class CalendarFragment extends Fragment {
    private FragmentCalendarBinding root;
    private CalendarViewModel mViewModel;
    private RecyclerView recyclerView;
    private CalendarAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);

        root = FragmentCalendarBinding.inflate(getLayoutInflater());
        return root.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);

        mViewModel.getCalendars().observe(getViewLifecycleOwner(), new Observer<List<Calendar>>() {
            @Override
            public void onChanged(List<Calendar> data) {
                show(root.getRoot());
            }
        });
    }

    public void show(View root) {
        recyclerView = root.findViewById(R.id.calendars);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new CalendarAdapter(Core.getInstance().getCalendars());
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        root = null;
    }

}