package com.pktworld.chatdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseUser;
import com.pktworld.chatdemo.R;
import com.pktworld.chatdemo.imageutils.ImageLoader;
import com.pktworld.chatdemo.util.ApplicationConstant;
import com.pktworld.chatdemo.util.CircularImage;

import java.util.List;

/**
 * Created by ubuntu1 on 18/3/16.
 */
public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.DataObjectHolder> {

    private static final String TAG = UsersListAdapter.class.getSimpleName();
    private List<ParseUser> mDataset;
    private Context mContext;
    private ImageLoader imageLoader;


    public UsersListAdapter(Context context, List<ParseUser> myDataset) {
        mDataset = myDataset;
        mContext = context;
        imageLoader =  ImageLoader.getInstance(mContext);
        //imageLoader = CustomVolleyRequestQueue.getInstance(mContext).getImageLoader();

    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {

        holder.txtUserName.setText(mDataset.get(position).getString("name"));
        loadUserIcon(mDataset.get(position).getString("profileImage"), holder.userIcon, mContext);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(mDataset.get(position).getObjectId(),mDataset.get(position).getString("name"));
            }
        });



    }

    private void loadUserIcon(String url,CircularImage imageView, Context context) {
        try{
            //Ion.with(context).load(url).intoImageView(imageView);
            imageLoader.DisplayImage(url,imageView);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }

    }

    public void addItem(ParseUser dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void sendMessage(String fId, String usName) {
        Log.d(TAG, "Broadcasting message");
        Intent intent = new Intent(ApplicationConstant.USER_LIST_ADAPTER);
        intent.putExtra(ApplicationConstant.FLAG1, fId);
        intent.putExtra(ApplicationConstant.FLAG2, usName);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {

        TextView txtUserName;
        CircularImage userIcon;
        CardView view;

        public DataObjectHolder(View itemView) {
            super(itemView);
            userIcon = (CircularImage) itemView.findViewById(R.id.imgUser);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            view = (CardView) itemView.findViewById(R.id.card_view);

        }

    }
}