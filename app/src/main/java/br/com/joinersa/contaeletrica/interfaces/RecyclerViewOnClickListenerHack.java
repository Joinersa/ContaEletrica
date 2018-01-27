package br.com.joinersa.contaeletrica.interfaces;

import android.view.View;

/**
 * Created by Joiner on 16/07/2016.
 */
public interface RecyclerViewOnClickListenerHack {

    public void onClickListener(View view, int position);
    public void onLongPressClickListener(View view, int position);

}