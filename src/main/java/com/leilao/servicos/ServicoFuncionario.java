/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.servicos;

import com.leilao.entidades.Funcionario;
import java.util.List;

/**Serviço de persistência de funcionários no SGBD 
 *
 * @author igor
 */
public interface ServicoFuncionario {
    
    /** Busca o funcionário com o id especificado
     * 
     * @param id
     * @return Funcionário, caso exista, null, caso contrário
     */
    Funcionario get(int id);
    
    /** Busca todos os funcionários
     * 
     * @return Lista com todos os funcionários. 
     */
    List<Funcionario> findAll();
    
    /** Deleta o funcionário com o id especificado
     * 
     * @param id 
     */
    void delete(int id);
    
    /** Grava o funcionário. Pode ser usado para gravar pela
     *  primeira vez e para update
     * @param f 
     */
    void save(Funcionario f);
}
