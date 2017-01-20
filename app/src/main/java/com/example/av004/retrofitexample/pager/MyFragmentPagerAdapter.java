package com.example.av004.retrofitexample.pager;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.av004.retrofitexample.R;
import com.example.av004.retrofitexample.model.User;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by AV004 on 1/17/2017.
 */

public class MyFragmentPagerAdapter extends PagerAdapter {
    private List<User> userList;
    @Bind(R.id.textUserName)
    TextView txtUserName;
    @Bind(R.id.textUserEmail)
    TextView txtUserEmail;
    @Bind(R.id.textUserCity)
    TextView txtUserCity;

    public MyFragmentPagerAdapter(List<User> userList) {
        this.userList = userList;
    }


    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        Log.d("MyFragmentPagerAdap", "instantiateItem: Called....");
        View item = LayoutInflater.from(collection.getContext()).inflate(R.layout.user_detail_adapter, collection, false);
        ButterKnife.bind(this, item);

        txtUserName.setText(userList.get(position).getName());
        txtUserEmail.setText(userList.get(position).getEmail());
        txtUserCity.setText(userList.get(position).getAddress().getCity());
        collection.addView(item);
        return item;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return userList.get(position).getName();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void updateUsers() {
        List<User> userList = SQLite.select().
                from(User.class).queryList();
        this.userList = userList;
        notifyDataSetChanged();


    }

    public void removeUser(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }
}
