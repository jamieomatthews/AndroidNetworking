package com.example.android.network.kit;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{
	ProgressDialog progress;
	Context context;
	Button search;
	ListView lv;
	TweetAdapter adapter;
	ArrayList<TStatus> statuses;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        progress= new ProgressDialog(context);
        
        search = (Button)findViewById(R.id.search);
        search.setOnClickListener(this);
        statuses = new ArrayList<TStatus>();
        
        lv = (ListView) findViewById(R.id.list);
    }

    @Override
	public void onClick(View arg0) {
    	//Generate a get request to the following URL
    	String url = "/1/statuses/user_timeline.json";
    	List<NameValuePair> pairs = new ArrayList<NameValuePair>(1);
		pairs.add(new BasicNameValuePair("screen_name", "Munchful")); //set a get param called screen_name, set to "Munchful"
		progress.setTitle("Loading Tweets");
    	GetTweets getter = new GetTweets(url, "GET", pairs, "tweets", asyncHandler, context);
    	getter.execute();
	}  
    
    public class GetTweets extends ANTask{
    	Status status;
		public GetTweets(String URL, String action, List<NameValuePair> params,
				String type, Handler mainUIHandler, Context context) {
			super(URL, action, params, type, mainUIHandler, context);
		}

		@Override
		protected Object decode(String jsondata) throws JSONException {
			Log.d("JSON", jsondata);
			//JSONObject object = new JSONObject(jsondata);
			JSONArray jtweets = new JSONArray(jsondata);
			ArrayList<TStatus> statuses = new ArrayList<TStatus>();
			for(int i = 0; i < jtweets.length(); i++){
				//JSONArray userarray = (JSONArray)jtweets.get(i);
				//JSONObject ob = userarray.getJSONObject(0);//get the first user
				JSONObject ob = jtweets.getJSONObject(i);
				TStatus status = new TStatus();
				status.text = ob.getString("text");
				status.date = ob.getString("created_at");
				JSONObject juser = ob.getJSONObject("user");
				User user = new User();
				user.name = juser.getString("name");
				user.screenName= juser.getString("screen_name");
				user.userId = juser.getInt("id");
				status.user = user;
				statuses.add(status);
			}
			
			return statuses;
		}
		
	}
    ANHandler asyncHandler = new ANHandler()
    {
		@Override
		public void resultOK(Message msg) 
		{
			Bundle b = msg.getData();

			if(progress.isShowing()){
    			progress.dismiss();
    		}
			statuses = (ArrayList<TStatus>)msg.obj;
			adapter = new TweetAdapter(MainActivity.this, R.layout.tweet_cell, statuses);
	        lv.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}

		@Override
		public void resultFailed(Message msg) {
			Log.d("LocationActivity", "Handling Error Message");
			if(progress.isShowing()){
    			progress.dismiss();
    		}
		}
    };
    private class TweetAdapter extends ArrayAdapter<TStatus> 
    {
        private ArrayList<TStatus> tweets;

        
        public TweetAdapter(Context context, int textViewResourceId, ArrayList<TStatus> tweets) 
        {
                super(context, textViewResourceId, tweets);
                this.tweets = tweets;
        }
        
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.tweet_cell, null);
                }
                TStatus status = tweets.get(position);
            	
                //initialize all the text views
                TextView text = (TextView) v.findViewById(R.id.text);
                text.setText("@"+status.user.screenName + ": " + status.text);
                return v;
        }
    }
    
}
