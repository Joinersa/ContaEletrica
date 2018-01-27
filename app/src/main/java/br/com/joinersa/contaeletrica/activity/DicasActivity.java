package br.com.joinersa.contaeletrica.activity;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import br.com.joinersa.contaeletrica.R;
import br.com.joinersa.contaeletrica.adapter.TabsAdapter;
import br.com.joinersa.contaeletrica.extras.SlidingTabLayout;

public class DicasActivity extends AppCompatActivity {

    Toolbar toolbar;

    private CharSequence titulos[]= {"DICAS","DIREITOS E DEVERES", "CURIOSIDADES"};
    private int numeroAbas = 3;
    private ViewPager pager;
    private SlidingTabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dicas);

        toolbar = (Toolbar) findViewById(R.id.tb_dicas);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        carregarSlidingTabs();
    }

    private void carregarSlidingTabs() {
        pager = (ViewPager) findViewById(R.id.viewpager_dicas);
        pager.setAdapter(new TabsAdapter(getSupportFragmentManager(), new DicasActivity(), titulos, numeroAbas));

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs_dicas);
        tabs.setDistributeEvenly(false); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(DicasActivity.this, R.color.cardview_light_background);
            }
        });
        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}
