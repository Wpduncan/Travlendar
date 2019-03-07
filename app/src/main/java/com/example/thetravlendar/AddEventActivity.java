package com.example.thetravlendar;

import android.app.SearchManager;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.thetravlendar.models.Events;
import com.example.thetravlendar.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.thetravlendar.models.Utility.hideKeyboard;


public class AddEventActivity extends AppCompatActivity implements
        DatePickerFragment.DateDialogListener, StartTimePickerFragment.TimeDialogListener,
        EndTimePickerFragment.TimeDialogListener, ModeOfTransportationFragment.MODDialogListener {

    private static final String TAG = "AddToDatabase";
    private static final String REQUIRED = "Required";
    private static final String DIALOG_TIME = "AddEventActivity.TimeDialog";
    private static final String DIALOG_DATE = "AddEventActivity.DateDialog";
    private static final String DIALOG_MOD = "AddEventActivity.";
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

    private DatabaseReference mDatabase;

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

        mDatabase = FirebaseDatabase.getInstance().getReference();

        layout = (LinearLayout) findViewById(R.id.act_add_event);

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
    }

    private void submitEvent() {
        final String name = editEventName.getText().toString();
        final String date = editEventDate.getText().toString();
        final String startTime = editEventStart.getText().toString();
        final String endTime = editEventEnd.getText().toString();
        final String address = editEventAddress.getText().toString();
        final String city = editEventCity.getText().toString();
        final String state = editEventState.getText().toString();
        final String zip = editEventZipCode.getText().toString();
        final String mod = editEventMOD.getText().toString();
        final String note = editEventNote.getText().toString();

        if (TextUtils.isEmpty(name)){
            editEventName.setError(REQUIRED);
            return;
        }

        if(TextUtils.isEmpty(date)){
            editEventDate.setError(REQUIRED);
            return;
        }

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if (user == null) {
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(AddEventActivity.this,"Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            writeNewEvent(userId, name,date,startTime,endTime, address,
                                    city,state,zip,mod,note);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
        //startActivity(new Intent(AddEventActivity.this, CalendarActivity.class));
    }

    private void writeNewEvent(String userId, String name, String date, String startTime,
                               String endTime, String address, String city,
                               String state, String zip, String mod, String note) {
        String key = mDatabase.child("events").push().getKey();
        Events events = new Events(userId, name, date, startTime, endTime, address, city,
                state, zip, mod, note);
        Map<String, Object> eventValues = events.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
<<<<<<< HEAD
        childUpdates.put("/events/" + userId + "/", eventValues);
=======
        childUpdates.put("/events/" + key + "/", eventValues);
>>>>>>> master
        childUpdates.put("/users/" + userId + "/" + "events/" + key + "/", eventValues);

        mDatabase.updateChildren(childUpdates);
    }

    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
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




}


