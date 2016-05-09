package com.pktworld.chatdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pktworld.chatdemo.R;
import com.pktworld.chatdemo.imageutils.ImageLoader;
import com.pktworld.chatdemo.model.Message;
import com.pktworld.chatdemo.util.CircularImage;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;


/**
 * Created by ubuntu1 on 15/3/16.
 */
public class OneToOneChatListAdapter extends RecyclerView.Adapter<OneToOneChatListAdapter.DataObjectHolder> {

    private static final String TAG = OneToOneChatListAdapter.class.getSimpleName();
    private List<Message> mDataset;
    private Context mContext;
    private ImageLoader imageLoader;
    private String mUserId;


    public OneToOneChatListAdapter(Context context, String userId, List<Message> myDataset) {
        mDataset = myDataset;
        mContext = context;
        mUserId = userId;
        imageLoader =  ImageLoader.getInstance(mContext);
        //imageLoader = CustomVolleyRequestQueue.getInstance(mContext).getImageLoader();


    }

    // Create a gravatar image based on the hash value obtained from userId
    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat1, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {
        final boolean isMe = mDataset.get(position).getUserId().equals(mUserId);
        if (isMe) {
            holder.imageRight.setVisibility(View.VISIBLE);
            holder.imageLeft.setVisibility(View.GONE);

            //holder.txtBody.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            holder.txtBody.setTextColor(mContext.getResources().getColor(R.color.smoke_white));
            holder.llBody.setGravity(Gravity.CENTER_VERTICAL |Gravity.RIGHT);

        } else {
            holder.imageLeft.setVisibility(View.VISIBLE);
            holder.imageRight.setVisibility(View.GONE);

            holder.txtBody.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            holder.txtBody.setTextColor(mContext.getResources().getColor(R.color.cardview_dark_background));
            holder.llBody.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);


        }
        final CircularImage profileView = isMe ? holder.imageRight : holder.imageLeft;
        try {
            if (mDataset.get(position).getProfileImage().length() != 0) {

                loadImage(mDataset.get(position).getProfileImage(), profileView, mContext);
            }else {

                loadImage(getProfileUrl(mDataset.get(position).getUserId()), profileView,mContext);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        holder.txtBody.setText(mDataset.get(position).getBody());
        //holder.txtUserName.setText(mDataset.get(position).getUserName() + " at " + mDataset.get(position).getDateTime());
        profileView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               /* String oId = mDataset.get(position).getUserId();
                Intent i = new Intent(mContext, UserProfileActivity.class);
                i.putExtra(ApplicationConstant.FLAG,oId);
                mContext.startActivity(i);*/
            }
        });
    }

    public void addItem(Message dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void loadImage( String url,CircularImage imageView, Context context) {
        //Ion.with(context).load(url).intoImageView(imageView);
        imageLoader.DisplayImage(url,imageView);
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {

        TextView txtBody;
        CircularImage imageLeft, imageRight;
        LinearLayout llBody;

        public DataObjectHolder(View itemView) {
            super(itemView);
            imageLeft = (CircularImage) itemView.findViewById(R.id.ivProfileLeft);
            imageRight = (CircularImage) itemView.findViewById(R.id.ivProfileRight);
            txtBody = (TextView) itemView.findViewById(R.id.txtBody);
            llBody = (LinearLayout) itemView.findViewById(R.id.llText);

            //txtUserName = (TextView) itemView.findViewById(R.id.userName);
            //llBody = (LinearLayout)itemView.findViewById(R.id.llBody);
        }

    }
}
