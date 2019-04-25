package com.example.thetravlendar;

import android.app.SearchManager;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.thetravlendar.Utils.Utility;
import com.example.thetravlendar.models.Events;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UpdateEventActivity extends AppCompatActivity {//implements View.OnClickListener{

    public static final String EXTRA_EVENT_KEY = "event_key";
    public static final String TAG = "UpdateEventActivity";

    EditText editViewEventName;
    EditText editViewEventDate;
    EditText editViewEventStartTime;
    EditText editViewEventEndTime;
    EditText editViewEventAddress;
    EditText editViewEventCity;
    EditText editViewEventState;
    EditText editViewEventZipCode;
    EditText editViewEventMOD;
    EditText editViewEventNote;
    EditText editViewEventLocation;
    ImageView imageViewEventAddLocation;
    Button btnEditEvent;
    Button btnDeleteEvent;
    Button btnSaveEvent;
    Button btnAddNewEvent;
    private DocumentSnapshot documentSnapshot;
    private CollectionReference eventsRef;
    private Events events;
    private EventsAdapter adapter;
    private LinearLayout layout;
    private DocumentReference ViewEventReference;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ValueEventListener mEventListener;
    private String EventKey, currentUserID, databaseUserID, name, date, start, end, address, state,
            city, zip, mod, note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //EventKey = getIntent().getExtras().get("EventKey").toString();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        //Events event = new Events();
        //ViewEventReference = db.collection("users").document(currentUserID)
          //      .collection("events").document(EventKey);
        //ViewEventReference = FirebaseDatabase.getInstance().getReference().child("events").child(EventKey);
        editViewEventName = findViewById(R.id.view_event_name);
        editViewEventDate = findViewById(R.id.view_event_date);
        editViewEventStartTime = findViewById(R.id.view_event_start_time);
        editViewEventEndTime = findViewById(R.id.view_event_end_time);
        editViewEventAddress = findViewById(R.id.view_event_address);
        editViewEventCity = findViewById(R.id.view_event_city);
        editViewEventState = findViewById(R.id.view_event_state);
        editViewEventZipCode = findViewById(R.id.view_event_zip_code);
        editViewEventMOD = findViewById(R.id.view_event_mod);
        editViewEventNote = findViewById(R.id.view_event_note);
        btnEditEvent = findViewById(R.id.editEventButton);
        btnDeleteEvent = findViewById(R.id.deleteEventButton);
        btnSaveEvent = findViewById(R.id.saveEventButton);
        btnAddNewEvent = findViewById(R.id.addNewEventButton);
        layout = findViewById(R.id.act_view_event);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        Intent intent = getIntent();
        String path = getIntent().getExtras().get("path").toString();
        //eventsRef = path;
        //Events events = documentSnapshot.toObject(Events.class);
        HashMap eventMap = (HashMap<String,String>)intent.getSerializableExtra("map");
        editViewEventName.setText(eventMap.get("name").toString());
        editViewEventAddress.setText(eventMap.get("address").toString());
        //editViewEventLocation.setText(eventMap.get("location").toString());
        editViewEventCity.setText(eventMap.get("city").toString());
        editViewEventDate.setText(eventMap.get("date").toString());
        editViewEventEndTime.setText(eventMap.get("end_time").toString());
        editViewEventStartTime.setText(eventMap.get("start_time").toString());
        //editViewEventMOD.setText(eventMap.get("mod").toString());
        editViewEventNote.setText(eventMap.get("note").toString());
        editViewEventState.setText(eventMap.get("state").toString());
        editViewEventZipCode.setText(eventMap.get("zip").toString());

        //ViewEventReference.addS

       /*ViewEventReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    address = dataSnapshot.child("address").getValue().toString();
                    city = dataSnapshot.child("city").getValue().toString();
                    date = dataSnapshot.child("date").getValue().toString();
                    end = dataSnapshot.child("end_time").getValue().toString();
                    mod = dataSnapshot.child("mode_of_transportation").getValue().toString();
                    name = dataSnapshot.child("name").getValue().toString();
                    note = dataSnapshot.child("note").getValue().toString();
                    start = dataSnapshot.child("start_time").getValue().toString();
                    state = dataSnapshot.child("state").getValue().toString();
                    zip = dataSnapshot.child("zip").getValue().toString();
                    databaseUserID = dataSnapshot.child("uid").getValue().toString();

                    //if(currentUserID.equals(databaseUserID))

                    editViewEventName.setText(name);
                    editViewEventDate.setText(date);
                    editViewEventStartTime.setText(start);
                    editViewEventEndTime.setText(end);
                    editViewEventAddress.setText(address);
                    editViewEventCity.setText(city);
                    editViewEventState.setText(state);
                    editViewEventZipCode.setText(zip);
                    editViewEventMOD.setText(mod);
                    editViewEventNote.setText(note);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utility.hideKeyboard(v,getApplicationContext());
                return false;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CalendarActivity.class));
            }
        });

        btnAddNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateEventActivity.this,
                        AddEventActivity.class));
            }
        });

        btnSaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEvent();
            }

        });

        /*editViewEventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

        /*imageViewEventAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    private void updateEvent() {
        String eName = editViewEventName.getText().toString().trim();
        String eDate = editViewEventDate.getText().toString().trim();
        String eStartTime = editViewEventStartTime.getText().toString().trim();
        String eEndTime = editViewEventEndTime.getText().toString().trim();
        String eAddress = editViewEventAddress.getText().toString().trim();
        String eCity = editViewEventCity.getText().toString().trim();
        String eState = editViewEventState.getText().toString().trim();
        String eZip = editViewEventZipCode.getText().toString().trim();
    }

    @Override
    public void onStart(){
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }
    public static String getFormattedDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return simpleDateFormat.format(date);
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
}
