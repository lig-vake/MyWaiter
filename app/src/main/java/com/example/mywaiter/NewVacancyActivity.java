package com.example.mywaiter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mywaiter.ui.home.Vacancy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class NewVacancyActivity extends AppCompatActivity {
    Calendar dateAndTime = Calendar.getInstance();
    TextView tvTime, tvDate;

    private DatabaseReference vacanciesDatabase;

    String time, date;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_new_vacancy);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        FirebaseApp.initializeApp(this);

        vacanciesDatabase = FirebaseDatabase.getInstance().getReference("/vacancies/");

        tvTime = findViewById(R.id.tvNewVacTime);
        tvDate = findViewById(R.id.tvNewVacDate);

        showDateTime();
    }

    // кнопка назад
    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(NewVacancyActivity.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // отображаем диалоговое окно для выбора времени
    public void setTime(View v) {
        new TimePickerDialog(NewVacancyActivity.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    // отображение даты и времени
    private void showDateTime() {
        time = String.format("%02d", dateAndTime.get(Calendar.HOUR_OF_DAY)) + "."
                + String.format("%02d", dateAndTime.get(Calendar.MINUTE));
        tvTime.setText(time);

        date = String.format("%02d", dateAndTime.get(Calendar.YEAR)) + "."
                + String.format("%02d", dateAndTime.get(Calendar.MONTH) + 1) + "."
                + String.format("%02d", dateAndTime.get(Calendar.DAY_OF_MONTH));
        tvDate.setText(date);
    }

    // при изменении времени
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            showDateTime();
        }
    };

    // при изменении даты
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            showDateTime();
        }
    };
    
    public void createVacancy(View view) {
        FirebaseFirestore fr = FirebaseFirestore.getInstance();

        EditText edName = findViewById(R.id.etNewVacName);
        EditText edReward = findViewById(R.id.edNewVacReward);
        Vacancy vacancy = new Vacancy(null, date, FirebaseAuth.getInstance().getUid(), edName.getText().toString(), edReward.getText().toString(), time);

        fr.collection("vacancies")
                .add(vacancy)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Ошибка создания вакансии: \n" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}