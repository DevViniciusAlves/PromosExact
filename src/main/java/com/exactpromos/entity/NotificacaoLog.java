package com.exactpromos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table (name = "notificacaoLogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class NotificacaoLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "promocao_id")
    private Promocao promocao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoNotificacao tipo;

    @Column(nullable = false, length = 1000)
    private String mensagem;

    @Column(nullable = false)
    private Boolean enviada = false;

    @Column(nullable = false)
    private LocalDateTime dataEnvio;

    private LocalDateTime dataLeitura;

}
