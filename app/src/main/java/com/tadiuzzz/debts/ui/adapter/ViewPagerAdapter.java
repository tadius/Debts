package com.tadiuzzz.debts.ui.adapter;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tadiuzzz.debts.ui.view.DebtsFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        position = position + 1;
        Bundle bundle = new Bundle();

        switch (position) {
            case 1:
                DebtsFragment debtsFragmentBorrower = new DebtsFragment();
                bundle.putInt("position", position);
                debtsFragmentBorrower.setArguments(bundle);
                return debtsFragmentBorrower;
            case 2:
                DebtsFragment debtsFragmentCreditor = new DebtsFragment();
                bundle.putInt("position", position);
                debtsFragmentCreditor.setArguments(bundle);
                return debtsFragmentCreditor;
            default:
                DebtsFragment debtsFragmentDefault = new DebtsFragment();
                bundle.putInt("position", position);
                debtsFragmentDefault.setArguments(bundle);
                return debtsFragmentDefault;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        position = position + 1;

        switch (position) {
            case 1:
                return "Мне должны";
            case 2:
                return "Я должен";
            default:
                return "";
        }
    }
}
