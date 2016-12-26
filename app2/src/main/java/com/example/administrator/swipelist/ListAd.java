package com.example.administrator.swipelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fortysevendeg.swipelistview.SwipeListView;

import java.util.List;

/**
 * Created by Administrator on 2016/4/14.
 */
public class ListAd extends ArrayAdapter<String> {
    private LayoutInflater mInflater ;
    private List<String> objects ;
    private SwipeListView mSwipeListView ;
    public ListAd(Context context, int textViewResourceId,List<String> objects, SwipeListView mSwipeListView) {
        super(context, textViewResourceId, objects);
        this.objects = objects ;
        this.mSwipeListView = mSwipeListView ;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null ;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item, parent, false);
            holder = new ViewHolder();
            holder.mFrontText = (TextView) convertView.findViewById(R.id.example_row_tv_title);
            holder.mBackEdit = (Button) convertView.findViewById(R.id.example_row_b_action_3);
            holder.mBackDelete = (Button) convertView.findViewById(R.id.example_row_b_action_2);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mBackDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭动画
                mSwipeListView.closeAnimate(position);
                //调用dismiss方法删除该项（这个方法在MainActivity中）
                mSwipeListView.dismiss(position);
            }
        });
        String item = getItem(position);
        holder.mFrontText.setText(item);
        return convertView;
    }
    class ViewHolder{
        TextView mFrontText ;
        Button mBackEdit,mBackDelete ;
    }
}