package br.com.joinersa.contaeletrica.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import br.com.joinersa.contaeletrica.entities.Historico;
import br.com.joinersa.contaeletrica.entities.Item;
import br.com.joinersa.contaeletrica.entities.ItemComCustoMensal;
import br.com.joinersa.contaeletrica.R;
import br.com.joinersa.contaeletrica.adapter.ItemResultadoAdapter;
import br.com.joinersa.contaeletrica.dao.HistoricoDAO;
import br.com.joinersa.contaeletrica.dao.ItemDAO;

public class ResultadoActivity extends AppCompatActivity { // implements OnChartValueSelectedListener {

    private TextView tvValorTotal, tvConsumoTotal;
    private static final String PREF_CONCESSIONARIA = "preferencia";

    private RecyclerView recyclerViewItemResultado;
    private List<ItemComCustoMensal> itemResultadoList = new ArrayList<>();
    private ItemResultadoAdapter itemResultadoAdapter;

    //private HorizontalBarChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_resultado);
        //toolbar.setTitle("Resultados");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// bt voltar


        tvValorTotal = (TextView) findViewById(R.id.tv_valor_total);
        tvConsumoTotal = (TextView) findViewById(R.id.tv_consumo_total);

        ItemDAO itemDAO = new ItemDAO(this);
        float consumoTotal = itemDAO.getSomaConsumo();

        SharedPreferences preferences = getSharedPreferences(PREF_CONCESSIONARIA, Context.MODE_PRIVATE);
        float taxa = preferences.getFloat("valor", 0); // taxa da concessionária

        NumberFormat numFormatado = NumberFormat.getCurrencyInstance();
        //NumberFormat numFormatado = NumberFormat.getNumberInstance(Locale.FRANCE);
        tvValorTotal.setText("R$ " + numFormatado.format(consumoTotal * taxa).replace("R$", ""));

        numFormatado = NumberFormat.getNumberInstance(Locale.FRANCE);
        tvConsumoTotal.setText(numFormatado.format(consumoTotal) + " kWh");

        List<Item> listaItens = itemDAO.buscar();
        List<ItemComCustoMensal> listaItemComCustoMensal = new ArrayList<>();

        // int idImagem, String nome, float potencia, float tempo, String periodo, int quantidade, float consumoMes, float custoMensal
        for(int i = 0; i < listaItens.size(); i++) {
            listaItemComCustoMensal.add(new ItemComCustoMensal(
                    listaItens.get(i).getIdImagem(),
                    listaItens.get(i).getNome(),
                    listaItens.get(i).getPotencia(),
                    listaItens.get(i).getTempo(),
                    listaItens.get(i).getPeriodo(),
                    listaItens.get(i).getQuantidade(),
                    listaItens.get(i).getConsumoMes(),
                    (listaItens.get(i).getConsumoMes() * taxa) // custo mensal --- (consumoMensal * taxa)
            ));
        }


        // --------------------------------- RecyclerView ------------------------------------------
        recyclerViewItemResultado = (RecyclerView) findViewById(R.id.rv_resultado);
        itemResultadoAdapter = new ItemResultadoAdapter(itemResultadoList);
        recyclerViewItemResultado.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewItemResultado.setLayoutManager(layoutManager);
        recyclerViewItemResultado.setItemAnimator(new DefaultItemAnimator());
        recyclerViewItemResultado.setAdapter(itemResultadoAdapter);


        itemResultadoList.addAll(listaItemComCustoMensal);
        itemResultadoAdapter.notifyDataSetChanged();
        // -----------------------------------------------------------------------------------------

        ScrollView scrollView = (ScrollView) findViewById(R.id.sv_resultado);
        scrollView.fullScroll(View.FOCUS_UP); // deixar scroll no topo para não se mover


        // ------------------------ Inserir dados na tabela de historico -------------------------
        // pegando data atual do sistema
        Date data = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(data);
        int mesAtual = calendar.get(Calendar.MONTH) + 1; // somando 1, pois o mês em java começa em zero.
        int anoAtual = calendar.get(Calendar.YEAR);

        Historico historico = new Historico(mesAtual, anoAtual, consumoTotal);

        HistoricoDAO historicoDAO = new HistoricoDAO(getApplicationContext());
        historicoDAO.inserir(historico);

        // -----------------------------------------------------------------------------------------


        // -------------------------------- Horisontal Chart ---------------------------------------
        /*
        mChart = (HorizontalBarChart) findViewById(R.id.pc_grafico_itens);
        //mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(true); // false
        mChart.setDrawValueAboveBar(true); // true - valor na frente ou em cima das barras.
        mChart.setDrawMarkerViews(true);
        mChart.setDescription(""); // ""
        mChart.setMaxVisibleValueCount(60); // 60
        mChart.setPinchZoom(true); // false
        mChart.setDrawGridBackground(true); // false
        mChart.setFitBars(true); // true
        mChart.animateY(2500); // 2500
        //mChart.getXAxis().setDrawLabels(false); //some valores no eixo y
        //mChart.getDefaultValueFormatter();
        //mChart.setHighlightFullBarEnabled(false); // true

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xl.setTypeface(mTfLight);
        xl.setDrawAxisLine(true); // true
        xl.setDrawGridLines(false); // false
        xl.setGranularity(1f); // 10
        xl.setDrawLabels(true);
        xl.setDrawLimitLinesBehindData(true);
        //xl.setDrawLabels(false);

        YAxis yl = mChart.getAxisLeft();
        //yl.setTypeface(Typeface.SERIF);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        //yl.setInverted(true);
        yl.setSpaceTop(20f);

        YAxis yr = mChart.getAxisRight();
        //yr.setTypeface(mTfLight);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(true);
        yr.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        //yr.setInverted(true);

        float spaceForBar = 1f;
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < listaItens.size(); i++) {
            yVals1.add(new BarEntry(i * spaceForBar, listaItens.get(i).getConsumoMes()*taxa, listaItens.get(i).getNome()));
        }

        // creamos una lista de colores
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.MATERIAL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.COLORFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.LIBERTY_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.PASTEL_COLORS) {
            colors.add(c);
        }
        for (int c : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(c);
        }

        colors.add(ColorTemplate.getHoloBlue());

        final BarDataSet dataset = new BarDataSet(yVals1, "Legenda");
        dataset.setValueTypeface(Typeface.defaultFromStyle(2));
        dataset.setValueTextColor(Color.BLACK);
        dataset.setBarBorderWidth(0.5f);
        dataset.setColors(colors);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT); // BELOW_CHART_LEFT
        l.setFormSize(8f);
        l.setXEntrySpace(4f);

        final BarData data = new BarData(dataset);
        data.setValueTextSize(10f);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                NumberFormat numFormatado = NumberFormat.getCurrencyInstance();
                return "R$ " + numFormatado.format(value).replace("R$", "");
            }
        });

        mChart.setData(data); // set the data and list of lables into chart

        xl.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "" + dataset.getEntryForXPos(value).getData();
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        */
        // ---------------------------------- fim chart---------------------------------------------
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }


    // ------------------ OnChartValueSelectedListener -------------------------
    /*
    protected RectF mOnValueSelectedRectF = new RectF();
    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;

        //Toast.makeText(ResultadoActivity.this, "" + e.getData().toString(), Toast.LENGTH_SHORT).show();

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);

        MPPointF position = mChart.getPosition(e, mChart.getData().getDataSetByIndex(h.getDataSetIndex())
                .getAxisDependency());

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());



        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() {

    }
    */
    // -------------------------------------------------------------------------
}
