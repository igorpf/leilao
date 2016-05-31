/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.servicos;

import com.leilao.entidades.Usuario;
import java.util.List;

/** Serviço de persistência de usuários no SGBD
 *
 * @author igor
 */
public interface ServicoUsuario {
    /** Busca o usuário com o id especificado
     * 
     * @param id
     * @return Usuario, caso exista, null, caso contrário
     */
    Usuario get(int id);
    
    /** Busca o usuário com o nome especificado
     * 
     * @param nome
     * @return Usuario, caso exista, null, caso contrário
     */
    Usuario get(String nome);
    
    /** Busca todos os usuários
     * 
     * @return 
     */
    List<Usuario> findAll();
    
    /** Deleta o usuário com o id especificado
     * 
     * @param id 
     */
    void delete(int id);
    
    /** Salva o usuário 
     * 
     * @param i 
     */
    void save(Usuario i);
}
