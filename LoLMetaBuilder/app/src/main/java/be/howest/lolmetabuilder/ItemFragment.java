package be.howest.lolmetabuilder;

import android.app.ActionBar;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import be.howest.lolmetabuilder.data.models.Item;

public class ItemFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Item selectedItem = null;
    private int gridPosition;

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

        //reset tabs
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        //gekozen champion ophalen
        Bundle bundle = getArguments();
        gridPosition = bundle.getInt("GridPosition");
        Item item = new Gson().fromJson(bundle.getString("Item"), Item.class);
        //TODO req & builds into id's ophalen en objecten ervan ophalen. Daarna in
        String from = bundle.getString("From");
        selectedItem = item;

        //layout elementen ophalen
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.imgItem = (ImageView) view.findViewById(R.id.imgVItemAvatar);
        viewHolder.txtItemName = (TextView) view.findViewById(R.id.txtVItemName);
        viewHolder.txtItemPrice = (TextView) view.findViewById(R.id.txtVItemPrice);
        viewHolder.txtItemDescription = (TextView) view.findViewById(R.id.txtVItemDescription);
        viewHolder.gVRequires = (GridView) view.findViewById(R.id.gVRequires);
        viewHolder.gvBuildsInto = (GridView) view.findViewById(R.id.gVBuildsInto);
        viewHolder.btnAddToBuild = (Button) view.findViewById(R.id.btnAddToBuild);

        //layout invullen

        // Ik heb bij Item een nieuw property gemaakt image.

        int itemImageId = getResources().getIdentifier("i1001", "drawable", getActivity().getPackageName());
        viewHolder.imgItem.setImageResource(itemImageId);
        viewHolder.txtItemName.setText(item.getName());
        viewHolder.txtItemPrice.setText(item.getTotalGold() + " gold");
        viewHolder.txtItemDescription.setText(item.getDescription());
        if(from.equals("Items")) {
            viewHolder.btnAddToBuild.setVisibility(View.GONE);
        }
        if(from.equals("Builds")){
            viewHolder.btnAddToBuild.setVisibility(View.VISIBLE);

            viewHolder.btnAddToBuild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //gekozen item met de fragment meesturen
                    Fragment fragment = GeneratedBuildFragment.newInstance();

                    Bundle args = new Bundle();
                    args.putString("Item", new Gson().toJson(selectedItem));

                    fragment.setArguments(args);

                    //Item in het build object steken
                    Item[] buildItems = MainActivity.championBuild.getItems();
                    buildItems[gridPosition] = selectedItem;
                    MainActivity.championBuild.setItems(buildItems);


                    //openen fragment
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .addToBackStack("GeneratedBuild")
                            .commit();
                }
            });
        }

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
