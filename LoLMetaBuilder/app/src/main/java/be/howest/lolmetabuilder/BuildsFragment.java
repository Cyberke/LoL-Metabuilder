package be.howest.lolmetabuilder;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class BuildsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ListView lstBuilds;
    private ArrayList<File> files = new ArrayList<File>();

    public static BuildsFragment newInstance() {
        BuildsFragment fragment = new BuildsFragment();

        return fragment;
    }
    public BuildsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_builds, container, false);

        MainActivity.drawerList.setSelection(3);
        MainActivity.drawerList.setItemChecked(3, true);

        //reset tabs
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/LoLMetaBuilder");
        //files ophalen in folder
        files = getDirectoryFiles(dir);

        lstBuilds = (ListView) view.findViewById(R.id.lstBuilds);
        lstBuilds.setAdapter(new BuildAdapter());

        return view;
    }

    private ArrayList<File> getDirectoryFiles(File dirPath)
    {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = dirPath.listFiles();
        for(File file : files)
        {
            if(!file.isDirectory())
            {
                if(file.getName().endsWith(".bld"))
                {
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
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


    class BuildAdapter extends ArrayAdapter<File>
    {
        public BuildAdapter(){
            super(getActivity().getBaseContext(), R.layout.listitem_build, R.id.txtFilename);

            this.addAll(files);
        }

        class ViewHolder {
            TextView txtFilename;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //View row = super.getView(position, convertView, parent);
            ViewHolder viewHolder = new ViewHolder();

            final File file = files.get(position);

            if(convertView == null)
            {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.listitem_build, null);
                viewHolder = new ViewHolder();

                viewHolder.txtFilename = (TextView) convertView.findViewById(R.id.txtFilename);

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.txtFilename.setText(file.getName());

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity().getBaseContext(), file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    /*Intent intent = new Intent(getBaseContext(), Build_detail.class);
                    intent.putExtra(Intent.EXTRA_TEXT, file.getAbsolutePath());

                    startActivity(intent);*/
                }
            });

            return convertView;
        }
    }

}
