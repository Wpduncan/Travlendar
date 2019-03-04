package com.example.thetravlendar;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity implements TimePickerFragment.TimeDialogListener{

    private static final String TAG = "AddToDatabase";
    private static final String DIALOG_TIME = "AddEventActivity.TimeDialog";
    Button saveEvent;
    EditText mEventName;
    EditText mEventDate;
    EditText mEventLocation;
    EditText mEventStart;
    EditText mEventEnd;
    EditText mEventNote;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("testing", "addNote - oncreate");
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //final CalendarView datePicker = findViewById(R.id.datePicker);
        //final EditText noteEditText = findViewById(R.id.noteEditText);
        saveEvent = findViewById(R.id.addEventButton);
        mEventName = findViewById(R.id.event_name);
        mEventDate = findViewById(R.id.event_date);
        mEventLocation = findViewById(R.id.event_location);
        mEventStart = findViewById(R.id.event_start_time);
        mEventEnd = findViewById(R.id.event_end_time);
        mEventNote = findViewById(R.id.event_note);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef =  mFirebaseDatabase.getReference();

        /*mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if(user != null) {
                toastMessage("Successfully signed in with: " + user.getEmail());
            }
            else {
                toastMessage("Successfully signed out.");
            }
        };*/

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        /*
        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to add object to database.");
                String eventDate = mEventDate.getText().toString();
                String eventName = mEventName.getText().toString();
                String eventLocation = mEventLocation.getText().toString();
                String eventStart = mEventStart.getText().toString();
                String eventEnd = mEventEnd.getText().toString();
                String eventNote = mEventNote.getText().toString();

                if (!(eventDate.equals("")) && !(eventName.equals("")) &&
                    !(eventStart.equals(""))){
                    FirebaseUser user = mAuth.getCurrentUser();
                    DatabaseReference userRef = myRef.child("users");
                    String key = userRef.push().getKey();
                    myRef.child(key).push({
                            event_date: eventDate,

                    })
                }
            }

        });*/
/////////////////////////////////////////////////////////////////////////////////////////////////
        mEventStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment dialog = new TimePickerFragment();
                dialog.show(getSupportFragmentManager(),DIALOG_TIME);
            }
        });
        mEventEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment dialog = new TimePickerFragment();
                dialog.show(getSupportFragmentManager(),DIALOG_TIME);
            }
        });
    }
    
    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String hireDate = sdf.format(date);
        return hireDate;
    }

    @Override
    public void onFinishDialog(String time) {
        Toast.makeText(this, "Selected Time : "+ time, Toast.LENGTH_SHORT).show();
        mEventStart.setText(time);
    }
//////////////////////////////////////////////////////////////////////////////////////////////////
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


