package com.example.thetravlendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.thetravlendar.models.Events;
import com.example.thetravlendar.models.RecviewEvents;

import java.util.ArrayList;
import java.util.List;


public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder> {
    private List<Events> eList = new ArrayList<>();
    private Context mCtx;

    public EventRecyclerAdapter(List<Events> list, Context mctx) {
        eList = list;
        mCtx = mctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recycler_view_event,null,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(eList.get(position).getName());
        holder.date.setText(eList.get(position).getDate());
        holder.startTime.setText(eList.get(position).getsTime());
        holder.endTime.setText(eList.get(position).geteTime());
    }

    @Override
    public int getItemCount() {
        return eList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, startTime, endTime;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cv_event_name);
            date = itemView.findViewById(R.id.cv_event_date);
            startTime = itemView.findViewById(R.id.cv_event_start);
            endTime = itemView.findViewById(R.id.cv_event_end);
        }

        public void setName(String eventName){

            //Log.e("Event " + eventName + " is unexp", TAG);
            name.setText(eventName);
        }
        public void setDate(String eventDate) {

            date.setText(eventDate);
        }
        public void setStart_Time(String eventStart) {

            startTime.setText(eventStart);
        }
        public void setEnd_Time(String eventEnd) {

            endTime.setText(eventEnd);
        }
    }
}
