package be.howest.lolmetabuilder;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.howest.lolmetabuilder.json.api_ophalen;


public class MyActivity extends Activity {
    private ArrayList<String> ao;
    private ProgressDialog pDialog;

    private static Boolean isInGeladen = false;

    class GetChampionTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MyActivity.this);
            pDialog.setMessage("Data ophalen...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected List<String> doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String PACKAGE_NAME = getApplicationContext().getPackageName();
                ApplicationInfo appInfo = getPackageManager().getApplicationInfo(PACKAGE_NAME, PackageManager.GET_META_DATA);

                return api_ophalen.freechampRotation(appInfo);
            }
            catch (Exception e) {}

            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            // TODO Auto-generated method stub

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            super.onPostExecute(result);

            ao = (ArrayList<String>) result;

            Toast.makeText(getBaseContext(), "Ingeladen champions: " + ao.size(), Toast.LENGTH_LONG).show();
        }
    }

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] layers;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Eenmalig uitvoeren

        if(!isInGeladen) {
            GetChampionTask task = new GetChampionTask();
            task.execute();

            isInGeladen = true;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle((Activity) this, drawerLayout, R.drawable.ic_drawer, 0, 0) {
            public void onDrawerClosed(View view) {
                // TODO: Titel veranderen naar activity name
                getActionBar().setTitle(R.string.app_name);


            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(R.string.app_name);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        layers = getResources().getStringArray(R.array.titles_array);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, layers));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        int position = getIntent().getIntExtra("position", 0);

        drawerList.setItemChecked(position, true);
        drawerList.setSelection(position);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
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
            View rootView = inflater.inflate(R.layout.fragment_my, container, false);
            return rootView;
        }
    }

    public class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = null;

            switch (position) {
                case 0: // Home
                    intent = new Intent(view.getContext(), MyActivity.class);
                    break;
                case 1: // Champions
                    intent = new Intent(view.getContext(), MyChampionActivity.class);
                    break;
                case 2: // Items
                    intent = new Intent(view.getContext(), MyItemsActivity.class);
                    break;
                case 3: // Builds
                    intent = new Intent(view.getContext(), MyBuildsActivity.class);
                    break;
                case 4: // Simulate
                    intent = new Intent(view.getContext(), MySimulateActivity.class);
                    break;
                case 5: // Settings
                    intent = new Intent(view.getContext(), MySettingsActivity.class);
                    break;
            }

            intent.putExtra("position", position);
            startActivity(intent);
        }

    }
}
