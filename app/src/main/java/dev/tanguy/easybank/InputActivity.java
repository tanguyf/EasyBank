package dev.tanguy.easybank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class InputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_fullscreen);

        EditText editText = (EditText) findViewById(R.id.input_amount);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    try{
                        Intent resultIntent = new Intent();
                        float f = Float.parseFloat(v.getText().toString());
                        resultIntent.putExtra(FullscreenActivity.AMOUNT_DATA, f);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                    catch(Exception e){}
                    handled = true;
                }
                return handled;
            }
        });
    }
}
