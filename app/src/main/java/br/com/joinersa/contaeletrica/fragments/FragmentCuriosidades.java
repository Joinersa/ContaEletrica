package br.com.joinersa.contaeletrica.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.joinersa.contaeletrica.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCuriosidades extends Fragment {

    public FragmentCuriosidades() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_curiosidades, container, false);
    }

}
