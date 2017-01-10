package com.example.av004.retrofitexample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, UsersAdapter.OnUserSelectListener {
    //Root URL of our web service
    public static final String ROOT_URL = "http://jsonplaceholder.typicode.com/";
    private UsersAdapter usersAdapter = null;
    // private RecyclerView recyclerView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    ProgressDialog loading;
    public static final int REQUEST_ADD_USER = 10;
    public static final int REQUEST_USER_DETAIL = 11;

    SharedPreferences preference;

    boolean UserLoaded;


    //List of type users this list will store type User which is our data model
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        preference = getSharedPreferences("pref", MODE_PRIVATE);
        UserLoaded = preference.getBoolean("UserLoaded", false);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
                Color.RED, Color.CYAN);
        swipeRefreshLayout.setDistanceToTriggerSync(20);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);

        //Recycler View Decoration
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loading = new ProgressDialog(MainActivity.this);
        preference.getBoolean("UserLoaded", true);
        if (UserLoaded) {
            //Shows the User List From Database
            showUserList();
        } else {
            //While the app fetched data we are displaying a progress dialog
            loading = ProgressDialog.show(MainActivity.this, "Fetching Data", "Please wait...", false, false);
            fetchUsers();
        }

    }

    public void onRefresh() {
        //Delete all the Users of the list and Load new Users from json
        Delete.table(User.class);
        fetchUsers();
    }

    private void fetchUsers() {

        //Creating a rest adapter
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ROOT_URL)
                .build();

        //Creating an object of our api interface
        UsersAPI api = restAdapter.create(UsersAPI.class);

        //Defining the method
        api.getUsers(new Callback<List<User>>() {
            @Override
            public void success(List<User> list, Response response) {
                //Dismissing the loading progressbar
                loading.dismiss();
                //Storing the data in our list
                users = list;
                //Storing Preference
                preference.edit().putBoolean("UserLoaded", true).apply();

                //Calling a method to insert the list
                insertUsers();
            }

            @Override
            public void failure(RetrofitError error) {
                //you can handle the errors here
            }
        });
    }

    private void insertUsers() {

        for (int i = 0; i < users.size(); i++) {

            users.get(i).save();
        }

        //Calling the method for showing the list
        showUserList();
    }

    //Our method to show list
    private void showUserList() {
        List<User> userList = SQLite.select().
                from(User.class).queryList();
        usersAdapter = new UsersAdapter(MainActivity.this, userList);
        recyclerView.setAdapter(usersAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.btnfab)
    public void addUser() {
        Intent i = new Intent(this, AddUserDetails.class);
        startActivityForResult(i, REQUEST_ADD_USER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_USER) {
            if (resultCode == RESULT_OK) {

                User user = (User) Parcels.unwrap(data.getParcelableExtra("user"));
                List<User> userList = SQLite.select().
                        from(User.class).queryList();
                usersAdapter.updateUsers(userList);
                usersAdapter.notifyDataSetChanged();
            }

        } else if (requestCode == REQUEST_USER_DETAIL) {
            if (resultCode == RESULT_OK) {
                // do this if request code is 11.
                List<User> userList = SQLite.select().
                        from(User.class).queryList();
                usersAdapter.updateUsers(userList);
                usersAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void setOnUserSelectListener(int position) {
        Intent intent = new Intent(this, UserDetails.class);
        intent.putExtra("Position", position);
        startActivityForResult(intent, REQUEST_USER_DETAIL);
    }
}
