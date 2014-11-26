package be.howest.lolmetabuilder;

import android.app.Activity;
import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.howest.lolmetabuilder.data.Champion;


public class MyChampionActivity extends MyActivity {

    private GridView gvChamps;
    private Map<String, Integer> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        loadChampions();
    }


    private void loadChampions(){
        gvChamps = (GridView) findViewById(R.id.gvChampions);
        //gvChamps.setAdapter(new ChampionAdapter());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_champion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my_champion, container, false);

            return rootView;
        }
    }

    class ChampionAdapter extends ArrayAdapter<Champion>
    {
        private ArrayList<Champion> champions = new ArrayList<Champion>();
        public ChampionAdapter(){
            super(MyChampionActivity.this, R.layout.cel_champ, R.id.txtChampName);


            //map => link tussen string & drawable image
            map = new HashMap<String, Integer>();
            map.put("gnar", R.drawable.gnar);
            map.put("aatrox", R.drawable.aatrox);
            map.put("ahri", R.drawable.ahri);
            map.put("akali", R.drawable.akali);
            map.put("alistar", R.drawable.alistar);
            map.put("amumu", R.drawable.amumu);
            map.put("anivia", R.drawable.anivia);

            Champion champ1 = new Champion("gnar", "gnar", 6300);
            Champion champ2 = new Champion("aatrox", "aatrox", 6300);
            Champion champ3 = new Champion("ahri", "ahri", 6300);
            Champion champ4 = new Champion("akali", "akali", 6300);
            Champion champ5 = new Champion("alistar", "alistar", 6300);
            Champion champ6 = new Champion("amumu", "amumu", 6300);
            Champion champ7 = new Champion("anivia", "anivia", 6300);

            champions.add(champ1);
            champions.add(champ2);
            champions.add(champ3);
            champions.add(champ4);
            champions.add(champ5);
            champions.add(champ6);
            champions.add(champ7);

            this.addAll(champions);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = super.getView(position, convertView, parent);

            final Champion champ = champions.get(position);

            ImageView image = (ImageView) row.findViewById(R.id.imgChamp);
            image.setImageResource(map.get(champ.getImage()));

            TextView txtChampName = (TextView) row.findViewById(R.id.txtChampName);
            txtChampName.setText(champ.getName());

            TextView txtChampPrice = (TextView) row.findViewById(R.id.txtChampPrice);
            txtChampPrice.setText("" + champ.getPrice());

            row.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), "Champion: " + champ.getName(), Toast.LENGTH_LONG).show();
                }
            });

            return row;
        }
    }
}
