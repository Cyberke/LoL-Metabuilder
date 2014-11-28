package be.howest.lolmetabuilder;

import android.app.Activity;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.howest.lolmetabuilder.data.Champion;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChampionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChampionFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ChampionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public GridView gvChamps;
    public Map<String, Integer> map;

    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static ChampionFragment newInstance() {
        ChampionFragment fragment = new ChampionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        //TODO: toon champions
        //showChampions();
        gvChamps = (GridView) view.findViewById(R.id.gvChampions);
        gvChamps.setAdapter(new ChampionAdapter());

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    class ChampionAdapter extends ArrayAdapter<Champion>
    {
        private ArrayList<Champion> champions = new ArrayList<Champion>();

        public ChampionAdapter(){
            //TODO: activity adapter omzetten van fragment
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

            Champion champ1 = new Champion("Gnar", "gnar", 6300);
            Champion champ2 = new Champion("Aatrox", "aatrox", 6300);
            Champion champ3 = new Champion("Ahri", "ahri", 6300);
            Champion champ4 = new Champion("Akali", "akali", 6300);
            Champion champ5 = new Champion("Alistar", "alistar", 6300);
            Champion champ6 = new Champion("Amumu", "amumu", 6300);
            Champion champ7 = new Champion("Anivia", "anivia", 6300);

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
            ImageView imgChamp;
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

                viewHolder.imgChamp = (ImageView) convertView.findViewById(R.id.imgChamp);
                viewHolder.txtChampName = (TextView) convertView.findViewById(R.id.txtChampName);
                viewHolder.txtChampPrice = (TextView) convertView.findViewById(R.id.txtChampPrice);

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.imgChamp.setImageResource(map.get(champ.getImage()));
            viewHolder.txtChampName.setText(champ.getName());
            viewHolder.txtChampPrice.setText("" + champ.getPrice());

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
