package br.com.joinersa.contaeletrica.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import br.com.joinersa.contaeletrica.activity.HistoricoActivity;
import br.com.joinersa.contaeletrica.fragments.FragmentCuriosidades;
import br.com.joinersa.contaeletrica.fragments.FragmentDicas;
import br.com.joinersa.contaeletrica.fragments.FragmentDireitosDeveres;
import br.com.joinersa.contaeletrica.fragments.FragmentHistoricoConsumo;
import br.com.joinersa.contaeletrica.fragments.FragmentHistoricoCusto;

/**
 * Created by eliez on 31/10/2016.
 */

public class TabsAdapter extends FragmentStatePagerAdapter {
    CharSequence[] titulos;
    int numeroAbas;
    Object object;
    private String ano;
    private FragmentHistoricoConsumo fragmentHistoricoConsumo;
    private FragmentHistoricoCusto fragmentHistoricoCusto;

    public TabsAdapter(FragmentManager fm, Object object, CharSequence[] titulos, int numeroAbas) {
        super(fm);
        this.object = object;
        this.titulos = titulos;
        this.numeroAbas = numeroAbas;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    @Override
    public Fragment getItem(int position) {


        if (object instanceof HistoricoActivity){
            Log.i("script", "getItem()");

            fragmentHistoricoConsumo = new FragmentHistoricoConsumo();
            fragmentHistoricoCusto = new FragmentHistoricoCusto();
            Bundle bundle = new Bundle();
            bundle.putString("ano", ano);
            fragmentHistoricoConsumo.setArguments(bundle);
            fragmentHistoricoCusto.setArguments(bundle);

            switch (position){
                case 0 : return fragmentHistoricoConsumo;
                case 1 : return fragmentHistoricoCusto;
            }
        } else {
            switch (position){
                case 0 : return new FragmentDicas();
                case 1 : return new FragmentDireitosDeveres();
                case 2 : return new FragmentCuriosidades();
            }
        }

        return null;
    }

    @Override
    public int getCount() {
        return numeroAbas;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titulos[position];
    }
}
