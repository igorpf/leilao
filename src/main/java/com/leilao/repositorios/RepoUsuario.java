/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.repositorios;

import com.leilao.entidades.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author igor
 */
@Repository
public interface RepoUsuario extends CrudRepository<Usuario, Integer>{
    Usuario findByNome(String name);
}
