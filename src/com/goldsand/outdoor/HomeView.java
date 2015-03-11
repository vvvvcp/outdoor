package com.goldsand.outdoor;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class HomeView extends Activity implements OnClickListener{
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outdoor);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        
    }
    
}
