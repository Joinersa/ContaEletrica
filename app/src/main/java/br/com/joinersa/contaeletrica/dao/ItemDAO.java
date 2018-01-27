package br.com.joinersa.contaeletrica.dao;

/**
 * Created by Joiner on 16/07/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.joinersa.contaeletrica.bdcore.BDCore;
import br.com.joinersa.contaeletrica.entities.Item;

public class ItemDAO {
    private SQLiteDatabase bd;

    public ItemDAO(Context context) {
        BDCore auxBd = new BDCore(context);
        bd = auxBd.getWritableDatabase();
    }

    public void inserir(Item item) {
        ContentValues valores = new ContentValues();
        valores.put("id_imagem", item.getIdImagem());
        valores.put("nome", item.getNome());
        valores.put("potencia", item.getPotencia());
        valores.put("tempo", item.getTempo());
        valores.put("periodo", item.getPeriodo());
        valores.put("quantidade", item.getQuantidade());
        valores.put("consumo_mes", item.getConsumoMes());

        bd.insert("itens", null, valores);
    }

    public void atualizar(Item item) {
        ContentValues valores = new ContentValues();
        valores.put("potencia", item.getPotencia());
        valores.put("tempo", item.getTempo());
        valores.put("periodo", item.getPeriodo());
        valores.put("quantidade", item.getQuantidade());
        valores.put("consumo_mes", item.getConsumoMes());

        bd.update("itens", valores, "_id = ?", new String[]{"" + item.getId()});
    }

    public void deletar(Item item) {
        bd.delete("itens", "_id = " + item.getId(), null);
    }

    public void deleteAll() {
        bd.execSQL("delete from itens");
    }

    public List<Item> buscar() {
        List<Item> list = new ArrayList<Item>();
        String[] colunas = new String[] {"_id", "id_imagem", "nome", "potencia", "tempo", "periodo", "quantidade", "consumo_mes"};

        Cursor cursor = bd.query("itens", colunas, null, null, null, null, "_id");

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Item item = new Item();
                item.setId(cursor.getInt(0));
                item.setIdImagem(cursor.getInt(1));
                item.setNome(cursor.getString(2));
                item.setPotencia(cursor.getFloat(3));
                item.setTempo(cursor.getFloat(4));
                item.setPeriodo(cursor.getString(5));
                item.setQuantidade(cursor.getInt(6));
                item.setConsumoMes(cursor.getFloat(7));
                list.add(item);
            } while (cursor.moveToNext());
        }
        return list;
    }

    public float getSomaConsumo() {
        Cursor cursor =  bd.rawQuery("SELECT SUM(consumo_mes) AS soma FROM itens", null);
        cursor.moveToFirst();
        //return cursor.getFloat(cursor.getColumnIndex("soma"));
        return cursor.getFloat(0);
    }

}

