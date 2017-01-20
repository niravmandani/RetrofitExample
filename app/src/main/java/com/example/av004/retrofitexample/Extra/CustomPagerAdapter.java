package com.example.av004.retrofitexample.Extra;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.av004.retrofitexample.model.User;

import java.util.List;

/**
 * Created by AV004 on 12/29/2016.
 */

public class CustomPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    UserFragement fragment;
    List<User> userList;


    public CustomPagerAdapter(FragmentManager supportFragmentManager, Context context, List<User> userList) {
        super(supportFragmentManager);
        this.context = context;
        this.userList = userList;
    }

    @Override
    public Fragment getItem(int position) {
        fragment = new UserFragement();
        Bundle args = new Bundle();
        Log.d("CustomPagerAdapter", userList.get(position).getName());
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return userList.get(position).getName();
    }

    public void updateUsers(List<User> userList, int position) {
        this.userList = userList;
        fragment.updateUsers(position);
        notifyDataSetChanged();

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
