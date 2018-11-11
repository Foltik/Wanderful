package com.example.smax.hackprinceton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smax.hackprinceton.util.api.APICall;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.net.URL;
import java.util.zip.Inflater;

public class Itinerary extends AppCompatActivity {
    private LinearLayout linearLayout;
    private Button genItin;
    private Button addPl;
    private String coords;
    private LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_itinerary);
        coords = intent.getStringExtra("COORDINATES");//from HomePage
        inflater = LayoutInflater.from(this);
        String jsonobjectstring = intent.getStringExtra("ENTRIES");
        JSONObject object = null;
        try {
             object = (JSONObject) new JSONTokener(jsonobjectstring).nextValue();
        }catch(Exception e){
            Log.e("JSON", e.toString());
        }
        linearLayout = findViewById(R.id.itineraryHolder);
        genItin = (Button)findViewById(R.id.addGen);
        addPl = (Button)findViewById(R.id.addIndiv);
        addPl.setOnClickListener(new addPlace());
        genItin.setOnClickListener(new generateItinerary());
        populateItinerary(object);
    }
    class generateItinerary implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ItinegenMenu.class);
            intent.putExtra("COORDINATES", coords);
            startActivity(intent);
        }
    }
    class addPlace implements  View.OnClickListener{
        @Override
        public void onClick(View v){

        }
    }
    public void populateItinerary(JSONObject object){


            try {
                JSONArray places = object.getJSONArray("result");
                for(int i = 0; i<places.length(); i++) {
                    JSONObject place = places.getJSONObject(i);
                    LinearLayout newEntry = (LinearLayout) inflater.inflate(R.layout.place_entry, null);
                    String entryName = place.getString("name");
                    TextView title = (TextView) ((GridLayout) newEntry.getChildAt(1)).getChildAt(0);
                    title.setText(entryName);
                    TextView rating = (TextView) ((GridLayout) newEntry.getChildAt(1)).getChildAt(1);
                    rating.setText(place.getString("rating"));
                    int id = View.generateViewId();
                    newEntry.setId(id);
                    //entryNameURLs.add(place.getString("photo"));
                    linearLayout.addView(newEntry);
                    new APICall("/photoid", result -> {
                        String[] query = {result.getString("result"), "" + id};
                        Log.e("result", query[0]);
                        new setImage().execute(query);
                    }).param("reference", place.getString("photo")).param("max_width", 800).execute();
                }

            } catch (Exception e) {
                Log.e("hackprinceton", e.toString());
            }

    }
    public class setImage extends AsyncTask<String, Void, Bitmap>{
        ImageView imageView;
        @Override
        protected Bitmap doInBackground(String... strings) {
            imageView = (ImageView)((LinearLayout)(findViewById(Integer.parseInt(strings[1])))).getChildAt(0);
            InputStream input = null;
            try{
                URL url = new URL(strings[0]);
                input = (InputStream)url.getContent();

            }catch(Exception e){
                Log.e("setimage",e.toString());
            }
            return BitmapFactory.decodeStream(input);
        }
        protected void onPostExecute(Bitmap image){
            /*
            for(int i = 0; i<images.size(); i++){
                LinearLayout entry = findViewById(listEntryIDs.get(i));
                ImageView current = (ImageView)entry.getChildAt(0);
                current.setImageBitmap(images.get(i));
            }*/
            imageView.setImageBitmap(image);
        }
    }
}
