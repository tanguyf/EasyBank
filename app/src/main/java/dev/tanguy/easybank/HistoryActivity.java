package dev.tanguy.easybank;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<String> history = new ArrayList<>(30);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        setUpViews();
    }

    private void setUpViews() {
        getHistoryFromFile();
        ListView listView = (ListView) findViewById(R.id.list);
        CustomListAdapter adapter = new CustomListAdapter(this, history);
        listView.setAdapter(adapter);
    }

    private void getHistoryFromFile(){
        try {
            BufferedReader buffer
                    = new BufferedReader(new FileReader(
                    new File(getFilesDir()+File.separator+InputActivity.FILE_NAME)));
            String line = buffer.readLine();
            while(line != null){
                String[] strings = line.split(InputActivity.DELIMITER);
                if(FullscreenActivity.PLUS_ACTION.equals(strings[0]))
                    history.add("+ " + strings[1]);
                else
                    history.add("- " + strings[1]);
                line = buffer.readLine();
            }
            Collections.reverse(history);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "unable to read history", Toast.LENGTH_SHORT).show();
        }
    }

    public class CustomListAdapter extends BaseAdapter {
        private Context context; //context
        private ArrayList<String> items; //data source of the list adapter

        //public constructor
        public CustomListAdapter(Context context, ArrayList<String> items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size(); //returns total of items in the list
        }

        @Override
        public Object getItem(int position) {
            return items.get(position); //returns list item at the specified position
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // inflate the layout for each list row
            if (convertView == null) {
                convertView = LayoutInflater.from(context).
                        inflate(R.layout.history_item, parent, false);
            }
            String line = (String) getItem(position);
            TextView plusView = (TextView)
                    convertView.findViewById(R.id.plus_view);
            TextView minusView = (TextView)
                    convertView.findViewById(R.id.minus_view);
            if(line.startsWith("+")){
                minusView.setText("");
                plusView.setText(line);
            } else {
                minusView.setText(line);
                plusView.setText("");
            }
            return convertView;
        }
    }

}
