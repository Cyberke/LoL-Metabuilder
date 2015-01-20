package be.howest.lolmetabuilder;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;

import be.howest.lolmetabuilder.data.models.*;

public class GeneratedBuildFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private static Item item;
    private Champion champion;
    private ArrayList<buildResult> results = new ArrayList<buildResult>();

    public static GeneratedBuildFragment newInstance() {
        GeneratedBuildFragment fragment = new GeneratedBuildFragment();

        return fragment;
    }
    public GeneratedBuildFragment() {
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
        View view = inflater.inflate(R.layout.fragment_generated_build, container, false);

        Bundle bundle = getArguments();
        //Champion champion = new Gson().fromJson(bundle.getString("Champion"), Champion.class);
        champion = MainActivity.currentChampion;

        //reset tabs
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        final ViewHolder viewHolder = new ViewHolder();

        viewHolder.imgVChampAvatar = (ImageView) view.findViewById(R.id.imgVChampionAvatar);
        viewHolder.gvBuildItems = (GridView) view.findViewById(R.id.gvBuildItems);
        viewHolder.txtLimitGold = (EditText) view.findViewById(R.id.txtLimitTotalGold);
        viewHolder.btnGenerateBuild = (Button) view.findViewById(R.id.btnGenerateBuild);
        viewHolder.cbAttack = (CheckBox) view.findViewById(R.id.cbAttack);
        viewHolder.cbAbility = (CheckBox) view.findViewById(R.id.cbAbility);
        viewHolder.cbArmor = (CheckBox) view.findViewById(R.id.cbArmor);
        viewHolder.cbMagicResist = (CheckBox) view.findViewById(R.id.cbMagicResist);
        viewHolder.cbLifesteal = (CheckBox) view.findViewById(R.id.cbLifesteal);
        viewHolder.cbSpellVamp = (CheckBox) view.findViewById(R.id.cbSpellVamp);

        int champImageId = getResources().getIdentifier(champion.getImage().toLowerCase(), "drawable", getActivity().getPackageName());
        viewHolder.imgVChampAvatar.setImageResource(champImageId);
        viewHolder.gvBuildItems.setAdapter(new ItemAdapter());

        //event on checkboxes
        ArrayList<CheckBox> chkStats = new ArrayList<CheckBox>();

        chkStats.add(viewHolder.cbAttack);
        chkStats.add(viewHolder.cbAbility);
        chkStats.add(viewHolder.cbArmor);
        chkStats.add(viewHolder.cbMagicResist);
        chkStats.add(viewHolder.cbLifesteal);
        chkStats.add(viewHolder.cbSpellVamp);


        for(CheckBox chkStat : chkStats) {
            chkStat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox check = (CheckBox) view;
                    if(check.isChecked())
                    {
                        MainActivity.championBuild.addPrioriteit(check.getTag().toString());
                    }else{
                        MainActivity.championBuild.removePrioriteit(check.getTag().toString());
                    }
                }
            });
        }

        //when submitting build settings
        viewHolder.btnGenerateBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //ingevulde waarden in build object steken
                String slimitGold = viewHolder.txtLimitGold.getText().toString();
                int limitGold = 0;
                if(!slimitGold.equals(""))
                {
                    limitGold = Integer.parseInt(slimitGold);
                }else{
                    limitGold = 0;
                }
                MainActivity.championBuild.setLimitGold(limitGold);

                //TODO Milan: hier is op de button geduwt en word de limitgold nog in het object gestoken. hierna mag het algoritme lopen (misch andere functie)
                ArrayList<Item> filterItems = new ArrayList<Item>();
                results = new ArrayList<buildResult>();
                for(Item i : MainActivity.items)
                {
                    for(Stat s : i.getStats())
                    {
                        for(String p : MainActivity.championBuild.getPrioriteit())
                        {
                            if(p.equals("Attack"))
                            {
                                if(s.getName().equals("FlatPhysicalDamageMod") && !filterItems.contains(i))
                                    filterItems.add(i);
                            }
                            else if(p.equals("Ability"))
                            {
                                if(s.getName().equals("FlatMagicDamageMod") && !filterItems.contains(i))
                                    filterItems.add(i);
                            }
                            else if(p.equals("Armor"))
                            {
                                if(s.getName().equals("FlatArmorMod") && !filterItems.contains(i))
                                    filterItems.add(i);
                            }
                            else if(p.equals("Magic Resist"))
                            {
                                if(s.getName().equals("FlatSpellBlockMod") && !filterItems.contains(i))
                                    filterItems.add(i);
                            }
                            else if(p.equals("Lifesteal"))
                            {
                                if(s.getName().equals("PercentLifeStealMod") && !filterItems.contains(i))
                                    filterItems.add(i);
                            }
                            else if(p.equals("Spell Vamp"))
                            {
                                if(s.getName().equals("PercentSpellVampMod") && !filterItems.contains(i))
                                    filterItems.add(i);
                            }
                        }
                    }
                }
                //generateAlgorithm(filterItems, 0, MainActivity.championBuild.getItems(), true);



            }
        });

        return view;
    }

    public int generateAlgorithm(ArrayList<Item> itemlist, int position, ArrayList<Item> from, boolean debug)
    {
        int counter = 0;
        if(itemlist.size() == 6)
        {
            if(debug)
            {
                String IDs = "";
                for(int i = 0; i < itemlist.size(); i++)
                {
                    IDs += ""+itemlist.get(i).getId() +" ";
                }
                Log.d("Debug:", IDs);
            }
            buildResult result = buildResult.setBuildResult(itemlist);
            if(result.getTotalGold() < MainActivity.championBuild.getLimitGold())
                results.add(result);

            return 1;
        }
        for(int i = position; i < from.size(); i++)
        {
            itemlist.add(from.get(i));
            counter += generateAlgorithm(itemlist, i, from, debug);
            itemlist.remove(itemlist.size()-1);
        }
        return counter;
    }



    class ViewHolder {
        ImageView imgVChampAvatar;

        GridView gvBuildItems;

        EditText txtLimitGold;

        Button btnGenerateBuild;

        CheckBox cbAttack, cbAbility, cbArmor, cbMagicResist, cbLifesteal, cbSpellVamp;
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
        private Item[] items = new Item[6];

        public ItemAdapter() {
            super(getActivity(), R.layout.cel_item, R.id.txtItemPrice);

            //items die op de mainactivity worden opgehaald gebruiken
            items = MainActivity.championBuild.getItems();

            this.addAll(items);
        }

        class ViewHolder {
            RelativeLayout imgItem;

            TextView txtItemPrice;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //View row = super.getView(position, convertView, parent);
            ViewHolder viewHolder = new ViewHolder();

            final Item item2 = items[position];

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

            if(item2 == null)
            {
                viewHolder.imgItem.setBackground(getDrawableResourceByName("itemplaceholder"));
            }else{
                //viewHolder.imgItem.setBackground(getDrawableResourceByName(item.getId()+""));
                viewHolder.imgItem.setBackground(getDrawableResourceByName("i1001"));
            }

            viewHolder.txtItemPrice.setVisibility(View.GONE);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item = item2;


                    //Openen item detail met fragment

                    //gekozen item met de fragment meesturen
                    Fragment fragment = ItemsFragment.newInstance();
                    Bundle args = new Bundle();
                    args.putString("Item", new Gson().toJson(item));
                    args.putString("From", "Builds");
                    args.putInt("GridPosition", position);
                    fragment.setArguments(args);

                    //openen fragment
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, fragment)
                            .addToBackStack("Item")
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
