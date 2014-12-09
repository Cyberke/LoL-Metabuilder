package be.howest.lolmetabuilder;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import be.howest.lolmetabuilder.data.Champion;

public class ChampionFragment extends Fragment {

    public GridView gvChamps;
    private static Champion champion;

    private OnFragmentInteractionListener mListener;

    public static ChampionFragment newInstance() {
        ChampionFragment fragment = new ChampionFragment();

        return fragment;
    }
    public ChampionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_champion, container, false);

        gvChamps = (GridView) view.findViewById(R.id.gvChampions);
        gvChamps.setAdapter(new ChampionAdapter());

        //reset tabs
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }


    class ChampionAdapter extends ArrayAdapter<Champion>
    {
        private ArrayList<Champion> champions = new ArrayList<Champion>();

        public ChampionAdapter(){
            super(getActivity(), R.layout.cel_champ, R.id.txtChampName);

            //champions die op de mainactivity worden opgehaald gebruiken
            champions = MyActivity.champions;

            this.addAll(champions);
        }

        class ViewHolder {
            RelativeLayout imgChamp;
            TextView txtChampName;
            TextView txtChampPrice;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //View row = super.getView(position, convertView, parent);
            ViewHolder viewHolder = new ViewHolder();

            final Champion champ = champions.get(position);

            if(convertView == null)
            {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.cel_champ, null);
                viewHolder = new ViewHolder();

                viewHolder.imgChamp = (RelativeLayout) convertView.findViewById(R.id.imgChamp);
                viewHolder.txtChampName = (TextView) convertView.findViewById(R.id.txtChampName);
                viewHolder.txtChampPrice = (TextView) convertView.findViewById(R.id.txtChampPrice);

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.txtChampName.setText(champ.getName());
            viewHolder.txtChampPrice.setText("" + champ.getPriceIP());
            viewHolder.imgChamp.setBackground(getDrawableResourceByName(champ.getImage().toLowerCase()));

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    champion = champ;
                    loadChampionDetail(v);
                }
            });

            return convertView;
        }

        private void loadChampionDetail(View v)
        {
        //Openen champion detail met fragment

        //gekozen champion met de fragment meesturen
        Fragment fragment = new ChampionOverviewFragment();
        Bundle args = new Bundle();
        args.putString("Champion", new Gson().toJson(champion));
        fragment.setArguments(args);

        //openen fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("Champion")
                .commit();

        //Tabs instellen
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);

        String[] tabTitles = getResources().getStringArray(R.array.titles_champion_tabs);

        ActionBar.Tab tab = actionBar.newTab()
                .setText(tabTitles[0])
                .setTabListener(new TabListener<ChampionOverviewFragment>(getActivity(), "Overview", ChampionOverviewFragment.class));
        actionBar.addTab(tab, false);

        tab = actionBar.newTab()
                .setText(tabTitles[1])
                .setTabListener(new TabListener<SkinFragment>(getActivity(), "Skins", SkinFragment.class));
        actionBar.addTab(tab, false);

        tab = actionBar.newTab()
                .setText(tabTitles[2])
                .setTabListener(new TabListener<LoreFragment>(getActivity(), "Lore", LoreFragment.class));
        actionBar.addTab(tab, false);

        tab = actionBar.newTab()
                .setText(tabTitles[3])
                .setTabListener(new TabListener<AbilitiesFragment>(getActivity(), "Abilities", AbilitiesFragment.class));
        actionBar.addTab(tab, false);

        }

        private Drawable getDrawableResourceByName(String name) {
            String packageName = getActivity().getPackageName();
            int resId = getResources().getIdentifier( name, "drawable", packageName);
            return getResources().getDrawable(resId);
        }
    }

    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;

        public TabListener(Activity activity, String tag, Class<T> clz){
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft){

            Fragment fragment = null;

            switch(tab.getPosition())
            {
                case 0:
                    //overview
                    fragment = ChampionOverviewFragment.newInstance();
                    break;
                case 1:
                    //skins
                    fragment = SkinFragment.newInstance();
                    break;
                case 2:
                    //lore
                    fragment = LoreFragment.newInstance();
                    break;
                case 3:
                    //abilities
                    fragment = AbilitiesFragment.newInstance();
                    break;
            }

            if(fragment != null)
            {

                Bundle args = new Bundle();
                args.putString("Champion", new Gson().toJson(champion));
                fragment.setArguments(args);

                mActivity.getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack("Tab")
                        .commit();
            }
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                // Detach the fragment, because another one is being attached
                ft.detach(mFragment);
            }
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // User selected the already selected tab. Usually do nothing.
        }
    }
}
