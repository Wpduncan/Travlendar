package com.example.thetravlendar;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorSpace;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.bumptech.glide.load.model.Model;
import com.example.thetravlendar.models.Events;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity {
    public static final String RESULT = "result";
    public static final String EVENT = "event";
    private static final int ADD_NOTE = 44;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String Date;
    private boolean longClick = false;
    private CalendarView calendarView;

    private List<EventDay> eventDays = new ArrayList<>();
    List<EventDay> events = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        calendarView = findViewById(R.id.calendarView);

        calendarView.showCurrentMonthPage();
        Date = formatDate(Calendar.getInstance().getTime().toString());
        Log.d("testing", Date);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("testing", "onClick");
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                addNote();
            }
        });

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                String date = eventDay.getCalendar().getTime().toString();

                Log.d("testing", date);
                Date = formatDate(date);
                Log.d("testing", Date);
                //Toast.makeText(CalendarActivity.this.getApplicationContext(),
                //        eventDay.getCalendar().toString() + " "
                //               + eventDay.isEnabled(),
                //        Toast.LENGTH_SHORT).show();
                //Toast.makeText(CalendarActivity.this.getApplicationContext(),
                //        date,Toast.LENGTH_SHORT);
                /*Calendar clickedDayCalendar = eventDay.getCalendar();
                //Date date =
                //Calendar selectedDate = calendarView.getFirstSelectedDate();
                Log.d("testing", "onDayClick");
                System.out.println(clickedDayCalendar);
                //System.out.println(selectedDate);
                */
                //if () {}
                previewNote(eventDay);

            }

        });

        //calendarView.;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_NOTE && resultCode == RESULT_OK) {
            MyEventDay myEventDay = data.getParcelableExtra(RESULT);
            AddEventActivity mAddEvent;
            try {
                calendarView.setDate(myEventDay.getCalendar());
                eventDays.add(myEventDay);
                calendarView.setEvents(eventDays);
            } catch (OutOfDateRangeException exception) {

                Toast.makeText(getApplicationContext(),
                        "Date is out of range",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_calendar, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchData(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        /*final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));*/
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    private void searchData(String s) {
        Intent intent = new Intent(CalendarActivity.this, ViewEventRecyclerActivity.class);
        intent.putExtra("searchID", s.toLowerCase());
        intent.putExtra("actID", "search");
        startActivity(intent);
        /*db.collection("users").document(user)
                .collection("events")
                .whereEqualTo("search", s.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        HashMap<String, Object> eventMap = new HashMap<>();
                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            Events events = new Events();

                            eventMap.put("name", events.getName());
                            eventMap.put("date", events.getDate());
                            eventMap.put("start_time", events.getStart_time());
                            eventMap.put("end_time", events.getEnd_time());
                            eventMap.put("address", events.getAddress());
                            eventMap.put("city", events.getCity());
                            eventMap.put("state", events.getState());
                            eventMap.put("zip", events.getZip());
                            eventMap.put("mod", events.getMode_of_transportation());
                            eventMap.put("location", events.getLocation());
                            eventMap.put("note", events.getNote());
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CalendarActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });*/
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
                Toast.makeText(this, "Search selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }

        return true;
    }

    private void addNote() {
        Intent intent = new Intent(this, AddEventActivity.class);
        Log.d("testing", "add note");
        intent.putExtra("date", Date);
        intent.putExtra("actID", "calendar");
        startActivity(intent);
        Log.d("testing", "add note - after exec");
    }

    private void previewNote(EventDay eventDay) {
        Intent intent = new Intent(this, ViewEventRecyclerActivity.class);
        Events events;
        //final DatabaseReference eventRef = getRef()
        //intent.putExtra(UpdateEventActivity.EXTRA_EVENT_KEY, )
        //Map<String, Object> eventValues = events.toMap();
        Log.d("testing", "previewNote");
        if(eventDay instanceof MyEventDay){
            intent.putExtra(EVENT, (MyEventDay) eventDay);
        }
        intent.putExtra("date", Date);
        intent.putExtra("actID", "previewDate");
        startActivity(intent);
    }
}

class MyEventDay extends EventDay implements Parcelable {
    private int imageResource;
    private String mNote;

    public MyEventDay(Calendar day, int imageResource, String note) {
        super(day, imageResource);
        mNote = note;
    }
    String getNote() {
        return mNote;
    }

    private MyEventDay(Parcel in) {
        super((Calendar) in.readSerializable(), in.readInt());
        mNote = in.readString();
    }

    public static final Creator<MyEventDay> CREATOR = new Creator<MyEventDay>() {
        @Override
        public MyEventDay createFromParcel(Parcel in) {
            return new MyEventDay(in);
        }

        @Override
        public MyEventDay[] newArray(int size) {
            return new MyEventDay[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeSerializable(getCalendar());
        parcel.writeInt(i);
        parcel.writeString(mNote);
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
