/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.repositorios;

import java.util.List;
import com.leilao.entidades.Lote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author igor
 */
@Repository
public interface RepoLote extends CrudRepository<Lote, Integer>{
    
    /** Retorna todos os lotes que estão aprovados ou não
     * 
     * @param aprovado 
     * @return 
     */
    List<Lote> findByAprovado(boolean aprovado);

    /** Retorna todos os lotes que estão finalizados ou não
     *
     * @param finalizado
     * @return
     */
    List<Lote> findByFinalizado(boolean finalizado);

    /** Retorna todos os lotes que estão vendidos ou não
     *
     * @param vendido
     * @return
     */
    List<Lote> findByVendido(boolean vendido);
}
