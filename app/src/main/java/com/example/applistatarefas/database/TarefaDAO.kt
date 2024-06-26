package com.example.applistatarefas.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.applistatarefas.model.Tarefa

class TarefaDAO(context: Context) : ITarefaDAO {

    private val escrita = DatabaseHelper(context).writableDatabase
    private val leitura = DatabaseHelper(context).readableDatabase

    override fun salvar(tarefa: Tarefa): Boolean {

        val valores = ContentValues()
        valores.put("${DatabaseHelper.DESCRICAO}", tarefa.descricao)

        try {
            escrita.insert(
                DatabaseHelper.NOME_TABELA_TAREFAS,
                null,
                valores
            )
            Log.i("info_db", "Sucesso ao salvar tarefa")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("info_db", "Erro ao salvar tarefa")
            return false
        }
        return true
    }

    override fun atualizar(tarefa: Tarefa): Boolean {
        val args = arrayOf(tarefa.idTarefa.toString())
        val conteudo = ContentValues()
        conteudo.put("${DatabaseHelper.DESCRICAO}", tarefa.descricao)
        try {
            escrita.update(
                DatabaseHelper.NOME_TABELA_TAREFAS,
                conteudo,
                "${DatabaseHelper.ID_TAREFA} = ?",
                args
            )
            Log.i("info_db", "Sucesso ao atualizar tarefa")
        }catch (e:Exception){
            e.printStackTrace()
            Log.i("info_db", "Erro ao atualizar tarefa")
            return false
        }
        return true
    }

    override fun remover(idTarefa: Int): Boolean {
        val args = arrayOf(idTarefa.toString())
        try {
            escrita.delete(
                DatabaseHelper.NOME_TABELA_TAREFAS,
                "${DatabaseHelper.ID_TAREFA} = ?",
                args
            )
            Log.i("info_db", "Sucesso ao remover tarefa")
        }catch (e:Exception){
            e.printStackTrace()
            Log.i("info_db", "Erro ao remover tarefa")
            return false
        }
        return true
    }

    override fun listar(): List<Tarefa> {

        val listaTarefas = mutableListOf<Tarefa>()
        val sql = "SELECT ${DatabaseHelper.ID_TAREFA}, " +
                "${DatabaseHelper.DESCRICAO}," +
                "strftime('%d/%m/%Y %H:%M', ${DatabaseHelper.DATA_CRIACAO}) AS " +
                "${DatabaseHelper.DATA_CRIACAO} " +
                "FROM ${DatabaseHelper.NOME_TABELA_TAREFAS}"

        val cursor = leitura.rawQuery(sql, null)
        val indiceId = cursor.getColumnIndex(DatabaseHelper.ID_TAREFA)
        val indiceDescricao = cursor.getColumnIndex(DatabaseHelper.DESCRICAO)
        val indiceDataCriacao = cursor.getColumnIndex(DatabaseHelper.DATA_CRIACAO)

        while (cursor.moveToNext()) {
            val idTarefa = cursor.getInt(indiceId)
            val descricao = cursor.getString(indiceDescricao)
            val dataCriacao = cursor.getString(indiceDataCriacao)

            listaTarefas.add(
                Tarefa(
                    idTarefa,
                    descricao,
                    dataCriacao
                )
            )
        }
        return listaTarefas
    }
}