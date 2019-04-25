package com.example.thetravlendar;

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.thetravlendar.Utils.Utility.hideKeyboard;


public class  AddEventActivity extends AppCompatActivity implements
        DatePickerFragment.DateDialogListener, StartTimePickerFragment.TimeDialogListener,
        EndTimePickerFragment.TimeDialogListener, ModeOfTransportationFragment.MODDialogListener {

    private static final String TAG = "AddToDatabase";
    private static final String REQUIRED = "Required";
    private String Date;
    private static final String DIALOG_TIME = "AddEventActivity.TimeDialog";
    private static final String DIALOG_DATE = "AddEventActivity.DateDialog";
    private static final String DIALOG_MOD = "AddEventActivity.";
    private String ename;
    LinearLayout layout;
    Button buttonSaveEvent;
    EditText editEventName;
    EditText editEventDate;
    EditText editEventStart;
    EditText editEventEnd;
    EditText editEventAddress;
    EditText editEventCity;
    EditText editEventState;
    EditText editEventZipCode;
    EditText editEventMOD;
    EditText editEventNote;
    EditText editEventLocation;
    ImageView imageAddLocation;

    private DatabaseReference mUserRef, mEventRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("testing", "addNote - oncreate");
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        buttonSaveEvent = findViewById(R.id.addEventButton);
        editEventName = findViewById(R.id.event_name);
        editEventDate = findViewById(R.id.event_date);
        editEventStart = findViewById(R.id.event_start_time);
        editEventEnd = findViewById(R.id.event_end_time);
        editEventAddress = findViewById(R.id.event_address);
        editEventCity = findViewById(R.id.event_city);
        editEventState = findViewById(R.id.event_state);
        editEventZipCode = findViewById(R.id.event_zip_code);
        editEventMOD = findViewById(R.id.event_mod);
        editEventNote = findViewById(R.id.event_note);
        editEventLocation = findViewById(R.id.event_location);
        imageAddLocation = findViewById(R.id.event_add_location);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUserRef = FirebaseDatabase.getInstance().getReference();
        mEventRef = FirebaseDatabase.getInstance().getReference().child("events");

        layout = (LinearLayout) findViewById(R.id.act_add_event);
        //accepts the date from the calendar activity and sets date text field
        Intent intent = getIntent();
        Date = intent.getExtras().getString("sendingDate");
        editEventDate.setText(Date);



        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CalendarActivity.class));
            }
        });

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(v,getApplicationContext());
                return false;
            }
        });
        editEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.show(getSupportFragmentManager(), DIALOG_DATE);
            }
        });

        editEventStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTimePickerFragment dialog = new StartTimePickerFragment();
                dialog.show(getSupportFragmentManager(),DIALOG_TIME);
            }
        });

        editEventEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndTimePickerFragment dialog = new EndTimePickerFragment();
                dialog.show(getSupportFragmentManager(),DIALOG_TIME);
            }
        });

        editEventMOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModeOfTransportationFragment dialog = new ModeOfTransportationFragment();
                dialog.show(getSupportFragmentManager(), DIALOG_MOD);
            }
        });

        buttonSaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitEvent();
            }
        });

        editEventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*displayToast(getString(R.string.Test1));
                Intent myIntent = new Intent(AddEventActivity.this,MapsActivity.class);
                startActivity(myIntent);*/
            }
        });


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
            editEventAddress.setText(street);
            editEventCity.setText(city);
            editEventState.setText(state);
            editEventZipCode.setText(zip);
            editEventLocation.setText(name);
            editEventNote.setText(travel);

        }

    }
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

    private void submitEvent() {
        final String name = editEventName.getText().toString();
        final String date = editEventDate.getText().toString();
        System.out.println("Date = " + date);
        final String startTime = editEventStart.getText().toString();
        final String endTime = editEventEnd.getText().toString();
        final String address = editEventAddress.getText().toString();
        final String city = editEventCity.getText().toString();
        final String state = editEventState.getText().toString();
        final String zip = editEventZipCode.getText().toString();
        final String mod = editEventMOD.getText().toString();
        final String location = editEventLocation.getText().toString();
        final String note = editEventNote.getText().toString();
        //final String location = editEventLocation.getText().toString();

        //Query query = mEventRef.orderByChild("start_time").

        /*Calendar calfordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
        String saveCurrentDate = currentDate.format(calfordDate.getTime());

        //Calendar calfordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String saveCurrentTime = currentTime.format(calfordDate.getTime());

        //String eventRandom = saveCurrentDate + saveCurrentTime;*/

        if (TextUtils.isEmpty(name)) {
            editEventName.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(date)) {
            editEventDate.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(startTime)) {
            editEventStart.setError(REQUIRED);
            return;
        }

        if (TextUtils.isEmpty(endTime)) {
            editEventEnd.setError(REQUIRED);
            return;
        }

        String user = mAuth.getCurrentUser().getUid();
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("name", name);
        eventMap.put("date", date);
        eventMap.put("start_time", startTime);
        eventMap.put("end_time", endTime);
        eventMap.put("address", address);
        eventMap.put("city", city);
        eventMap.put("state", state);
        eventMap.put("zip", zip);
        eventMap.put("mod", mod);
        eventMap.put("location", location);
        eventMap.put("note", note);
        //DocumentReference userId = db.collection("users").document(user).collection("events").document();
        //System.out.println("userid" + userId);
        db.collection("users").document(user)
                .collection("events").document()
                .set(eventMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                SendUserToCalendarActivity();
                Toast.makeText(AddEventActivity.this, "Successfully added event", Toast.LENGTH_SHORT).show();
            }
        });

        /*mUserRef.child("users").child(userId).addValueEventListener(new ValueEventListener() {
=======
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUserRef.child("users").child(userId)
                .addValueEventListener(new ValueEventListener() {
>>>>>>> master
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DatabaseReference pushKey = mEventRef.push();
                    String key = pushKey.getKey();
                    HashMap<String, Object> eventMap = new HashMap<>();
                    eventMap.put("uid", userId);
                    eventMap.put("name", name);
                    eventMap.put("date", date);
                    eventMap.put("startTime", startTime);
                    eventMap.put("endTime", endTime);
                    eventMap.put("address", address);
                    eventMap.put("city", city);
                    eventMap.put("state", state);
                    eventMap.put("zip", zip);
                    eventMap.put("location", location);
                    eventMap.put("mod", mod);
                    eventMap.put("note", note);
                    eventMap.put("uid_name", userId + "_" + name);
                    eventMap.put("uid_date", userId + "_" + date);
                    eventMap.put("uid_startTime", userId + "_" + startTime);
                    eventMap.put("uid_endTime", userId + "_" + endTime);
                    eventMap.put("uid_address", userId + "_" + address);
                    eventMap.put("uid_city", userId + "_" + city);
                    eventMap.put("uid_state", userId + "_" + state);
                    eventMap.put("uid_zip", userId + "_" + zip);
                    eventMap.put("uid_location", userId + "_" + location);
                    eventMap.put("uid_mod", userId + "_" + mod);
                    eventMap.put("uid_note", userId + "_" + note);

                    //eventMap.put("startTime", startTime);
                    eventMap.put("uid_date_startTime", userId + "_" + date + "_" + startTime);
                    eventMap.put("uid_date_endTime", userId + "_" + date + "_" + endTime);

                    //HashMap<String, Object> childUpdates = new HashMap<>();
                    //childUpdates.put("/users/" + userId + "/" + key + "/", eventMap);

                    //mUserRef.updateChildren(childUpdates);
                    mEventRef.child(key).updateChildren(eventMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                SendUserToCalendarActivity();
                                Toast.makeText(AddEventActivity.this, "new event updated.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddEventActivity.this, "error occurred updating event", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                //startActivity(new Intent(AddEventActivity.this, CalendarActivity.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    private void SendUserToCalendarActivity() {
        Intent intent = new Intent(AddEventActivity.this, CalendarActivity.class);
        startActivity(intent);
    }

    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
        String hireDate = sdf.format(date);
        return hireDate;
    }
    @Override
    public void onFinishMODDialog(String mod){
        editEventMOD.setText(mod);
    }
    @Override
    public void onFinishDateDialog(Date date){
        editEventDate.setText(formatDate(date));
    }
    @Override
    public void onFinishStartDialog(String time) {
        Toast.makeText(this, "Selected Time : "+ time, Toast.LENGTH_SHORT).show();
        editEventStart.setText(time);
    }
    @Override
    public void onFinishEndDialog(String time) {
        Toast.makeText(this, "Selected Time : "+ time, Toast.LENGTH_SHORT).show();
        editEventEnd.setText(time);
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
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    //@Override
    //protected void onStart() {
    //    super.onStart();
        
    //}
   /*@Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        //savedInstanceState.putInt(STATE_SCORE, currentScore);
        //savedInstanceState.putInt(STATE_LEVEL, currentLevel);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("EventName", editEventName.getText().toString());
    }


    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        //currentScore = savedInstanceState.getInt(STATE_SCORE);
        //currentLevel = savedInstanceState.getInt(STATE_LEVEL);
        editEventName.setText(savedInstanceState.getString("EventName"));
    }*/

    @Override
    public void onPause() {
        super.onPause();

    }
    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void onStart(){
        super.onStart();
        //editEventName.setText(ename);
    }

    @Override
    public void onStop() {
        super.onStop();
        //ename = editEventName.getText().toString();

    }
}


