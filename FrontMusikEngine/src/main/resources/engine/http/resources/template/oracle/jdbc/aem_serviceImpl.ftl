package com.xiaoyang.service.Impl.${ent.packageN};

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoyang.dao.${ent.packageN}.${ent.entityName?cap_first}Dao;
import com.xiaoyang.entity.${ent.packageN}.${ent.entityName?cap_first};
import com.xiaoyang.util.system.AeMustRollBackException;

/**
 * 此文件由应用引擎生成，严禁修改，如重复生成时，该文件将被覆盖。
 * ${ent.entityName?cap_first}ServiceImpl
 * <#if ent.remark??>${ent.remark}</#if>服务实现类
 * @author 应用引擎自动生成
 * @date ${nowDate}
 *
 */ 
@Service("${ent.entityName?uncap_first}Service")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,rollbackFor = AeMustRollBackException.class)
public class ${ent.entityName?cap_first}ServiceImpl implements ${ent.entityName?cap_first}Service{
	// 声明DAO属性
	private ${ent.entityName?cap_first}Dao ${ent.entityName?uncap_first}Dao;

	/**删除函数
	 * 
	 * @param List<${ent.entityName?cap_first}>
	 * @return int 返回删除记录行数
	 */
	public int ${ent.entityName?uncap_first}_delete(List<${ent.entityName?cap_first}> _delete${ent.entityName?cap_first}List) {
		
		try {
			
			if ( _delete${ent.entityName?cap_first}List == null) {
				_delete${ent.entityName?cap_first}List= new ArrayList<${ent.entityName?cap_first}>();
			}
			return this.${ent.entityName?uncap_first}Dao.delete(_delete${ent.entityName?cap_first}List);
			
		} catch (Exception e) {
			
			throw new AeMustRollBackException(e.getMessage());
		}
	}

	/**功能：查询 不分页,不建议使用。
	*  建议使用 queryByPage(${ent.entityName?cap_first} ${ent.entityName?uncap_first} ,Integer page,Integer rows),将 分页信息page,rows设置为0即可。
    * @param ${ent.entityName?cap_first} 
	* @return int 返回查询记录行数
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<${ent.entityName?cap_first}> ${ent.entityName?uncap_first}_query(${ent.entityName?cap_first} _${ent.entityName?uncap_first} ) {
		
		try {
			
			if ( _${ent.entityName?uncap_first}  == null) {
				_${ent.entityName?uncap_first} = new ${ent.entityName?cap_first}();
			}
			return this.${ent.entityName?uncap_first}Dao.select(_${ent.entityName?uncap_first} );
			
		} catch (Exception e) {
			
			throw new AeMustRollBackException(e.getMessage());
		}
	}

	/**功能：分页查询，建议使用。
	* @param ${ent.entityName?cap_first},page,rows
	* @return List<${ent.entityName?cap_first}> 返回查询记录动态对象数组
	*/
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<${ent.entityName?cap_first}> ${ent.entityName?uncap_first}_queryByPage(${ent.entityName?cap_first} _${ent.entityName?uncap_first}, Integer page, Integer rows) {
		
		try {
			
			if (_${ent.entityName?uncap_first} == null) {
				_${ent.entityName?uncap_first} = new ${ent.entityName?cap_first}();
			}
			return this.${ent.entityName?uncap_first}Dao.selectByPage(_${ent.entityName?uncap_first}, page, rows);
			
		} catch (Exception e) {
			
			throw new AeMustRollBackException(e.getMessage());
		}
	}

	/**功能：查询记录条数
	* @param ${ent.entityName?cap_first}
	* @return Integer 返回查询记录条数
	*/
	public Integer ${ent.entityName?uncap_first}_queryCount(${ent.entityName?cap_first} _${ent.entityName?uncap_first}) {
		
		try {
			
			if (_${ent.entityName?uncap_first} == null) {
				_${ent.entityName?uncap_first} = new ${ent.entityName?cap_first}();
			}
			return this.${ent.entityName?uncap_first}Dao.selectCount(_${ent.entityName?uncap_first});
			
		} catch (Exception e) {
			
			throw new AeMustRollBackException(e.getMessage());
		}
	}

	/**功能：插入对象
	* @param List<${ent.entityName?cap_first}>
	* @return int 返回 插入 记录条数
	*/
	public int ${ent.entityName?uncap_first}_save(List<${ent.entityName?cap_first}> _save${ent.entityName?cap_first}List) {
		
		try {
			
			if (_save${ent.entityName?cap_first}List == null) {
				_save${ent.entityName?cap_first}List = new ArrayList<${ent.entityName?cap_first}>();
			}
			return this.${ent.entityName?uncap_first}Dao.insert(_save${ent.entityName?cap_first}List);
			
		} catch (Exception e) {
			
			throw new AeMustRollBackException(e.getMessage());
		}
	}

	/**功能：修改对象
	* @param List<${ent.entityName?cap_first}>
	* @return int 返回 修改 记录条数
	*/
	public int ${ent.entityName?uncap_first}_update(List<${ent.entityName?cap_first}> _update${ent.entityName?cap_first}List) {
		
		try {
			
			if (_update${ent.entityName?cap_first}List == null) {
				_update${ent.entityName?cap_first}List = new ArrayList<${ent.entityName?cap_first}>();
			}
			return this.${ent.entityName?uncap_first}Dao.update(_update${ent.entityName?cap_first}List);
			
		} catch (Exception e) {
			
			throw new AeMustRollBackException(e.getMessage());
		}
	}

	/**功能：自由条件查询 不分页，不建议使用。
	*  建议使用 queryUserDefineConditionByPage(String sql,Integer page,Integer rows),将 page,rows 分页信息设置为空即可。
	* @param ${ent.entityName?cap_first}
	* @return List<${ent.entityName?cap_first}> 返回查询记录动态对象数组
	*/
	public List<${ent.entityName?cap_first}> ${ent.entityName?uncap_first}_queryUserDefineCondition(String sql) {
		try {
			
			return this.${ent.entityName?uncap_first}Dao.queryUserDefineCondition(sql);
			
		} catch (Exception e) {
			
			throw new AeMustRollBackException(e.getMessage());
		}
	}

	/**功能：自由条件查询分页
	* @param sql,page rows
	* @return List<${ent.entityName?cap_first}> 返回查询记录动态对象数组
	*/
	public List<${ent.entityName?cap_first}> ${ent.entityName?uncap_first}_queryUserDefineConditionByPage(String sql, Integer page, Integer rows) {

		try {
		
			return this.${ent.entityName?uncap_first}Dao.queryUserDefineConditionByPage(sql,page,rows);
			
		} catch (Exception e) {
		
			throw new AeMustRollBackException(e.getMessage());
		}
	}

	/**功能：自定义SQL查询记录条数,返回Integer，记录条数
	* sql 为用户定义sql条件，如果全部查询请填写null
    * @param extParm
	* @return Integer 记录条数
	*/
	public Integer ${ent.entityName?uncap_first}_queryUserDefineConditionCount(String sql) {
		try {

			return this.${ent.entityName?uncap_first}Dao.queryForUserDefineSQLCount(sql);

		} catch (Exception e) {

			throw new AeMustRollBackException(e.getMessage());
		}
	}

	public ${ent.entityName?cap_first}Dao get${ent.entityName?uncap_first}Dao() {
		return ${ent.entityName?uncap_first}Dao;
	}
	@Autowired
	public void set${ent.entityName?uncap_first}Dao(${ent.entityName?cap_first}Dao ${ent.entityName?uncap_first}Dao) {
		this.${ent.entityName?uncap_first}Dao = ${ent.entityName?uncap_first}Dao;
	}
}
