package com.example.av004.retrofitexample;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


/**
 * Created by AV004 on 1/3/2017.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {
    OnUserSelectListener onUserSelectListener;
    private List<User> usersList;
    Context context;
    public int position;

    public interface OnUserSelectListener {

        void setOnUserSelectListener(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txtname)
        TextView textviewname;
        @Bind(R.id.txtemail)
        TextView textviewemail;
        @Bind(R.id.txtcity)
        TextView textviewcity;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public UsersAdapter(Context context, List<User> usersList) {
        this.usersList = usersList;
        this.context = context;
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            @OnItemClick()
            public void onClick(View v) {
                onUserSelectListener = (OnUserSelectListener) context;

                onUserSelectListener.setOnUserSelectListener(position);
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

            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
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

