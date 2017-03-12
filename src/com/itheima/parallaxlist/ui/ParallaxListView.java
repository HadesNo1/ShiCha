package com.itheima.parallaxlist.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ListView;

import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

/**
 * 有视差特效的ListView
 * @author poplar
 *
 */
public class ParallaxListView extends ListView {

	private ImageView imageView;
	private int drawableHeight; // 图片高度
	private int originalheight; // 原始高度

	public ParallaxListView(Context context) {
		super(context);
	}

	public ParallaxListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ParallaxListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setParallaxImage(ImageView imageView){
		this.imageView = imageView;
		
		Drawable drawable = imageView.getDrawable();
		drawableHeight = drawable.getIntrinsicHeight();
		
		System.out.println("drawableHeight: " + drawableHeight);
		
		originalheight = imageView.getHeight();
		System.out.println("getHeight: " + originalheight);
		System.out.println("getMeasuredHeight: " + imageView.getMeasuredHeight());
	}
	
	/**
	 * 当View滑动到边缘时, 继续拖动, 此方法会被调用
	 */
	@Override
	protected boolean overScrollBy(
			int deltaX, int deltaY, 
			int scrollX, int scrollY, 
			int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		
		System.out.println("deltaY: " + deltaY + " scrollY: " + scrollY
				+ " scrollRangeY: " + scrollRangeY 
				+ " maxOverScrollY: " + maxOverScrollY 
				+ " isTouchEvent: " + isTouchEvent);
		// deltaY: 竖直方向的瞬时偏移量 , 顶部下拉 -, 底部上拉+
		// scrollY: 竖直方向滚动距离, 顶部滚动 -, 底部滚动+
		// scrollRangeY:  竖直方向滚动范围位置
		// maxOverScrollY: 竖直方向最大滚动距离
		// isTouchEvent: 是否是手指触摸到控件边缘, true触摸, false惯性
		
		// 将手指下拉的瞬时!偏移量!绝对值, 累加给头布局
		if(deltaY < 0 && isTouchEvent){
			// 计算得到新的高度
			int newHeight = (int) (imageView.getHeight() + Math.abs(deltaY / 3.0f));
			
			if(newHeight <= drawableHeight){
				// 让新的高度生效
				imageView.getLayoutParams().height = newHeight;
				imageView.requestLayout();
			}
		}
		
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
				scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		if(ev.getAction() == MotionEvent.ACTION_UP){
			// 手指抬起, 弹回
			int startHeight = imageView.getHeight();
			int endHeight = originalheight;
			
			// 172 -> 171 , 169, 168 -> 160
			ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
			animator.addUpdateListener(new AnimatorUpdateListener() {
				
				@Override
				public void onAnimationUpdate(ValueAnimator anim) {
					// 0.0 -> 1.0
					float fraction = anim.getAnimatedFraction();
					// 获取动画执行过程中模拟得到的数值
					Integer newHeight = (Integer) anim.getAnimatedValue();
					
					System.out.println("fraction: " + fraction + " value: " + newHeight);
					
					// 让新的高度生效
					imageView.getLayoutParams().height = newHeight;
					imageView.requestLayout();
					
				}
			});
			animator.setInterpolator(new OvershootInterpolator(4));
			animator.setDuration(300);
			animator.start();
			
		}
		
		return super.onTouchEvent(ev);
	}

}
