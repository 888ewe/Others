package com.atguigu.slider;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class SliderLayout extends FrameLayout {

    private int contentWidth,menuWidth,viewHeight,leftWidth;
    private Scroller scroller;

    public SliderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);

    }

    /* 写流程   1.正常显示item代码实现
     * 1.1).得到子View对象（ContentView,MenuView）-->onFinishInflate()
     * 1.2).得到子View的宽和高-->onMeasure()
     * 1.3).对item视图进行重新布局-->onLayout
     * */
    private View contentView;//sliderLayout(FrameLayout)中的子view(第一个)
    private View menuView;//sliderLayout(FrameLayout)中的子view(第二个)
    private View leftView;//sliderLayout(FrameLayout)中的子view(第3个)

    //1.得到子view对象
    //当布局文件加载完成后回调此方法
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        menuView = getChildAt(1);
        leftView=getChildAt(2);
    }

    //得到子view的宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contentWidth = contentView.getMeasuredWidth();
        menuWidth = menuView.getMeasuredWidth();
        leftWidth = leftView.getMeasuredWidth();
        viewHeight = getMeasuredHeight();//item,sliderLayout的两个子view的高相同
    }

    //对view重新布局
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        menuView.layout(contentWidth, 0, contentWidth + menuWidth, viewHeight);
        leftView.layout(-leftWidth, 0,0, viewHeight);
    }
    /*
    * 解决item滑动后不能自动打开和关闭
    * 原因:在listview中item被拦截,父层view拦截子view的事件
    * 1.计算滑动的方向,如果是水平方向滑动,反拦截把事件给当前控件DX > DY >8
    * 反拦截,其他就不管
    *
    * 内容视图设置点击事件时不能滑动item
    * 分析原因:事件被点击TextView事件消费,拦截
    */

    //第一次按下的值
    private float startX;
    private float startY;
    //down下的x,y
    private float downX;
    private float downY;

    //3_通过手势拖动打开或者关闭menu ,实现左右滑动
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录起始坐标
                downX = startX = event.getX();
                downY = startY = event.getY();

                if(onStateChangeListener != null) {
                    onStateChangeListener.onDown(this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //2.来到新的坐标
                float endX =  event.getX();
                float endY =  event.getY();
                //计算偏移量
                float distanceX = endX - startX;//eventX在down事件中坐标改变
                //转换为scrollTo识别的坐标
                int toScrollX = (int) (getScrollX() - distanceX);//getScrollX起始坐标
                //屏蔽非法值[0,menuWidth]
                if (toScrollX < 0) {
                    toScrollX = 0;
                } else if (toScrollX > menuWidth) {
                    toScrollX = menuWidth;
                }
                //移动到某个点
                // scrollTo(toScrollX, 0);//也可以
                scrollTo(toScrollX, getScrollY());//getScrollY()这里也可以写为0

                //重新赋值(因为每次执行down,up事件lastX都不一样)
                startX  = event.getX();

                //内容视图设置点击事件时不能滑动item
                //计算在水平方向和垂直方向的距离
                float DX = Math.abs(endX - downX);//得到绝对值
                float DY = Math.abs(endY - downY);
                if(DX > DY && DX > 8) {//此事件为水平方向的移动
                    //将此事件传递给item
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_UP:
                //4_判断是平滑的打开还是关闭,当up时，判断是平滑的打开还是关闭
                int totalScrollX = getScrollX();//当前x轴坐标

                if ((totalScrollX < menuWidth / 2  &&totalScrollX>0)||(totalScrollX < leftWidth/2&&totalScrollX>0)) {
                    closeMenu();//关闭menuView
                } else if(totalScrollX > menuWidth / 2) {
                    openMenu();//打开menuView
                }else if(totalScrollX>leftWidth/2) {
                    openLeft();
                }
                break;
        }
        return true;//消费掉此事件,在当前控件中
    }

    public void openMenu() {
        //平滑的移动
        scroller.startScroll(getScrollX(), getScrollY(), menuWidth - getScrollX(), 0);
        if(onStateChangeListener != null) {
            onStateChangeListener.onOpen(this);
        }
    }
    public void openLeft() {
        //平滑的移动
        scroller.startScroll(getScrollX(), getScrollY(), leftWidth+getScrollX(), 0);
        if(onStateChangeListener != null) {
            onStateChangeListener.onOpen(this);
        }
    }

    public void closeMenu() {
        //平滑的移动
        scroller.startScroll(getScrollX(), getScrollY(), 0 - getScrollX(), 0);
        //强制重绘
        invalidate();//computeScroll
        if(onStateChangeListener != null) {
         onStateChangeListener.onClose(this);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {//返回true,表示正在移动
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();//强制重绘
        }
    }
    //拦截事件
    /*当返回true的时候，事件拦截，孩子没有事件；但是会触发当层视图的onTouchEvent()
     * 当在当前视图内容继续偏移(x , y)个单位，显示(可视)区域也跟着偏移(x,y)个单位。防护false的时候，事件不拦截，孩子有事件，但不会触发当层视图的onTouchEvent()
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean intercept = false;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN :
                //记录起始坐标
                downX = startX = ev.getX();
                downY = startY = ev.getY();
                if(onStateChangeListener != null){
                    onStateChangeListener.onDown(this);
                }
                break;
            case MotionEvent.ACTION_MOVE :
                float endX = ev.getX();
                float endY = ev.getY();

                float DX = Math.abs(endX - downX);//得到绝对值
                if(DX > 8) {//此事件为水平方向的移动
                    //将此事件传递给item
                   intercept = true;
                }

                break;
        }
        return intercept;
    }
    public interface OnStateChangeListener {
        /**
         当侧滑菜单关闭的时候被回调
         */
        public void onClose(SliderLayout layout);

        /**
         当侧滑菜单打开的时候被回调
         */
        public void onOpen(SliderLayout layout);

        /**
         当侧滑菜单按下的时候被回调
         */
        public void onDown(SliderLayout layout);

    }

    private OnStateChangeListener onStateChangeListener;

    public void setOnStateChangeListener(OnStateChangeListener l) {

        this.onStateChangeListener = l;
    }

}
