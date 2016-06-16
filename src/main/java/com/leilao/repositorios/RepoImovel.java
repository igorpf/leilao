/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.leilao.repositorios;

import com.leilao.entidades.Imovel;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author igor
 */
@Repository
public interface RepoImovel extends CrudRepository<Imovel, Integer>{
    List<Imovel> findByTipo(int tipo);
    
    List<Imovel> findByTipoByOrderByValorMinimoAsc(int tipo);
    List<Imovel> findByTipoByOrderByNumeroQuartosAsc(int tipo);
    List<Imovel> findByTipoByOrderByNumeroBanheirosAsc(int tipo);
    
    List<Imovel> findByTipoByOrderByAreaAsc(int tipo);
    
}
