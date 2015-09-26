package co.edu.udea.cmovil.gr5.yamba;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thenewcircle.yamba.client.YambaClient;

/**
 * A simple {@link Fragment} subclass.
 */


public class StatusFragment extends Fragment {
    //instancias
    private static String TAG = StatusActivity.class.getSimpleName();
    private Button mButtonTweet;
    private EditText mTextStatus;
    private TextView mTextCount;
    private int mDefaultColor;
    private int numCaracteres;


    public StatusFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_status, null, false);

        //inicializar
        mButtonTweet = (Button) v.findViewById(R.id.status_button_tweet);
        mTextStatus = (EditText) v.findViewById(R.id.status_text);
        mTextCount = (TextView) v.findViewById(R.id.status_text_count);

        mTextCount.setText(Integer.toString(140));
        mDefaultColor = mTextCount.getTextColors().getDefaultColor();

        mButtonTweet.setEnabled(false);



        mButtonTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "pulsaste publicar", Toast.LENGTH_SHORT).show();
                String status = mTextStatus.getText().toString();
                PostTask postTask = new PostTask();
                postTask.execute(status);
                Log.d(TAG, "onClicked");
                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });



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


        return v;
    }

    void limpiaTexto(){
        mTextStatus.setText("");
    }


    private void cambiaContador(int count){

        mTextCount.setText(Integer.toString(count));

        if (count < 50) {
            mTextCount.setTextColor(Color.RED);
        } else {
            mTextCount.setTextColor(mDefaultColor);
        }
        if(count != 140 && count > 0){
            mButtonTweet.setEnabled(true);
        } else {
            mButtonTweet.setEnabled(false);
        }

        if (count < 0){
            Toast.makeText(getActivity(), "Hay mas de 140 caracteres", Toast.LENGTH_SHORT).show();
            mButtonTweet.setEnabled(false);
        }

    }
/*
    public void clickTweet(View v){
        if (numCaracteres < 0){
            Toast.makeText(getActivity(), "Hay mas de 140 caracteres", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "pulsaste publicar", Toast.LENGTH_SHORT).show();
            String status = mTextStatus.getText().toString();
            PostTask postTask = new PostTask();
            postTask.execute(status);
            Log.d(TAG, "onClicked");
            //InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            //imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            mTextStatus.setText("");
        }

    }
*/

    public void textoCambiado(Editable s){


    }


    class PostTask extends AsyncTask<String, Void, String> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), "Publicando",
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
            if ( result != null) {
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                mTextStatus.setText("");
            }
        }

    }



}
