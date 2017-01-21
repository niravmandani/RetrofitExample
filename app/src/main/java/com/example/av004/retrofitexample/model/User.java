package com.example.av004.retrofitexample.model;

import com.example.av004.retrofitexample.db.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

/**
 * Created by AV004 on 12/23/2016.
 */


@Table(database = MyDatabase.class)
@Parcel(analyze = {User.class})
public class User extends BaseModel {

    //Variables that are in our json
    @PrimaryKey
    @Column
    private int userId;
    @Column
    @PrimaryKey
    private String name;
    @Column
    private String email;
    @Column
    private boolean selected;

    @ForeignKey(stubbedRelationship = true)
    private Address address;


    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    @Table(database = MyDatabase.class)
    @Parcel(analyze = {Address.class})
    public static class Address extends BaseModel {

        @Column
        @PrimaryKey
        private String city;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

    }

}

