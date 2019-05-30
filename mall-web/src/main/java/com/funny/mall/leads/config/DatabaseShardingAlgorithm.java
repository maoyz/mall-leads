package com.funny.mall.leads.config;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

public final class DatabaseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Long> preciseShardingValue) {
        int size = collection.size();
        for (String each : collection) {
            if (each.endsWith(preciseShardingValue.getValue() % size + "")) {
                return each;
            }
        }
        throw new UnsupportedOperationException();
    }
}