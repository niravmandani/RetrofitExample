package com.example.av004.retrofitexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.av004.retrofitexample.model.User;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddUserDetails extends AppCompatActivity {

    @Bind(R.id.edtName)
    EditText edtUserName;

    @Bind(R.id.edtEmail)
    EditText edtUserEmail;

    @Bind(R.id.edtCity)
    EditText edtUserCity;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_details);
        getSupportActionBar().setTitle("Add User");
        ButterKnife.bind(this);

    }

    public void submit() {

        if (edtUserName.getText().toString().equalsIgnoreCase("")) {
            edtUserName.setError("Enater Name");
        } else if (edtUserEmail.getText().toString().equalsIgnoreCase("")) {
            edtUserEmail.setError("Enater Email");
        } else if (edtUserCity.getText().toString().equalsIgnoreCase("")) {
            edtUserCity.setError("Enater City");
        } else {
            User user = new User();
            user.setName(edtUserName.getText().toString());
            if (edtUserEmail.getText().toString().matches(emailPattern)) {
                user.setEmail(edtUserEmail.getText().toString());
                User.Address address = new User.Address();
                address.setCity(edtUserCity.getText().toString());
                user.setAddress(address);
                user.save();
                Intent intent = new Intent();
                intent.putExtra("user", Parcels.wrap(user));
                setResult(RESULT_OK, intent);
                this.finish();
            } else {
                Toast.makeText(this, "Please Insert Valid Email", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addUser:
                submit();
                return true;
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
