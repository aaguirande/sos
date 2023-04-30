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
import tk.meceap.sos.models.Calendar;
import tk.meceap.sos.models.Occurency;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private Context context;
    private List<Calendar> calendars;

    public CalendarAdapter(List<Calendar> calendars) {
        this.calendars = calendars;
    }

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
        if(calendars != null && calendars.size() > 0){
            Calendar oc = calendars.get(position);
            if(oc != null){
                holder.category.setText(oc.getGroupCalendar());
                holder.name.setText(oc.getEventCalendar());
                holder.status_call.setText(oc.getEventDate());
                holder.occurrency_type.setText(oc.getEventStartTime());
            }
        }
    }

    @Override
    public int getItemCount() {
        return calendars.size();

    }

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
                    if(calendars.size() > 0){
                        Core.getInstance().setSelectedCalendar(calendars.get(getLayoutPosition()));
                        Core.getInstance().alertDialog(Core.getInstance().getSelectedCalendar().getEventLog());
                    }
                }
            });
        }
    }
}
