package tk.meceap.sos.ui.calendars;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tk.meceap.sos.R;
import tk.meceap.sos.adapters.CalendarAdapter;
import tk.meceap.sos.adapters.CalendarViewModel;
import tk.meceap.sos.constants.Core;
import tk.meceap.sos.databinding.FragmentSlideshowBinding;
import tk.meceap.sos.models.Calendar;

public class CalendarFragment extends Fragment {
    private FragmentSlideshowBinding root;
    private RecyclerView recyclerView;
    private CalendarAdapter adapter;
    private CalendarViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(CalendarViewModel.class);

        root = FragmentSlideshowBinding.inflate(getLayoutInflater());

        return root.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CalendarViewModel.class);

        viewModel.getCalendars().observe(getViewLifecycleOwner(), new Observer<List<Calendar>>() {
            @Override
            public void onChanged(List<Calendar> data) {
                show(root.getRoot());
            }
        });
    }

    public void show(View root) {
        recyclerView = root.findViewById(R.id.occurencies);
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