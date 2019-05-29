package com.funny.mall.base;


import java.util.List;

/**
 * 基础Service接口实现<br>
 * 处理公用的CRUD、分页等
 *
 * @author fangli 2014-7-28
 */
public abstract class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {


    protected abstract BaseMapper<T> baseMapper();

    /**
     * 保存实体
     *
     * @throws Exception
     */
    @Override
    public int insert(T entity) throws Exception {
        return baseMapper().insert(entity);
    }


    /**
     * 更新实体(根据主键ID)
     *
     * @throws Exception
     */
    @Override
    public int updateById(T entity) throws Exception {
        return baseMapper().updateById(entity);
    }

    /**
     * 返回实体
     *
     * @return
     * @throws Exception
     */
    @Override
    public T findById(Long id) throws Exception {
        return baseMapper().findById(id);
    }

    /**
     * 返回实体List
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<T> findList(T entity) throws Exception {
        return baseMapper().findList(entity);
    }

    @Override
    public int count(T entity) throws Exception {
        return baseMapper().count(entity);
    }

}
