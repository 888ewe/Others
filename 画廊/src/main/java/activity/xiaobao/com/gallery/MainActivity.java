package activity.xiaobao.com.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

    Integer[]  mImageIds = { R.drawable.g1, R.drawable.g2, R.drawable.g3, R.drawable.g4, R.drawable.g5, R.drawable.g6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //定义UI组件
//        final ImageView iv= (ImageView)findViewById(R.id.ImageView01);
        Gallery g = (Gallery) findViewById(R.id.Gallery01);

        //设置图片匹配器
        g.setAdapter(new ImageAdapter(this));

        //图片数组
        int middle;

        if(mImageIds.length%2==0) {
           middle=  (mImageIds.length/2)+1;
        }else {
            middle=  mImageIds.length/2;
        }
        g.setSelection(middle);
        //设置AdapterView点击监听器，Gallery是AdapterView的子类
        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //显示点击的是第几张图片
                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_LONG).show();
                //设置背景部分的ImageView显示当前Item的图片
//                iv.setImageResource(((ImageView)view).getId());
            }
        });
    }

    //定义继承BaseAdapter的匹配器
    public class ImageAdapter extends BaseAdapter {

        //Item的修饰背景
        int mGalleryItemBackground;

        //上下文对象
        private Context mContext;


        //构造方法
        public ImageAdapter(Context c){
            mContext = c;
            //读取styleable资源
            TypedArray a = obtainStyledAttributes(R.styleable.HelloGallery);
            mGalleryItemBackground = a.getResourceId(
                    R.styleable.HelloGallery_android_galleryItemBackground, 0);
            a.recycle();

        }

        //返回项目数量
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        //返回项目
        @Override
        public Object getItem(int position) {
            return position;
        }

        //返回项目Id
        @Override
        public long getItemId(int position) {
            return position;
        }

        //返回视图
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

//            ImageView iv = new ImageView(mContext);
//            iv.setImageResource(mImageIds[position]);
//            //给生成的ImageView设置Id，不设置的话Id都是-1
//            iv.setId(mImageIds[position]);
//            iv.setLayoutParams(new Gallery.LayoutParams(DisplayUtil.dip2px(mContext, 300), DisplayUtil.dip2px(mContext,450)));
//            iv.setScaleType(ImageView.ScaleType.FIT_XY);
//            iv.setBackgroundResource(mGalleryItemBackground);
              /* 创建一个ImageView对象 */
            ImageView i = new ImageView(mContext);
//            i.setPadding(10, 10, 10, 10);
            i.setAlpha(100);
            // i.setImageResource(this.myImageIds[position]);
            if (position < 0) {
                position = position + mImageIds.length;
            }
            i.setImageResource(mImageIds[position % mImageIds.length]);
            i.setScaleType(ImageView.ScaleType.FIT_XY);
            i.setBackgroundResource(mGalleryItemBackground);
    /* 设置这个ImageView对象的宽高，单位为dip */
            i.setLayoutParams(new Gallery.LayoutParams(DisplayUtil.dip2px(mContext, 300), DisplayUtil.dip2px(mContext,450)));

            return i;
        }

}
}
