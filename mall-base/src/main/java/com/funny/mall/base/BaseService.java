package com.funny.mall.base;



import java.util.List;

/**
 * 基础Service接口<br>
 * 处理公用的CRUD、分页等
 * 
 * @author fangli 2018-7-28
 * 
 * @param <T>
 */
public interface BaseService<T extends BaseEntity> {

	/**
	 * 保存实体
	 * 
	 * @throws Exception
	 */
	int insert(T entity) throws Exception;

	/**
	 * 更新实体
	 * 
	 * @throws Exception
	 */
	int updateById(T entity) throws Exception;

	/**
	 * 返回实体
	 * 
	 * @return
	 * @throws Exception
	 */
	T findById(Long id) throws Exception;
	/**
	 * 返回实体List
	 * 
	 * @return
	 * @throws Exception
	 */
	List<T> findList(T entity) throws Exception;

	int count(T entity) throws Exception;
}
