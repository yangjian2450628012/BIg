package com.xiaoyang.dao.${ent.packageN};

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.xiaoyang.entity.${ent.packageN}.${ent.entityName?cap_first};

/**
 * 此文件由应用引擎生成，严禁修改，如重复生成时，该文件将被覆盖。
 * ${ent.entityName?cap_first}Dao
 * <#if ent.remark??>${ent.remark}</#if>数据访问接口
 * @author 应用引擎自动生成
 * @date ${nowDate}
 *
 */ 
 public interface ${ent.entityName?cap_first}Dao {

 	/** 插入记录
	 * @param _${ent.entityName?uncap_first}List 要保存的数据集合
	 * @return 返回插入记录数
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public abstract int insert(List<${ent.entityName?cap_first}> _${ent.entityName?uncap_first}List)
			throws IllegalArgumentException, IllegalAccessException,InvocationTargetException;
			
	/** 删除记录
	 * @param _${ent.entityName?uncap_first}List 要删除的数据集合
	 * @return 返回删除记录条数
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public abstract int delete(List<${ent.entityName?cap_first}> _${ent.entityName?uncap_first}List)
			throws IllegalArgumentException, IllegalAccessException,InvocationTargetException;		
			
	/** 更新记录
	 * @param _${ent.entityName?uncap_first}List 要更新的数据集合
	 * @return 返回更新影响记录数
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public abstract int update(List<${ent.entityName?cap_first}> _${ent.entityName?uncap_first}List)
			throws IllegalArgumentException, SecurityException,IllegalAccessException, InvocationTargetException,NoSuchMethodException;
			
	/** 使用对象检索返回List
	 * @param _${ent.entityName?uncap_first} 实体对象
	 * @return 返回查询结果列表
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public abstract List<${ent.entityName?cap_first}> select(${ent.entityName?cap_first} _${ent.entityName?uncap_first})
			throws IllegalArgumentException, IllegalAccessException,InvocationTargetException;		
			
	/** 分页查询
	 * @param _${ent.entityName?uncap_first} 实体对象
	 * @param page 第几页
	 * @param rows 显示条数
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public abstract List<${ent.entityName?cap_first}> selectByPage(${ent.entityName?cap_first} _${ent.entityName?uncap_first}, Integer page,Integer rows)
			throws IllegalArgumentException, IllegalAccessException,InvocationTargetException;	
			
	/**查询记录行数
	 * @param _${ent.entityName?uncap_first} 实体对象
	 * @return 返回记录条数
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public abstract Integer selectCount(${ent.entityName?cap_first} _${ent.entityName?uncap_first})
			throws IllegalArgumentException, IllegalAccessException,InvocationTargetException;	
			
	/** 自定义条件查询
	 * @param sql 自定义sql结尾部分
	 * @return 返回查询结果的数据集合
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public abstract List<${ent.entityName?cap_first}> queryUserDefineCondition(String sql)
			throws IllegalArgumentException, IllegalAccessException,InvocationTargetException;			
				
	/** 自定义条件查询分页
	 * @param sql 自定义sql结尾部分
	 * @param page 第几页
	 * @param rows 显示条数
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public List<${ent.entityName?cap_first}> queryUserDefineConditionByPage(String sql,Integer page,Integer rows)
			throws IllegalArgumentException, IllegalAccessException,InvocationTargetException;	
			
	/** 自定义SQL查询,可用于复杂字典转换或多表查询
	 * @param sql 自定义sql
	 * @param _${ent.entityName?uncap_first} 实体对象
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public List<${ent.entityName?cap_first}> queryForUserDefineSQL(String sql,${ent.entityName?cap_first} _${ent.entityName?uncap_first}) 
			throws IllegalArgumentException,IllegalAccessException, InvocationTargetException,SecurityException, NoSuchMethodException;
			
	/** 自定义条件查询 查询记录条数 接口函数
	 * @param sql 自定义sql结尾部分
	 * @return 返回查询结果条数
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public abstract Integer queryForUserDefineSQLCount(String sql)
			throws IllegalArgumentException, IllegalAccessException,InvocationTargetException;
}						