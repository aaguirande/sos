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
        View view = inflater.inflate(R.layout.cardview_calendar, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(calendars != null && calendars.size() > 0){
            Calendar oc = calendars.get(position);
            if(oc != null){
                holder.calendarGroup.setText(oc.getGroupCalendar());
                holder.calendarTitle.setText(oc.getEventCalendar());
                holder.calendarBody.setText(oc.getDetailsCalendar());
                holder.calendarLocation.setText(oc.getEventLocation());
                holder.calendarDate.setText(oc.getEventDate());
                holder.calendarTime.setText(oc.getEventStartTime() + " to " + oc.getEventEndTime());
            }
        }
    }

    @Override
    public int getItemCount() {
        return calendars.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView calendarLog, calendarTime, calendarDate, calendarBody, calendarLocation, calendarGroup, calendarTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            calendarTime = (TextView) itemView.findViewById(R.id.calendar_time);
            calendarDate = (TextView) itemView.findViewById(R.id.calendar_date);
            calendarBody = (TextView) itemView.findViewById(R.id.calendar_body);
            calendarLocation = (TextView) itemView.findViewById(R.id.calendar_location);
            calendarGroup = (TextView) itemView.findViewById(R.id.calendar_group);
            calendarTitle = (TextView) itemView.findViewById(R.id.calendar_title);
            calendarLog = (TextView) itemView.findViewById(R.id.calendar_log);

            calendarLog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(calendars.size() > 0){
                        Core.getInstance().setSelectedCalendar(calendars.get(getLayoutPosition()));
                        Core.getInstance().eventCalendarLogs(Core.getInstance().getSelectedCalendar().getEventLog());
                    }
                }
            });
        }
    }
}
