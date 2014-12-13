package be.howest.lolmetabuilder;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import be.howest.lolmetabuilder.data.models.Champion;

public class ChampionDetailFragment extends Fragment implements ActionBar.TabListener {

    private OnFragmentInteractionListener mListener;
    ActionBar actionBar;
    ViewPager viewPager;
    ChampionAdapterFragment ft;

    public static ChampionDetailFragment newInstance() {
        ChampionDetailFragment fragment = new ChampionDetailFragment();

        return fragment;
    }
    public ChampionDetailFragment() {
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
        View view = inflater.inflate(R.layout.fragment_champion_detail, container, false);

        //get selected champion
        Bundle bundle = getArguments();
        Champion champion = new Gson().fromJson(bundle.getString("Champion"), Champion.class);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        ft = new ChampionAdapterFragment(getActivity().getSupportFragmentManager(), champion);

        actionBar = getActivity().getActionBar();
        viewPager.setAdapter(ft);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        String[] tabTitles = getResources().getStringArray(R.array.titles_champion_tabs);
        for(String tabTitle : tabTitles)
        {
            actionBar.addTab(actionBar.newTab().setText(tabTitle).setTabListener(this));
        }

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                // When swiping between pages, select the
                // corresponding tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
