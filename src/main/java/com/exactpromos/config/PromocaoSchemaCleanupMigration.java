package com.exactpromos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PromocaoSchemaCleanupMigration {

    private static final Logger log = LoggerFactory.getLogger(PromocaoSchemaCleanupMigration.class);

    private final JdbcTemplate jdbcTemplate;

    public PromocaoSchemaCleanupMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void removerColunaCashbackSeExistir() {
        Integer existe = jdbcTemplate.queryForObject("""
                select count(*)
                from information_schema.columns
                where table_schema = 'public'
                  and table_name = 'promocoes'
                  and column_name = 'cashback'
                """, Integer.class);

        if (existe != null && existe > 0) {
            jdbcTemplate.execute("alter table promocoes drop column cashback");
            log.info("Coluna cashback removida da tabela promocoes");
        }
    }
}
