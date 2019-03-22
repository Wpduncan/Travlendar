package com.example.thetravlendar;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.thetravlendar.Utils.Utility;
import com.example.thetravlendar.models.Events;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewEventActivity extends AppCompatActivity {//implements View.OnClickListener{

    public static final String EXTRA_EVENT_KEY = "event_key";
    public static final String TAG = "ViewEventActivity";

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
    Button btnEditEvent;
    Button btnDeleteEvent;
    Button btnSaveEvent;
    Button btnAddNewEvent;
    private LinearLayout layout;
    private DatabaseReference ViewEventReference;
    private FirebaseAuth mAuth;
    private ValueEventListener mEventListener;
    private String EventKey, currentUserID, databaseUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EventKey = getIntent().getExtras().get("EventKey").toString();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        ViewEventReference = FirebaseDatabase.getInstance().getReference().child("events").child(EventKey);
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

        ViewEventReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String address = dataSnapshot.child("address").getValue().toString();
                String city = dataSnapshot.child("city").getValue().toString();
                String date = dataSnapshot.child("date").getValue().toString();
                String end_time = dataSnapshot.child("end_time").getValue().toString();
                String mode_of_transportation = dataSnapshot.child("mode_of_transportation").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String note = dataSnapshot.child("note").getValue().toString();
                String start_time = dataSnapshot.child("start_time").getValue().toString();
                String state = dataSnapshot.child("state").getValue().toString();
                String zip = dataSnapshot.child("zip").getValue().toString();
                databaseUserID = dataSnapshot.child("uid").getValue().toString();

                if(currentUserID.equals(databaseUserID))

                editViewEventName.setText(name);
                editViewEventDate.setText(date);
                editViewEventStartTime.setText(start_time);
                editViewEventEndTime.setText(end_time);
                editViewEventAddress.setText(address);
                editViewEventCity.setText(city);
                editViewEventState.setText(state);
                editViewEventZipCode.setText(zip);
                editViewEventMOD.setText(mode_of_transportation);
                editViewEventNote.setText(note);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                startActivity(new Intent(ViewEventActivity.this,
                        AddEventActivity.class));
            }
        });


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
