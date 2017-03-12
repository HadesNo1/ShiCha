package com.itheima.parallaxlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.itheima.parallaxlist.ui.ParallaxListView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        final ParallaxListView plv = (ParallaxListView) findViewById(R.id.plv);
        
        View mHeaderView = View.inflate(this, R.layout.layout_list_header, null);
        // 添加头布局
        plv.addHeaderView(mHeaderView);
        
        final ImageView imageView = (ImageView) mHeaderView.findViewById(R.id.iv_image);
        
        plv.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				// 布局渲染完毕时, 被调用
				plv.setParallaxImage(imageView);
				plv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
        
        
        // 设置数据适配器
        plv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Cheeses.NAMES));
    }
    


}
