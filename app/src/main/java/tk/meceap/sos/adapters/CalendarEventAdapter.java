package tk.meceap.sos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tk.meceap.sos.R;
import tk.meceap.sos.constants.Core;
import tk.meceap.sos.models.Calendar;
import tk.meceap.sos.models.CalendarEventLog;

public class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventAdapter.ViewHolder> {
    private Context context;
    private List<CalendarEventLog> calendars;

    public CalendarEventAdapter(List<CalendarEventLog> calendars) {
        this.calendars = calendars;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View view = inflater.inflate(R.layout.card_event_calendar, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(calendars != null && calendars.size() > 0){
            CalendarEventLog oc = calendars.get(position);
            if(oc != null){
                holder.eventTime.setText(oc.getTime());
                holder.eventType.setText(oc.getEventStatus().getName());
            }
        }
    }

    @Override
    public int getItemCount() {
        return calendars.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTime, eventType;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTime = (TextView) itemView.findViewById(R.id.event_time);
            eventType = (TextView) itemView.findViewById(R.id.event_type);
        }
    }
}
