package com.example.av004.retrofitexample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by AV004 on 12/29/2016.
 */

public class UserFragement extends Fragment {
    List<User> userList;
    @Bind(R.id.textUserName)
    TextView txtUserName;
    @Bind(R.id.textUserEmail)
    TextView txtUserEmail;
    @Bind(R.id.textUserCity)
    TextView txtUserCity;
    int position;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout resource that'll be returned
        View rootView = inflater.inflate(R.layout.user_detail_adapter, container, false);
        userList = SQLite.select().
                from(User.class).queryList();
        ButterKnife.bind(this, rootView);
        // Get the arguments that was supplied when
        // the fragment was instantiated in the
        // CustomPagerAdapter
        Log.d("UserFragment", userList.get(position).getName());

        txtUserName.setText(userList.get(position).getName());
        txtUserEmail.setText(userList.get(position).getEmail());
        txtUserCity.setText(userList.get(position).getAddress().getCity());
        return rootView;

    }

    //For Refresh the fragment
    public void updateUsers(int position) {
        List<User> userList = SQLite.select().
                from(User.class).queryList();
        this.userList = userList;
        this.position = position;

    }

}
