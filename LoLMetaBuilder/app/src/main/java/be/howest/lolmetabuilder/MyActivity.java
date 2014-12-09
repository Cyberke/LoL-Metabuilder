package be.howest.lolmetabuilder;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.howest.lolmetabuilder.data.Champion;
import be.howest.lolmetabuilder.data.FreeChamp;
import be.howest.lolmetabuilder.data.Item;
import be.howest.lolmetabuilder.data.Leaf;
import be.howest.lolmetabuilder.data.MasteryTree;
import be.howest.lolmetabuilder.data.Rune;
import be.howest.lolmetabuilder.json.api_ophalen;


public class MyActivity extends Activity implements ChampionFragment.OnFragmentInteractionListener,
                                                    ItemsFragment.OnFragmentInteractionListener,
                                                    BuildsFragment.OnFragmentInteractionListener,
                                                    SimulateFragment.OnFragmentInteractionListener,
                                                    SettingsFragment.OnFragmentInteractionListener,
                                                    ChampionOverviewFragment.OnFragmentInteractionListener,
                                                    ItemFragment.OnFragmentInteractionListener{
    private ProgressDialog pDialog;
    private static Boolean isInGeladen = false;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private String[] layers;
    private ActionBarDrawerToggle drawerToggle;
    private int currentFragment = 0;

    public static ArrayList<FreeChamp> freeChamps = new ArrayList<FreeChamp>();

    public static ArrayList<Champion> champions = new ArrayList<Champion>();
    public static ArrayList<Item> items = new ArrayList<Item>();
    public static ArrayList<Leaf> leafs = new ArrayList<Leaf>();
    public static ArrayList<Rune> runes = new ArrayList<Rune>();
    public static ArrayList<MasteryTree> masteryTrees = new ArrayList<MasteryTree>();

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class GetChampionTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MyActivity.this);
            pDialog.setMessage("Loading data...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected List<String> doInBackground(String... params) {
            try {
                String PACKAGE_NAME = getApplicationContext().getPackageName();
                ApplicationInfo appInfo = getPackageManager().getApplicationInfo(PACKAGE_NAME, PackageManager.GET_META_DATA);

                // Als je ze wilt testen moet je ze 1 per 1 uit commentaar halen
                // En freeChamps bijvoorbeeld naar collection hernoemen

                ArrayList<String> dc = new ArrayList<String>();

                champions = api_ophalen.champions(appInfo);
                items = api_ophalen.items(appInfo);
                leafs = api_ophalen.leafs(appInfo);
                runes = api_ophalen.runes(appInfo);
                masteryTrees = api_ophalen.masteryTrees(appInfo);

                ArrayList<FreeChamp> temp = api_ophalen.freechampRotation(appInfo);

                if (champions != null) {
                    for (Champion c : champions) {
                        for (FreeChamp fc : temp) {
                            if (c.getId() == fc.getId()) {
                                FreeChamp freeChamp = new FreeChamp(fc.getId());
                                freeChamp.setChampion(c);

                                freeChamps.add(freeChamp);
                            }
                        }
                    }
                }

                if (freeChamps.size() == 10) {
                    dc.add("Complete");
                }
                else {
                    dc.add("Fail");
                }

                return dc;
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        // Zoeken van een index van een bepaald object in den array
        // Om daarna specifiek data van een object te kunnen testen
        /*private String filterObjects(ArrayList<String> collection, String id) {
            String result = "Nothing";

            for (String o : collection) {
                if (o.equals(id)) { //
                    result = id + " (" + collection.indexOf(o) + ")";
                }
            }

            return result;
        }*/

        @Override
        protected void onPostExecute(List<String> result) {

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            super.onPostExecute(result);

            ArrayList<String> dc = (ArrayList<String>) result;

            //String o = filterObjects(ao, "4112"); // Fury -> result: 5

            //System.out.println(ao.get(5));

            Toast.makeText(getBaseContext(), "" + dc.get(0), Toast.LENGTH_SHORT).show();

            GridView gvFreeChamps = (GridView) findViewById(R.id.gvFreeChamps);
            PlaceholderFragment.FreeChampionAdapter adapter = (PlaceholderFragment.FreeChampionAdapter) gvFreeChamps.getAdapter();

            adapter.addAll(freeChamps);
            adapter.notifyDataSetChanged();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

        // Eerste item van navigation default selecteren
        drawerList.setItemChecked(0, true);
        drawerList.setSelection(0);

        handleIntent(getIntent());

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, PlaceholderFragment.newInstance())
                    .commit();
        }
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent)
    {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(getBaseContext(), query, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my, menu);

        //Associate searchable config with the searchview
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

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

    public class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Fragment fragment = null;
            currentFragment = position;

            switch(position)
            {
                case 0:
                    fragment = PlaceholderFragment.newInstance();
                    break;
                case 1:
                    fragment = ChampionFragment.newInstance();
                    break;
                case 2:
                    fragment = ItemsFragment.newInstance();
                    break;
                case 3:
                    fragment = BuildsFragment.newInstance();
                    break;
                case 4:
                    fragment = SimulateFragment.newInstance();
                    break;
                case 5:
                    fragment = SettingsFragment.newInstance();
                    break;
                default:
                    break;
            }

            if(fragment != null)
            {

                FragmentManager fragmentManager = getFragmentManager();

                fragmentManager.popBackStack();

                FragmentTransaction trans = fragmentManager.beginTransaction();
                trans.replace(R.id.container, fragment);
                trans.addToBackStack(null);
                trans.commit();


                drawerLayout.closeDrawers();
            }

        }
    }
}
