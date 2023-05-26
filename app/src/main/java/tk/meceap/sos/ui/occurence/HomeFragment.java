package tk.meceap.sos.ui.occurence;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tk.meceap.sos.R;
import tk.meceap.sos.adapters.OccurenceAdapter;
import tk.meceap.sos.adapters.OccurenceViewModel;
import tk.meceap.sos.constants.Core;
import tk.meceap.sos.databinding.FragmentHomeBinding;
import tk.meceap.sos.models.Occurrence;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private OccurenceAdapter adapter;
    private OccurenceViewModel occurenceViewModel;
    FragmentHomeBinding root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        occurenceViewModel = new ViewModelProvider(this).get(OccurenceViewModel.class);

        root = FragmentHomeBinding.inflate(getLayoutInflater());
        return root.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        occurenceViewModel = new ViewModelProvider(this).get(OccurenceViewModel.class);

        occurenceViewModel.getOccurences().observe(getViewLifecycleOwner(), new Observer<List<Occurrence>>() {
            @Override
            public void onChanged(List<Occurrence> categories) {
                show(root.getRoot());
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        menu.clear();
        inflater.inflate(R.menu.occurrence, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_occurrence_refresh:

                // Not implemented here
                return false;
            case R.id.menu_occurrence_filter:

                // Do Fragment menu item stuff here
                return true;

            default:
                break;
        }

        return false;
    }

    public void show(View root) {
        recyclerView = root.findViewById(R.id.occurencies);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new OccurenceAdapter(Core.getInstance().getOccurencies());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        root = null;
    }
}