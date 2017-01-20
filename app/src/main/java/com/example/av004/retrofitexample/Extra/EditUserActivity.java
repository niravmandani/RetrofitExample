package com.example.av004.retrofitexample.Extra;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.av004.retrofitexample.R;
import com.example.av004.retrofitexample.model.User;
import  com.example.av004.retrofitexample.model.User_Table;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by AV004 on 1/5/2017.
 */

public class EditUserActivity extends AppCompatActivity {
    @Bind(R.id.edtName)
    EditText edtUserName;

    @Bind(R.id.edtEmail)
    EditText edtUserEmail;

    @Bind(R.id.edtCity)
    EditText edtUserCity;
    List<User> userList;

    public int position;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_details);
        getSupportActionBar().setTitle("Edit User");
        ButterKnife.bind(this);
        List<User> userList = SQLite.select().
                from(User.class).queryList();
        this.userList = userList;
        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        this.position = position;
        User user;
        user = userList.get(position);
        edtUserName.setText(user.getName());
        edtUserEmail.setText(user.getEmail());
        edtUserCity.setText(user.getAddress().getCity());

    }

    public void reSubmit() {
        SQLite.update(User.class)
                .set(User_Table.name.eq(edtUserName.getText().toString()), User_Table.email.eq(edtUserEmail.getText().toString()), User_Table.address_city.eq(edtUserCity.getText().toString()))
                .where(User_Table.name.is(userList.get(position).getName()))
                .async()
                .execute();
        Intent i = new Intent();
        i.putExtra("Passed_name", edtUserName.getText().toString());
        i.putExtra("Passed_email", edtUserEmail.getText().toString());
        i.putExtra("Passed_city", edtUserCity.getText().toString());
        setResult(RESULT_OK, i);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_updateUser:
                reSubmit();

            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
