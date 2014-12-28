package be.howest.lolmetabuilder;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.howest.lolmetabuilder.data.models.Build;
import be.howest.lolmetabuilder.data.models.Champion;
import be.howest.lolmetabuilder.data.models.FreeChamp;
import be.howest.lolmetabuilder.data.models.Item;
import be.howest.lolmetabuilder.data.models.Leaf;
import be.howest.lolmetabuilder.data.models.MasteryTree;
import be.howest.lolmetabuilder.data.models.Rune;
import be.howest.lolmetabuilder.data.models.Version;
import be.howest.lolmetabuilder.json.api_ophalen;


public class MainActivity extends FragmentActivity implements ChampionFragment.OnFragmentInteractionListener,
                                                    ItemsFragment.OnFragmentInteractionListener,
                                                    BuildsFragment.OnFragmentInteractionListener,
                                                    SimulateFragment.OnFragmentInteractionListener,
                                                    SettingsFragment.OnFragmentInteractionListener,
                                                    ChampionOverviewFragment.OnFragmentInteractionListener,
                                                    ItemFragment.OnFragmentInteractionListener,
                                                    SkinFragment.OnFragmentInteractionListener,
                                                    LoreFragment.OnFragmentInteractionListener,
                                                    AbilitiesFragment.OnFragmentInteractionListener,
                                                    ChampionDetailFragment.OnFragmentInteractionListener,
                                                    GeneratedBuildFragment.OnFragmentInteractionListener{
    private ProgressDialog pDialog;
    private static Boolean isInGeladen = false;
    private DrawerLayout drawerLayout;
    public static ListView drawerList;
    private String[] layers;
    private ActionBarDrawerToggle drawerToggle;
    private int currentFragment = 0;

    public static Build championBuild;
    public static Champion currentChampion;

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

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading data...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected List<String> doInBackground(String... params) {
            try {

                String PACKAGE_NAME = getApplicationContext().getPackageName();
                ApplicationInfo appInfo = getPackageManager().getApplicationInfo(PACKAGE_NAME, PackageManager.GET_META_DATA);

                ArrayList<String> dc = new ArrayList<String>();

                champions = api_ophalen.champions(appInfo);
                items = api_ophalen.items(appInfo);
                leafs = api_ophalen.leafs(appInfo);
                runes = api_ophalen.runes(appInfo);
                masteryTrees = api_ophalen.masteryTrees(appInfo);

                // TODO : API versies vergelijken met database stored versies
                ArrayList<Version> versions = api_ophalen.versions(appInfo);

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

                if (freeChamps.size() != 0) {
                    dc.add("Complete");
                }
                else {
                    dc.add("Fail");
                }

                //Lijsten naar DB brengen


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

            //height gridview free champs instellen
            /*
            ViewGroup.LayoutParams params = gvFreeChamps.getLayoutParams();
            int oneRowHeight = gvFreeChamps.getHeight();
            double rows = Math.ceil(freeChamps.size() / 5);
            params.height = (int) (oneRowHeight * rows);
            gvFreeChamps.setLayoutParams(params);
            */
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
        setContentView(R.layout.activity_main);
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
        Fragment fragment = PlaceholderFragment.newInstance();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
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
            FragmentManager fragmentManager = getSupportFragmentManager();
            currentFragment = position;
            Bundle args = new Bundle();

            switch(position)
            {
                case 0:
                    fragmentManager.popBackStack("NavDrawer", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    drawerLayout.closeDrawers();
                    break;
                case 1:
                    fragment = ChampionFragment.newInstance();
                    break;
                case 2:
                    fragment = ItemsFragment.newInstance();
                    args.putString("From", "Items");
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

                fragmentManager.popBackStack("NavDrawer", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragment.setArguments(args);
                FragmentTransaction trans = fragmentManager.beginTransaction();
                trans.replace(R.id.container, fragment);
                trans.addToBackStack("NavDrawer");
                trans.commit();

                drawerLayout.closeDrawers();
            }

        }
    }
}
