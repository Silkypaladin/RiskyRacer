package com.example.riskyracer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ScoresFragment extends ListFragment {

    class CustomAdapter extends ArrayAdapter<String>
    {
        Context context;
        ArrayList<String> scores;


        CustomAdapter(Context c, ArrayList<String> scores) {
            super(c, R.layout.score_item,scores);
            this.context = c;
            this.scores=scores;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = vi.inflate(R.layout.score_item, parent, false);
            TextView title = (TextView) row.findViewById(R.id.scoreItem);
            int pos = position+1;
            title.setText(pos + ". " + this.scores.get(position));
            pos++;
            return row;
        }

    }

    ArrayList<String> scores = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.scores_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadScores();
        CustomAdapter adapter = new CustomAdapter(this.getContext(), scores);
        setListAdapter(adapter);
    }

    protected void loadScores() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        String scoresString = preferences.getString("highScores", "");
        if (!scoresString.equals("")) {
            ArrayList<String> scoresTemp = new ArrayList<>(Arrays.asList(scoresString.split(",")));
            ArrayList<Integer> temp = new ArrayList<>();
            for (int i = scoresTemp.size() - 1; i >= 0; i--) {
                if (!scoresTemp.get(i).equals(""))
                    temp.add(Integer.parseInt(scoresTemp.get(i)));
            }
            Collections.sort(temp);
            for (int i = temp.size() - 1; i >= 0; i--) {
                scores.add(String.valueOf(temp.get(i)));
            }

        }
    }

}
