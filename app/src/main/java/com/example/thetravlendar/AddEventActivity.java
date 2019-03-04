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

import com.applandeo.materialcalendarview.CalendarView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AddEventActivity extends AppCompatActivity implements
        DatePickerFragment.DateDialogListener, StartTimePickerFragment.TimeDialogListener,
        EndTimePickerFragment.TimeDialogListener, ModeOfTransportationFragment.MODDialogListener {

    private static final String TAG = "AddToDatabase";
    private static final String DIALOG_TIME = "AddEventActivity.TimeDialog";
    private static final String DIALOG_DATE = "AddEventActivity.DateDialog";
    private static final String DIALOG_MOD = "AddEventActivity.";
    Button button;
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

        button = findViewById(R.id.addEventButton);
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

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef =  mFirebaseDatabase.getReference();

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CalendarActivity.class));
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


