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
import android.widget.TextView;

import com.google.gson.Gson;

import be.howest.lolmetabuilder.data.models.Champion;

public class LoreFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    Champion champion;

    public static LoreFragment newInstance() {
        LoreFragment fragment = new LoreFragment();

        return fragment;
    }
    public LoreFragment() {
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
        View view = inflater.inflate(R.layout.fragment_lore, container, false);

        //get selected champion
        Bundle bundle = getArguments();
        //champion = new Gson().fromJson(bundle.getString("Champion"), Champion.class);
        champion = MainActivity.currentChampion;

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.txtLoreTitle = (TextView) view.findViewById(R.id.txtVLoreTitle);
        viewHolder.txtLoreText = (TextView) view.findViewById(R.id.txtVLore);

        viewHolder.txtLoreTitle.setText(champion.getName() + " " + champion.getTitle());
        viewHolder.txtLoreText.setText(champion.getLore());

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

    class ViewHolder {
        TextView txtLoreTitle,
                txtLoreText;
    }

}
