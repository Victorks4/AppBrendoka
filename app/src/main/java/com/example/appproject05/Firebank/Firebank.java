package com.example.appproject05.Firebank;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class Firebank {

    // Referencia geral para o banco de dados
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();


    // Método para adicionar um documento
    public static boolean addDocumento(String caminhoColecao, Map<String,Object> campos) {

        // Constante de retorno
        final boolean[] adicionou = new boolean[0];

        // Configurando a referência de acesso
        CollectionReference refCol = db.collection(caminhoColecao);

        // Adicionando documento
        refCol.add(campos)
                // Configurando a constante de retorno
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        // Verifica se a inserção foi finalizada com sucesso
                        if (task.isSuccessful()) {
                            adicionou[0] = true;
                            Log.d("Firebank","Documento adicionado");
                        } else {
                            Exception e = task.getException();
                            adicionou[0] = false;
                            Log.e("Firebank","Erro ao adicionar documento\n" + e.getStackTrace());
                        }

                    }
                });

        // Retorno da operação
        return adicionou[0];
    }


    // Método para atualizar um documento
    public static Boolean updDocumento(String caminhoColecao, String idDocumento,Map<String,Object> campos){

        // Constante de retorno
        final Boolean[] atualizou = new Boolean[0];

        // Configurando a referência de acesso
        DocumentReference refDoc = db.collection(caminhoColecao).document(idDocumento);

        // Atualizando documento
        refDoc.update(campos)
                // Configurando a constante de retorno
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        // Verifica se a tarefa foi finalizada com sucesso
                        if (task.isSuccessful()) {
                            atualizou[0] = true;
                            Log.d("Firebank","Documento atualizado");
                        } else {
                            Exception e = task.getException();
                            atualizou[0] = false;
                            Log.e("Firebank","Erro ao atualizar documento.\n" + e.getStackTrace());
                        }

                    }
                });

        // Retorno da operação
        return atualizou[0];
    }


    // Método para deletar um documento
    public static Boolean delDocumento(String caminhoColecao, String idDocumento){

        // Constante de retorno
        final Boolean[] deletou = new Boolean[0];

        // Configurando a referência de acesso
        DocumentReference refDoc = db.collection(caminhoColecao).document(idDocumento);

        // Deletando documento
        refDoc.delete()
                // Configurando a constante de retorno
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        // Verifica se a tarefa foi finalizada com sucesso
                        if (task.isSuccessful()) {
                            deletou[0] = true;
                            Log.d("Firebank","Documento deletado");
                        } else {
                            deletou[0] = false;
                            Log.e("Firebank","Documento não deletado");
                        }

                    }
                });

        // Retorno da operação
        return deletou[0];
    }


    // Método para buscar lista de documentos de uma coleção
    public static Map<String,Object>[] buscaDocumentos(String caminhoColecao) {

        // Lista de documentos mapeados
        List<Map<String,Object>> listaDocumentos= new ArrayList<>();

        // Configurando a referência de acesso
        CollectionReference refCol = db.collection(caminhoColecao);

        // Buscando documento
        refCol.get()
                // Configurando o vetor de retorno
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        // Verifica se a consulta foi finalizada com sucesso
                        if (task.isSuccessful()) {

                            // Obtem o resultado da consulta
                            QuerySnapshot documentosAchados = task.getResult();

                            // Verifica se houveram resultados
                            if (!documentosAchados.isEmpty()) {
                                // Adiciona os documentos mapeados a lista de retorno
                                for (DocumentSnapshot documento : documentosAchados.getDocuments()) {

                                    Map<String, Object> mapDocumento = documento.getData();

                                    // Seleciona apenas os documentos com campo estabelecido
                                    if (mapDocumento != null) {
                                        mapDocumento.put("id",documento.getId());
                                        listaDocumentos.add(mapDocumento);
                                    }

                                }
                                Log.d("Firebank","Documentos mapeados");
                            } else {
                                Log.e("Firebank","Nenhum documento encontrado");
                            }

                        } else {
                            Log.e("Firebank","Erro no mapeamento dos documentos.\n" + task.getException().getStackTrace());
                        }

                    }
                });

        // Retornando os resultados obtidos
        return listaDocumentos.toArray(new Map[listaDocumentos.size()]);
    }


    // Método para buscar um documento específico
    public static Map<String,Object> selecionaDocumento(String caminhoPacote, String idDocumento) {

        // Mapa do documento
        final Map<String, Object>[] campos = new Map[1];

        // Configurando a referência de acesso
        DocumentReference refDoc = db.collection(caminhoPacote).document(idDocumento);

        // Buscando documento
        refDoc.get()
                // Configurando a variável de retorno
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        // Verifica se a consulta foi finalizada com sucesso
                        if (task.isSuccessful()) {

                            campos[0] = task.getResult().getData();
                            Log.d("Firebank","Documento mapeado");

                        } else {

                            campos[0] = null;
                            Log.d("Firebank","Erro ao mapear documento.\n" + task.getException().getStackTrace());

                        }

                    }
                });

        return campos[0];
    }


}
