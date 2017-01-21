package com.example.av004.retrofitexample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.example.av004.retrofitexample.Extra.DividerItemDecoration;
import com.example.av004.retrofitexample.interfaces.OnPageSelectedListener;
import com.example.av004.retrofitexample.interfaces.UsersAPI;
import com.example.av004.retrofitexample.list.UsersAdapter;
import com.example.av004.retrofitexample.model.User;
import com.example.av004.retrofitexample.pager.UserFragmentContainerFragment;
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


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, UsersAdapter.OnUserSelectListener, UserFragmentContainerFragment.OnFragmentInteractionListener, OnPageSelectedListener, UsersAdapter.OnUserDeleteListener, UserFragmentContainerFragment.OnUserEditListener {
    //Root URL of our web service
    private static final String ROOT_URL = "http://jsonplaceholder.typicode.com/";
    private static final int REQUEST_ADD_USER = 10;
    private static final int REQUEST_USER_DETAIL = 11;
    private static final int REQUEST_EDIT_USER = 12;
    private static final int DEFAULT_INDEX = 0;
    private UsersAdapter usersAdapter = null;
    int position = DEFAULT_INDEX;
    int curPosition = DEFAULT_INDEX;
    boolean userLoaded;
    //List of type users this list will store type User which is our data model
    private List<User> users;
    // private RecyclerView recyclerView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    @Bind(R.id.activity_main_viewPager_container)
    ViewGroup activity_main_viewPager_container;

    OnPageSelectedListener onPageSelectedListener;
    UserFragmentContainerFragment containerFragment;
    ViewGroup container;
    ProgressDialog progressDialog;
    SharedPreferences preference;
    UserFragmentContainerFragment userFragmentContainerFragment;
    private Boolean mInitialCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("User List");

        container = (ViewGroup) findViewById(R.id.fragment_user_fragment_viewPager_container);
        preference = getSharedPreferences("pref", MODE_PRIVATE);
        userLoaded = preference.getBoolean("UserLoaded", false);

        //swipeRefreshView Initialization
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
        progressDialog = new ProgressDialog(MainActivity.this);

        preference.getBoolean("UserLoaded", true);
        if (userLoaded) {
            //Shows the User List From Database
            showUserList();
        } else {
            //While the app fetched data displaying a progress dialog
            progressDialog = ProgressDialog.show(MainActivity.this, "Fetching Data", "Please wait...", false, false);
            fetchUsers();
        }

        if (savedInstanceState != null) {
            mInitialCreate = false;
        } else {
            mInitialCreate = true;
            //initialize User Detail fragment for Tablet
            if (activity_main_viewPager_container != null) {
                Log.i("MainAcivityFragement:::", "onCreate: adding ImageRotatorFragment to MainActivity");

                // Add User Details fragment to the activity's container layout
                userFragmentContainerFragment = new UserFragmentContainerFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                //Log.d("CustomPagerAdapter", userList.get(position).getName());
                args.putInt("position", position);
                userFragmentContainerFragment.setArguments(args);
                fragmentTransaction.replace(activity_main_viewPager_container.getId(), userFragmentContainerFragment,
                        UserFragmentContainerFragment.class.getName());

                // Commit the transaction
                fragmentTransaction.commit();
            }
        }
        if (mInitialCreate && usersAdapter.getItemCount() > 0) {

            // Default the selection to the first item
            usersAdapter.setSelected(DEFAULT_INDEX);
        }

        // Track that onCreateView has been called at least once since the
        // initial onCreate
        if (mInitialCreate) {
            mInitialCreate = false;
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
                progressDialog.dismiss();
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
        //Adding each row into database
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
        if (userFragmentContainerFragment != null) {
            userFragmentContainerFragment.updateUsers(userList, position);
        }
    }

    //Floating Action Button Click Event
    @OnClick(R.id.btnfab)
    public void addUser() {
        Intent i = new Intent(this, AddUserDetails.class);
        startActivityForResult(i, REQUEST_ADD_USER);
    }

    //onActivityResult method for notify the changes
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
                if (userFragmentContainerFragment != null) {
                    userFragmentContainerFragment.updateUsers(userList, position);
                }
            }

        } else if (requestCode == REQUEST_USER_DETAIL) {
            if (resultCode == RESULT_OK) {
                // do this if request code is 11.
                List<User> userList = SQLite.select().
                        from(User.class).queryList();
                if (userFragmentContainerFragment != null) {
                    userFragmentContainerFragment.updateUsers(userList, position);
                }
                usersAdapter.updateUsers(userList);
                usersAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == REQUEST_EDIT_USER) {
            if (resultCode == RESULT_OK) {
                // do this if request code is 11.
                List<User> userList = SQLite.select().
                        from(User.class).queryList();
                if (userFragmentContainerFragment != null) {
                    userFragmentContainerFragment.updateUsers(userList, position);
                }
                usersAdapter.updateUsers(userList);
                usersAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void setOnUserSelect(int position) {

        container = (ViewGroup) findViewById(R.id.fragment_user_fragment_viewPager_container);
        if (container != null) {
            //set on item click user detail in viewpager fragment in tablet
            onPageSelectedListener = new OnPageSelectedListener() {
                @Override
                public void onPageSelected(int position) {
                    if (curPosition != position) {

                        curPosition = position;
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        containerFragment = (UserFragmentContainerFragment) fragmentManager.findFragmentByTag(UserFragmentContainerFragment.class.getName());

                        if (containerFragment != null) {
                            containerFragment.setPageSelected(position);
                        }
                    }
                }
            };

            onPageSelectedListener.onPageSelected(position);

        } else {
            //setting user detail fragment for Handset
            FragmentManager fragmentManager = getSupportFragmentManager();
            userFragmentContainerFragment = new UserFragmentContainerFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle args = new Bundle();
            args.putInt("position", position);
            userFragmentContainerFragment.setArguments(args);
            this.container = (ViewGroup) findViewById(R.id.activity_main_root_container);
            fragmentTransaction.replace(container.getId(), userFragmentContainerFragment, UserFragmentContainerFragment.class.getName());
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.addToBackStack(null);

            // Commit the transaction
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onPageSelected(int position) {
        usersAdapter.setSelected(position);
        recyclerView.scrollToPosition(position);
        usersAdapter.notifyDataSetChanged();
    }

    @Override
    public void setOnUserDelete() {
        if (activity_main_viewPager_container != null) {
            userFragmentContainerFragment.removeUser();
        } else {

        }
    }

    @Override
    public void setOnUserEdit() {
        //on user edit notifydatasetchanged to users adapter
        List<User> userList = SQLite.select().
                from(User.class).queryList();
        usersAdapter.updateUsers(userList);
        usersAdapter.notifyDataSetChanged();
    }
}
