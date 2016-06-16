/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.servicos;

import com.leilao.entidades.Imovel;
import java.util.List;
/** Serviço de persistência de imoveis no SGBD 
 *
 * @author igor
 */
public interface ServicoImovel {
    /** Busca o imovel com o id especificado
     * 
     * @param id
     * @return Imovel, caso exista, null, caso contrário
     */
    Imovel get(int id);
    
    /** Busca todos os imoveis
     * 
     * @return 
     */
    List<Imovel> findAll();
    
    /** Deleta o imovel com o id especificado
     * 
     * @param id 
     */
    void delete(int id);
    
    /** Salva o imovel 
     * 
     * @param i 
     */
    void save(Imovel i);
    
     List<Imovel> findByTipo(int tipo);
    
    //List<Imovel> findByTipoByOrderByValorMinimoAsc(int tipo);
   // List<Imovel> findByTipoByOrderByNumeroQuartosAsc(int tipo);
   // List<Imovel> findByTipoByOrderByNumeroBanheirosAsc(int tipo);
    
   // List<Imovel> findByTipoByOrderByAreaAsc(int tipo);
}
