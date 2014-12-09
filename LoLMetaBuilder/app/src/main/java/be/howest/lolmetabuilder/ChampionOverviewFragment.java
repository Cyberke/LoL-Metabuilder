package be.howest.lolmetabuilder;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.PorterDuff;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.gson.Gson;

import be.howest.lolmetabuilder.data.Champion;
import be.howest.lolmetabuilder.data.ChampionTag;
import be.howest.lolmetabuilder.data.Skin;
import be.howest.lolmetabuilder.data.Tag;

import be.howest.lolmetabuilder.data.Tip;

public class ChampionOverviewFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public static ChampionOverviewFragment newInstance() {
        ChampionOverviewFragment fragment = new ChampionOverviewFragment();

        return fragment;
    }
    public ChampionOverviewFragment() {
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
        View view = inflater.inflate(R.layout.fragment_champion_overview, container, false);



        //gekozen champion ophalen
        Bundle bundle = getArguments();
        if(bundle.getString("Champion") != null)
        {
            Champion champion = new Gson().fromJson(bundle.getString("Champion"), Champion.class);



        //layout elementen ophalen
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.imgChamp = (ImageView) view.findViewById(R.id.imgVChampAvatar);
        viewHolder.txtChampName = (TextView) view.findViewById(R.id.txtVChampionName);
        viewHolder.txtChampTitle = (TextView) view.findViewById(R.id.txtVChampionTitle);
        viewHolder.txtCategorieen = (TextView) view.findViewById(R.id.txtVCategorieen);
        viewHolder.txtIPPrice = (TextView) view.findViewById(R.id.txtVIPCost);
        viewHolder.txtRPPrice = (TextView) view.findViewById(R.id.txtVRPCost);
        viewHolder.txtAttack = (TextView) view.findViewById(R.id.txtVAttackStats);
        viewHolder.txtDefense = (TextView) view.findViewById(R.id.txtVDefenseStats);
        viewHolder.txtMagic = (TextView) view.findViewById(R.id.txtVSpellsStats);
        viewHolder.txtDifficulty = (TextView) view.findViewById(R.id.txtVDifficultyStats);
        viewHolder.pbAttack = (ProgressBar) view.findViewById(R.id.pBAttackStats);
        viewHolder.pbDefense = (ProgressBar) view.findViewById(R.id.pBDefenseStats);
        viewHolder.pbMagic = (ProgressBar) view.findViewById(R.id.pBSpellsStats);
        viewHolder.pbDifficulty = (ProgressBar) view.findViewById(R.id.pBDifficultyStats);
        viewHolder.txtAllyTips = (TextView) view.findViewById(R.id.txtVAllyTips);
        viewHolder.txtEnemyTips = (TextView) view.findViewById(R.id.txtVEnemyTips);

        //layout invullen
        int champImageId = getResources().getIdentifier(champion.getImage().toLowerCase(), "drawable", getActivity().getPackageName());
        viewHolder.imgChamp.setImageResource(champImageId);
        viewHolder.txtChampName.setText(champion.getName());
        viewHolder.txtChampTitle.setText(champion.getTitle());
        viewHolder.txtIPPrice.setText("Ip: " + champion.getPriceIP());
        viewHolder.txtRPPrice.setText("Rp: " + champion.getPriceRP());
        viewHolder.txtAttack.setText("" + champion.getAttack());
        viewHolder.txtDefense.setText("" + champion.getDefense());
        viewHolder.txtMagic.setText("" + champion.getMagic());
        viewHolder.txtDifficulty.setText("" + champion.getDifficulty());
        viewHolder.pbAttack.setProgress(champion.getAttack() * 10);
        viewHolder.pbDefense.setProgress(champion.getDefense() * 10);
        viewHolder.pbMagic.setProgress(champion.getMagic() * 10);
        viewHolder.pbDifficulty.setProgress(champion.getDifficulty() * 10);

        String categorieen = "";

        for (Tag tag : champion.getTags()) {
            categorieen += tag.getName() + ", ";
        }

        // Verwijderd laatste komma
        categorieen = categorieen.substring(0, categorieen.length() - 2);

        viewHolder.txtCategorieen.setText(categorieen);

        String allyTips = "";

        for (Tip allyTip : champion.getAllyTips()) {
            allyTips += allyTip.getContent() + "\n\n";
        }

        viewHolder.txtAllyTips.setText(allyTips);

        String enemyTips = "";

        for (Tip enemyTip : champion.getEnemyTips()) {
            enemyTips += enemyTip.getContent() + "\n\n";
        }

        viewHolder.txtEnemyTips.setText(enemyTips);

        }

        if(getActivity().getActionBar() != null)
        {
            getActivity().getActionBar().setSelectedNavigationItem(0);
        }


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
        getActivity().getFragmentManager().popBackStack("Tab", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(Uri uri);
    }

    private Drawable getDrawableResourceByName(String name) {
        String packageName = getActivity().getPackageName();
        int resId = getResources().getIdentifier( name, "drawable", packageName);
        return getResources().getDrawable(resId);
    }

    class ViewHolder {
        ImageView imgChamp;

        TextView txtChampName,
                txtChampTitle,
                txtCategorieen,
                txtIPPrice,
                txtRPPrice,
                txtAttack,
                txtDefense,
                txtMagic,
                txtDifficulty,
                txtAllyTips,
                txtEnemyTips;

        ProgressBar pbAttack,
                    pbDefense,
                    pbMagic,
                    pbDifficulty;

    }
}
