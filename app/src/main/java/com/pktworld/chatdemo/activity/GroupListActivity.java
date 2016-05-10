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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.pktworld.chatdemo.R;
import com.pktworld.chatdemo.adapter.GroupListAdapter;
import com.pktworld.chatdemo.model.Group;
import com.pktworld.chatdemo.util.ApplicationConstant;
import com.pktworld.chatdemo.util.SpacesItemDecoration;
import com.pktworld.chatdemo.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupListActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = GroupListActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Group> group;
    private GroupListAdapter mAdapter;
    private LinearLayout llGroup,llNoGroup;
    private EditText editGroupName;
    private Button btnCreateGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.listUsers);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(8));
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        group = new ArrayList<Group>();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(ApplicationConstant.GROUP_LIST_ADAPTER));

        llGroup = (LinearLayout)findViewById(R.id.layoutRecyclerList);
        llNoGroup = (LinearLayout)findViewById(R.id.layoutNoGroup);
        editGroupName = (EditText)findViewById(R.id.editGroupName);
        btnCreateGroup = (Button)findViewById(R.id.btnCreateGroup);
        btnCreateGroup.setOnClickListener(this);


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

        mAdapter = new GroupListAdapter(GroupListActivity.this, group);
        mRecyclerView.setAdapter(mAdapter);
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String userId = intent.getStringExtra(ApplicationConstant.FLAG1);
            String userName = intent.getStringExtra(ApplicationConstant.FLAG2);

            Log.d(TAG, "Got message: " + userId);
            Intent i = new Intent(GroupListActivity.this, OneToOneChatActivity.class);
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
        ParseQuery<Group> query = ParseQuery.getQuery(Group.class);
        query.setLimit(70);
        query.findInBackground(new FindCallback<Group>() {
            public void done(List<Group> messages, ParseException e) {
                if (e == null) {
                    group.clear();
                    Collections.reverse(messages);
                    group.addAll(messages);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.invalidate();

                    if (messages.size() == 0){
                        llNoGroup.setVisibility(View.VISIBLE);
                    }else {
                        llGroup.setVisibility(View.VISIBLE);
                    }


                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(GroupListActivity.this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        if (v == btnCreateGroup){
            if (Utils.isConnected(GroupListActivity.this)){
                if (editGroupName.getText().toString().length() == 0 || editGroupName.getText().toString().isEmpty()){
                    Utils.showToastMessage(GroupListActivity.this,"Please enter group name");
                }else{
                    createGroup();
                }
            }
        }
    }

    void createGroup(){
        Group message = new Group();
        message.setGroupName(editGroupName.getText().toString().trim());
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    receiveAllUsers("");
                    mAdapter = new GroupListAdapter(GroupListActivity.this, group);
                    mRecyclerView.setAdapter(mAdapter);
                }

            }
        });
    }

}
