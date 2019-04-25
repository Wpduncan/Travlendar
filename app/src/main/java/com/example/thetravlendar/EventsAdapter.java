package com.example.thetravlendar;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thetravlendar.models.Events;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

//RecyclerView.Adapter<EventsAdapter.EventViewHolder> {
public class EventsAdapter extends FirestoreRecyclerAdapter<Events, EventsAdapter.EventHolder> {
    private OnEventClickListener listener;

    public EventsAdapter(@NonNull FirestoreRecyclerOptions<Events> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EventHolder holder, int position, @NonNull Events model) {
        holder.textViewName.setText(model.getName());
        holder.textViewDate.setText(model.getDate());
        holder.textViewStart.setText(model.getStart_time());
        holder.textViewEnd.setText(model.getEnd_time());
    }

    @NonNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_event,
                parent,false);
        return new EventHolder((v));
    }

    public void deleteEvent(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class EventHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewDate, textViewStart, textViewEnd;

        public EventHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.cv_event_name);
            textViewDate = itemView.findViewById(R.id.cv_event_date);
            textViewStart = itemView.findViewById(R.id.cv_event_start);
            textViewEnd = itemView.findViewById(R.id.cv_event_end);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onEventclick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnEventClickListener {
        void onEventclick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnEventClickListener(OnEventClickListener listener) {
        this.listener = listener;
    }
}

    /*private Context mCtx;
    private List<Events> eventList;
    private String date;

    public EventsAdapter(Context mCtx, List<Events> eventList, String date) {
        this.mCtx = mCtx;
        this.eventList = eventList;
        this.date = date;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(
                LayoutInflater.from(mCtx).inflate(R.layout.recycler_view_event, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Events events = eventList.get(position);

    }

    @Override
    public int getItemCount(){
        return eventList.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewName, textViewDate, textViewStart, textViewEnd;

        public EventViewHolder (View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.cv_event_name);
            textViewDate = itemView.findViewById(R.id.cv_event_date);
            textViewStart = itemView.findViewById(R.id.cv_event_start);
            textViewEnd = itemView.findViewById(R.id.cv_event_end);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Events events = eventList.get(getAdapterPosition());
            Intent intent = new Intent(mCtx, UpdateEventActivity.class);
            intent.putExtra("events", events);
            mCtx.startActivity(intent);
        }
    }

}*/
