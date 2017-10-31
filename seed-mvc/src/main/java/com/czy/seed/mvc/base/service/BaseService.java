package com.czy.seed.mvc.base.service;

import com.czy.seed.mybatis.base.QueryParams;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 基本Service
 * 其中，关于操作成功条件的返回值，只有在Mybatis 的 defaultExecutorType 配置为“SIMPLE”时才有效
 * Created by PLC on 2017/4/30.
 */
public interface BaseService<T> {

    /**
     * 新增数据
     *
     * @param record 要新增的数据实体
     * @return 操作成功的条数
     */
    int insert(T record);

    /**
     * 批量新增
     * 使用注意：
     * 1.recordList过长时，一次性提交会失败（SQL长度超过数据库允许）
     * 2.recordList中，如果有实体的id不为空(被指定了值)，则返回的id不正确。
     * @param recordList 要新增的数据列表
     *
     * @return 操作成功的条数
     */
    int insertList(List<T> recordList);

    /**
     * 根据主键查询数据——只查询本表数据
     *
     * @param key 主键值
     * @return
     */
    T selectByPrimaryKey(long key);

    /**
     * 根据主键查询数据——查询关联表数据
     *
     * @param key 主键值
     * @return
     */
    T selectRelativeByPrimaryKey(long key);

    /**
     * 根据参数查询数据列表——只查询本表数据
     *
     * @param params 查询参数
     * @return
     */
    List<T> selectListByParams(QueryParams params);

    /**
     * 根据参数查询数据列表——查询关联表数据
     *
     * @param params 查询参数
     * @return
     */
    List<T> selectListRelativeByParams(QueryParams params);

    /**
     * 根据参数查询一条数据——只查询本表数据
     * 当匹配查询条件的数据超过1条时，将抛出异常
     *
     * @param params 查询参数
     * @return
     */
    T selectOneByParams(QueryParams params);

    /**
     * 根据参数查询一条数据——查询关联表数据
     * 当匹配查询条件的数据超过1条时，将抛出异常
     *
     * @param params 查询参数
     * @return
     */
    T selectOneRelativeByParams(QueryParams params);

    /**
     * 根据参数统计数量
     *
     * @param params 查询参数
     * @return
     */
    int selectCountByParams(QueryParams params);

    /**
     * 分页查询——只查询本表数据
     * @param pageNo    页号
     * @param pageLimit 分页大小
     * @param params    查询参数
     * @return
     */
    Page<T> selectPageByParams(int pageNo, int pageLimit, final QueryParams params);

    /**
     * 分页查询——查询关联表数据
     * @param pageNo    页号
     * @param pageLimit 分页大小
     * @param params    查询参数
     * @return
     */
    Page<T> selectPageRelativeByParams(int pageNo, int pageLimit, final QueryParams params);

    /**
     * 根据主键修改数据，参数中为空的字段将设置为空
     *
     * @param record 实体数据 主键不能为空
     * @return 返回更新成功的条数
     */
    int updateByPrimaryKey(T record);

    /**
     * 根据主键修改数据，参数中为空的字段将不做修改
     *
     * @param record 实体数据 主键不能为空
     * @return 返回更新成功的条数
     */
    @Deprecated
    int updateByPrimaryKeySelective(T record);

    /**
     * 根据主键修改数据，参数中为空的字段将不做修改
     *
     * @param record 实体数据 主键不能为空
     * @return 返回更新成功的条数
     */
    int updateSelectiveByPrimaryKey(T record);

    /**
     * 根据参数修改数据，record中值为null的字段，在数据库中将“同样设置为null”
     *
     * @param record 修改值
     * @param params 查询参数
     * @return
     */
    int updateByParams(@Param("record") T record, @Param("params") QueryParams params);

    /**
     * 根据参数修改数据，record中值为null的字段，在数据库中将“保留现有值”
     *
     * @param record 修改值
     * @param params 查询参数
     * @return
     */
    int updateSelectiveByParams(@Param("record") T record, @Param("params") QueryParams params);

    /**
     * 根据主键删除数据
     *
     * @param id 主键
     * @return
     */
    int deleteByPrimaryKey(long id);

    /**
     * 根据参数删除数据
     *
     * @param params
     * @return
     */
    int deleteByParams(QueryParams params);

}
