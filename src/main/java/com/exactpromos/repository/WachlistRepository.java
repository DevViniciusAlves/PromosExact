package com.exactpromos.repository;

import com.exactpromos.entity.Wachlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WachlistRepository extends JpaRepository<Wachlist, Long> {

    List<Wachlist> findByUsuarioId(Long usuarioId);
}
