package br.com.joinersa.contaeletrica.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
//import android.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import com.software.shell.fab.ActionButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.joinersa.contaeletrica.Concessionaria;
import br.com.joinersa.contaeletrica.entities.Item;
import br.com.joinersa.contaeletrica.R;
import br.com.joinersa.contaeletrica.adapter.ItemAdapter;
import br.com.joinersa.contaeletrica.dao.ItemDAO;
import br.com.joinersa.contaeletrica.interfaces.RecyclerViewOnClickListenerHack;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class MainActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {

    private RecyclerView recyclerViewMeusEletros; // lista de eletrodomésticos escolhidos
    private List<Item> itemList = new ArrayList<>();
    private ItemAdapter itemAdapter;
    private Menu menu;// = (Menu) findViewById(R.id.item_excluir_tudo);//tem(R.id.item_excluir_tudo);

    private static final String PREF_CONCESSIONARIA = "preferencia";

    private static ActionButton fabAdd;
    private static ActionButton fabCalcular;
    private Toolbar toolbarPrincipal;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ---------------  toolbar principal: ---------------------
        toolbarPrincipal = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbarPrincipal);// para funcionar nas versões de androids inferiores às atuais.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// bt voltar


        // ------------------------------------ FAB ADD --------------------------------------------
        fabAdd = (ActionButton) findViewById(R.id.fab_add);
        fabAdd.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        fabAdd.setButtonColorPressed(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        fabAdd.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        fabAdd.setHideAnimation(ActionButton.Animations.ROLL_TO_RIGHT);
        fabAdd.setImageResource(R.drawable.ic_add);
        float scale = getResources().getDisplayMetrics().density;
        int shadow = (int) (3 * scale + 0.5);
        fabAdd.setShadowRadius(shadow);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // abre nova activity para escolha de um aparelho

                Intent intent = new Intent(MainActivity.this, ListaAparelhosActivity.class);
                startActivityForResult(intent, 0);

                // código antigo:

                //Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                //startActivityForResult(intent, 0);

            }
        });
        fabAdd.playShowAnimation();
        // -----------------------------------------------------------------------------------------

        // -------- FAB CALCULAR ---------------------------------------
        fabCalcular = (ActionButton) findViewById(R.id.fab_calcular);
        fabCalcular.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        fabCalcular.setButtonColorPressed(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

        fabCalcular.setShowAnimation(ActionButton.Animations.ROLL_FROM_RIGHT);
        fabCalcular.setHideAnimation(ActionButton.Animations.ROLL_TO_RIGHT);

        fabCalcular.setImageResource(R.drawable.ic_cifrao);
        fabCalcular.setShadowRadius(shadow);

        fabCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(PREF_CONCESSIONARIA, Context.MODE_PRIVATE);
                if (preferences.contains("valor")) { // verifica se existe a chave 'valor' no sharedPreferences.
                    // código para calcular...
                    Intent intent = new Intent(MainActivity.this, ResultadoActivity.class);
                    startActivity(intent);

                } else {

                    // configurar concessionária antes de calcular, utilizando o app pela primeira vez:
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Aviso!");
                    builder.setMessage("Como é a primeira vez que o aplicativo está sendo utilizado, " +
                            "será necessário configurar a concessionária de energia elétrica para que o aplicativo " +
                            "possa estimar sua conta elétrica!");
                    builder.setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this, ConcessionariaActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                    //builder.show();
                    // mudar cor do texto do botão Configurar:
                    AlertDialog ad = builder.create();
                    ad.show();
                    Button btConfigurar = ad.getButton(DialogInterface.BUTTON_POSITIVE);
                    btConfigurar.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));

                }
            }
        });
        fabCalcular.playShowAnimation();
        // -------------------------------------------------------------


        // lista de meus eletrodomésticos
        recyclerViewMeusEletros = (RecyclerView) findViewById(R.id.recycler_view_meus_eletros);
        // botão pressionado: ??????
        recyclerViewMeusEletros.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), recyclerViewMeusEletros, this));
        // adapter:
        itemAdapter = new ItemAdapter(itemList);
        //meuEletroAdapter.setRecyclerViewOnClickListenerHack(this); // click
        recyclerViewMeusEletros.setHasFixedSize(true);

        // colocar movimento da RecyclerView, para fazer animação do botão flutuante
        /*
        recyclerViewMeusEletros.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //System.out.println("############ onScrolled() ############");
                if (dy >= 0) {
                    fabAdd.show();
                    fabCalcular.show();
                } else {
                    fabAdd.hide();
                    fabCalcular.hide();
                }
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                System.out.println("############ onScrollStateChanged() ############");
            }
        });
        */

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewMeusEletros.setLayoutManager(layoutManager);
        recyclerViewMeusEletros.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMeusEletros.setAdapter(itemAdapter);

        itemAdapter.limpaRecycler(); //só limpa o recyclerView para poder adicionar itens do banco de dados, pois senão ficariam itens repetidos.
        itemList.addAll(new ItemDAO(MainActivity.this).buscar());
        itemAdapter.notifyDataSetChanged();

        SharedPreferences sp = getSharedPreferences(PREF_CONCESSIONARIA, Context.MODE_PRIVATE);

        boolean valorTargetComplete = sp.getBoolean("tapTargetComplete", false);

        if (!valorTargetComplete) {
            showFabPrompt();
        }

        if (itemList.size() == 0 && valorTargetComplete){
            showTapTargetFabAdd();
        }

    }

    private void showFabPrompt() {
        new MaterialTapTargetPrompt.Builder(MainActivity.this)
                .setTarget(findViewById(R.id.fab_add))
                .setPrimaryText("Adicionar Itens")
                .setSecondaryText("Toque para adicionar equipamentos elétricos")
                .setBackgroundColour(Color.parseColor("#9E9E9E"))
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {

                    }

                    @Override
                    public void onHidePromptComplete() {
                        new MaterialTapTargetPrompt.Builder(MainActivity.this)
                                .setTarget(findViewById(R.id.fab_calcular))
                                .setPrimaryText("Calcular")
                                .setSecondaryText("Toque para calcular o custo e o consumo")
                                .setBackgroundColour(Color.parseColor("#9E9E9E"))
                                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                    @Override
                                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {

                                    }

                                    @Override
                                    public void onHidePromptComplete() {
                                        SharedPreferences preferences = getSharedPreferences(PREF_CONCESSIONARIA, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putBoolean("tapTargetComplete", true);
                                        editor.commit();

                                    }
                                })
                                .show();
                    }
                })
                .show();
    }

    private void showTapTargetFabAdd() {
        new MaterialTapTargetPrompt.Builder(MainActivity.this)
                .setTarget(findViewById(R.id.fab_add))
                .setPrimaryText("Adicionar Itens")
                .setSecondaryText("Toque para adicionar equipamentos elétricos")
                .setBackgroundColour(Color.parseColor("#9E9E9E"))
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {

                    }

                    @Override
                    public void onHidePromptComplete() {

                    }
                })
                .show();
    }


    // ----------- recebe o resultado da activity que foi chamada por essa activity ----------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // o if serve para não listar itens do banco no recyclerView sem necessidade ao clicar no botão de voltar.
        // Só vão ser listados os itens se o botão salvar for pressionado na outra activity.
        // Verifica se o resultCode veio ok da outra activity.
        if (resultCode == RESULT_OK) {
            Log.i("onActivityResult()", "Pressionado o botão salvar! Itens foram salvos e listados no RecyclerView!");
            itemAdapter.limpaRecycler(); //só limpa o recyclerView para poder adicionar itens do banco de dados, pois senão ficariam itens repetidos.
            itemList.addAll(new ItemDAO(MainActivity.this).buscar());
            itemAdapter.notifyDataSetChanged();

            if (itemList.size() > 0) {
                menu.findItem(R.id.item_excluir_tudo).setVisible(true);// ativar botão 'excluir tudo'
            }


        } else {
            Log.i("onActivityResult()", "Pressionado o botão de voltar! Não teve que listar novamente os dados do banco!");
        }
    }
    // ---------------------------------------------------------------------------------------------


    // ----------------------------------- botões na toolbar ---------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        this.menu = menu; // pegar a referência do menu da toolbar para poder manipular seus botões em outros lugares dessa classe.
        if (itemList.size() > 0) { // verifica se existe itens na lista que fica no recyclerView.
            menu.findItem(R.id.item_excluir_tudo).setVisible(true); // ativar botão 'excluir tudo'
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_excluir_tudo:
                clearLista();
                break;
            case R.id.item_concessionaria:
                Intent intent = new Intent(MainActivity.this, ConcessionariaActivity.class);
                startActivity(intent);
                break;
            /*case R.id.item_sobre:
                startActivity(new Intent(MainActivity.this, SobreActivity.class));
                break;*/
            case android.R.id.home:
                finish();
        }
        return true;
    }
    // ---------------------------------------------------------------------------------------------


    public void clearLista() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Aviso!");
        builder.setMessage("Deseja excluir todos os itens da lista?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MainActivity.this, "Lista vazia!", Toast.LENGTH_SHORT).show();
                if (itemAdapter.getItemCount() == 1) {
                    Toast.makeText(MainActivity.this, "Item Excluido!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Itens Excluidos!", Toast.LENGTH_SHORT).show();
                }

                itemAdapter.removeAllItens(MainActivity.this); // exclui todos os dados do banco de dados, além de limpar o recyclerView
                menu.findItem(R.id.item_excluir_tudo).setVisible(false);
            }
        });
        builder.setNegativeButton("Não", null);
        builder.show();
    }


    // evento de click em uma posição do recyclerView:
    @Override
    public void onClickListener(View view, int position) {
        //Toast.makeText(getApplicationContext(), "onClickListener(): " + position, Toast.LENGTH_SHORT).show();
        //meuEletroAdapter.removeListItem(position);
        Item item = itemList.get(position);
        if (actionMode == null) {
            // faz alguma coisa com apenas um click, implementar depois...
        } else { // se a CAB está ativada

            // animação da imagem do item selecionado: -------------
            /*
            try {
                YoYo.with(Techniques.FadeIn).
                        duration(700).
                        playOn(view.findViewById(R.id.iv_checked));

            } catch (Exception e) {}
            */
            // -----------------------------------------------------
            // seleciona o item
            item.selected = !item.selected;
            // atualiza o título com a quantidade de itens selecionados:
            updateActionModeTitle();
            // redesenha a lista:
            //recyclerViewMeusEletros.getAdapter().notifyDataSetChanged(); // não seria bom para animação do item selecionado
            // redesenhar o item modificado e não toda lista com em cima.
            itemAdapter.notifyItemChanged(position);
        }

    }

    // evento de longo click em uma posição do recyclerView:
    @Override
    public void onLongPressClickListener(View view, int position) {

        // do livro:
        if (actionMode != null) {
            return;
        }
        // vibrar ao pressionar:
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

        // animação da imagem do item selecionado: -------------
        /*
        try {
            YoYo.with(Techniques.FadeIn).
                    duration(700).
                    playOn(view.findViewById(R.id.iv_checked));
        } catch (Exception e) {
        }
        */
        // -----------------------------------------------------

        // liga a action bar de contexto (CAB):
        actionMode = startSupportActionMode(getActionModeCallback());
        //actionMode = startActionMode(getActionModeCallback());
        Item item = itemList.get(position);
        item.selected = true; // seleciona o item
        // solicita ao android para desenhar a lista novamente:
        //recyclerViewMeusEletros.getAdapter().notifyDataSetChanged(); // não seria bom para animação do item selecionado
        // redesenhar o item modificado e não toda lista com em cima.
        itemAdapter.notifyItemChanged(position);
        // atualiza o título para mostrar a quantidade de itens selecionados:
        updateActionModeTitle();
    }

    // Atualizar o título da action bar (CAB)
    private void updateActionModeTitle() {
        if (actionMode != null) {
            //actionMode.setTitle("Selecione os Itens");
            //actionMode.setSubtitle(null);
            List<Item> selectedItens = getSelectedItens();
            if (selectedItens.size() == 1) {
                actionMode.getMenu().findItem(R.id.item_detalhes).setVisible(true);
                actionMode.getMenu().findItem(R.id.item_editar).setVisible(true);
                actionMode.setTitle("1 Item");
            } else if (selectedItens.size() > 1) {
                actionMode.getMenu().findItem(R.id.item_detalhes).setVisible(false);
                actionMode.getMenu().findItem(R.id.item_editar).setVisible(false);
                actionMode.setTitle(selectedItens.size() + " Itens");
            } else { // se for zero, ou seja, os item não estão selecionados
                actionMode.finish(); //
            }
        }
    }

    // retorna a lista de itens selecionados: ------------------------------------------------------
    private List<Item> getSelectedItens() {
        List<Item> list = new ArrayList<>();
        for (Item item : itemList) {
            if (item.selected) {
                list.add(item);
            }
        }
        return list;
    }
    // ---------------------------------------------------------------------------------------------


    // -------------------------- Método que retorna o Callback ------------------------------------
    boolean flagExluirItens = false; // verificar se foi excluido itens pelo menu da CAB para poder fazer um efeito de exclusão no recyclerView.

    private ActionMode.Callback getActionModeCallback() {
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // infla o menu específico da action bar de contexto (CAB)
                getMenuInflater().inflate(R.menu.menu_itens_selecionados, menu);
                fabAdd.hide();
                fabCalcular.hide();
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                List<Item> selectedItens = getSelectedItens();

                switch (item.getItemId()) {
                    case R.id.item_excluir:
                        flagExluirItens = true;
                        //ItemDAO bd = new ItemDAO(getBaseContext());
                        for (Item it : selectedItens) {
                            //bd.deletar(it);
                            itemAdapter.removeListItem(MainActivity.this, itemList.indexOf(it)); //melhorou em performance com relação ao código em baixo comentado.. haha
                            //itemAdapter.limpaRecycler(); //só limpa o recyclerView para poder reprintar itens do banco de dados, pois senão ficariam itens repetidos.
                            //itemList.addAll(bd.buscar());
                            //itemAdapter.notifyDataSetChanged();

                            if (itemList.size() > 0) {
                                menu.findItem(R.id.item_excluir_tudo).setVisible(true);// ativar botão 'excluir tudo'
                            } else {
                                menu.findItem(R.id.item_excluir_tudo).setVisible(false);
                            }
                        }
                        // recyclerViewMeusEletros.getAdapter().notifyDataSetChanged();
                        if (selectedItens.size() == 1) {
                            Toast.makeText(MainActivity.this, "Item Excluido!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Itens Excluidos!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.item_detalhes:
                        Item it = selectedItens.get(0); // só existirá um na lista, na posição 0.
                        NumberFormat format = NumberFormat.getNumberInstance(Locale.FRANCE);
                        String s = it.getPotencia() == 1.0 ? "" : "s";

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Detalhes");
                        builder.setMessage("Aparelho: " + it.getNome() + "\n" +
                                "Potência: " + format.format(it.getPotencia()) + " Watt" + s + "\n" +
                                "Tempo de Uso: " + format.format(it.getTempo()) + " " + it.getPeriodo() + "\n" +
                                "Quantidade: " + it.getQuantidade() + "\n" +
                                "Consumo: " + format.format(it.getConsumoMes()) + " kWh/mês");
                        builder.setPositiveButton("OK", null);
                        builder.show();
                        break;
                    case R.id.item_editar:

                        Item itemEscolhido = selectedItens.get(0);
                        Bundle parans = new Bundle();
                        //parans.putInt("idImagem", itemEscolhido.getIdImagem());
                        parans.putLong("id", itemEscolhido.getId());
                        parans.putString("nome", itemEscolhido.getNome());
                        parans.putFloat("potencia", itemEscolhido.getPotencia());
                        parans.putFloat("tempo", itemEscolhido.getTempo());
                        parans.putString("periodo", itemEscolhido.getPeriodo());
                        parans.putInt("quantidade", itemEscolhido.getQuantidade());

                        parans.putString("activity_chamada", "main"); // servirá para ser entendida pela outra activity que foi a main que a chamou.

                        Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                        intent.putExtras(parans);
                        startActivityForResult(intent, 0);
                        /*
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setMessage("Opção editar em breve!");
                        alert.setPositiveButton("OK", null);
                        alert.show();*/
                        break;
                }
                // encerra o action mode
                mode.finish();
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // limpa estado
                actionMode = null;
                // configura todos os itens para não selecionados:
                for (Item item : itemList) {
                    item.selected = false;
                }

                if (!flagExluirItens) { // senão for excluido nenhum item (pressionado o menuItem de excluir da CAB), apenas o usuário saiu do ActionMode com o botão de voltar ou outro e desfez a CAB.
                    recyclerViewMeusEletros.getAdapter().notifyDataSetChanged(); // desmarca os itens marcados.
                } else {
                    flagExluirItens = false; // se for exluido algum item, volta a ser falso a flag, pois é uma variável global.
                }

                fabAdd.show();
                fabCalcular.show();
            }
        };
    }
    // ---------------------------------------------------------------------------------------------

    public static class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {

        private Context mContext;
        private GestureDetector mGestureDetector;
        private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

        public RecyclerViewTouchListener(Context c, final RecyclerView rv, RecyclerViewOnClickListenerHack rvoclh) {
            mContext = c;
            mRecyclerViewOnClickListenerHack = rvoclh;

            mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);

                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if (cv != null && mRecyclerViewOnClickListenerHack != null) {
                        mRecyclerViewOnClickListenerHack.onLongPressClickListener(cv, rv.getChildAdapterPosition(cv));
                    }
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {

                    View cv = rv.findChildViewUnder(e.getX(), e.getY());

                    if (cv != null && mRecyclerViewOnClickListenerHack != null) {
                        mRecyclerViewOnClickListenerHack.onClickListener(cv, rv.getChildAdapterPosition(cv));
                    }

                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            mGestureDetector.onTouchEvent(e);

            //System.out.println("*************** onInterceptTouchEvent() *******************");
            /*
            fabAdd.hide();
            fabCalcular.hide();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fabAdd.show();
                    fabCalcular.show();
                }
            }, 1200);
            */

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            //Log.i("onTouchEvent()", "AQUI!");
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            //Log.i("onRequestDisallow", "AQUI!");
        }
    }

}