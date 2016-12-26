package com.atguigu.slider;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ListView lv_main;
    private List<MyBean> myBeans;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_main = (ListView) findViewById(R.id.lv_main);
        myBeans = new ArrayList<>();
        for (int i = 0; i < 100; i++) {

            myBeans.add(new MyBean("content" + i));
        }
        adapter = new MyAdapter();
        lv_main.setAdapter(adapter);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return myBeans.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item_main, null);
                viewHolder = new ViewHolder();
                viewHolder.item_content = (TextView) convertView.findViewById(R.id.item_content);
                viewHolder.item_menu = (TextView) convertView.findViewById(R.id.item_menu);
                viewHolder.item_left = (TextView) convertView.findViewById(R.id.item_left);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final MyBean myBean = myBeans.get(position);
            viewHolder.item_content.setText(myBean.getName());

            viewHolder.item_content.setTag(position);

            viewHolder.item_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    MyBean bean = myBeans.get(position);
                    Toast.makeText(MainActivity.this, "bean==" + bean.getName(), Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.item_menu.setTag(position);

            viewHolder.item_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SliderLayout layout = (SliderLayout) v.getParent();
                    layout.closeMenu();//关闭Menu
                    //int position = (int) v.getTag();
                    myBeans.remove(myBean);
                    adapter.notifyDataSetChanged();//更新列表
                }
            });

            viewHolder.item_left.setTag(position);
            viewHolder.item_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SliderLayout layout = (SliderLayout) v.getParent();
                    layout.closeMenu();//关闭Menu
                    int position = (int) v.getTag();
                    MyBean bean = myBeans.get(position);
                    Toast.makeText(MainActivity.this, "Left", Toast.LENGTH_SHORT).show();
                }
            });
            //设置SliderLayout(自定义接口)的监听
            SliderLayout layout = (SliderLayout) convertView;
            layout.setOnStateChangeListener(new SliderLayout.OnStateChangeListener() {
                @Override
                public void onClose(SliderLayout layout) {
                     if(sliderLayout == layout) {
                         sliderLayout = null;
                     }
                }

                @Override
                public void onOpen(SliderLayout layout) {
                    sliderLayout = layout;
                }

                @Override
                public void onDown(SliderLayout layout) {
                    if(sliderLayout != null && sliderLayout != layout) {
                        sliderLayout.closeMenu();
                    }
                }
            });
            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }
    private SliderLayout sliderLayout;

    static class ViewHolder {
        TextView item_content;
        TextView item_left;
        TextView item_menu;
    }
}
