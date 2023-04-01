package edu.northeastern.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private TextView nannyShareTextView;
    private ImageView browseImageView;
    private ImageView nannyShareImageView;
    private ImageView tipsShareImageView;
    private ImageView myAccountImageView;
    private Button allTipsBtn;
    private Button myTipsBtn;

    private List<Tip> tipDataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CardViewAdapter adapter;
    private StaggeredGridLayoutManager layoutManager;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);


        // pass the Open and Close toggle for the drawer layout listener to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // greeting according to time of day in textView
        greetingTextView = findViewById(R.id.greet_tv);
        Calendar c = Calendar.getInstance();
        int currentHour = c.get(Calendar.HOUR_OF_DAY);
        System.out.println("currentHour" + currentHour);
        String message = "";
        if(currentHour >= 12 && currentHour < 17){
            message = "Good Afternoon, ";
        } else if(currentHour >= 17 && currentHour < 21){
            message = "Good Evening, ";
        } else if(currentHour >= 21 && currentHour < 24){
            message = "Good Night, ";
        } else {
            message = "Good Morning, ";
        }

        greetingTextView.setText(message);

        // username in textview
        userNameTextView = findViewById(R.id.username_tv);

        // search in textview
        searchTextView = findViewById(R.id.search_tv);

        // search button
        searchBtn = findViewById(R.id.searchBtn);

        // filters
        filter1TextView = findViewById(R.id.tv_filter1);
        filter2TextView = findViewById(R.id.tv_filter2);
        filter3TextView = findViewById(R.id.tv_filter3);

        // nanny share info textview
        nannyShareTextView = findViewById(R.id.nannyShareInfo);

        // 2 Tips button
        allTipsBtn = findViewById(R.id.btn_allTips);
        myTipsBtn = findViewById(R.id.btn_myTips);

        // recyclerView
        initView();
        setData();
        setRecycler();

        // ImageView(browse, nanny share, tips share, my account)
        browseImageView = findViewById(R.id.tv_browse);
        nannyShareImageView = findViewById(R.id.tv_nanny);
        tipsShareImageView = findViewById(R.id.tv_tips);
        myAccountImageView = findViewById(R.id.tv_myAccount);

    }

    // 导入本地图片
    private void setData() {
        //RecyclerView
        tipDataList.add(new Tip("Title", "chair", R.drawable.ic_baseline_chair_24));
        tipDataList.add(new Tip("Title dggfgfghghgjhjhh", "username", R.drawable.ic_baseline_insert_emoticon_24));
        tipDataList.add(new Tip("titletitletitle","user687", R.drawable.ic_baseline_airport_shuttle_24));
        tipDataList.add(new Tip("gytryrryryry5656767", "user123", R.drawable.ic_baseline_account_circle_24));
        tipDataList.add(new Tip("RecylerView实现瀑布流布局StaggeredGridLayoutManager使用", "阿呆", R.drawable.ic_baseline_add_circle_outline_24));
        tipDataList.add(new Tip("RecylerView实现瀑布流布局", "amzsd1", R.drawable.ic_baseline_baby_changing_station_24));
        tipDataList.add(new Tip("使用RecylerView实现瀑布流布局StaggeredGridLayoutManager", "kk", R.drawable.ic_baseline_travel_explore_24));

    }

    private void setRecycler() {
        adapter = new CardViewAdapter(tipDataList);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //每行两个瀑布流排列
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


}