package dev.tanguy.easybank;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;


public class FullscreenActivity extends AppCompatActivity {


    public static final String AMOUNT_DATA = "AMOUNT";
    private static final String TAG = "FULL_SCREEN_ACTIVITY";

    static final int PLUS_REQUEST = 1;
    static final int MINUS_REQUEST = 2;
    private static final String PREFS = "PREFERENCES";
    private static float amount;
    private TextView amountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        setUpUI();
        retrieveAmount();
    }

    private void setUpUI() {
        amountView = (TextView) findViewById(R.id.amount);
    }

    public void onPlusClicked(View view){
        startInputActivity(true);
    }

    public void onMinusClicked(View view){
        startInputActivity(false);
    }

    private void startInputActivity(boolean plus) {
        Intent intent = new Intent(this, InputActivity.class);
        startActivityForResult(intent, plus ? PLUS_REQUEST : MINUS_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null){
            float x = data.getFloatExtra(AMOUNT_DATA, 0);
            if (requestCode == PLUS_REQUEST) {
                if (resultCode == RESULT_OK) {
                    updateAmount(true, x);
                }
            } else if (requestCode == MINUS_REQUEST) {
                if (resultCode == RESULT_OK) {
                    updateAmount(false, x);
                }
            }
        }
    }

    private void retrieveAmount(){
        Log.d(TAG, "retrieveAmount...");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        amount = sharedPref.getFloat(PREFS, 0);
        Log.d(TAG, "amount = "+ amount);
        updateUI();
    }

    private void updateAmount(boolean plus, float x){
        Log.d(TAG, "updateAmount...");
        Log.d(TAG, "old amount = "+ amount);
        amount = plus ? amount + x : amount - x;
        Log.d(TAG, "new amount = "+ amount);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(PREFS, amount);
        editor.commit();
        updateUI();
    }

    private void updateUI(){
        amountView.setText("" + amount);
        Intent intent = new Intent(this, EasyWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), EasyWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

    public static class EasyWidgetProvider extends AppWidgetProvider {

        public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
            final int N = appWidgetIds.length;

            // Perform this loop procedure for each App Widget that belongs to this provider
            for (int i=0; i<N; i++) {
                int appWidgetId = appWidgetIds[i];

                Intent intent = new Intent(context, FullscreenActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
                views.setOnClickPendingIntent(R.id.amount, pendingIntent);
                views.setTextViewText(R.id.amount, ""+amount);

                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }
    }
}
