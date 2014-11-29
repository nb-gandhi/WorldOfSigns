package com.example.db_connect_blog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.wos.versionOne.R;

public class MainActivity extends Activity implements OnClickListener{
	Button fetch;
	TextView text1;
	EditText et;
	boolean wordFlag = true;
	boolean nullFlag = true;
	Intent intent;
	boolean flag = true;
	public Set<String> wordsList = new HashSet<String>(Arrays.asList(
		     new String[] {"what","is","your","name","birthday","brother", "cook", "color", "couch", "cousin", "cup", "cupcake"
		 			, "curtain", "deaf", "drive", "drunk", "earn", "email", "eyes", "finish", "favorite", "homework", "house", "indian"
					, "interview", "keep", "money", "accept", "address", "afraid", "again", "agree", "and", "answer", "aunt", "awesome", "terrible"
					, "awful", "bachelor", "bathroom", "believe", "between", "blanket", "blue", "bookstore", "boy", "cat", "college", "cookie"
					, "excuse", "girl", "grandma", "grandpa", "house", "hurt", "milk", "red", "school", "shoes", "sleep", "stepsister", "tall"
					, "uncle", "where", "what", "which", "who", "why", "are"}
		));
	
	public static ArrayList<Integer> cnt = new ArrayList<Integer>();
	public static String[] arr;
	String db_detail="";
	 Handler hand = new Handler();
	int dbcount = 0;
	boolean dbflag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		intent = getIntent();
		fetch= (Button) findViewById(R.id.fetch);
		text1 = (TextView) findViewById(R.id.text1);
		et = (EditText) findViewById(R.id.et);
		
		fetch.setOnClickListener(this);
	}
	
	class task extends AsyncTask<String, String, Void>
	{
		private ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
	    	InputStream is = null ;
		    String result = "";
		    protected void onPreExecute() {
		       progressDialog.setMessage("Fetching Data...");
		       progressDialog.show();
		       progressDialog.setOnCancelListener(new OnCancelListener() {
			@Override
				public void onCancel(DialogInterface arg0) {
				task.this.cancel(true);
			   }
			});
	     }
	       @Override
		protected Void doInBackground(String... params) {
		  String url_select = "http://nrnmc.byethost12.com/speak.php";

		  HttpClient httpClient = new DefaultHttpClient();
		  HttpPost httpPost = new HttpPost(url_select);

	          ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

		    try {
			httpPost.setEntity(new UrlEncodedFormEntity(param));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();

			//read content
			is =  httpEntity.getContent();					

			} catch (Exception e) {

			Log.e("log_tag", "Error in http connection "+e.toString());
			//Toast.makeText(MainActivity.this, "Please Try Again", Toast.LENGTH_LONG).show();
			}
		try {
		    BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = "";
			while((line=br.readLine())!=null)
			{
			   sb.append(line+"\n");
			}
				is.close();
				result=sb.toString();				

					} catch (Exception e) {
						// TODO: handle exception
						Log.e("log_tag", "Error converting result "+e.toString());
					}

				return null;

			}
		protected void onPostExecute(Void v) {
			nullFlag = true;
			arr = et.getText().toString().split("\\s+");
			int len = arr.length;
			System.out.println("CHECK LEN: "+len);
			if (len == 0) {
				nullFlag = false;
//				Toast t = Toast.makeText(getApplicationContext(), "Input can not be blank! Please enter some string.", Toast.LENGTH_LONG);
//        		t.show();
			}
				try {
					JSONArray Jarray = new JSONArray(result);
					for(int j=0; j<len; j++)
					{
						for(int i=0;i<Jarray.length();i++)
						{
							dbcount +=1;
							dbflag = true;
							wordFlag = true;
							JSONObject Jasonobject = null;
							//text_1 = (TextView)findViewById(R.id.txt1);
							Jasonobject = Jarray.getJSONObject(i);
			
							//get an output on the screen
							//String id = Jasonobject.getString("id");
							String name = Jasonobject.getString("name");
							
							int length = arr[j].length();
							String check = arr[j].substring(length-2, length-1);
							if(check.equals("'")){
								arr[j]=arr[j].substring(0,length-2);
								System.out.println(arr[j]);
							}
							length = arr[j].length();
							check = arr[j].substring(length-1, length);
							if(check.equals("?")){
								arr[j] = arr[j].substring(0, length-1);
							}
							
							boolean x = arr[j].toString().equalsIgnoreCase(name);
							System.out.println("CHECK: "+x+"NAME: "+arr[j].toString()+"--");
							
							if(arr[j].toString().equalsIgnoreCase(name)) {
								flag = true;
								arr[j]=name;
							    cnt.add(j,Integer.parseInt(Jasonobject.getString("count")));
							    db_detail = db_detail+"--"+cnt.get(j);
							    System.out.println(arr[j]+"--"+cnt.get(j));
								dbcount = 0;
								break;
							}
							System.out.println("Length: "+Jarray.length());
							if (dbcount == Jarray.length()) {
								dbcount = 0;
								dbflag = false;
								break;
							}
						}
						if (dbflag == false || wordsList.contains(arr[j].toString())==false) {
							wordFlag = false;
							Toast t = Toast.makeText(getApplicationContext(), "Invalid String! Enter again.", Toast.LENGTH_LONG);
			        		t.show();
			        		et.setText("");
						}
					}
					
					text1.setText("Processing your request.. Please wait!");
					db_detail = "";
					this.progressDialog.dismiss();
					if (dbflag == false || wordFlag == false) {
					text1.setText(""); }
	
				} catch (Exception e) {
					// TODO: handle exception
					Log.e("log_tag", "Error parsing data "+e.toString());
				}
			}
	}

	@Override
	public void onClick(View v) {
		nullFlag = true;
		if(et.getText().toString().equals("")){
			nullFlag = false;
			Toast t = Toast.makeText(getApplicationContext(), "Input can not be blank! Please enter some string.", Toast.LENGTH_LONG);
    		t.show();
			
		}
		if(nullFlag) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
		case R.id.fetch : new task().execute();		
		break;
		}
			try{
				hand.postDelayed(run, 6000);
			}
			catch(Exception e)
			{
				System.out.print(e);
			}
		}
	}
	
	
	Runnable run = new Runnable() {
        @Override
        public void run() {
        		if (dbflag) {
        			Intent i = new Intent (MainActivity.this, OneActivity.class);
        			startActivity(i);
        		}
            }
        };
}
