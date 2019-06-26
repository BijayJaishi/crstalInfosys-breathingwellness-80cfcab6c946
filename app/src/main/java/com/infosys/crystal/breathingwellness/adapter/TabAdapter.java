package com.infosys.crystal.breathingwellness.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.infosys.crystal.breathingwellness.tab_fragments.CallsFragment;
import com.infosys.crystal.breathingwellness.tab_fragments.ChatsFragment;
import com.infosys.crystal.breathingwellness.tab_fragments.GroupsFragment;

public class TabAdapter extends FragmentPagerAdapter {

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int i) {

        switch (i){
            case 0:
                ChatsFragment chatsFragment = new ChatsFragment();

                return chatsFragment;

            case 1:
                GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;

            case 2:
                CallsFragment callsFragment = new CallsFragment();
                return callsFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:

                return "Chats";

            case 1:

                return "Groups";

            case 2:

                return "Calls";

            default:
                return null;
        }
    }
}
