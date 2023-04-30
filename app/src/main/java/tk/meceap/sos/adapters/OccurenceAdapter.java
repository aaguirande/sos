package tk.meceap.sos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tk.meceap.sos.R;
import tk.meceap.sos.constants.Core;
import tk.meceap.sos.models.Occurency;

public class OccurenceAdapter extends RecyclerView.Adapter<OccurenceAdapter.ViewHolder> {
    private Context context;
    private List<Occurency> occurencies;

    public OccurenceAdapter(List<Occurency> occurencies) { this.occurencies = occurencies; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.item_call, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(occurencies != null && occurencies.size() > 0){
            Occurency oc = occurencies.get(position);
            if(oc != null){
                holder.category.setText(oc.getVictim_uid());
                holder.name.setText(oc.getVictim_address());
                holder.status_call.setText(oc.getVictim_name());
                holder.occurrency_type.setText(oc.getOccurencyType().getName());
            }
        }
    }

    @Override
    public int getItemCount() { return occurencies.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, category, status_call, occurrency_type, qtdAcquire, date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.location);
            category = (TextView) itemView.findViewById(R.id.status_segment);
            status_call = (TextView) itemView.findViewById(R.id.status_call);
            occurrency_type = (TextView) itemView.findViewById(R.id.occurrency_type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(occurencies.size() > 0){
                        Core.getInstance().setSelectedOccurency(occurencies.get(getLayoutPosition()));
                        Core.getInstance().displaySelectedScreen(R.layout.content_form);
                    }
                }
            });
        }
    }
}
