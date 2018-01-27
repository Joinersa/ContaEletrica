package br.com.joinersa.contaeletrica.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import br.com.joinersa.contaeletrica.R;
import br.com.joinersa.contaeletrica.adapter.TabsAdapter;
import br.com.joinersa.contaeletrica.dao.HistoricoDAO;
import br.com.joinersa.contaeletrica.extras.SlidingTabLayout;

public class HistoricoActivity extends AppCompatActivity {// implements OnChartValueSelectedListener {

    Toolbar toolbar;

    private CharSequence titulos[]= {"CONSUMO","CUSTO"};
    private int numeroAbas = 2;
    private ViewPager pager;
    private SlidingTabLayout tabs;

    private List<String> listAnos;
    private String[] vetorAnos;

    private int positionAno = 0;

    private TabsAdapter tabsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        toolbar = (Toolbar) findViewById(R.id.toolbar_estatistica);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listAnos = new HistoricoDAO(HistoricoActivity.this).getAnos();
        vetorAnos = listAnos.toArray(new String[listAnos.size()]);

        carregarSlidingTabs();
    }

    private void carregarSlidingTabs() {

        tabsAdapter = new TabsAdapter(getSupportFragmentManager(), new HistoricoActivity(), titulos, numeroAbas);
        tabsAdapter.setAno(vetorAnos[0]); // primeiro ano da lista, o ano atual

        pager = (ViewPager) findViewById(R.id.viewpager_estatistica);
        pager.setAdapter(tabsAdapter); // ************ modificado ************

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs_estatistica);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                //return getResources().getColor(R.color.cardview_light_background);
                return ContextCompat.getColor(HistoricoActivity.this, R.color.cardview_light_background);
            }
        });
        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_historico, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.item_limpar_historico) {
            AlertDialog.Builder alertApagarHistorico = new AlertDialog.Builder(HistoricoActivity.this);
            alertApagarHistorico.setMessage("Deseja realmente apagar o histórico?");
            alertApagarHistorico.setCancelable(false);
            alertApagarHistorico.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new HistoricoDAO(getApplicationContext()).deleteAll();
                    finish();
                    Toast.makeText(HistoricoActivity.this, "Histórico de consumo e custo apagado!", Toast.LENGTH_SHORT).show();
                }
            });
            alertApagarHistorico.setNegativeButton("CANCELAR", null);
            AlertDialog ad = alertApagarHistorico.create();
            ad.show();
            Button btOk = ad.getButton(DialogInterface.BUTTON_POSITIVE);
            btOk.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));

            Button btCancelar = ad.getButton(DialogInterface.BUTTON_NEGATIVE);
            btCancelar.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));

//            alertApagarHistorico.show();
        }

        if (id == R.id.item_filtro_ano) {
            AlertDialog.Builder alert = new AlertDialog.Builder(HistoricoActivity.this);
            alert.setTitle("Selecione um ano");
            alert.setSingleChoiceItems(vetorAnos, positionAno, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // ... código...
                    tabsAdapter.setAno(vetorAnos[which]);
                    pager.setAdapter(tabsAdapter);
                    tabs.setViewPager(pager);

                    positionAno = which; // guarda o ano que foi selecionado para poder ficar selecionado o radio na proxima vez.

                    dialog.dismiss();
                }
            });
            alert.show();
        }
        
        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }

}
