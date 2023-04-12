package edu.northeastern.myapplication;

import edu.northeastern.myapplication.entity.Tip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.northeastern.myapplication.entity.User;
import edu.northeastern.myapplication.nanny.NannyshareMain;
import edu.northeastern.myapplication.recylerView.CardViewAdapter;

/**
 * The HomeActivity class.
 */
public class HomeActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    private TextView greetingTextView;
    private TextView userNameTextView;
    private EditText searchTextView;
    private ImageButton searchBtn;
    private TextView filter1TextView;
    private TextView filter2TextView;
    private TextView filter3TextView;
    private TextView filter4TextView;

    private TextView nannyShareTextView;
    private Button allTipsBtn;
    private Button myTipsBtn;
    private ImageView browseImageView;
    private ImageView nannyShareImageView;
    private ImageView tipsShareImageView;
    private ImageView myAccountImageView;

    private User user;

    private RecyclerView recyclerView;
    private CardViewAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;
    private List<Tip> tipsList = new ArrayList<>();

    private boolean isPediatriciansSelected = false;
    private boolean isDaycareSelected = false;
    private boolean isEventInfoSelected = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = getIntent().getExtras().getParcelable("user");

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_account:
                        // Handle "My Account" click
                        break;
                    case R.id.nav_settings:
                        // Handle "Settings" click
                        break;
                    case R.id.nav_logout:
                        logout();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // greeting according to time of day in textView
        greetingTextView = findViewById(R.id.greet_tv);
        LocalDateTime currentTime = LocalDateTime.now();
        int currentHour = currentTime.getHour();
        System.out.println("currentHour" + currentHour);

        String message = "";
        if (currentHour >= 6 && currentHour < 12) {
            message = "Good Morning, ";
        } else if (currentHour >= 12 && currentHour < 18) {
            message = "Good Afternoon, ";
        } else if (currentHour >= 18 && currentHour < 22) {
            message = "Good Evening, ";
        } else {
            message = "Good Night, ";
        }

        greetingTextView.setText(message);

        // username in textview
        userNameTextView = findViewById(R.id.username_tv);
        userNameTextView.setText(user.getUsername());

        // search in textview
        searchTextView = findViewById(R.id.search_tv);

        // search button
        searchBtn = findViewById(R.id.searchBtn);

        // filters
        filter1TextView = findViewById(R.id.tv_filter1);
        filter1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<String> selectedCategories = new HashSet<>();
                selectedCategories.add("Pediatricians");
                List<Tip> filteredTips = filterTipsByCategories(tipsList, selectedCategories);
//                isPediatriciansSelected = !isPediatriciansSelected;
//                updateFilteredTips();
                Set<Tip> filteredTipsSet = new HashSet<>(filteredTips);
                updateRecyclerView(filteredTipsSet);
            }
        });

        filter2TextView = findViewById(R.id.tv_filter2);
        filter2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<String> selectedCategories = new HashSet<>();
                selectedCategories.add("Daycare");
                List<Tip> filteredTips = filterTipsByCategories(tipsList, selectedCategories);
//                isDaycareSelected = !isDaycareSelected;
//                updateFilteredTips();
                Set<Tip> filteredTipsSet = new HashSet<>(filteredTips);
                updateRecyclerView(filteredTipsSet);

            }
        });

        filter3TextView = findViewById(R.id.tv_filter3);
        filter3TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<String> selectedCategories = new HashSet<>();
                selectedCategories.add("Event Info");
                List<Tip> filteredTips = filterTipsByCategories(tipsList, selectedCategories);
//                isEventInfoSelected = !isEventInfoSelected;
//                updateFilteredTips();
                Set<Tip> filteredTipsSet = new HashSet<>(filteredTips);
                updateRecyclerView(filteredTipsSet);
            }
        });

        filter4TextView = findViewById(R.id.tv_filter4);
        filter4TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<String> selectedCategories = new HashSet<>();
                selectedCategories.add("Others");
                List<Tip> filteredTips = filterTipsByCategories(tipsList, selectedCategories);
//                updateFilteredTips();
                Set<Tip> filteredTipsSet = new HashSet<>(filteredTips);
                updateRecyclerView(filteredTipsSet);
            }
        });

        // nanny share info textview
        nannyShareTextView = findViewById(R.id.nannyShareInfo);

        // allTips button, myTips button
        allTipsBtn = findViewById(R.id.btn_allTips);
        myTipsBtn = findViewById(R.id.btn_myTips);

        // ImageView(browse, nanny share, tips share, my account)
        browseImageView = findViewById(R.id.tv_browse);

        browseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshHomeActivity();
            }
        });

        nannyShareImageView = findViewById(R.id.tv_nanny);
        tipsShareImageView = findViewById(R.id.tv_tips);
        tipsShareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PostActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        myAccountImageView = findViewById(R.id.tv_myAccount);
        myAccountImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MyInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        nannyShareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, NannyshareMain.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        initView();
        setRecycler();
        loadDataFromFirebase();
    }

    /**
     * 根据所选类别过滤tips列表,getFilter()方法来获取分类信息
     *
     * @param tipsList
     * @param selectedCategories
     * @return
     */
    private List<Tip> filterTipsByCategories(List<Tip> tipsList, Set<String> selectedCategories) {
        List<Tip> filteredTips = new ArrayList<>();

        for (Tip tip : tipsList) {
            String[] tipCategories = tip.getFilter().split(" ");
            for (String tipCategory : tipCategories) {
                if (selectedCategories.contains(tipCategory)) {
                    filteredTips.add(tip);
                    break;
                }
            }
        }
        return filteredTips;
    }


    /**
     * 根据所选过滤器更新RecyclerView
     */
    private void updateFilteredTips() {
        Set<String> selectedCategories = new HashSet<>();
        if (isPediatriciansSelected) {
            selectedCategories.add("Pediatricians");
        } else if (isDaycareSelected) {
            selectedCategories.add("Daycare");
        } else if (isEventInfoSelected) {
            selectedCategories.add("Event Info");
        } else {
            selectedCategories.add("Others");
        }

        List<Tip> filteredTips = filterTipsByCategories(tipsList, selectedCategories);
        Set<Tip> filteredTipsSet = new HashSet<>(filteredTips);
        updateRecyclerView(filteredTipsSet);
    }

    private void updateRecyclerView(Set<Tip> filteredTips) {
        List<Tip> filteredTipsList = new ArrayList<>(filteredTips);

        CardViewAdapter adapter = new CardViewAdapter(this, filteredTipsList);
        recyclerView.setAdapter(adapter);
    }

    private void loadDataFromFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tips");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot tipSnapshot : dataSnapshot.getChildren()) {
                    Tip tip = tipSnapshot.getValue(Tip.class);
                    tipsList.add(tip);
                }

                adapter.setTipDataList(tipsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void setRecycler() {
        adapter = new CardViewAdapter(this, tipsList);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
    }

    // override the onOptionsItemSelected() function to implement the item click listener callback
    // to open and close the navigation drawer when the icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Logout in drawer.
     */
    private void logout() {
        // Perform any necessary cleanup, such as clearing user data
        // Redirect the user to the login screen
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Return to and refresh Home page, at the meantime keeping the user login.
     */
    private void refreshHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}