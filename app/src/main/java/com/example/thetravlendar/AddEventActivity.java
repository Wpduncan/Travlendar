package com.example.thetravlendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.thetravlendar.models.Events;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.thetravlendar.Utils.Utility.hideKeyboard;
import static java.lang.Integer.parseInt;


public class  AddEventActivity extends AppCompatActivity implements
        DatePickerFragment.DateDialogListener, StartTimePickerFragment.TimeDialogListener,
        EndTimePickerFragment.TimeDialogListener, ModeOfTransportationFragment.MODDialogListener {

    private static final String TAG = "AddToDatabase";
    private static final String REQUIRED = "Required";
    private String Date;
    private String path;
    private static final String DIALOG_TIME = "AddEventActivity.TimeDialog";
    private static final String DIALOG_DATE = "AddEventActivity.DateDialog";
    private static final String DIALOG_MOD = "AddEventActivity.";
    private String ename;
    LinearLayout layout;
    Button buttonSaveEvent;
    TextInputEditText editEventName;
    TextInputEditText editEventDate;
    TextInputEditText editEventStart;
    TextInputEditText editEventEnd;
    TextInputEditText editEventAddress;
    TextInputEditText editEventCity;
    TextInputEditText editEventState;
    TextInputEditText editEventZipCode;
    TextInputEditText editEventMOD;
    TextInputEditText editEventNote;
    TextInputEditText editEventLocation;
    ImageView imageAddLocation;

    //private DatabaseReference mUserRef, mEventRef;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentSnapshot snapshot;
    private DocumentReference docRef;
    private HashMap<String, Object> eventMap = new HashMap<>();

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
        //docRef = db.collection("users").document(mAuth.getUid())
        //        .collection("events").document();
        mAuth = FirebaseAuth.getInstance();
        //mUserRef = FirebaseDatabase.getInstance().getReference();
        //mEventRef = FirebaseDatabase.getInstance().getReference().child("events");

        layout = (LinearLayout) findViewById(R.id.act_add_event);
        //accepts the date from the calendar activity and sets date text field
        Intent intent = getIntent();
        Date = intent.getExtras().getString("date");
        editEventDate.setText(Date);
        editEventMOD.setText("Driving");

        String actID = intent.getExtras().getString("actID");
        if(actID.equals("recycler")){
            updateEvent();
            System.out.println("recycler " + actID);
        }
        if (actID.equals("restartAddEvent")) {
            restartAddEvent();
        }

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
                Intent myIntent = new Intent(AddEventActivity.this,MapsActivity.class);
                myIntent.putExtra("MOD", editEventMOD.getText().toString());
                startActivityForResult(myIntent, 1);
            }
        });



        // For Maps Activity
        //Bundle extras = getIntent().getExtras();
        if (actID.equals("mapsActivity") || actID.equals("mapsView")) {
            /*MapsActivity maps = new MapsActivity();
            String travelTime = maps.getTravel();
            System.out.println("travel " + travelTime);*/
            String street = intent.getExtras().getString("address");
            String city = intent.getExtras().getString("city");
            String state = intent.getExtras().getString("state");
            String zip = intent.getExtras().getString("zip");
            String name = intent.getExtras().getString("name");
            /*eventMap.put("address", street);
            eventMap.put("city", city);
            eventMap.put("state", state);
            eventMap.put("zip", zip);
            eventMap.put("location", name);
            setTextFields(eventMap);*/
            //String travel = intent.getExtras().getString("time");
            //The key argument here must match that used in the other activity
            System.out.println("addMaps");
            editEventAddress.setText(street);
            editEventCity.setText(city);
            editEventState.setText(state);
            editEventZipCode.setText(zip);
            editEventLocation.setText(name);
            //editEventNote.setText(travel);

        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String street = data.getStringExtra("street");
                String city = data.getStringExtra("city");
                String state = data.getStringExtra("state");
                String zip = data.getStringExtra("zip");
                String name = data.getStringExtra("name");
                String travel = data.getStringExtra("time");
                //The key argument here must match that used in the other activity
                if (street != null){
                    editEventAddress.setText(street);}
                editEventCity.setText(city);
                editEventState.setText(state);
                editEventZipCode.setText(zip);
                editEventLocation.setText(name);
                editEventNote.setText(travel);
            }
        }
    }

    private void restartAddEvent() {
        eventMap = (HashMap<String, Object>) getIntent().getSerializableExtra("map");
        setTextFields(eventMap);
    }

    private void updateEvent(){
        path = getIntent().getExtras().get("path").toString();
        HashMap eventMap = (HashMap<String,Object>)getIntent().getSerializableExtra("map");
        setTextFields(eventMap);
    }

    private void setTextFields(Map<String, Object> eventMap){
        editEventName.setText(eventMap.get("name").toString());
        editEventDate.setText(eventMap.get("date").toString());
        editEventStart.setText(eventMap.get("start_time").toString());
        editEventEnd.setText(eventMap.get("end_time").toString());
        editEventAddress.setText(eventMap.get("address").toString());
        editEventLocation.setText(eventMap.get("location").toString());
        editEventCity.setText(eventMap.get("city").toString());
        editEventMOD.setText(eventMap.get("mod").toString());
        editEventNote.setText(eventMap.get("note").toString());
        editEventState.setText(eventMap.get("state").toString());
        editEventZipCode.setText(eventMap.get("zip").toString());
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
        if (TextUtils.isEmpty(location)){
            editEventLocation.setError(REQUIRED);
            return;
        }

        // start time
        int SHOUR = startTime.indexOf(":");
        int SMIN  = startTime.indexOf(" ");
        String sHour = startTime.substring(0, SHOUR);
        System.out.println("hour = " + sHour);
        String sMin = startTime.substring(SHOUR+1, SMIN);
        System.out.println("min = " + sMin);
        String sME = startTime.substring(SMIN+1);
        System.out.println("sME = " + sME);
        int sTime1 = parseInt(sHour+sMin);
        //int sTime;

        // end time
        int EHOUR = endTime.indexOf(":");
        int EMIN  = endTime.indexOf(" ");
        String eHour = endTime.substring(0, EHOUR);
        System.out.println("hour = " + eHour);
        String eMin = endTime.substring(EHOUR+1, EMIN);
        System.out.println("min = " + eMin);
        String eME = endTime.substring(EMIN+1);
        System.out.println("eME = " + eME);
        int eTime1 = parseInt(eHour+eMin);
        //int eTime;

        if(sME.equals("AM")) {
            if (sTime1 >= 1200 && sTime1 <= 1259) { // || eTimeH <= 1259){
                sHour = "00";
                sTime1 = parseInt(sHour + sMin);
                //System.out.println("stime = " + sTime1);
            }
        }
        if(eME.equals("AM")) {
            if (eTime1 >= 1200 && eTime1 <= 1259) {
                eHour = "00";
                eTime1 = parseInt(eHour + eMin);
                //System.out.println("etime = "+ eTime1);
            }
        }
        if(sME.equals("PM") && sTime1 < 1200) {
            sTime1 += 1200;
            //System.out.println("stime = " + sTime1);
        }
        if(eME.equals("PM") && eTime1 < 1200) {
            eTime1 += 1200;
            //System.out.println("etime = "+ eTime1);
        }
        final int sTime = sTime1;
        final int eTime = eTime1;
        System.out.println("sTime = " + sTime);
        System.out.println("eTime = " + eTime);
        if(sTime > eTime){
            Toast.makeText(this, "End Time error.", Toast.LENGTH_SHORT).show();
            editEventEnd.setError(REQUIRED);
            return;
        }
        if(sTime == eTime) {
            Toast.makeText(this, "Can not start and end at the same time.", Toast.LENGTH_SHORT).show();
            editEventStart.setError(REQUIRED);
            editEventEnd.setError(REQUIRED);
            return;
        }

        String user = mAuth.getCurrentUser().getUid();
        //Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("name", name);
        eventMap.put("search", name.toLowerCase());
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
        eventMap.put("military_start", sTime);
        eventMap.put("military_end", eTime);

        //DocumentReference userId = db.collection("users").document(user).collection("events").document();
        //System.out.println("userid" + userId);

        /*
        db.collection("users").document(user)
                .collection("events").whereEqualTo("date", date)
                .whereEqualTo("end_time", startTime)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddEventActivity.this, "Start time conflicts with another event", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
                */
        System.out.println("right before");

            System.out.println("conflict testing");
            db.collection("users").document(user)
                    .collection("events").whereEqualTo("date", Date).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            //boolean flag = false;
                            String conflict = "false";
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Events events = documentSnapshot.toObject(Events.class);
                                System.out.println("get stime = " + events.getMilitary_start());
                                System.out.println("get etime = " + events.getMilitary_end());
                                if (startTime.equals(events.getEnd_time())) {
                                    System.out.println("start time conflict");
                                    //SendUserToRecycler();
                                    conflictToast(events.getName());
                                    editEventStart.setError(REQUIRED);
                                    conflict = "true";
                                    return;
                                }
                                if (endTime.equals(events.getStart_time())) {
                                    System.out.println("end time conflict");
                                    //SendUserToRecycler();
                                    conflictToast(events.getName());
                                    conflict = "true";
                                    editEventEnd.setError(REQUIRED);
                                    return;
                                }
                                if (startTime.equals(events.getStart_time())) {
                                    System.out.println("start time == start time conflict");
                                    //SendUserToRecycler();
                                    if (path == null) {
                                        conflict = "true";
                                        conflictToast(events.getName());
                                        //Toast.makeText(AddEventActivity.this, "Start Time conflicts with " + events.getName(), Toast.LENGTH_SHORT).show();
                                        editEventStart.setError(REQUIRED);
                                        return;
                                    }
                                    else
                                        conflict = "false";
                                }
                                if (endTime.equals(events.getEnd_time())) {
                                    System.out.println("end time == end time conflict");
                                    //SendUserToRecycler();
                                    if (path == null) {
                                        conflict = "true";
                                        conflictToast(events.getName());
                                        editEventEnd.setError(REQUIRED);
                                        return;
                                    }
                                    else
                                        conflict = "false";
                                }
                                if(sTime >= (events.getMilitary_start()) && sTime <= (events.getMilitary_end())){
                                    System.out.println("nested event conflict");
                                    if (path == null) {
                                        conflict = "true";
                                        conflictToast(events.getName());
                                        editEventEnd.setError(REQUIRED);
                                        return;
                                    }
                                    else
                                        conflict = "false";
                                }
                            }
                            if (conflict.equals("false")) {
                                System.out.println("conflict flag == 0");
                                if (path == null) {
                                    db.collection("users").document(user)
                                            .collection("events").document()
                                            .set(eventMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            SendUserToCalendarActivity();
                                            Toast.makeText(AddEventActivity.this, "Successfully added event", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else {
                                    db.document(path).update(eventMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            SendUserToCalendarActivity();
                                            Toast.makeText(AddEventActivity.this, "Successfully updated event", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }
                            else if (conflict.equals("true")) {
                                System.out.println("conflict flag == 1");
                                Toast.makeText(AddEventActivity.this, "Conflicting events", Toast.LENGTH_SHORT).show();
                                restartAddEventActivity();
                            }
                        }
                    });

        }

    private void conflictToast(String name){
        Toast.makeText(AddEventActivity.this, "Conflict with " + name + " event.", Toast.LENGTH_SHORT).show();
    }

    private void SendUserToRecycler() {
        Intent intent = new Intent(AddEventActivity.this, ViewEventRecyclerActivity.class);
        intent.putExtra("date", Date);
        startActivity(intent);
        finish();
    }

    private void SendUserToCalendarActivity() {
        Intent intent = new Intent(AddEventActivity.this, CalendarActivity.class);
        startActivity(intent);
    }

    private void restartAddEventActivity() {
        Intent intent = new Intent(AddEventActivity.this, AddEventActivity.class);
        intent.putExtra("date", Date);
        intent.putExtra("map", eventMap);
        intent.putExtra("actID", "restartAddEvent");
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
        //Toast.makeText(this, "Selected Time : "+ time, Toast.LENGTH_SHORT).show();
        editEventStart.setText(time);
    }
    @Override
    public void onFinishEndDialog(String time) {
        //Toast.makeText(this, "Selected Time : "+ time, Toast.LENGTH_SHORT).show();
        editEventEnd.setText(time);
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
        /*docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

            }
        });*/
        //editEventName.setText(ename);
    }

    @Override
    public void onStop() {
        super.onStop();
        //ename = editEventName.getText().toString();

    }
}


