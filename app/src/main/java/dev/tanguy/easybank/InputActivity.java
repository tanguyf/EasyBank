package dev.tanguy.easybank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.Locale;

public class InputActivity extends AppCompatActivity {

    public static final String FILE_NAME = "easy_history";
    public static final String DELIMITER = ";";

    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_fullscreen);
        Intent intent = getIntent();
        if(intent != null){
            action = intent.getAction();
        }
        setUpEditor();
    }

    private void setUpEditor() {
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
                        saveHistory(f);
                        finish();
                    }
                    catch(Exception e){}
                    handled = true;
                }
                return handled;
            }
        });
    }

    private void saveHistory(float f){
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        try {
            String amount = numberFormat.format(f);
            FileWriter fileWriter = new FileWriter(
                    new File(getFilesDir()+File.separator+FILE_NAME), true);
            fileWriter.append(action)
                    .append(DELIMITER)
                    .append(amount)
                    .append("\n");
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "unable to save this in history", Toast.LENGTH_SHORT).show();
        }
    }

}
