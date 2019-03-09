package com.example.thetravlendar;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.thetravlendar.models.Events;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class ViewEventRecyclerActivity extends AppCompatActivity {

    private static final String TAG = "checking get event";
    private RecyclerView myEvents;
    private DatabaseReference EventsRef, UserRef;
    private FirebaseAuth mAuth;
    private String online_user_id;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_event);


        mAuth = FirebaseAuth.getInstance();
        online_user_id = mAuth.getCurrentUser().getUid();
        EventsRef = FirebaseDatabase.getInstance().getReference().child("events");
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");
        query = EventsRef;

        myEvents = findViewById(R.id.recview);
        //myEvents.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        myEvents.setLayoutManager(linearLayoutManager);
        myEvents.setHasFixedSize(true);

        //linearLayoutManager.setStackFromEnd(true);
        DisplayAllEvents();
    }

    private void DisplayAllEvents() {
        //query = FirebaseDatabase.getInstance().getReference().child("events").limitToLast(50);
        FirebaseRecyclerOptions<Events> options = new FirebaseRecyclerOptions.Builder<Events>()
                .setQuery(query, new SnapshotParser<Events>() {
                    @NonNull
                    @Override
                    public Events parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new Events(snapshot.child("uid").getValue().toString(),
                                snapshot.child("name").getValue().toString(),
                                snapshot.child("date").getValue().toString(),
                                snapshot.child("start_time").getValue().toString(),
                                snapshot.child("end_time").getValue().toString());
                    }
                })
                .build();
        firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Events, EventsViewHolder> (options)
                //(Events.class, EventsViewHolder.class, R.layout.cardview_event, EventsRef)
        {
            @Override
            public EventsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.cardview_event, viewGroup, false);
                return new EventsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull EventsViewHolder holder, final int position, @NonNull Events model) {
                holder.setName(model.getName());
                Log.e(TAG,"name = " + model.getName() + " yikes"  );
                holder.setDate(model.getDate());
                Log.e(TAG,"date = " + model.getDate() + " yikes"  );
                holder.setStart_Time(model.getStart_time());
                Log.e(TAG,"start = " + model.getStart_time() + " yikes"  );
                holder.setEnd_Time(model.getEnd_time());
                Log.e(TAG,"end = " + model.getEnd_time() + " yikes"  );
            }

            /*@Override
            protected void populateViewHolder(EventsViewHolder viewHolder, Events model, int position){
                viewHolder.seteName(model.geteName());
                Log.e(TAG,"name = " + model.geteName() + " yikes"  );
                viewHolder.seteDate(model.geteDate());
                viewHolder.seteStartTime(model.geteStartTime());
                viewHolder.seteEndTime(model.geteEndTime());
            }*/


        };
        myEvents.setAdapter(firebaseRecyclerAdapter);
    }


    public static class EventsViewHolder extends RecyclerView.ViewHolder {

        public TextView textEventName;
        public TextView textEventDate;
        public TextView textEventStart;
        public TextView textEventEnd;
        public EventsViewHolder(View itemView) {
            super(itemView);

            textEventName = itemView.findViewById(R.id.cv_event_name);
            textEventDate = itemView.findViewById(R.id.cv_event_date);
            textEventStart = itemView.findViewById(R.id.cv_event_start);
            textEventEnd = itemView.findViewById(R.id.cv_event_end);
        }

        public void setName(String eventName){

            //Log.e("Event " + eventName + " is unexp", TAG);
            textEventName.setText(eventName);
        }
        public void setDate(String eventDate) {

            textEventDate.setText(eventDate);
        }
        public void setStart_Time(String eventStart) {

            textEventStart.setText(eventStart);
        }
        public void setEnd_Time(String eventEnd) {

            textEventEnd.setText(eventEnd);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
}


    /*public static class EventsViewHolder extends RecyclerView.ViewHolder {

    }
}*/
