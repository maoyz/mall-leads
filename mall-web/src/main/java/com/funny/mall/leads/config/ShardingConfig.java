package com.funny.mall.leads.config;

import com.google.common.collect.Lists;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.funny.mall.leads.dao", sqlSessionTemplateRef = "shardSqlSessionTemplate")
public class ShardingConfig {

    @ConfigurationProperties(prefix = "spring.datasource.hikari.master0")
    @Bean(name = "master0")
    public HikariDataSource dataSource0() {
        return new HikariDataSource();
    }

    @ConfigurationProperties(prefix = "spring.datasource.hikari.master0slave0")
    @Bean(name = "master0slave0")
    public HikariDataSource dataSource00() {
        return new HikariDataSource();
    }

    @ConfigurationProperties(prefix = "spring.datasource.hikari.master0slave1")
    @Bean(name = "master0slave1")
    public HikariDataSource dataSource01() {
        return new HikariDataSource();
    }

    @ConfigurationProperties(prefix = "spring.datasource.hikari.master1")
    @Bean(name = "master1")
    public HikariDataSource dataSource1() {
        return new HikariDataSource();
    }

    @ConfigurationProperties(prefix = "spring.datasource.hikari.master1slave0")
    @Bean(name = "master1slave0")
    public HikariDataSource dataSource10() {
        return new HikariDataSource();
    }

    @ConfigurationProperties(prefix = "spring.datasource.hikari.master1slave1")
    @Bean(name = "master1slave1")
    public HikariDataSource dataSource11() {
        return new HikariDataSource();
    }

    @Bean(name = "shardingDataSource")
    @Primary
    public DataSource getDataSource(@Qualifier("master0") DataSource master0,
                                    @Qualifier("master0slave0") DataSource master0slave0,
                                    @Qualifier("master0slave1") DataSource master0slave1,
                                    @Qualifier("master1") DataSource master1,
                                    @Qualifier("master1slave0") DataSource master1slave0,
                                    @Qualifier("master1slave1") DataSource master1slave1) throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(getOrderTableRuleConfiguration());
        shardingRuleConfig.getTableRuleConfigs().add(getOrderItemTableRuleConfiguration());
        shardingRuleConfig.getBindingTableGroups().add("t_order,t_order_item");

        shardingRuleConfig.setMasterSlaveRuleConfigs(getMasterSlaveRuleConfigurations());

        // 分库算法
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration("user_id",
                new DatabaseShardingAlgorithm()));
        /// 分表算法
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("order_id",
                new TableShardingAlgorithm(), new TableRangeShardingAlgorithm()));

        Map<String, DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put("master0", master0);
        dataSourceMap.put("master0slave0", master0slave0);
        dataSourceMap.put("master0slave1", master0slave1);
        dataSourceMap.put("master1", master1);
        dataSourceMap.put("master1slave0", master1slave0);
        dataSourceMap.put("master1slave1", master1slave1);
        Properties properties = new Properties();
        properties.setProperty("sql.show", Boolean.TRUE.toString());
        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, properties);
    }

    //
    private static TableRuleConfiguration getOrderTableRuleConfiguration() {
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration("t_order","ds${0..1}.t_order_${0..1}");
        //配置逻辑表名，并非数据库中真实存在的表名，而是sql中使用的那个，不受分片策略而改变.
        //例如：select * frpm t_order where user_id = xxx
        //配置真实的数据节点，即数据库中真实存在的节点，由数据源名 + 表名组成
        //${} 是一个groovy表达式，[]表示枚举，{...}表示一个范围。
        //整个inline表达式最终会是一个笛卡尔积，表示ds_0.t_order_0. ds_0.t_order_1
        // ds_1.t_order_0. ds_1.t_order_0
        //主键生成列，默认的主键生成算法是 SNOWFLAKE
//        KeyGeneratorConfiguration keyGeneratorConfiguration = new KeyGeneratorConfiguration("SNOWFLAKE","order_id");
//        orderTableRuleConfig.setKeyGeneratorConfig(keyGeneratorConfiguration);
        //设置分片策略，这里简单起见直接取模，也可以使用自定义算法来实现分片规则
        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id",
                "t_order_${order_id % 2}"));

        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration("user_id",
                new DatabaseShardingAlgorithm()));

        return orderTableRuleConfig;
    }


    List<MasterSlaveRuleConfiguration> getMasterSlaveRuleConfigurations() {
        MasterSlaveRuleConfiguration masterSlaveRuleConfig1 = new MasterSlaveRuleConfiguration("ds0",
                "master0", Arrays.asList("master0slave0", "master0slave1"));
        MasterSlaveRuleConfiguration masterSlaveRuleConfig2 = new MasterSlaveRuleConfiguration("ds1",
                "master1", Arrays.asList("master1slave0", "master1slave1"));
        return Lists.newArrayList(masterSlaveRuleConfig1, masterSlaveRuleConfig2);
    }




    private static TableRuleConfiguration getOrderItemTableRuleConfiguration() {
        TableRuleConfiguration result = new TableRuleConfiguration("t_order_item","ds${0..1}.t_order_item_${0..1}");
        return result;
    }


    @Bean(name = "shardSqlSessionFactory")
    public SqlSessionFactory shardSqlSessionFactory(@Qualifier("shardingDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        bean.setConfigLocation(new DefaultResourceLoader().getResource("classpath:mybatis-config.xml"));
        return bean.getObject();
    }

    @Bean(name = "shardTransactionManager")
    public DataSourceTransactionManager shardTransactionManager(@Qualifier("shardingDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "shardSqlSessionTemplate")
    public SqlSessionTemplate shardSqlSessionTemplate(@Qualifier("shardSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


}