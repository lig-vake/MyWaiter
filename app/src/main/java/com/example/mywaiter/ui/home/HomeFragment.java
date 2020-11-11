    package com.example.mywaiter.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mywaiter.NewVacancyActivity;
import com.example.mywaiter.R;
import com.example.mywaiter.VacancyInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private View root;

    CollectionReference database;

    View vacLoadingBar;

    LayoutInflater layoutInflater;

    final ArrayList<Vacancy> vacancyList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        init();

        database = FirebaseFirestore.getInstance().collection("vacancies");

        layoutInflater = inflater;

        getVacancies();

        return root;
    }

    private void getVacancies() {
        Calendar c = Calendar.getInstance();
        final String date = String.format("%02d", c.get(Calendar.YEAR)) + "."
                + String.format("%02d", c.get(Calendar.MONTH) + 1) + "."
                + String.format("%02d", c.get(Calendar.DAY_OF_MONTH));

        final String time = String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + "."
                + String.format("%02d", c.get(Calendar.MINUTE));

        Query query = database;
        query.whereGreaterThan("date", date);
        query.whereGreaterThan("time", time);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Vacancy newVacancy = document.toObject(Vacancy.class);
                                vacancyList.add(newVacancy);
                            }
                            refreshVacancies();
                        }
                    }
                });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onPause() {
        super.onPause();
//        Fragment curFragment = getParentFragmentManager().putFragment();
    }

    private void init() {
        vacLoadingBar = root.findViewById(R.id.vacLoadingBar);

        // шобы он не держал оффлайн копию
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);

        // create new vacancy button
        root.findViewById(R.id.btnNewVac).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewVacancyActivity.class);
                startActivity(intent);
            }
        });

//        ChildEventListener listener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Vacancy vac = snapshot.getValue(Vacancy.class);
//                if (vac != null) {
//                    vacancyList.add(vac);
//                    refreshVacancies();
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                for (int i = 0; i < vacancyList.size(); i++) {
//                    String vacKey = vacancyList.get(i).key;
//                    if(vacKey.equals(snapshot.getKey())) {
//                        vacancyList.set(i, snapshot.getValue(Vacancy.class));
//                        break;
//                    }
//                }
//                refreshVacancies();
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                for (int i = 0; i < vacancyList.size(); i++) {
//                    String vacKey = vacancyList.get(i).key;
//                    if(vacKey.equals(snapshot.getKey())) {
//                        vacancyList.remove(i);
//                        break;
//                    }
//                }
//                refreshVacancies();
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };

        final SwipeRefreshLayout swipeRefresh = root.findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                vacancyList.clear();
                refreshVacancies();
                getVacancies();
                swipeRefresh.setRefreshing(false);
                vacLoadingBar.setVisibility(View.VISIBLE);
            }
        });
    }

    public void refreshVacancies() {
        ViewGroup parent = root.findViewById(R.id.vacContainer);

        if(vacancyList.size() > 0) {
            root.findViewById(R.id.vacLoadingBar).setVisibility(View.GONE);
        }

        parent.removeAllViews();
        for (Vacancy vac: vacancyList) {
            ViewGroup view = (ViewGroup)getLayoutInflater().inflate(R.layout.vac_block, parent, false);

            ((TextView)view.getChildAt(0)).setText(vac.date);
            ((TextView)view.getChildAt(1)).setText(vac.name);
            ((TextView)view.getChildAt(2)).setText(vac.reward);
            ((TextView)view.getChildAt(3)).setText(vac.time);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(root.getContext(), VacancyInfo.class);
                    startActivity(intent);
                }
            });

            parent.addView(view);
        }
    }
}