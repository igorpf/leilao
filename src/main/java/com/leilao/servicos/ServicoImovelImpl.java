/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.servicos;

import com.leilao.entidades.Imovel;
import com.leilao.repositorios.RepoImovel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicoImovelImpl implements ServicoImovel {

    @Autowired
    RepoImovel repositorio;

    @Override
    public Imovel get(int id) {
        return repositorio.findOne(id);
    }

    @Override
    public List<Imovel> findAll() {
        List<Imovel> l = new ArrayList<>();
        Iterator<Imovel> i = repositorio.findAll().iterator();
        i.forEachRemaining(imo->{
            l.add(imo);
        });
        return l;
    }

    @Override
    public void delete(int id) {
        repositorio.delete(id);
    }

    @Override
    public void save(Imovel i) {
        repositorio.save(i);
    }
    
    @Override
    public List<Imovel> getResidencial() {
        return repositorio.findByTipo(0);
    }
    
     @Override
    public List<Imovel> getComercial() {
        return repositorio.findByTipo(1);
    }
    
    public List<Imovel> findByNome(String nome) {
        return repositorio.findByNome(nome);
    }

}
