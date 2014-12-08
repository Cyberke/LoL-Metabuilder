package be.howest.lolmetabuilder;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import be.howest.lolmetabuilder.data.Item;

public class ItemsFragment extends Fragment {

    public GridView gvItems;

    private OnFragmentInteractionListener mListener;

    public static ItemsFragment newInstance() {
        ItemsFragment fragment = new ItemsFragment();

        return fragment;
    }

    public ItemsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_items, container, false);

        gvItems = (GridView) view.findViewById(R.id.gVItems);
        gvItems.setAdapter(new ItemAdapter());

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

    class ItemAdapter extends ArrayAdapter<Item> {
        private ArrayList<Item> items = new ArrayList<Item>();

        public ItemAdapter() {
            super(getActivity(), R.layout.cel_item, R.id.txtItemPrice);

            //items die op de mainactivity worden opgehaald gebruiken
            items = MyActivity.items;

            this.addAll(items);
        }

        class ViewHolder {
            RelativeLayout imgItem;
            TextView txtItemPrice;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //View row = super.getView(position, convertView, parent);
            ViewHolder viewHolder = new ViewHolder();

            final Item item = items.get(position);

            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.cel_item, null);
                viewHolder = new ViewHolder();

                viewHolder.imgItem = (RelativeLayout) convertView.findViewById(R.id.imgItem);
                viewHolder.txtItemPrice = (TextView) convertView.findViewById(R.id.txtItemPrice);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.txtItemPrice.setText("" + item.getBaseGold());
            //TODO alle items in drawable steken
            //viewHolder.imgItem.setBackground(getDrawableResourceByName(item.getId()+""));
            viewHolder.imgItem.setBackground(getDrawableResourceByName("i1001"));

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Openen item detail met fragment

                    //gekozen item met de fragment meesturen
                    Fragment fragment = new ItemFragment();
                    Bundle args = new Bundle();
                    args.putString("Item", new Gson().toJson(item));
                    fragment.setArguments(args);

                    //openen fragment
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            return convertView;
        }

        private Drawable getDrawableResourceByName(String name) {
            String packageName = getActivity().getPackageName();
            int resId = getResources().getIdentifier(name, "drawable", packageName);
            return getResources().getDrawable(resId);
        }
    }
}
