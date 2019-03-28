package com.example.thetravlendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.DialogInterface;
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
import android.widget.ImageView;
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
    EditText editViewEventLocation;
    ImageView imageViewEventAddLocation;
    Button btnEditEvent;
    Button btnDeleteEvent;
    Button btnSaveEvent;
    Button btnAddNewEvent;
    private LinearLayout layout;
    private DatabaseReference ViewEventReference;
    private FirebaseAuth mAuth;
    private ValueEventListener mEventListener;
    private String EventKey, currentUserID, databaseUserID, name, date, start, end, address, state,
            city, zip, mod, note;

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

        btnDeleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteCurrentEvent();
            }
        });

        btnSaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewEventReference.child("name").setValue(editViewEventName.getText().toString());
                ViewEventReference.child("date").setValue(editViewEventDate.getText().toString());
                ViewEventReference.child("start_time").setValue(editViewEventStartTime.getText().toString());
                ViewEventReference.child("end_time").setValue(editViewEventEndTime.getText().toString());
                ViewEventReference.child("address").setValue(editViewEventAddress.getText().toString());
                ViewEventReference.child("state").setValue(editViewEventState.getText().toString());
                ViewEventReference.child("city").setValue(editViewEventCity.getText().toString());
                ViewEventReference.child("mode_of_transportation").setValue(editViewEventMOD.getText().toString());
                ViewEventReference.child("note").setValue(editViewEventNote.getText().toString());
                ViewEventReference.child("zip").setValue(editViewEventZipCode.getText().toString());

                Toast.makeText(ViewEventActivity.this, "Event Updated Successfully", Toast.LENGTH_SHORT).show();
                /*AlertDialog.Builder builder = new AlertDialog.Builder(ViewEventActivity.this);
                builder.setTitle("Edit Event:");

                final EditText inputName = new EditText(ViewEventActivity.this);
                inputName.setText(name);

                final EditText inputDate = new EditText(ViewEventActivity.this);
                inputDate.setText(date);

                final EditText inputStart = new EditText(ViewEventActivity.this);
                inputStart.setText(start);

                final EditText inputEnd = new EditText(ViewEventActivity.this);
                inputEnd.setText(end);

                final EditText inputAddress = new EditText(ViewEventActivity.this);
                inputAddress.setText(address);

                final EditText inputState = new EditText(ViewEventActivity.this);
                inputState.setText(state);

                final EditText inputZip = new EditText(ViewEventActivity.this);
                inputZip.setText(zip);

                final EditText inputMod = new EditText(ViewEventActivity.this);
                inputMod.setText(mod);

                final EditText inputNote = new EditText(ViewEventActivity.this);
                inputNote.setText(note);

                final EditText inputCity = new EditText(ViewEventActivity.this);
                inputCity.setText(city);

                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ViewEventReference.child("name").setValue(inputName.getText().toString());
                        ViewEventReference.child("date").setValue(inputDate.getText().toString());
                        ViewEventReference.child("start_time").setValue(inputStart.getText().toString());
                        ViewEventReference.child("end_time").setValue(inputEnd.getText().toString());
                        ViewEventReference.child("address").setValue(inputAddress.getText().toString());
                        ViewEventReference.child("state").setValue(inputState.getText().toString());
                        ViewEventReference.child("city").setValue(inputCity.getText().toString());
                        ViewEventReference.child("mode_of_transportation").setValue(inputMod.getText().toString());
                        ViewEventReference.child("note").setValue(inputNote.getText().toString());
                        ViewEventReference.child("zip").setValue(inputZip.getText().toString());

                        Toast.makeText(ViewEventActivity.this, "Event Updated Successfully", Toast.LENGTH_SHORT).show();

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                Dialog dialog = builder.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_dark);*/
            }

        });

        editViewEventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageViewEventAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void DeleteCurrentEvent() {
        ViewEventReference.removeValue();
        Intent intent = new Intent(ViewEventActivity.this, ViewEventRecyclerActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Event has been Deleted.", Toast.LENGTH_SHORT).show();
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
