package com.example.danil.skilder;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;


public class BaseFragment extends Fragment {;


    private OnFragmentInteractionListener mListener;

    public BaseFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    protected void onFragmentInteraction( String action, Bundle extra){
        mListener.onFragmentInteraction(this.getClass(),action, extra);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Class clazz, String action, Bundle extra);
    }
}
