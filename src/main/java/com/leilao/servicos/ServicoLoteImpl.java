/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.servicos;

import com.leilao.entidades.Lote;
import com.leilao.repositorios.RepoLote;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicoLoteImpl implements ServicoLote {

    @Autowired
    RepoLote repositorio;

    @Override
    public Lote get(int id) {
        return repositorio.findOne(id);
    }

    @Override
    public List<Lote> findAll() {
        List<Lote> l = new ArrayList<>();
        Iterator<Lote> i = repositorio.findAll().iterator();
        i.forEachRemaining(lote->{
            l.add(lote);
        });
        return l;
    }

    @Override
    public void delete(int id) {
        repositorio.delete(id);
    }

    @Override
    public void save(Lote i) {
        repositorio.save(i);
    }

    @Override
    public List<Lote> getAprovados() {
        return repositorio.findByAprovado(true);
    }

    @Override
    public List<Lote> getNaoAprovados() {
        List<Lote> lotes = new ArrayList<Lote>();

        List<Lote> naoAprovados = repositorio.findByAprovado(false);
        List<Lote> naoFinalizados = repositorio.findByFinalizado(false);
        List<Lote> naoVendidos = repositorio.findByVendido(false);

        for(Lote l : naoAprovados) {
            if(naoFinalizados.contains(l) && naoVendidos.contains(l)) {
                lotes.add(l);
            }
        }

        return lotes;
    }

    @Override
    public List<Lote> getValidos() {
        List<Lote> lotes = new ArrayList<Lote>();

        List<Lote> aprovados = repositorio.findByAprovado(true);
        List<Lote> naoFinalizados = repositorio.findByFinalizado(false);
        List<Lote> naoVendidos = repositorio.findByVendido(false);

        for(Lote l : aprovados) {
            if(naoFinalizados.contains(l) && naoVendidos.contains(l)) {
                lotes.add(l);
            }
        }

        return lotes;
    }
    
    @Override
    public List<Lote> findAllByOrderByValorMinimoAsc(){
        return repositorio.findAllByOrderByValorMinimoAsc();
    }
    
}
