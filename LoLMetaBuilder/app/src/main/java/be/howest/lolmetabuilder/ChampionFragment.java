package be.howest.lolmetabuilder;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import java.util.ArrayList;
import be.howest.lolmetabuilder.data.models.Champion;

public class ChampionFragment extends Fragment {

    public GridView gvChamps;
    private static Champion champion;
    ActionBar actionBar;
    ViewPager viewPager;
    ChampionAdapterFragment ft;

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

        MainActivity.drawerList.setSelection(1);
        MainActivity.drawerList.setItemChecked(1, true);

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
            champions = MainActivity.champions;

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
            Fragment fragment = new ChampionDetailFragment();
            Bundle args = new Bundle();
            args.putString("Champion", new Gson().toJson(champion));
            fragment.setArguments(args);

            //openen fragment
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack("Champion")
                    .commit();
        }

        private Drawable getDrawableResourceByName(String name) {
            String packageName = getActivity().getPackageName();
            int resId = getResources().getIdentifier( name, "drawable", packageName);
            return getResources().getDrawable(resId);
        }
    }
}
