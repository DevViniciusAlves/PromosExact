package com.exactpromos.repository;

import com.exactpromos.Enum.PromocaoEnum;
import com.exactpromos.entity.Promocao;
import com.exactpromos.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PromocaoRepository extends JpaRepository<Promocao, Long> {

    boolean existsByProdutoAndStatusIn(Produto produto, Collection<PromocaoEnum> statuses);

}
