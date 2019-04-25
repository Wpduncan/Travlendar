package com.example.thetravlendar;

import android.app.SearchManager;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UpdateEventActivity extends AppCompatActivity implements
        DatePickerFragment.DateDialogListener, StartTimePickerFragment.TimeDialogListener,
        EndTimePickerFragment.TimeDialogListener, ModeOfTransportationFragment.MODDialogListener {

    private static final String DIALOG_TIME = "ViewEventActvity.TimeDialog";
    private static final String DIALOG_DATE = "ViewEventActivity.DateDialog";
    private static final String DIALOG_MOD = "ViewEventActivity.MOD";
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
    Button btnCancelEvent;
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
            city, zip, mod, note, Date;

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
        editViewEventLocation = findViewById(R.id.view_event_location);
        imageViewEventAddLocation = findViewById(R.id.view_event_add_location);
        btnSaveEvent = findViewById(R.id.saveEventButton);
        btnDeleteEvent = findViewById(R.id.deleteEventButton);
        btnCancelEvent = findViewById(R.id.cancelEventButton);
        //btnAddNewEvent = findViewById(R.id.addNewEventButton);
        layout = findViewById(R.id.act_view_event);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        Date = formatDate(Calendar.getInstance().getTime().toString());

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
                if (dataSnapshot.exists()) {
                    address = dataSnapshot.child("address").getValue().toString();
                    city = dataSnapshot.child("city").getValue().toString();
                    date = dataSnapshot.child("date").getValue().toString();
                    end = dataSnapshot.child("endTime").getValue().toString();
                    mod = dataSnapshot.child("mod").getValue().toString();
                    name = dataSnapshot.child("name").getValue().toString();
                    note = dataSnapshot.child("note").getValue().toString();
                    start = dataSnapshot.child("startTime").getValue().toString();
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
                Utility.hideKeyboard(v, getApplicationContext());
                return false;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
            }
        });

        editViewEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.show(getSupportFragmentManager(), DIALOG_DATE);
            }
        });

        editViewEventStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTimePickerFragment dialog = new StartTimePickerFragment();
                dialog.show(getSupportFragmentManager(),DIALOG_TIME);
            }
        });

        editViewEventEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndTimePickerFragment dialog = new EndTimePickerFragment();
                dialog.show(getSupportFragmentManager(),DIALOG_TIME);
            }
        });

        editViewEventMOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModeOfTransportationFragment dialog = new ModeOfTransportationFragment();
                dialog.show(getSupportFragmentManager(), DIALOG_MOD);
            }
        });

        /*btnAddNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateEventActivity.this,
                        AddEventActivity.class));
            }
        });*/

        btnSaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*updateEvent();

                ViewEventReference.child("name").setValue(editViewEventName.getText().toString());
                ViewEventReference.child("date").setValue(editViewEventDate.getText().toString());
                ViewEventReference.child("startTime").setValue(editViewEventStartTime.getText().toString());
                ViewEventReference.child("endTime").setValue(editViewEventEndTime.getText().toString());
                ViewEventReference.child("address").setValue(editViewEventAddress.getText().toString());
                ViewEventReference.child("state").setValue(editViewEventState.getText().toString());
                ViewEventReference.child("city").setValue(editViewEventCity.getText().toString());
                ViewEventReference.child("mod").setValue(editViewEventMOD.getText().toString());
                ViewEventReference.child("note").setValue(editViewEventNote.getText().toString());
                ViewEventReference.child("zip").setValue(editViewEventZipCode.getText().toString());
                ViewEventReference.child("location").setValue(editViewEventLocation.getText().toString());

                Toast.makeText(ViewEventActivity.this, "Event Updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ViewEventActivity.this, ViewEventRecyclerActivity.class);
                intent.putExtra("sendingDate", Date);
                startActivity(intent);
>>>>>>> master:app/src/main/java/com/example/thetravlendar/ViewEventActivity.java*/
            }
        });

        /*btnCancelEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewEventActivity.this, ViewEventRecyclerActivity.class);
                intent.putExtra("sendingDate", Date);
                startActivity(intent);
                Toast.makeText(ViewEventActivity.this, "Cacnelled editing the event", Toast.LENGTH_SHORT).show();
            }
        });*/

        editViewEventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageViewEventAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(UpdateEventActivity.this,MapsActivity.class);
                startActivity(myIntent);
            }
        });
    }

    /*private void updateEvent() {
        String eName = editViewEventName.getText().toString().trim();
        String eDate = editViewEventDate.getText().toString().trim();
        String eStartTime = editViewEventStartTime.getText().toString().trim();
        String eEndTime = editViewEventEndTime.getText().toString().trim();
        String eAddress = editViewEventAddress.getText().toString().trim();
        String eCity = editViewEventCity.getText().toString().trim();
        String eState = editViewEventState.getText().toString().trim();
        String eZip = editViewEventZipCode.getText().toString().trim();
=======
        });*/

        // For Maps Activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String street = extras.getString("street");
            String city = extras.getString("city");
            String state = extras.getString("state");
            String zip = extras.getString("zip");
            String name = extras.getString("name");
            String travel = extras.getString("time");
            //The key argument here must match that used in the other activity
            editViewEventAddress.setText(street);
            editViewEventCity.setText(city);
            editViewEventState.setText(state);
            editViewEventZipCode.setText(zip);
            editViewEventLocation.setText(name);
            editViewEventNote.setText(travel);
        }


    /*private void DeleteCurrentEvent() {
        ViewEventReference.removeValue();
        Intent intent = new Intent(ViewEventActivity.this, ViewEventRecyclerActivity.class);
        intent.putExtra("sendingDate", Date);
        startActivity(intent);
        Toast.makeText(this, "Event has been Deleted.", Toast.LENGTH_SHORT).show();
>>>>>>> master:app/src/main/java/com/example/thetravlendar/ViewEventActivity.java
    }*/

    @Override
    public void onFinishMODDialog(String mod){
        editViewEventMOD.setText(mod);
    }
    @Override
    public void onFinishDateDialog(Date date){
        editViewEventDate.setText(formatDate(date));
    }
    @Override
    public void onFinishStartDialog(String time) {
        Toast.makeText(this, "Selected Time : "+ time, Toast.LENGTH_SHORT).show();
        editViewEventStartTime.setText(time);
    }
    @Override
    public void onFinishEndDialog(String time) {
        Toast.makeText(this, "Selected Time : "+ time, Toast.LENGTH_SHORT).show();
        editViewEventEndTime.setText(time);
    }

    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
        String hireDate = sdf.format(date);
        return hireDate;
    }

    @Override
    public void onStart(){
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }
    public String formatDate(String sdate) {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy");
        String newDate = sdate.substring(4);
        String date1 = newDate.substring(0,6);
        String date2 = newDate.substring(20,24);
        String date3 = date1 + " " + date2;
        Log.d("testing", newDate);
        Log.d("testing", date1);
        Log.d("testing", date2);
        Log.d("testing", date3);
        return date3;
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
