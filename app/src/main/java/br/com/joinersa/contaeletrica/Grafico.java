package br.com.joinersa.contaeletrica;

import android.graphics.Color;
import android.graphics.Typeface;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.joinersa.contaeletrica.entities.Historico;

/**
 * Created by Joiner on 28/10/2016.
 */

public class Grafico {

    public static void criarGrafico(List<Historico> lista, HorizontalBarChart mChart, final String unidade) {
        //mChart.setOnChartValueSelectedListener(this);// click
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
        yr.setSpaceTop(20f);

        float spaceForBar = 1f;
        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        int index = 0;

        if (unidade.equals("kWh")) {
            for (int i = 0; i < 12; i++) {
                try {
                    if (i == (lista.get(index).getMes() - 1)) {
                        yVals1.add(new BarEntry(i * spaceForBar, lista.get(index).getConsumoMes(), lista.get(index).getMes()));
                        index++;
                    } else {
                        yVals1.add(new BarEntry(i * spaceForBar, 0, i + 1));
                    }
                } catch (IndexOutOfBoundsException e) {
                    yVals1.add(new BarEntry(i * spaceForBar, 0, i + 1));
                }
            }
        } else {
            for (int i = 0; i < 12; i++) {
                try {
                    if (i == (lista.get(index).getMes() - 1)) {
                        yVals1.add(new BarEntry(i * spaceForBar, lista.get(index).getCustoMes(), lista.get(index).getMes()));
                        index++;
                    } else {
                        yVals1.add(new BarEntry(i * spaceForBar, 0, i + 1));
                    }
                } catch (IndexOutOfBoundsException e) {
                    yVals1.add(new BarEntry(i * spaceForBar, 0, i + 1));
                }
            }
        }

        //int d = 1;
        // criando uma lista de cores
        ArrayList<Integer> colors = new ArrayList<Integer>();
        // --- gera 14 cores diferentes -- para o gráfico só é preciso 12 cores, uma para cada mês.
        for (int c : ColorTemplate.MATERIAL_COLORS) {
            colors.add(c);
            //System.out.println("color " + d);
            //d++;
        }
        for (int c : ColorTemplate.COLORFUL_COLORS) {
            colors.add(c);
            //System.out.println("color " + d);
            //d++;
        }
        for (int c : ColorTemplate.PASTEL_COLORS) {
            colors.add(c);
            //System.out.println("color " + d);
            //d++;
        }
        /*for (int c : ColorTemplate.JOYFUL_COLORS) {
            colors.add(c);
            System.out.println("color " + d);
            d++;
        }*/
        /*
        for (int c : ColorTemplate.LIBERTY_COLORS) {
            colors.add(c);
            System.out.println("color " + d);
            d++;
        }*/
        /*
        for (int c : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(c);
            System.out.println("color " + d);
            d++;
        }*/
        //colors.add(ColorTemplate.getHoloBlue());

        final BarDataSet dataset = new BarDataSet(yVals1, "");
        //dataset.setLabel(null);
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
                if (unidade.equals("R$")) {
                    NumberFormat numFormatado = NumberFormat.getCurrencyInstance(); // num com vírgula
                    return unidade + " " + numFormatado.format(value).replace("R$", "");
                } else {
                    NumberFormat numFormatado = NumberFormat.getInstance(); // num com vírgula
                    return numFormatado.format(value) + " " + unidade;
                }
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
    }
}
