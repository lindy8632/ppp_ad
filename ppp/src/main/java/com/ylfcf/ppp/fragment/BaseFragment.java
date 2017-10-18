package com.ylfcf.ppp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * fragment»ù´¡Àà
 * @author Administrator
 *
 */
public class BaseFragment extends Fragment{
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
