package co.edu.udea.cmovil.gr5.yamba;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.thenewcircle.yamba.client.YambaClient;

public class StatusActivity extends Activity {
//instancias

    //prueba github 5:35
    private static String TAG = StatusActivity.class.getSimpleName();
    private Button mButtonTweet;
    private EditText mTextStatus;
    private TextView mTextCount;
    private int mDefaultColor;
    private int numCaracteres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        //inicializar
        mButtonTweet = (Button) findViewById(R.id.status_button_tweet);
        mTextStatus = (EditText) findViewById(R.id.status_text);
        mTextCount = (TextView) findViewById(R.id.status_text_count);

        mTextCount.setText(Integer.toString(140));
        mDefaultColor = mTextCount.getTextColors().getDefaultColor();

        mButtonTweet.setEnabled(false);
/*
        mButtonTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        String status = mTextStatus.getText().toString();
        PostTask postTask = new PostTask();
        postTask.execute(status);
        Log.d(TAG, "onClicked");
            }
        });
*/


        mTextStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                numCaracteres = 140 - s.length();
                cambiaContador(numCaracteres);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                numCaracteres = 140 - s.length();
                cambiaContador(numCaracteres);

            }

            @Override
            public void afterTextChanged(Editable s) {
                numCaracteres = 140 - s.length();
                cambiaContador(numCaracteres);

            }
        });
    }


    void cambiaContador(int count){

        mTextCount.setText(Integer.toString(count));

        if (count < 50) {
            mTextCount.setTextColor(Color.RED);
        } else {
            mTextCount.setTextColor(mDefaultColor);
        }
        if(count != 140){
            mButtonTweet.setEnabled(true);
        } else {
            mButtonTweet.setEnabled(false);
        }
    }

    public void clickTweet(View v){
        if (numCaracteres < 0){
            Toast.makeText(getBaseContext(),"Hay mas de 140 caracteres", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "pulsaste publicar", Toast.LENGTH_SHORT).show();
            String status = mTextStatus.getText().toString();
            PostTask postTask = new PostTask();
            postTask.execute(status);
            Log.d(TAG, "onClicked");
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            mTextStatus.setText("");
        }

    }


    public void textoCambiado(Editable s){


    }


    class PostTask extends AsyncTask<String, Void, String> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(StatusActivity.this, "Publicando",
                    "Espere...");
            progress.setCancelable(true);


        }

        @Override
        protected String doInBackground(String... params) {
            try {
                YambaClient cloud = new YambaClient("student", "password");

                    cloud.postStatus(params[0]);


                Log.d(TAG, "Publicado con exito en la red" + params[0]);
                return "Publicado con exito";
            } catch (Exception e) {
                Log.e(TAG, "Error al publicar", e);
                e.printStackTrace();
                return "Error al publicar";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            progress.dismiss();
            if ( result != null)
                Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG)
                        .show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
