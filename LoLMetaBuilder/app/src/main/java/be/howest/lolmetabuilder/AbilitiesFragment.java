package be.howest.lolmetabuilder;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import be.howest.lolmetabuilder.data.models.Champion;
import be.howest.lolmetabuilder.data.models.Spell;

public class AbilitiesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Champion champion;
    private GridView gvSpells;

    public static AbilitiesFragment newInstance() {
        AbilitiesFragment fragment = new AbilitiesFragment();

        return fragment;
    }
    public AbilitiesFragment() {
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
        // Inflate the layout wssfor this fragment
        View view = inflater.inflate(R.layout.fragment_abilities, container, false);

        //get selected champion
        Bundle bundle = getArguments();
        champion = new Gson().fromJson(bundle.getString("Champion"), Champion.class);

        gvSpells = (GridView) view.findViewById(R.id.gVAbilities);
        gvSpells.setAdapter(new SpellAdapter());

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

    @Override
    public void onDestroy() {

        AbilitiesFragment f = (AbilitiesFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
        if (f.isResumed()){
            getFragmentManager().beginTransaction().remove(f).commit();
        }

        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    class SpellAdapter extends ArrayAdapter<Spell> {
        private ArrayList<Spell> spells = new ArrayList<Spell>();

        public SpellAdapter() {
            super(getActivity(), R.layout.cel_spell, R.id.txtVAbilityName);

            //champions die op de mainactivity worden opgehaald gebruiken
            spells = champion.getSpells();

            this.addAll(spells);
        }

        class ViewHolder {
            ImageView imgAbility;

            TextView txtName,
                    txtKeybind,
                    txtDesc,
                    txtCost,
                    txtCooldown,
                    txtRange;

            Button btnVideo;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //View row = super.getView(position, convertView, parent);
            ViewHolder viewHolder = new ViewHolder();

            final Spell spell = spells.get(position);

            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.cel_spell, null);
                viewHolder = new ViewHolder();

                viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtVAbilityName);
                viewHolder.txtKeybind = (TextView) convertView.findViewById(R.id.txtVKeybind);
                viewHolder.txtDesc = (TextView) convertView.findViewById(R.id.txtVAbilityDesc);
                viewHolder.txtCost = (TextView) convertView.findViewById(R.id.txtVAbilityCost);
                viewHolder.txtCooldown = (TextView) convertView.findViewById(R.id.txtVAbilityCooldown);
                viewHolder.txtRange = (TextView) convertView.findViewById(R.id.txtVAbilityRange);
                viewHolder.imgAbility = (ImageView) convertView.findViewById(R.id.imgVAbility);
                viewHolder.btnVideo = (Button) convertView.findViewById(R.id.btnVideo);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //TODO keybind, cost
            viewHolder.txtName.setText(spell.getName());
            viewHolder.txtKeybind.setText("Keybind");
            viewHolder.txtDesc.setText(spell.getDescription());
            viewHolder.txtCost.setText("Cost");
            viewHolder.txtCooldown.setText(spell.getCooldown());
            viewHolder.txtRange.setText(spell.getRange());

            int abilityImageId = getResources().getIdentifier(spell.getImage().toLowerCase(), "drawable", getActivity().getPackageName());
            viewHolder.imgAbility.setImageResource(abilityImageId);
            //viewHolder.imgAbility.setAdjustViewBounds(true);
            viewHolder.btnVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openVideo(view, position);
                }
            });

            return convertView;
        }

        private void openVideo(View v, int position)
        {

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String PACKAGE_NAME = getActivity().getApplicationContext().getPackageName();
                ApplicationInfo appInfo = getActivity().getPackageManager().getApplicationInfo(PACKAGE_NAME, PackageManager.GET_META_DATA);

                //get all values to construct the link
                String baseVideoURL = appInfo.metaData.getString("resource.spellvideo");
                String championId = String.format("%04d", champion.getId());
                String spellId = String.format("%02d", position+2);
                String videoURL = baseVideoURL + championId + "_" + spellId + ".mp4";

                //put url in intent and start
                intent.setDataAndType(Uri.parse(videoURL), "video/mp4");
                startActivity(intent);

                //TODO applicatie crasht na video
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }


    }

}
