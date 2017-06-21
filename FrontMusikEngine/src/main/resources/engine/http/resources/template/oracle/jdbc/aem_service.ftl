package com.xiaoyang.service.Impl.${ent.packageN};

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoyang.entity.${ent.packageN}.${ent.entityName?cap_first};

/**
 * 此文件由应用引擎生成，严禁修改，如重复生成时，该文件将被覆盖。
 * ${ent.entityName?cap_first}Service
 *<#if ent.remark??>${ent.remark}</#if>服务接口类
 * @author 应用引擎自动生成
 * @date ${nowDate}
 *
 */ 
 public interface ${ent.entityName?cap_first}Service {
 	/**
	 * 删除函数
	 * @param List<${ent.entityName?cap_first}>
	 * @return int 返回删除记录行数
	 */
	public int ${ent.entityName?uncap_first}_delete(List<${ent.entityName?cap_first}>  _delete${ent.entityName?cap_first}List);

	/**功能：查询 不分页,不建议使用。
	*  建议使用 queryByPage(${ent.entityName?cap_first} _${ent.entityName?uncap_first},Integer page,Integer rows),将 分页信息page,rows设置为0即可。
    * @param ${ent.entityName?cap_first}
	* @return int 返回查询记录行数
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<${ent.entityName?cap_first}> ${ent.entityName?uncap_first}_query(${ent.entityName?cap_first} _${ent.entityName?uncap_first});

	/**功能：分页查询，建议使用。
	* @param ${ent.entityName?cap_first},Integer,Integer
	* @return List<${ent.entityName?cap_first}> 返回查询记录动态对象数组
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<${ent.entityName?cap_first}> ${ent.entityName?uncap_first}_queryByPage(${ent.entityName?cap_first} _${ent.entityName?uncap_first},Integer page,Integer rows);

	/**功能：查询记录条数
	* @param ${ent.entityName?cap_first}
	* @return Integer 返回查询记录条数
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Integer ${ent.entityName?uncap_first}_queryCount(${ent.entityName?cap_first} _${ent.entityName?uncap_first});

	/**功能：插入对象
	* @param List<${ent.entityName?cap_first}>
	* @return int 返回 插入 记录条数
	*/
	public int ${ent.entityName?uncap_first}_save(List<${ent.entityName?cap_first}>  _save${ent.entityName?cap_first}List);

	/**功能：修改对象
	* @param List<${ent.entityName?cap_first}>
	* @return int 返回 修改 记录条数
	*/
	public int ${ent.entityName?uncap_first}_update(List<${ent.entityName?cap_first}>  _update${ent.entityName?cap_first}List) ;

	/**功能：自由条件查询 不分页，不建议使用。
	*  建议使用 queryUserDefineConditionByPage(String sql,Integer page,Integer rows),将 page,rows 分页信息设置为空即可。
	* @param ${ent.entityName?cap_first}
	* @return List<${ent.entityName?cap_first}> 返回查询记录动态对象数组
	*/
	public List<${ent.entityName?cap_first}> ${ent.entityName?uncap_first}_queryUserDefineCondition(String sql);

	/**功能：自由条件查询分页
	* @param sql,page rows
	* @return List<${ent.entityName?cap_first}> 返回查询记录动态对象数组
	*/
	public List<${ent.entityName?cap_first}> ${ent.entityName?uncap_first}_queryUserDefineConditionByPage(String sql,Integer page,Integer rows);

}	