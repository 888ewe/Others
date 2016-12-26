package com.example.administrator.swipelist;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/4/14.
 */
public class Main extends Activity {
    private SwipeListView mSwipeListView ;
    private ListAd mAdapter ;
    public static int deviceWidth ;
    private List<String> testData ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwipeListView = (SwipeListView) findViewById(R.id.example_lv_list);
        testData = getTestData();
        //数据适配
        mAdapter = new ListAd(this, R.layout.item, testData,mSwipeListView);
        //拿到设备宽度
        deviceWidth = getDeviceWidth();
        mSwipeListView.setAdapter(mAdapter);
        //设置事件监听
        mSwipeListView.setSwipeListViewListener( new TestBaseSwipeListViewListener());
        reload();
    }

    private List<String> getTestData() {
        String [] obj = new String[]{"红楼梦","西游记","水浒传","管锥编","宋诗选注","三国演义","android开发高级编程","红楼梦","西游记","水浒传","管锥编","宋诗选注","三国演义","android开发高级编程"};
        List<String> list = new ArrayList<String>(Arrays.asList(obj));
        return list;
    }

    private int getDeviceWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    private void reload() {
//      mSwipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
//      mSwipeListView.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
//      mSwipeListView.setSwipeActionRight(settings.getSwipeActionRight());
        //滑动时向左偏移量，根据设备的大小来决定偏移量的大小
        mSwipeListView.setOffsetLeft(deviceWidth * 1 / 3);
        mSwipeListView.setOffsetRight(deviceWidth * 1 / 3);
//      mSwipeListView.setOffsetRight(convertDpToPixel(settings.getSwipeOffsetRight()));
        //设置动画时间
        mSwipeListView.setAnimationTime(30);
        mSwipeListView.setSwipeOpenOnLongPress(false);
    }

    class TestBaseSwipeListViewListener extends BaseSwipeListViewListener{

        //点击每一项的响应事件
        @Override
        public void onClickFrontView(int position) {
            super.onClickFrontView(position);
            Toast.makeText(getApplicationContext(), testData.get(position), Toast.LENGTH_SHORT).show();
        }

        //关闭事件
        @Override
        public void onDismiss(int[] reverseSortedPositions) {
            for (int position : reverseSortedPositions) {
                Log.i("lenve", "position--:"+position);
                testData.remove(position);
            }
            mAdapter.notifyDataSetChanged();
        }
    }
}