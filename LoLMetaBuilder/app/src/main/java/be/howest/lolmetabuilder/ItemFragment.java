package be.howest.lolmetabuilder;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import be.howest.lolmetabuilder.data.Item;

public class ItemFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public static ItemFragment newInstance() {
        ItemFragment fragment = new ItemFragment();

        return fragment;
    }

    public ItemFragment() {
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
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        //gekozen champion ophalen
        Bundle bundle = getArguments();
        Item item = new Gson().fromJson(bundle.getString("Item"), Item.class);

        //layout elementen ophalen
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.imgItem = (ImageView) view.findViewById(R.id.imgVItemAvatar);
        viewHolder.txtItemName = (TextView) view.findViewById(R.id.txtVItemName);
        viewHolder.txtItemPrice = (TextView) view.findViewById(R.id.txtVItemPrice);
        viewHolder.txtItemDescription = (TextView) view.findViewById(R.id.txtVItemDescription);
        viewHolder.gVRequires = (GridView) view.findViewById(R.id.gVRequires);
        viewHolder.gvBuildsInto = (GridView) view.findViewById(R.id.gVBuildsInto);

        //layout invullen
        int itemImageId = getResources().getIdentifier("i1001", "drawable", getActivity().getPackageName());
        viewHolder.imgItem.setImageResource(itemImageId);
        viewHolder.txtItemName.setText(item.getName());
        viewHolder.txtItemPrice.setText(item.getTotalGold() + " gold");
        viewHolder.txtItemDescription.setText(item.getDescription());

        //TODO gridviews opvullen


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

    class ViewHolder {
        ImageView imgItem;

        TextView txtItemName,
                txtItemPrice,
                txtItemDescription;

        GridView gVRequires,
                gvBuildsInto;

        Button btnAddToBuild;

    }

}
