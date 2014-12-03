package be.howest.lolmetabuilder;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import be.howest.lolmetabuilder.data.Champion;
import be.howest.lolmetabuilder.data.FreeChamp;

/**
 * Created by manuel on 12/3/14.
 */
public class PlaceholderFragment extends Fragment {
    private GridView gvFreeChamps;

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        gvFreeChamps = (GridView) view.findViewById(R.id.gvFreeChamps);
        gvFreeChamps.setAdapter(new FreeChampionAdapter());

        return view;
    }

    class FreeChampionAdapter extends ArrayAdapter<FreeChamp> {
        public FreeChampionAdapter() {
            super(getActivity(), R.layout.cel_champ, R.id.txtChampName);

            this.addAll(MyActivity.freeChamps);
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

            final Champion champ = MyActivity.freeChamps.get(position).getChampion();

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

            viewHolder.txtChampName.setText(champ.getName());
            viewHolder.txtChampPrice.setAlpha(0);
            viewHolder.imgChamp.setBackground(getDrawableResourceByName(champ.getImage().toLowerCase()));

            return convertView;
        }

        private Drawable getDrawableResourceByName(String name) {
            String packageName = getActivity().getPackageName();
            int resId = getResources().getIdentifier( name, "drawable", packageName);
            return getResources().getDrawable(resId);
        }
    }
}
