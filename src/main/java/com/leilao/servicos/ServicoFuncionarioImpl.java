/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.servicos;

import com.leilao.entidades.Funcionario;
import com.leilao.repositorios.RepoFuncionario;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicoFuncionarioImpl implements ServicoFuncionario {
    
    @Autowired
    RepoFuncionario repositorio;
    
    @Override
    public Funcionario get(int id) {
        return repositorio.findOne(id);
    }

    @Override
    public List<Funcionario> findAll() {
        List<Funcionario> l = new ArrayList<>();
        Iterator<Funcionario> i = repositorio.findAll().iterator();
        i.forEachRemaining(fun->{
            l.add(fun);
        });
        return l;
    }

    @Override
    public void delete(int id) {
        repositorio.delete(id);
    }

    @Override
    public void save(Funcionario f) {
        repositorio.save(f);
    }
    
}
