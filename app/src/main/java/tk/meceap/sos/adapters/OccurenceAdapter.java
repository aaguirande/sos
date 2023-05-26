package tk.meceap.sos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.core.CometChat;

import java.util.List;

import tk.meceap.sos.R;
import tk.meceap.sos.constants.Core;
import tk.meceap.sos.models.Occurrence;

public class OccurenceAdapter extends RecyclerView.Adapter<OccurenceAdapter.ViewHolder> {
    private Context context;
    private List<Occurrence> occurencies;

    public OccurenceAdapter(List<Occurrence> occurencies) { this.occurencies = occurencies; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.cardview_occurrece, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(occurencies != null && occurencies.size() > 0){
            Occurrence oc = occurencies.get(position);
            if(oc != null){
                holder.occurrenceType.setText(oc.getOccurencyType().getName());
                holder.occurrenceLocation.setText(oc.getAgentsFollowUp());
                holder.occurrenceDetails.setText(oc.getDetails());
                holder.occurrenceFlag.setText(oc.getFlag());
                holder.occurrenceDate.setText(oc.getDate());
                holder.occurrenceVictim.setText(oc.getVictimName());
                holder.occurrenceAgent.setText(oc.getAllocatedTo());
            }
        }
    }

    @Override
    public int getItemCount() { return occurencies.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView occurrenceType, occurrenceLocation, occurrenceDetails, occurrenceFlag, occurrenceDate,
                occurrenceVictim, occurrenceAgent, occurrenceEdit, occurrenceMap, occurrenceCall;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            occurrenceType = (TextView) itemView.findViewById(R.id.occurrence_type);
            occurrenceLocation = (TextView) itemView.findViewById(R.id.occurrence_location);
            occurrenceDetails = (TextView) itemView.findViewById(R.id.occurrence_details);
            occurrenceFlag = (TextView) itemView.findViewById(R.id.occurrence_flag);
            occurrenceDate = (TextView) itemView.findViewById(R.id.occurrence_date);
            occurrenceVictim = (TextView) itemView.findViewById(R.id.occurrence_victim);
            occurrenceAgent = (TextView) itemView.findViewById(R.id.occurrence_agent);

            occurrenceEdit = (TextView) itemView.findViewById(R.id.occurrence_edit);
            occurrenceMap = (TextView) itemView.findViewById(R.id.occurrence_map);
            occurrenceCall = (TextView) itemView.findViewById(R.id.occurrence_call);

            occurrenceEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(occurencies.size() > 0){
                        Core.getInstance().setSelectedOccurency(occurencies.get(getLayoutPosition()));
                        Core.getInstance().updateOccurrenceDialog(getLayoutPosition());
                    }
                }
            });

            occurrenceMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(occurencies.size() > 0){
                        Core.getInstance().setSelectedOccurency(occurencies.get(getLayoutPosition()));
                        Core.getInstance().getMainActivity().navController.navigate(R.id.nav_maps);
                    }
                }
            });

            occurrenceCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(occurencies.size() > 0){
                        Core.getInstance().setSelectedOccurency(occurencies.get(getLayoutPosition()));
                        if(!Core.getInstance().getSelectedOccurency().getVictimUid().equalsIgnoreCase(CometChat.getLoggedInUser().getUid()))
                            Core.getInstance().makeCall(Core.getInstance().getSelectedOccurency().getVictimUid());
                        else
                            Core.getInstance().alertFail("Unable call to you...");
                    }
                }
            });
        }
    }
}
