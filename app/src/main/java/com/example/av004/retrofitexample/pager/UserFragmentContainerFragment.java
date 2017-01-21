package com.example.av004.retrofitexample.pager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.av004.retrofitexample.Extra.EditUserActivity;
import com.example.av004.retrofitexample.R;
import com.example.av004.retrofitexample.interfaces.OnPageSelectedListener;
import com.example.av004.retrofitexample.interfaces.PageSelector;
import com.example.av004.retrofitexample.model.User;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class UserFragmentContainerFragment extends Fragment implements ViewPager.OnPageChangeListener, OnPageSelectedListener, PageSelector {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = UserFragmentContainerFragment.class.getSimpleName();
    private static final String KEY_STATE_CUR_USER_POSITION = "KEY_STATE_CUR_USER_POSITION";
    private static final int REQUEST_EDIT_USER = 12;
    private Boolean initialCreate;
    private int curUserPosition;
    private int DEFAULT_INDEX = 0;
    public int position;
    private OnFragmentInteractionListener onFragmentInteractionListener;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    OnUserEditListener onUserEditListener;
    ViewPager viewPager;
    TabLayout tabLayout;
    Context context;
    OnPageSelectedListener onPageSelectedListener;
    List<User> userList;


    public UserFragmentContainerFragment() {
        // Required empty public constructor
    }

    public interface OnUserEditListener {

        void setOnUserEdit();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v("", "onAttach");
        context = getContext();

        // Check if parent fragment (if there is one) implements the image
        // selection interface
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof OnPageSelectedListener) {
            onPageSelectedListener = (OnPageSelectedListener) parentFragment;
        }
        // Otherwise, check if parent activity implements the image
        // selection interface
        else if (activity != null && activity instanceof OnPageSelectedListener) {
            onPageSelectedListener = (OnPageSelectedListener) activity;
        }
        // If neither implements the image selection callback, warn that
        // selections are being missed
        else if (onPageSelectedListener == null) {
            Log.w("", "onAttach: niether the parent fragment or parent activity implement OnImageSelectedListener, "
                    + "image selections will not be communicated to other components");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Restore state
        if (savedInstanceState != null) {
            initialCreate = false;

            //mCurImageResourceId = savedInstanceState.getInt(KEY_STATE_CUR_IMAGE_RESOURCE_ID);
            curUserPosition = savedInstanceState.getInt(KEY_STATE_CUR_USER_POSITION);
        }
        // Otherwise, default state
        else {
            initialCreate = true;

            curUserPosition = DEFAULT_INDEX;

        }

        // Set that this fragment has a menu
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_user_fragment_container, container, false);
        ButterKnife.bind(this, v);

        Log.d("UserFragmentContaerFrg", "userfrgContfrg called......");

        userList = SQLite.select().
                from(User.class).queryList();
        position = getArguments().getInt("position");
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
       // tabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(userList);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setCurrentItem(position, false);
        tabLayout=(TabLayout) v.findViewById(R.id.sliding_tabs);
        if(tabLayout!=null) {
             tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
             tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
             tabLayout.setupWithViewPager(viewPager);

        }
        // Listen for page changes to update other views
        viewPager.setOnPageChangeListener(this);

        return v;
    }


    public void onButtonPressed(Uri uri) {
        if (onFragmentInteractionListener != null) {
            onFragmentInteractionListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v(TAG, "onViewCreated");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, "onActivityCreated");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.v(TAG, "onViewStateRestored");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(TAG, "onSaveInstanceState");

        //  outState.putInt(KEY_STATE_CUR_IMAGE_RESOURCE_ID, mCurImageResourceId);
        outState.putInt(KEY_STATE_CUR_USER_POSITION, curUserPosition);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentInteractionListener = null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageSelected(int position) {
        /**if we want to set the list selected item Cheked */
           if (onPageSelectedListener != null) {
               onPageSelectedListener.onPageSelected(position);
         }
    }

    @Override
    public void setPageSelected(int position) {
        if (isResumed()) {
            // If the selected position is valid, and different than what is
            // currently selected, move the pager to that image
            if (position >= 0 && position < myFragmentPagerAdapter.getCount()
                    && position != viewPager.getCurrentItem()) {
                // Log.d(TAG, "setImageSelected: title = " + user.getTitle() + " position = " + position);
                // Move the view pager to the current image
                viewPager.setCurrentItem(position, true);

            }
        }
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);

    }

    public void updateUsers(List<User> userList, int position) {
        //  List<User> userList = SQLite.select().
        //        from(User.class).queryList();
        this.userList = userList;
        this.position = position;

        if (myFragmentPagerAdapter != null) {
            myFragmentPagerAdapter.updateUsers();
            // tabLayout.setupWithViewPager(viewPager);
            myFragmentPagerAdapter.notifyDataSetChanged();
        } else {

        }
    }

    public void removeUser() {
        List<User> userList = SQLite.select().
                from(User.class).queryList();
        this.userList = userList;
        // this.position = position;
        myFragmentPagerAdapter.removeUser(userList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.v(TAG, "onCreateOptionsMenu");

        // Inflate the action bar menu
        inflater.inflate(R.menu.edit_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_editUser:
                Intent intent = new Intent(getContext(), EditUserActivity.class);
                position = viewPager.getCurrentItem();
                intent.putExtra("position", position);
                startActivityForResult(intent, REQUEST_EDIT_USER);
                return true;

            case android.R.id.home:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_USER) {
            if (resultCode == RESULT_OK) {
                // do this if request code is 11.
                List<User> userList = SQLite.select().
                        from(User.class).queryList();
                this.updateUsers(userList, position);
                myFragmentPagerAdapter.notifyDataSetChanged();
                onUserEditListener = (UserFragmentContainerFragment.OnUserEditListener) context;
                onUserEditListener.setOnUserEdit();
            }
        }
    }
}
