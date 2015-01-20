package be.howest.lolmetabuilder;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class GenerateBuildResultFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public static GenerateBuildResultFragment newInstance() {
        GenerateBuildResultFragment fragment = new GenerateBuildResultFragment();

        return fragment;
    }

    public GenerateBuildResultFragment() {
        // Required empty public constructor
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_my_generate_build_result, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()){
            case R.id.action_save:
                showFileNamePrompt();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        View view = inflater.inflate(R.layout.fragment_generate_build_result, container, false);

        //reset tabs
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        setHasOptionsMenu(true);

        return view;
    }

    public void showFileNamePrompt(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity().getBaseContext());

        alert.setTitle("Filename");
        alert.setMessage("Please enter a name for the build");

        // Set an EditText view to get user input
        final EditText input = new EditText(getActivity().getBaseContext());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                //Trim front & end spaces from the input
                value = value.trim();
                //replace " " with a "_"
                value = value.replace(" ", "_");

                writeFile(value);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    public void writeFile(String value){

        if(isExternalStorageWritable())
        {
            File root = android.os.Environment.getExternalStorageDirectory();

            File dir = new File(root.getAbsolutePath() + "/LoLMetaBuilder");
            dir.mkdirs();

            String fileName = MainActivity.championBuild.getChampion().getName() + "_" + value + ".bld";

            File file = new File(dir, fileName);

            //Toast.makeText(getBaseContext(), "" + file, Toast.LENGTH_LONG).show();

            if(!file.exists())
            {
                try{
                    FileOutputStream f = new FileOutputStream(file);
                    PrintWriter pw = new PrintWriter(f);

                    pw.println("{");
                    pw.println("\"Build\":{");
                    pw.println("\"Champion\":" + MainActivity.championBuild.getChampion().getId()+",");

                    pw.println("\"Items\":[");
                    for(int i=0; i< MainActivity.championBuild.getItems().length; i++)
                    {
                        if(i<5)
                        {
                            pw.println("{\"Item\":" + MainActivity.championBuild.getItems()[i].getId()+ "},");
                        }else{
                            pw.println("{\"Item\":" + MainActivity.championBuild.getItems()[i].getId() + "}");
                        }
                    }
                    pw.println("]");
                    pw.println("}");
                    pw.println("}");

                    pw.flush();
                    pw.close();
                    f.close();

                    Toast.makeText(getActivity().getBaseContext(), "Build saved", Toast.LENGTH_LONG).show();
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getActivity().getBaseContext(), "Filename already exists", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
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

}
