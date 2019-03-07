package com.example.thetravlendar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.thetravlendar.models.Events;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class ViewEventRecyclerActivity extends AppCompatActivity {

    private RecyclerView myEvents;
    private DatabaseReference EventsRef, UserRef;
    private FirebaseAuth mAuth;
    private String online_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_event);

        mAuth = FirebaseAuth.getInstance();
        online_user_id = mAuth.getCurrentUser().getUid();
        EventsRef = FirebaseDatabase.getInstance().getReference().child("events");
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");

        myEvents = findViewById(R.id.recview);
        myEvents.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        myEvents.setLayoutManager(linearLayoutManager);
        //linearLayoutManager.setStackFromEnd(true);
        DisplayAllEvents();
    }

    private void DisplayAllEvents() {
        FirebaseRecyclerAdapter<Events, EventsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Events, EventsViewHolder>
                (
                        Events.class,
                        R.layout.cardview_event,
                        EventsViewHolder.class,
                        EventsRef
                )
        {

            @Override
            protected void populateViewHolder(EventsViewHolder viewHolder, Events model, int position){
                viewHolder.seteName(model.geteName());
                viewHolder.seteDate(model.geteDate());
                viewHolder.seteStartTime(model.geteStartTime());
                viewHolder.seteEndTime(model.geteEndTime());
            }


        };
        myEvents.setAdapter(firebaseRecyclerAdapter);
    }

    public static class EventsViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public EventsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void seteName(String eventName){
            TextView textEventName = mView.findViewById(R.id.cv_event_name);
            //Log.e("Event " + eventName + " is unexp", TAG);
            textEventName.setText(eventName);
        }
        public void seteDate(String eventDate) {
            TextView textEventDate = mView.findViewById(R.id.cv_date);
            textEventDate.setText(eventDate);
        }
        public void seteStartTime(String eventStart) {
            TextView textEventStart = mView.findViewById(R.id.cv_start_time);
            textEventStart.setText(eventStart);
        }
        public void seteEndTime(String eventEnd) {
            TextView textEventEnd = mView.findViewById(R.id.cv_end_time);
            textEventEnd.setText(eventEnd);
        }
    }
}


    /*public static class EventsViewHolder extends RecyclerView.ViewHolder {

    }
}*/
