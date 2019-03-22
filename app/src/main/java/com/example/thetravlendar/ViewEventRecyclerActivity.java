package com.example.thetravlendar;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


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
    private FloatingActionButton fab;
    private DatabaseReference EventsRef, UserRef;
    private FirebaseAuth mAuth;
    private String online_user_id;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_event);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        online_user_id = mAuth.getCurrentUser().getUid();
        EventsRef = FirebaseDatabase.getInstance().getReference().child("events");
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");
        query = EventsRef.orderByChild("uid").equalTo(online_user_id);

        myEvents = findViewById(R.id.recview);
        fab = findViewById(R.id.fab);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        myEvents.setLayoutManager(linearLayoutManager);
        myEvents.setHasFixedSize(true);



        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CalendarActivity.class));
            }
        });

        myEvents.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddEventActivity.class));
            }
        });

        DisplayAllEvents();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_calendar, menu);
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_account_settings:
                Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(this, AccountSettingsActivity.class);
                startActivity(intent);
                break;
            // action with ID action_settings was selected
            case R.id.action_search:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }

        return true;
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
                final String EventKey = getRef(position).getKey();
                if (model.uid.equals(online_user_id)) {
                    holder.setName(model.getName());
                    Log.e(TAG, "name = " + model.getName() + " yikes");
                    holder.setDate(model.getDate());
                    Log.e(TAG, "date = " + model.getDate() + " yikes");
                    holder.setStart_Time(model.getStart_time());
                    Log.e(TAG, "start = " + model.getStart_time() + " yikes");
                    holder.setEnd_Time(model.getEnd_time());
                    Log.e(TAG, "end = " + model.getEnd_time() + " yikes");
                }

                EventsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent clickEvent = new Intent(ViewEventRecyclerActivity.this, ViewEventActivity.class);
                        clickEvent.putExtra("EventKey", EventKey);
                        startActivity(clickEvent);
                    }
                });
            }
        };
        myEvents.setAdapter(firebaseRecyclerAdapter);
    }


    public static class EventsViewHolder extends RecyclerView.ViewHolder {

        //public static Object mView;
        public TextView textEventName;
        public TextView textEventDate;
        public TextView textEventStart;
        public TextView textEventEnd;
        static View mView;

        public EventsViewHolder(View itemView) {
            super(itemView);
            mView= itemView;
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
