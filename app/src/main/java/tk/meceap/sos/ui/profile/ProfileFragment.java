package tk.meceap.sos.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tk.meceap.sos.R;
import tk.meceap.sos.constants.Core;
import tk.meceap.sos.databinding.FragmentGalleryBinding;
import tk.meceap.sos.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private FragmentProfileBinding binding;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Core.getInstance().getMainActivity().viewIsAtHome = false;

        try {
            binding.profileName.setText(Core.getInstance().getUserLogged().getName());
            binding.totalLogin.setText(Core.getInstance().getUserLogged().getLoginCount());
            binding.totalActions.setText(Core.getInstance().getUserLogged().getActionLogCount());
            binding.totalRating.setText(Core.getInstance().getUserLogged().getRating());
            binding.profileEmail.setText(Core.getInstance().getUserLogged().getEmail());
            binding.profileGroup.setText(Core.getInstance().getUserLogged().getGroup());
            binding.profileEntity.setText(Core.getInstance().getUserLogged().getDaysSinceMember());
            binding.profileContact.setText(Core.getInstance().getUserLogged().getContact());
            binding.profileCalendar.setText(
                    Core.getInstance().getCalendars().get(0).getEventDate() + " " +
                    Core.getInstance().getCalendars().get(0).getEventStartTime() + " - "+
                    Core.getInstance().getCalendars().get(0).getEventEndTime() + " " +
                    Core.getInstance().getCalendars().get(0).getEventStatus() + " "
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}