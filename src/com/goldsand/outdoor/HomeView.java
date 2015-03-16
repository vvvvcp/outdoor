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
    }
    public HomeView(Context context, AttributeSet attrs){
        super(context,attrs);
        
    }
    public HomeView(Context context , AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        
    }
    
    
    @Override
    protected void onDraw(Canvas canvas){
        
        
    }
}
