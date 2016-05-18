/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.servicos;

import com.leilao.entidades.Usuario;
import com.leilao.repositorios.RepoUsuario;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicoUsuarioImpl implements ServicoUsuario {

    @Autowired
    RepoUsuario repositorio;

    @Override
    public Usuario get(int id) {
        return repositorio.findOne(id);
    }

    @Override
    public List<Usuario> findAll() {
        List<Usuario> l = new ArrayList<>();
        Iterator<Usuario> i = repositorio.findAll().iterator();
        i.forEachRemaining(user->{
            l.add(user);
        });
        return l;
    }

    @Override
    public void delete(int id) {
        repositorio.delete(id);
    }

    @Override
    public void save(Usuario u) {
        repositorio.save(u);
    }

}