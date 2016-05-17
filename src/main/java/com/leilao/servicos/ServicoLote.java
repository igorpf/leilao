/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.servicos;

import com.leilao.entidades.Lote;
import java.util.List;

/** Serviço de persistência de lote no SGBD 
 *
 * @author igor
 */
public interface ServicoLote {
    /** Busca o lote com o id especificado
     * 
     * @param id
     * @return Lote, caso exista, null, caso contrário
     */
    Lote get(int id);
    
    /** Busca todos os lotes
     * 
     * @return 
     */
    List<Lote> findAll();
    
    /** Deleta o lote com o id especificado
     * 
     * @param id 
     */
    void delete(int id);
    
    /** Salva o lote 
     * 
     * @param i 
     */
    void save(Lote i);
}
