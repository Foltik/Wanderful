[1mdiff --git a/android/.idea/caches/build_file_checksums.ser b/android/.idea/caches/build_file_checksums.ser[m
[1mindex 33088f0..4e931ae 100644[m
Binary files a/android/.idea/caches/build_file_checksums.ser and b/android/.idea/caches/build_file_checksums.ser differ
[1mdiff --git a/android/app/src/main/java/com/example/smax/hackprinceton/Exchange.java b/android/app/src/main/java/com/example/smax/hackprinceton/Exchange.java[m
[1mindex bfb6d5e..5763982 100644[m
[1m--- a/android/app/src/main/java/com/example/smax/hackprinceton/Exchange.java[m
[1m+++ b/android/app/src/main/java/com/example/smax/hackprinceton/Exchange.java[m
[36m@@ -12,8 +12,11 @@[m [mimport android.support.v7.app.AppCompatActivity;[m
 import android.os.Bundle;[m
 import android.support.v4.app.ActivityCompat;[m
 [m
[32m+[m[32mimport com.example.smax.hackprinceton.util.api.APICall;[m
[32m+[m
 import org.json.JSONObject;[m
 import org.json.JSONTokener;[m
[32m+[m[32mimport org.w3c.dom.Text;[m
 [m
 import java.io.BufferedReader;[m
 import java.io.IOException;[m
[36m@@ -27,8 +30,10 @@[m [mpublic class Exchange extends AppCompatActivity{[m
     private double exchangeRate;[m
     private TextView exchangedSum;[m
     private Double numDollars;[m
[32m+[m[32m    private TextView exchangeRateDisplay;[m
     private EditText usdSum;[m
     private Double exchangedAmount;[m
[32m+[m[32m    private String countryCode;[m
     @Override[m
     protected void onCreate(Bundle savedInstanceState){[m
         super.onCreate(savedInstanceState);[m
[36m@@ -36,33 +41,41 @@[m [mpublic class Exchange extends AppCompatActivity{[m
         setContentView(R.layout.activity_exchange);[m
         exchange = (Button)findViewById(R.id.currencyChange);[m
         exchange.setOnClickListener(new exchangeClick());[m
[32m+[m[32m        exchangeRateDisplay = (TextView)findViewById(R.id.exchangeRateDisplay);[m
         usdSum = (EditText)findViewById(R.id.usdText);[m
[31m-        exchangedSum = (TextView)findViewById(R.id.currencyDisplay);[m
[32m+[m[32m        exchangedSum = (TextView)findViewById(R.id.exchangeSum);[m
         //numDollars = Double.parseDouble(usdSum.getText().toString());[m
[31m-        String countryCode = intent.getStringExtra("COUNTRY_CODE");//from HomePage[m
[32m+[m[32m        countryCode = intent.getStringExtra("COUNTRY_CODE");//from HomePage[m
 [m
[31m-        //API Call to retrieve latest exchange rate[m
[31m-        new stdlibAPICall("/exchange", new stdlibAPICallback() {[m
[31m-            @Override[m
[31m-            public void onComplete(JSONObject result) {[m
[31m-                try {[m
[31m-                    exchangeRate = result.getDouble("result");[m
[31m-                }catch(Exception e){[m
[31m-                    Log.e("error",e.toString());[m
[31m-                }[m
[32m+[m[32m        new APICall("/exchange", result -> {[m
[32m+[m[32m            exchangeRate = result.getDouble("result");[m
[32m+[m[32m            exchangeRateDisplay.setText("Exchange Rate from USD to "+ countryCode + " is " +exchangeRate);[m
[32m+[m[32m        })[m
[32m+[m[32m            .param("from", "USD")[m
[32m+[m[32m            .param("to", countryCode).execute();[m
[32m+[m[32m    }[m
[32m+[m[32m        new APICall("/currency",result -> {[m
[32m+[m[41m            [m
 [m
[31m-            }[m
[31m-        }).add("from","USD").add("to",countryCode).execute();[m
     }[m
     class exchangeClick implements View.OnClickListener {[m
         @Override[m
         public void onClick(View v) {[m
[31m-        exchangedAmount = exchangeRate * numDollars;[m
[31m-        exchangedSum.setText(""+exchangedAmount);[m
[32m+[m[32m            try {[m
[32m+[m[32m                numDollars = Double.parseDouble(usdSum.getText().toString());[m
[32m+[m[32m            } catch (Exception e) {[m
[32m+[m[32m                Log.e("ALEXA PLAY DESPACITO", e.toString());[m
[32m+[m[32m            }[m
[32m+[m[32m            if (numDollars > 0) {[m
[32m+[m[32m                exchangedAmount = exchangeRate * numDollars;[m
[32m+[m[32m                String formattedSum = String.format("%.2f", exchangedAmount);[m
[32m+[m[32m                exchangedSum.setText("Amount in " + countryCode + " is " + formattedSum);[m
[32m+[m[32m            }[m
[32m+[m[32m        }[m
 [m
 [m
         }[m
     }[m
 [m
[31m-}[m
[41m+[m
 [m
[1mdiff --git a/android/app/src/main/java/com/example/smax/hackprinceton/Itinerary.java b/android/app/src/main/java/com/example/smax/hackprinceton/Itinerary.java[m
[1mindex 7b561c1..76f80f1 100644[m
[1m--- a/android/app/src/main/java/com/example/smax/hackprinceton/Itinerary.java[m
[1m+++ b/android/app/src/main/java/com/example/smax/hackprinceton/Itinerary.java[m
[36m@@ -26,7 +26,7 @@[m [mpublic class Itinerary extends AppCompatActivity {[m
         String countryCode = intent.getStringExtra("COUNTRY_CODE");//from HomePage[m
         linearLayout = findViewById(R.id.lnrDynamicTextHolder);[m
         numPlaces = findViewById(R.id.edtNoCreate);[m
[31m-        genItin = (Button)findViewById(R.id.btnCreate);[m
[32m+[m[32m        genItin = (Button)findViewById(R.id.addBtn);[m
         addPl = (Button)findViewById(R.id.addBtn);[m
         addPl.setOnClickListener(new addPlace());[m
         genItin.setOnClickListener(new generateItinerary());[m
[1mdiff --git a/android/app/src/main/java/com/example/smax/hackprinceton/Translator.java b/android/app/src/main/java/com/example/smax/hackprinceton/Translator.java[m
[1mindex a500e7b..da387bb 100644[m
[1m--- a/android/app/src/main/java/com/example/smax/hackprinceton/Translator.java[m
[1m+++ b/android/app/src/main/java/com/example/smax/hackprinceton/Translator.java[m
[36m@@ -5,14 +5,15 @@[m [mimport android.support.v7.app.AppCompatActivity;[m
 import android.util.Log;[m
 import android.view.View;[m
 import android.widget.Button;[m
[32m+[m[32mimport android.widget.ImageButton;[m
 import android.widget.TextView;[m
 import android.widget.Toast;[m
[31m-[m
[32m+[m[32m/*[m
 import com.google.cloud.translate.Translate;[m
 import com.google.cloud.translate.Translate.TranslateOption;[m
 import com.google.cloud.translate.TranslateOptions;[m
 import com.google.cloud.translate.Translation;[m
[31m-[m
[32m+[m[32m*/[m
 import org.json.JSONException;[m
 import org.json.JSONObject;[m
 import org.json.JSONTokener;[m
[36m@@ -35,7 +36,7 @@[m [mpublic class Translator extends AppCompatActivity {[m
         super.onCreate(savedInstanceState);[m
         // Instantiates a client[m
         setContentView(R.layout.activity_translator);[m
[31m-        Button button = findViewById(R.id.saveButton);[m
[32m+[m[32m        ImageButton button = findViewById(R.id.saveButton);[m
         final TextView userText = findViewById(R.id.userText);[m
         final TextView translateText = findViewById(R.id.translateText);[m
         final TextView phrasesText = findViewById(R.id.phrasesText);[m
[36m@@ -53,7 +54,7 @@[m [mpublic class Translator extends AppCompatActivity {[m
                     BufferedReader br = new BufferedReader(isr);[m
                     StringBuilder sb = new StringBuilder();[m
                     String readText;[m
[31m-                    while ((readText = br.readLine()) != null{[m
[32m+[m[32m                    while ((readText = br.readLine()) != null){[m
                         sb.append(readText).append("\n");[m
                     }[m
 [m
[36m@@ -103,7 +104,7 @@[m [mpublic class Translator extends AppCompatActivity {[m
             while ((temp = bReader.readLine()) != null) {[m
                 response += temp;[m
             }[m
[31m-            textView.setText(response);[m
[32m+[m[32m            translateText.setText(response);[m
             object = (JSONObject) new JSONTokener(response).nextValue();[m
         } catch (Exception e) {[m
             Log.e("hackprinceton",e.toString());[m
[36m@@ -130,15 +131,15 @@[m [mpublic class Translator extends AppCompatActivity {[m
 [m
 [m
             if (lang != null) {[m
[31m-                textView.setText(lang);[m
[32m+[m[32m                translateText.setText(lang);[m
 [m
[31m-                new RunTranslation().execute("Hello, world!", "en", lang);[m
[32m+[m[32m              //  new RunTranslation().execute("Hello, world!", "en", lang);[m
 [m
             }[m
 [m
         }[m
     }[m
[31m-[m
[32m+[m[32m/*[m
     public class RunTranslation extends AsyncTask<String, Void, String> {[m
         @Override[m
         protected String doInBackground(String... params) {[m
[36m@@ -169,5 +170,6 @@[m [mpublic class Translator extends AppCompatActivity {[m
     public void savePhrase(View view) {[m
 [m
     }[m
[32m+[m[32m    */[m
 }[m
 [m
[1mdiff --git a/android/app/src/main/java/com/example/smax/hackprinceton/util/api/APICall.java b/android/app/src/main/java/com/example/smax/hackprinceton/util/api/APICall.java[m
[1mindex 26656fd..c95e0c6 100644[m
[1m--- a/android/app/src/main/java/com/example/smax/hackprinceton/util/api/APICall.java[m
[1m+++ b/android/app/src/main/java/com/example/smax/hackprinceton/util/api/APICall.java[m
[36m@@ -40,6 +40,7 @@[m [mpublic class APICall {[m
     }[m
 [m
     public void execute() {[m
[32m+[m[32m        Log.e("thing", query.toString());[m
         new AsyncCall(this).execute(query.toString());[m
     }[m
 [m
[1mdiff --git a/android/app/src/main/res/layout/activity_exchange.xml b/android/app/src/main/res/layout/activity_exchange.xml[m
[1mindex d4bf3d0..9431a52 100644[m
[1m--- a/android/app/src/main/res/layout/activity_exchange.xml[m
[1m+++ b/android/app/src/main/res/layout/activity_exchange.xml[m
[36m@@ -7,39 +7,55 @@[m
     android:layout_height="match_parent">[m
 [m
     <TextView[m
[31m-        android:id="@+id/currencyDisplay"[m
[31m-        android:layout_width="101dp"[m
[31m-        android:layout_height="37dp"[m
[32m+[m[32m        android:id="@+id/exchangeSum"[m
[32m+[m[32m        android:layout_width="match_parent"[m
[32m+[m[32m        android:layout_height="wrap_content"[m
[32m+[m[32m        android:layout_marginStart="8dp"[m
[32m+[m[32m        android:layout_marginEnd="8dp"[m
[32m+[m[32m        android:layout_marginBottom="32dp"[m
[32m+[m[32m        android:text="[Value of Exchanged Currency]"[m
[32m+[m[32m        android:textSize="10pt"[m
[32m+[m[32m        app:layout_constraintBottom_toTopOf="@+id/usdText"[m
[32m+[m[32m        app:layout_constraintEnd_toEndOf="parent"[m
[32m+[m[32m        app:layout_constraintStart_toStartOf="parent" />[m
[32m+[m
[32m+[m[32m    <TextView[m
[32m+[m[32m        android:id="@+id/exchangeRateDisplay"[m
[32m+[m[32m        android:layout_width="match_parent"[m
[32m+[m[32m        android:layout_height="wrap_content"[m
         android:layout_marginStart="8dp"[m
         android:layout_marginTop="8dp"[m
[32m+[m[32m        android:textSize="10pt"[m
         android:layout_marginEnd="8dp"[m
         android:layout_marginBottom="8dp"[m
[31m-        android:text="Amount in exchanged currency"[m
[31m-        app:layout_constraintBottom_toTopOf="@+id/usdText"[m
[32m+[m[32m        android:text="Exchange Rate"[m
[32m+[m[32m        app:layout_constraintBottom_toTopOf="@+id/exchangeSum"[m
         app:layout_constraintEnd_toEndOf="parent"[m
         app:layout_constraintStart_toStartOf="parent"[m
[31m-        app:layout_constraintTop_toTopOf="parent" />[m
[32m+[m[32m        app:layout_constraintTop_toTopOf="parent"[m
[32m+[m[32m        app:layout_constraintVertical_bias="0.4" />[m
 [m
     <EditText[m
         android:id="@+id/usdText"[m
[31m-        android:layout_width="1.5in"[m
[31m-        android:layout_height="0.25in"[m
[32m+[m[32m        android:layout_width="match_parent"[m
[32m+[m[32m        android:layout_height="wrap_content"[m
         android:layout_marginStart="8dp"[m
         android:layout_marginTop="8dp"[m
         android:layout_marginEnd="8dp"[m
         android:layout_marginBottom="8dp"[m
[31m-        android:inputType="number"[m
         android:hint="Amount in USD ($)"[m
[32m+[m[32m        android:inputType="number"[m
         app:layout_constraintBottom_toBottomOf="parent"[m
         app:layout_constraintEnd_toEndOf="parent"[m
[32m+[m[32m        app:layout_constraintHorizontal_bias="0.502"[m
         app:layout_constraintStart_toStartOf="parent"[m
         app:layout_constraintTop_toTopOf="parent"[m
[31m-        app:layout_constraintVertical_bias="0.347" />[m
[32m+[m[32m        app:layout_constraintVertical_bias="0.458" />[m
 [m
     <Button[m
         android:id="@+id/currencyChange"[m
[31m-        android:layout_width="0.75in"[m
[31m-        android:layout_height="0.25in"[m
[32m+[m[32m        android:layout_width="match_parent"[m
[32m+[m[32m        android:layout_height="0.5in"[m
         android:layout_marginStart="8dp"[m
         android:layout_marginEnd="8dp"[m
         android:layout_marginBottom="8dp"[m
[1mdiff --git a/android/app/src/main/res/layout/activity_itinerary.xml b/android/app/src/main/res/layout/activity_itinerary.xml[m
[1mindex b9f210c..c931de2 100644[m
[1m--- a/android/app/src/main/res/layout/activity_itinerary.xml[m
[1m+++ b/android/app/src/main/res/layout/activity_itinerary.xml[m
[36m@@ -18,10 +18,10 @@[m
             android:inputType="number"/>[m
 [m
         <Button[m
[31m-            android:id="@+id/btnCreate"[m
[32m+[m[32m            android:id="@+id/btnAdd"[m
             android:layout_width="wrap_content"[m
             android:layout_height="wrap_content"[m
[31m-            android:text="Create"/>[m
[32m+[m[32m            android:text="Add New"/>[m
         <Button[m
             android:id="@+id/addBtn"[m
             android:layout_width="wrap_content"[m
