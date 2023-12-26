package com.qntm.qntm_survey.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.qntm.qntm_survey.Bean.AddLocationBean;
import com.qntm.qntm_survey.OnItemClickListenerData;
import com.qntm.qntm_survey.R;
import com.qntm.qntm_survey.swipereveallayout.SwipeRevealLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chau Thai on 4/8/16.
 */
public class LocationList_Adapter extends RecyclerView.Adapter {
    private List<AddLocationBean> mDataSet = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
   // private final ViewBinderHelper binderHelper = new ViewBinderHelper();

    private OnItemClickListenerData clickListener;
    public LocationList_Adapter(Context context, List<AddLocationBean> dataSet) {
        mContext = context;
        mDataSet = dataSet;
        mInflater = LayoutInflater.from(context);

        // uncomment if you want to open only one row at a time
        // binderHelper.setOpenOnlyOne(true);
    }
    public void setClickListener(OnItemClickListenerData itemClickListener) {
        this.clickListener = itemClickListener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.location_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        final ViewHolder holder = (ViewHolder) h;

        /*if (mDataSet == null || 0 > position || position >= mDataSet.size()) {
            return;
        }*/
        final AddLocationBean data = mDataSet.get(position);
        holder.textView.setText(data.LocationName);
        holder.img_planimage.setImageResource(R.drawable.sampleplan);
       // holder.img_planimage.setImageBitmap(data.PlanImage);
      /*  Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), Uri.parse(String.valueOf(data.PlanImage)));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
       /// holder.swipeLayout.setLockDrag(true);
        // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
        // put an unique string id as value, can be any string which uniquely define the data
        //binderHelper.setOpenOnlyOne(true);
       // binderHelper.lockSwipe(String.valueOf(position));
       // binderHelper.bind(holder.swipeLayout, String.valueOf(position));

        // Bind your data here
      //  holder.bind(data);
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null)
            return 0;
        return mDataSet.size();
    }

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onSaveInstanceState(Bundle)}
     */
   /* public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }*/

    /**
     * Only if you need to restore open/close state when the orientation is changed.
     * Call this method in {@link android.app.Activity#onRestoreInstanceState(Bundle)}
     */
   /* public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }*/

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SwipeRevealLayout swipeLayout;
        private View frontLayout;
        private LinearLayout ll_root;
        private TextView textView;
        private ImageView img_planimage;

        public ViewHolder(View itemView) {
            super(itemView);
          //  swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
          //  frontLayout = itemView.findViewById(R.id.front_layout);
            ll_root = itemView.findViewById(R.id.ll_root);
            img_planimage = itemView.findViewById(R.id.img_planimage);
            textView = (TextView) itemView.findViewById(R.id.txt_locationname);

         //   frontLayout.setOnClickListener(this);
            ll_root.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
               // swipeLayout.close(true);
                try {
                    clickListener.onClick(v, getAdapterPosition());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /*public void bind(final String data) {
            deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDataSet.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });

            textView.setText(data);

            frontLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String displayText = "" + data + " clicked";
                    Toast.makeText(mContext, displayText, Toast.LENGTH_SHORT).show();
                    Log.d("RecyclerAdapter", displayText);
                }
            });
        }*/
    }
}
