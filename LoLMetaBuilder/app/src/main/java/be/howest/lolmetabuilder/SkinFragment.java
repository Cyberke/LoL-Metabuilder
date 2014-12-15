package be.howest.lolmetabuilder;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import be.howest.lolmetabuilder.data.models.Champion;
import be.howest.lolmetabuilder.data.models.Skin;

public class SkinFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Champion champion;
    private GridView gvSkins;

    public static SkinFragment newInstance() {
        SkinFragment fragment = new SkinFragment();

        return fragment;
    }

    public SkinFragment() {
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
        View view = inflater.inflate(R.layout.fragment_skin, container, false);

        //get selected champion
        Bundle bundle = getArguments();
        champion = new Gson().fromJson(bundle.getString("Champion"), Champion.class);

        gvSkins = (GridView) view.findViewById(R.id.gvSkins);
        gvSkins.setAdapter(new SkinAdapter());

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

    class SkinAdapter extends ArrayAdapter<Skin> {
        private ArrayList<Skin> skins = new ArrayList<Skin>();

        public SkinAdapter() {
            super(getActivity(), R.layout.cel_skin, R.id.txtVSkinName);

            //champions die op de mainactivity worden opgehaald gebruiken
            skins = champion.getSkins();

            this.addAll(skins);
        }

        class ViewHolder {
            ImageView imgSkin;

            TextView txtSkinName;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //View row = super.getView(position, convertView, parent);
            ViewHolder viewHolder = new ViewHolder();

            final Skin skin = skins.get(position);

            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.cel_skin, null);
                viewHolder = new ViewHolder();

                viewHolder.txtSkinName = (TextView) convertView.findViewById(R.id.txtVSkinName);
                viewHolder.imgSkin = (ImageView) convertView.findViewById(R.id.imgVSkin);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.txtSkinName.setText(skin.getName());
            int skinImageId = getResources().getIdentifier(skin.getImage().toLowerCase(), "drawable", getActivity().getPackageName());
            viewHolder.imgSkin.setImageResource(skinImageId);
            viewHolder.imgSkin.setAdjustViewBounds(true);

            return convertView;
        }


    }
}
