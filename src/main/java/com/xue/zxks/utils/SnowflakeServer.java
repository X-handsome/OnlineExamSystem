package com.xue.zxks.utils;

import java.io.Serializable;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.model.relational.SqlStringGenerationContext;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

/**
 * @see IdentifierGenerator
 */
@Component
public class SnowflakeServer implements IdentifierGenerator {

    private IdWorker idWorker;

    @Override
    public void configure(
        Type type,
        Properties params,
        ServiceRegistry serviceRegistry
    ) throws MappingException {
        IdentifierGenerator.super.configure(type, params, serviceRegistry);
        long workerId = Long.parseLong(params.getProperty("workerId"));
        long datacenterId = Long.parseLong(params.getProperty("datacenterId"));
        long sequence = Long.parseLong(params.getProperty("sequence"));
        idWorker = new IdWorker(workerId, datacenterId, sequence);
    }

    @Override
    public void registerExportables(Database database) {
        IdentifierGenerator.super.registerExportables(database);
    }

    @Override
    public void initialize(SqlStringGenerationContext context) {
        IdentifierGenerator.super.initialize(context);
    }

    @Override
    public Serializable generate(
        SharedSessionContractImplementor sharedSessionContractImplementor,
        Object o
    ) throws HibernateException {
        return idWorker.nextId();
    }

    @Override
    public boolean supportsJdbcBatchInserts() {
        return IdentifierGenerator.super.supportsJdbcBatchInserts();
    }
}
