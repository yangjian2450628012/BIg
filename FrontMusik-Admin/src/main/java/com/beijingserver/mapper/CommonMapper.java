package com.beijingserver.mapper;

import com.beijingserver.pojo.Params;

import java.util.List;
import java.util.Map;

/**
 * Created by xiaoJ on 5/23/2017.
 */
public interface  CommonMapper {
    /**
     * 执行增删改sql语句
     *
     * @author zhy
     * @param sql sql语句
     * @return
     */
    public long executeAction(String sql);

    /**
     * 根据sql语句查询单调数据
     *
     * @author zhy
     * @param sql sql语句
     * @return
     */
    public Map<String, Object> findOneData(String sql);

    /**
     * 根据sql语句查询多调数据
     *
     * @author zhy
     * @param sql sql语句
     * @return
     */
    public List<Map<String, Object>> findManyData(String sql);

    /**
     * 添加实体数据
     *
     * @author zhy
     * @param params 添加参数类
     * @return
     */
    public int addEntity(Params params);

    /**
     * 根据sql语句查询条数
     *
     * @author zhy
     * @param sql sql语句
     * @return
     */
    public long findCount(String sql);

    /**
     * 批量添加
     *
     * @author zhy
     * @param params 添加参数
     * @return
     */
    public int batchAdd(Params params);

    /**
     * 批量删除
     *
     * @author zhy
     * @param params 参数类
     * @return
     */
    public int batchDelete(Params params);

    /**
     * 根据sql语句查询单个值
     *
     * @author zhy
     * @param sql sql语句
     * @return
     */
    public Object findOneValue(String sql);

}
