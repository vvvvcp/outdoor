package com.goldsand.outdoor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class HomeView extends ImageView{
    private float mDirection;
    private Drawable Home;
    
    public HomeView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        mDirection = 0.0f;
        Home = null;
    }
    public HomeView(Context context, AttributeSet attrs){
        super(context,attrs);
        mDirection = 0.0f;
        Home = null;
                
    }
    public HomeView(Context context , AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        mDirection = 0.0f;
        Home = null;
    }
    
    
    @Override
    protected void onDraw(Canvas canvas){
        if (Home == null){
               Home = getDrawable();
               Home.setBounds(0, 0, getWidth(), getHeight());
        }
        canvas.save();
        canvas.rotate(mDirection, getWidth()/2, getHeight()/2);
        Home.draw(canvas);
        canvas.restore();
    }
    public void updateDirection(float direction){
        mDirection = direction;
        invalidate();
    }
}
