package be.howest.lolmetabuilder;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
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


public class MyActivity extends Activity implements ChampionFragment.OnFragmentInteractionListener,
                                                    ItemFragment.OnFragmentInteractionListener,
                                                    BuildsFragment.OnFragmentInteractionListener,
                                                    SimulateFragment.OnFragmentInteractionListener,
                                                    SettingsFragment.OnFragmentInteractionListener {
    private ArrayList<String> ao;
    private ProgressDialog pDialog;
    private static Boolean isInGeladen = false;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] layers;
    private ActionBarDrawerToggle drawerToggle;
    private int currentFragment = 0;

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

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

        //TODO: Delete
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

            Fragment fragment = null;
            currentFragment = position;

            switch(position)
            {
                //TODO fragments opkuisen (param1 & param2)
                case 0:
                    fragment = new PlaceholderFragment();
                    break;
                case 1:
                    fragment = new ChampionFragment().newInstance();
                    break;
                case 2:
                    fragment = new ItemFragment().newInstance("","");
                    break;
                case 3:
                    fragment = new BuildsFragment().newInstance("","");
                    break;
                case 4:
                    fragment = new SimulateFragment().newInstance("","");
                    break;
                case 5:
                    fragment = new SettingsFragment().newInstance("","");
                    break;
                default:
                    break;
            }

            if(fragment != null)
            {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction trans = fragmentManager.beginTransaction();
                trans.replace(R.id.container, fragment);
                trans.commit();
                fragmentManager.popBackStack();

                drawerLayout.closeDrawers();
            }

        }
    }
}
