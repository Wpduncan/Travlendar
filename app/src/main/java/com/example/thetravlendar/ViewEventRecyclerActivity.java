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
import android.support.v7.widget.helper.ItemTouchHelper;
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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ViewEventRecyclerActivity extends AppCompatActivity {


    //private OnItemClickListener listener;
    private static final String TAG = "checking get event";
    private String online_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference eventsRef = db.collection("users").document(online_user_id)
            .collection("events");
    private FloatingActionButton fab;
    private FirebaseAuth mAuth;
    private EventsAdapter adapter;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private String Date;
    private HashMap<String, Object> eventMap = new HashMap<>();
    private String actID;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_event);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        //db = FirebaseFirestore.getInstance();
        online_user_id = mAuth.getCurrentUser().getUid();
                //adapter = new
        //EventsRef = FirebaseDatabase.getInstance().getReference().child("events");
        //UserRef = FirebaseDatabase.getInstance().getReference().child("users");
        //query = EventsRef.orderByChild("uid").equalTo(online_user_id);
        //String eventDocId = db.collection("users").document(online_user_id)
                //.collection("events").document().getId();
        //Log.d(TAG, "event doc id"+ eventDocId);
        //System.out.println("event doc id = " + eventDocId);


        //myEvents = findViewById(R.id.recview);
        fab = findViewById(R.id.fab);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //myEvents.setLayoutManager(linearLayoutManager);
        //myEvents.setHasFixedSize(true);



        //myEvents.setAdapter(adapter);

        //receiving date from calendarview
        Intent intent = getIntent();
        Date = intent.getExtras().getString("date");
        actID = intent.getExtras().getString("actID");
        System.out.println("actID " + actID);
        //if (actID.equals("search")){
        //    act = 1;
        //}

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CalendarActivity.class));
            }
        });

        /*myEvents.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });*/

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewEventRecyclerActivity.this, AddEventActivity.class);
                intent.putExtra("date", Date);
                intent.putExtra("actID", "calendar");
                startActivity(intent);
            }
        });

        DisplayAllEvents();

    }

    private void searchForEvent() {

    }

    private void DisplayAllEvents() {
        if (actID.equals("search")) {
            Intent intent = getIntent();
            String search = intent.getExtras().getString("searchID");
            query = eventsRef.whereEqualTo("search", search);
        }
        else {
            query = eventsRef.whereEqualTo("date", Date).orderBy("start_time",
                    Query.Direction.ASCENDING);
        }

        FirestoreRecyclerOptions<Events> options = new FirestoreRecyclerOptions.Builder<Events>()
                .setQuery(query, Events.class)
                .build();

        adapter = new EventsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                adapter.deleteEvent(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnEventClickListener(new EventsAdapter.OnEventClickListener() {
            @Override
            public void onEventclick(DocumentSnapshot documentSnapshot, int position) {
                Events events = documentSnapshot.toObject(Events.class);
                System.out.println("addr" + events.getAddress());
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                HashMap<String, Object> eventMap = new HashMap<>();
                eventMap.put("name", events.getName());
                eventMap.put("date", events.getDate());
                eventMap.put("start_time", events.getStart_time());
                eventMap.put("end_time", events.getEnd_time());
                eventMap.put("address", events.getAddress());
                eventMap.put("city", events.getCity());
                eventMap.put("state", events.getState());
                eventMap.put("zip", events.getZip());
                eventMap.put("mod", events.getMod());
                eventMap.put("location", events.getLocation());
                eventMap.put("note", events.getNote());
                System.out.println("address " + eventMap.get("address"));
                System.out.println("mod " + eventMap.get("mod"));
                Intent intent = new Intent(ViewEventRecyclerActivity.this, AddEventActivity.class);
                intent.putExtra("map", eventMap);
                intent.putExtra("path", path);
                intent.putExtra("date", Date);
                intent.putExtra("actID", "recycler");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        /*final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        */
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
            /*case R.id.action_search:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            */
            default:
                break;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
