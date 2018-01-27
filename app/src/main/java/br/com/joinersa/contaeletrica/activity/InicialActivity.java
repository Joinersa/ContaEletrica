package br.com.joinersa.contaeletrica.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import br.com.joinersa.contaeletrica.R;
import br.com.joinersa.contaeletrica.dao.HistoricoDAO;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class InicialActivity extends AppCompatActivity {

    Toolbar toolbar;
    Button btCalculaConsumo;
    Button btSobre;
    Button btDicas;
    Button btEstatistica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        iniciarViews();
        toolbar.setTitle("Conta Elétrica");
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        //toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        btCalculaConsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InicialActivity.this, MainActivity.class));
            }
        });

        btSobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InicialActivity.this, SobreActivity.class));
            }
        });

        btDicas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InicialActivity.this, DicasActivity.class));
            }
        });

        btEstatistica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!new HistoricoDAO(getApplicationContext()).isEmpty()) {
                    startActivity(new Intent(InicialActivity.this, HistoricoActivity.class));
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(InicialActivity.this);
                    alert.setTitle("Importante!");
                    alert.setMessage("O histórico está vazio, faça o cálculo de custo e consumo desse mês." +
                            "\n\nDica:\nÉ importante utilizar esse aplicativo todos os meses, assim você ficará informado da variação mensal de consumo de sua conta de energia elétrica.");
                    alert.setPositiveButton("OK", null);
                    //alert.show();
                    // mudar cor do texto do botão OK:
                    AlertDialog ad = alert.create();
                    ad.show();
                    Button btOk = ad.getButton(DialogInterface.BUTTON_POSITIVE);
                    btOk.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                }

            }
        });

    }

    private void iniciarViews() {
        toolbar = (Toolbar) findViewById(R.id.tb_inicial);
        btCalculaConsumo = (Button) findViewById(R.id.bt_custo_consumo);
        btSobre = (Button) findViewById(R.id.bt_sobre);
        btDicas = (Button) findViewById(R.id.bt_dicas);
        btEstatistica = (Button) findViewById(R.id.bt_estatistica_geral);
    }


}
