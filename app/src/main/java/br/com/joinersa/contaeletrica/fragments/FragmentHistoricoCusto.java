package br.com.joinersa.contaeletrica.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;

import java.util.List;

import br.com.joinersa.contaeletrica.Grafico;
import br.com.joinersa.contaeletrica.R;
import br.com.joinersa.contaeletrica.dao.HistoricoDAO;
import br.com.joinersa.contaeletrica.entities.Historico;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHistoricoCusto extends Fragment {

    private HorizontalBarChart mChart;
    private TextView tvAno;

    public FragmentHistoricoCusto() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historico_custo, container, false);

        mChart = (HorizontalBarChart) view.findViewById(R.id.chart2);

        String ano = this.getArguments().getString("ano");
        tvAno = (TextView) view.findViewById(R.id.tv_ano_custo);
        tvAno.setText(ano);

        List<Historico> lista = new HistoricoDAO(getActivity()).buscar(getActivity(), ano);

        Grafico.criarGrafico(lista, mChart, "R$");

        return view;
    }
}
