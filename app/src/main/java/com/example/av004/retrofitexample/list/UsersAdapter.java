package com.example.av004.retrofitexample.list;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.av004.retrofitexample.R;
import com.example.av004.retrofitexample.model.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * Created by AV004 on 1/3/2017.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {
    private List<User> usersList;
    public int position;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    RelativeLayout list_row;
    OnUserSelectListener onUserSelectListener;
    OnUserDeleteListener onUserDeleteListener;
    Context context;

    public interface OnUserDeleteListener {

        void setOnUserDelete();
    }

    public interface OnUserSelectListener {

        void setOnUserSelect(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txtname)
        TextView textviewname;
        @Bind(R.id.txtemail)
        TextView textviewemail;
        @Bind(R.id.txtcity)
        TextView textviewcity;
        RelativeLayout list_row;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            list_row = (RelativeLayout) view.findViewById(R.id.list_row);
        }
    }

    public UsersAdapter(Context context, List<User> usersList) {
        this.usersList = usersList;
        this.context = context;
        mPref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        mEditor = mPref.edit();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_list, parent, false);
        ButterKnife.bind(this, itemView);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        this.position = position;
        User user = usersList.get(position);
        holder.textviewname.setText(user.getName());
        holder.textviewemail.setText(user.getEmail());
        holder.textviewcity.setText(user.getAddress().getCity());
        if (usersList.get(position).isSelected()) {
            holder.list_row.setBackgroundColor(Color.parseColor("#7B8BA0"));
        } else {
            holder.list_row.setBackgroundColor(Color.TRANSPARENT);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnItemClick()
            public void onClick(View v) {
                setSelected(position);
                onUserSelectListener = (OnUserSelectListener) context;
                onUserSelectListener.setOnUserSelect(position);

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                removeItemFromList(position);
                return false;
            }
        });
    }

    public void setSelected(int pos) {
        try {
            if (usersList.size() > 1) {
                usersList.get(mPref.getInt("position", 0)).setSelected(false);
                mEditor.putInt("position", pos);
                mEditor.commit();
            }
            usersList.get(pos).setSelected(true);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    protected void removeItemFromList(final int position) {
        final int deletePosition = position;

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Delete");
        alert.setMessage("Do you want delete this item?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // main code on after clicking yes
                usersList.get(position).delete();
                usersList.remove(deletePosition);
                notifyDataSetChanged();
                onUserDeleteListener = (OnUserDeleteListener) context;
                onUserDeleteListener.setOnUserDelete();
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void updateUsers(List<User> usersList) {
        this.usersList = usersList;
        notifyDataSetChanged();
    }

}

