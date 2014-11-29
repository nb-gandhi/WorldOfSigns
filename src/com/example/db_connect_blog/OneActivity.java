package com.example.db_connect_blog;


import java.util.ArrayList;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import com.wos.versionOne.R;

public class OneActivity extends Activity {
	
	MainActivity ma;
	private static final long GET_DATA_INTERVAL = 500;
	int index = 0,count = 0;
    ImageView img;
    int cnt ;
    String wname;
    int poll;
    
    ArrayList<Integer> image = new ArrayList<Integer>();
    Handler hand = new Handler();

	
	@SuppressWarnings({ "static-access", "unused" })
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one);
		
		for(int i=0;i<ma.arr.length;i++){
			wname= ma.arr[i];
			cnt = ma.cnt.get(i);
			for(int j =1 ; j<=cnt; j++){
				String s = "R.drawable."+wname+j;
				int im = getResources().getIdentifier(wname + j, "drawable",getPackageName());
				image.add(count,im);
				count++;
			}
			
		}
		poll = image.size();
		try{
			img = (ImageView) findViewById(R.id.img11);
			hand.postDelayed(run, GET_DATA_INTERVAL);
		}catch(Exception e)
		{
			System.out.print(e);
		}
	}
	
	
	Runnable run = new Runnable() {
        @SuppressWarnings("deprecation")
		@Override
        public void run() {
        	try{
        		
            img.setBackgroundDrawable(getResources().getDrawable(image.get(index++)));
            if (index == image.size()){
                index = 0;
                count = 0;   
            }
            poll--;
            if(poll>=0){
            	hand.postDelayed(run, GET_DATA_INTERVAL);
            }
            else{
            	 Intent i = new Intent (OneActivity.this, TwoActivity.class);
     			 startActivity(i);
            }
            
        	}catch(Exception e)
        	{
//        		Toast t = Toast.makeText(getApplicationContext(), "Invalid String! Enter again.", Toast.LENGTH_SHORT);
//        		t.show();
        	}
        }
    };
	

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.one, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
