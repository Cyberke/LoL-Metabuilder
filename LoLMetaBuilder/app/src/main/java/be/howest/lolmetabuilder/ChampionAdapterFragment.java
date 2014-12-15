package be.howest.lolmetabuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.gson.Gson;

import be.howest.lolmetabuilder.data.models.Champion;

/**
 * Created by jelle on 13/12/2014.
 */
public class ChampionAdapterFragment extends FragmentPagerAdapter {
    //TODO er word slechts 1 bundle gestuurd naar deze klasse, als je de eerste keer tabs opent.

    Bundle bundle;

    public ChampionAdapterFragment(FragmentManager fm, Bundle bundle)
    {
        super(fm);
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int i) {

        switch(i)
        {
            case 0:
                Fragment champoverview = ChampionOverviewFragment.newInstance();
                champoverview.setArguments(bundle);
                return champoverview;
            case 1:
                Fragment champskin = SkinFragment.newInstance();
                champskin.setArguments(bundle);
                return champskin;
            case 2:
                Fragment champlore = LoreFragment.newInstance();
                champlore.setArguments(bundle);
                return champlore;
            case 3:
                Fragment champabilities = AbilitiesFragment.newInstance();
                champabilities.setArguments(bundle);
                return champabilities;
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
