package com.exactpromos.repository;

import com.exactpromos.Enum.PlataformaEnum;
import com.exactpromos.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByProdutoIdAndPlataforma(String produtoId, PlataformaEnum plataforma);

    Optional<Produto> findByProdutoId(String produtoId);
}
