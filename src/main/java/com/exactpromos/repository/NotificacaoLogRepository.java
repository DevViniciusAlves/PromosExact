package com.exactpromos.repository;

import com.exactpromos.entity.NotificacaoLog;
import com.exactpromos.entity.Promocao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacaoLogRepository extends JpaRepository<NotificacaoLog, Long> {
    boolean existsByPromocaoAndEnviadaTrue(Promocao promocao);
}

