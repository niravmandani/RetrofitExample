package com.example.av004.retrofitexample.Extra;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.av004.retrofitexample.R;
import com.example.av004.retrofitexample.model.User;
import com.example.av004.retrofitexample.pager.UserFragmentContainerFragment;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserDetails extends AppCompatActivity implements UserFragmentContainerFragment.OnFragmentInteractionListener {
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.sliding_tabs)
    TabLayout tabLayout;
    public int position;
    CustomPagerAdapter customPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("User Details");
        Intent intent = getIntent();
        this.position = intent.getIntExtra("Position", 0);

        List<User> userList = SQLite.select().
                from(User.class).queryList();

        customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), this, userList);
        // Attach the page change listener inside the activity

        viewPager.setAdapter(customPagerAdapter);
        viewPager.setCurrentItem(position, false);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                Intent intent = new Intent();
                position = viewPager.getCurrentItem();
                intent.putExtra("passed_position", position);
                setResult(RESULT_OK, intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            List<User> userList = SQLite.select().
                    from(User.class).queryList();
            int position = viewPager.getCurrentItem();
            customPagerAdapter.updateUsers(userList, position);
            tabLayout.setupWithViewPager(viewPager);
            customPagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}





