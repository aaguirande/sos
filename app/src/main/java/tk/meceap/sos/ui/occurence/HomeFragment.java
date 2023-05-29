package tk.meceap.sos.ui.occurence;

import static tk.meceap.sos.R.drawable.ic_more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

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
    TextView pageMore, pageFirst, pageLast, pagePenult, pageSecond, pageThird, pageRight, pageLeft;
    LinearLayout pages;

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

        pageMore = view.findViewById(R.id.page_more);
        pageFirst = view.findViewById(R.id.page_first);
        pageLast = view.findViewById(R.id.page_last);
        pagePenult = view.findViewById(R.id.page_penult);
        pageSecond = view.findViewById(R.id.page_second);
        pageThird = view.findViewById(R.id.page_third);
        pageRight = view.findViewById(R.id.page_right);
        pageLeft = view.findViewById(R.id.page_left);
        pages = view.findViewById(R.id.pages);

        if (Core.getInstance().occurrencePagination) pages.setVisibility(View.VISIBLE);
        else pages.setVisibility(View.GONE);

        if(Core.getInstance().getOccurrenceFilter().getLastPage() > 1 ){
            pageLeft.setVisibility(View.VISIBLE);
            pageRight.setVisibility(View.VISIBLE);
        }

        pageMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMorePagination();

                if(Core.getInstance().occurrencePagination){
                    Core.getInstance().occurrencePagination = false;
                    pages.setVisibility(View.VISIBLE);
                } else {
                    Core.getInstance().occurrencePagination = true;
                    pages.setVisibility(View.GONE);
                }

            }
        });

        showPagination();

        occurenceViewModel.getOccurences().observe(getViewLifecycleOwner(), new Observer<List<Occurrence>>() {
            @Override
            public void onChanged(List<Occurrence> categories) {
                show(root.getRoot());
            }
        });
    }

    private void showPagination() {
        if(Core.getInstance().getOccurrenceFilter().getLastPage() > 0)
            pageFirst.setVisibility(View.VISIBLE);
        if(Core.getInstance().getOccurrenceFilter().getLastPage() > 1)
            pageSecond.setVisibility(View.VISIBLE);
        if(Core.getInstance().getOccurrenceFilter().getLastPage() > 2)
            pageThird.setVisibility(View.VISIBLE);
        if(Core.getInstance().getOccurrenceFilter().getLastPage() > 3)
            pagePenult.setVisibility(View.VISIBLE);
        if(Core.getInstance().getOccurrenceFilter().getLastPage() > 4){
            pageLast.setVisibility(View.VISIBLE);
            pageLast.setText(String.valueOf(Core.getInstance().getOccurrenceFilter().getLastPage()));
        }

        showMorePagination();

    }

    private void showMorePagination() {
        if(Core.getInstance().getOccurrenceFilter().getCurrentPage() < 4 && Core.getInstance().getOccurrenceFilter().getLastPage() > 5){
            pagePenult.setText("");
            pageSecond.setText("2");
            pageThird.setText("3");
            pagePenult.setCompoundDrawables(getContext().getResources().getDrawable(R.drawable.ic_more), null, null, null);
            pageSecond.setCompoundDrawables(null, null, null, null);
        }
        else if (Core.getInstance().getOccurrenceFilter().getLastPage() - Core.getInstance().getOccurrenceFilter().getCurrentPage() < 3){
            pageSecond.setText("");
            pagePenult.setText(String.valueOf(Core.getInstance().getOccurrenceFilter().getLastPage() -2));
            pageThird.setText(String.valueOf(Core.getInstance().getOccurrenceFilter().getLastPage() -1));
            pagePenult.setCompoundDrawables(null, null, null, null);
            pageSecond.setCompoundDrawables(getContext().getResources().getDrawable(R.drawable.ic_more), null, null, null);

        }
        else if (Core.getInstance().getOccurrenceFilter().getLastPage() > 5){
            pageSecond.setText("");
            pagePenult.setText("");
            pageThird.setText(Core.getInstance().getOccurrenceFilter().getCurrentPage());
            pagePenult.setCompoundDrawables(getContext().getResources().getDrawable(R.drawable.ic_more), null, null, null);
            pageSecond.setCompoundDrawables(getContext().getResources().getDrawable(R.drawable.ic_more), null, null, null);

        }
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
                Core.getInstance().showOccurrenceFilter();
                // Do Fragment menu item stuff here
                return true;
            case R.id.menu_logout:

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