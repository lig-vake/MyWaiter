package com.example.mywaiter.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mywaiter.AnotherUser;
import com.example.mywaiter.LoginActivity;
import com.example.mywaiter.R;
import com.example.mywaiter.ui.home.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private View root;

    private TextView tvEmail, tvEmailStatus;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        init();
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.account_exit:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(root.getContext(), LoginActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        tvEmail = root.findViewById(R.id.tvEmail);
        tvEmailStatus = root.findViewById(R.id.tvEmailStatus);

        if (user != null) {
            tvEmail.setText(user.getEmail());
            if (user.isEmailVerified()) {
                String text = tvEmailStatus.getText() + "Подтверждена";
                tvEmailStatus.setText(text);
            } else {
                String text = tvEmailStatus.getText() + "Не подтверждена";
                tvEmailStatus.setText(text);
            }
        }
    }
}