package be.howest.lolmetabuilder;

import android.annotation.TargetApi;
import android.app.Activity;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.howest.lolmetabuilder.data.Champion;

public class ChampionFragment extends Fragment {

    public GridView gvChamps;
    public Map<String, Integer> map;

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


            //map => link tussen string & drawable image
            map = new HashMap<String, Integer>();
            map.put("gnar", R.drawable.gnar);
            map.put("aatrox", R.drawable.aatrox);
            map.put("ahri", R.drawable.ahri);
            map.put("akali", R.drawable.akali);
            map.put("alistar", R.drawable.alistar);
            map.put("amumu", R.drawable.amumu);
            map.put("anivia", R.drawable.anivia);

            //TODO code voor maken champion is niet correct. Zitten geen prijs & image values meer in!
            Champion champ1 = new Champion(0, "Gnar","gnar","",6300,0,0,0,0,"", "");
            Champion champ2 = new Champion(0,"Aatrox", "aatrox","",6300,0,0,0,0,"", "");
            Champion champ3 = new Champion(0,"Ahri", "ahri", "",6300,0,0,0,0,"", "");
            Champion champ4 = new Champion(0,"Akali", "akali", "",6300,0,0,0,0,"", "");
            Champion champ5 = new Champion(0,"Alistar", "alistar", "",6300,0,0,0,0,"", "");
            Champion champ6 = new Champion(0,"Amumu", "amumu", "",6300,0,0,0,0,"", "");
            Champion champ7 = new Champion(0,"Anivia", "anivia", "",6300,0,0,0,0,"", "");

            champions.add(champ1);
            champions.add(champ2);
            champions.add(champ3);
            champions.add(champ4);
            champions.add(champ5);
            champions.add(champ6);
            champions.add(champ7);
            champions.add(champ1);
            champions.add(champ2);
            champions.add(champ3);
            champions.add(champ4);
            champions.add(champ5);
            champions.add(champ6);
            champions.add(champ7);
            champions.add(champ1);
            champions.add(champ2);
            champions.add(champ3);
            champions.add(champ4);
            champions.add(champ5);
            champions.add(champ6);
            champions.add(champ7);
            champions.add(champ1);
            champions.add(champ2);
            champions.add(champ3);
            champions.add(champ4);
            champions.add(champ5);
            champions.add(champ6);
            champions.add(champ7);
            champions.add(champ1);
            champions.add(champ2);
            champions.add(champ3);
            champions.add(champ4);
            champions.add(champ5);
            champions.add(champ6);
            champions.add(champ7);
            champions.add(champ1);
            champions.add(champ2);
            champions.add(champ3);
            champions.add(champ4);
            champions.add(champ5);
            champions.add(champ6);
            champions.add(champ7);
            champions.add(champ1);
            champions.add(champ2);
            champions.add(champ3);
            champions.add(champ4);
            champions.add(champ5);
            champions.add(champ6);
            champions.add(champ7);
            champions.add(champ1);
            champions.add(champ2);
            champions.add(champ3);
            champions.add(champ4);
            champions.add(champ5);
            champions.add(champ6);
            champions.add(champ7);
            champions.add(champ1);
            champions.add(champ2);
            champions.add(champ3);
            champions.add(champ4);
            champions.add(champ5);
            champions.add(champ6);
            champions.add(champ7);
            champions.add(champ1);
            champions.add(champ2);
            champions.add(champ3);
            champions.add(champ4);
            champions.add(champ5);
            champions.add(champ6);
            champions.add(champ7);
            champions.add(champ1);
            champions.add(champ2);
            champions.add(champ3);
            champions.add(champ4);
            champions.add(champ5);
            champions.add(champ6);
            champions.add(champ7);
            champions.add(champ1);
            champions.add(champ2);
            champions.add(champ3);
            champions.add(champ4);
            champions.add(champ5);
            champions.add(champ6);
            champions.add(champ7);

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


            //ene code werkt alleen bij API 16. (of een @ toevoegen bovenaan, of min api veranderen)
            int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
            {
                viewHolder.imgChamp.setBackgroundDrawable(getResources().getDrawable(map.get(champ.getTitle())));
            }else{
                viewHolder.imgChamp.setBackground(getResources().getDrawable(map.get(champ.getTitle())));
            }

            viewHolder.txtChampName.setText(champ.getName());
            viewHolder.txtChampPrice.setText("" + champ.getAttack());

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Champion: " + champ.getName(), Toast.LENGTH_LONG).show();
                }
            });

            return convertView;
        }
    }
}
