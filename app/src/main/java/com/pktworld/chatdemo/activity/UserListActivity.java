package com.pktworld.chatdemo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pktworld.chatdemo.R;
import com.pktworld.chatdemo.adapter.UsersListAdapter;
import com.pktworld.chatdemo.util.ApplicationConstant;
import com.pktworld.chatdemo.util.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    private static final String TAG = UserListActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<ParseUser> allUsers;
    private UsersListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.listUsers);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(8));
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        allUsers = new ArrayList<ParseUser>();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(ApplicationConstant.USER_LIST_ADAPTER));


       /* editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(editSearch.getText().toString().trim());
                    editSearch.setText("");
                    return true;
                }
                return false;
            }

        });*/


        receiveAllUsers("");
        mAdapter = new UsersListAdapter(UserListActivity.this, allUsers);
        mRecyclerView.setAdapter(mAdapter);

    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String userId = intent.getStringExtra(ApplicationConstant.FLAG1);
            String userName = intent.getStringExtra(ApplicationConstant.FLAG2);

            Log.d(TAG, "Got message: " + userId);
            Intent i = new Intent(UserListActivity.this, OneToOneChatActivity.class);
            i.putExtra(ApplicationConstant.FLAG1, userId);
            i.putExtra(ApplicationConstant.FLAG2, userName);
            startActivity(i);
        }
    };



    /*private void performSearch(String cs) {
        if (!cs.isEmpty() && cs.length() >0){
            *//*db.addHistory(new HistoryModel(cs));
            if (Utils.isConnected(SearchActivity.this)){
                loadSearchList(SearchActivity.this,"search.php",cs);
            }*//*
            receiveAllUsers(cs);
            mAdapter = new UsersListAdapter(SearchActivity.this, allUsers);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    void receiveAllUsers(String q){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.setLimit(70);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> messages, ParseException e) {
                if (e == null) {
                    allUsers.clear();
                    Collections.reverse(messages);
                    allUsers.addAll(messages);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.invalidate();
                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(UserListActivity.this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }



}
