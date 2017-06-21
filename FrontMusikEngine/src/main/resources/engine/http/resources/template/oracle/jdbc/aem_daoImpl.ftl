package com.xiaoyang.dao.${ent.packageN};

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.xiaoyang.util.system.AeMustRollBackException;
import com.xiaoyang.entity.${ent.packageN}.${ent.entityName?cap_first};
import com.xiaoyang.util.system.ClassRowsMapper;
import com.xiaoyang.util.system.ClassSQLWrite;
import com.xiaoyang.util.system.DeAnnotaion;
import com.xiaoyang.util.system.Tools;

/**
 * 此文件由应用引擎生成，严禁修改，如重复生成时，该文件将被覆盖。
 * ${ent.entityName?cap_first}DaoImpl
 * <#if ent.remark??>${ent.remark}</#if>数据访问实现类
 * @author 应用引擎自动生成
 * @date ${nowDate}
 *
 */ 
@Repository("${ent.entityName?uncap_first}Dao")
public class ${ent.entityName?cap_first}DaoImpl implements ${ent.entityName?cap_first}Dao{
	private ClassSQLWrite classSQLWrite = new ClassSQLWrite();

	private JdbcTemplate jdbcTemplate;

	//插入记录
	public int insert(List<${ent.entityName?cap_first}> _${ent.entityName?uncap_first}List) 
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		int count = 0;
		try {
			if(_${ent.entityName?uncap_first}List == null || _${ent.entityName?uncap_first}List.size() == 0)
				throw new RuntimeException("插入记录时输入的List不能为空，且长度不能为0！");
			//数据校验
			for (int i = 0; i < _${ent.entityName?uncap_first}List.size(); i++) {
				Field field =  ${ent.entityName?cap_first}.class.getDeclaredField("${fldList.COLUMN_NAME}");
				if(field.isAnnotationPresent(DeAnnotaion.class)){
					DeAnnotaion deannotaion = field.getAnnotation(DeAnnotaion.class);
					<#if fldList.SOURCE_DATA_TYPE=="java.lang.Integer">
					if(deannotaion.is_autoincrement())continue; 
					if(deannotaion.isPrimary() && (_${ent.entityName?uncap_first}List.get(i).get${fldList.COLUMN_NAME?cap_first}() ==null || _${ent.entityName?uncap_first}List.get(i).get${fldList.COLUMN_NAME?cap_first}() == 0)) 
						throw new RuntimeException("插入记录时VO的ID主键不能为空！"); 
					<#elseif fldList.SOURCE_DATA_TYPE=="java.lang.String">	
					if(deannotaion.isPrimary() && Tools.StringIsNullOrSpace(_${ent.entityName?uncap_first}List.get(i).get${fldList.COLUMN_NAME?cap_first}()))
						throw new RuntimeException("插入记录时VO的ID主键不能为空！");
					</#if>
				}
			}
			List<String> sqlList = new ArrayList<String>();
			List<List> valueList = new ArrayList<List>();
			int sqlcount = this.classSQLWrite.batchClassSQLInsert(_${ent.entityName?uncap_first}List,sqlList,valueList);
			if(sqlList == null || sqlList.size() ==0) 
				throw new RuntimeException("生成的插入SQL为空，请检查");
			ClassSQLWrite.printSQL("成功生成 "+sqlcount+" 条，插入SQL语句。");
			for (int i = 0; i < sqlList.size(); i++) {
				count ++;
				this.getJdbcTemplate().update(sqlList.get(i), valueList.get(i).toArray());
			}
			return count;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new AeMustRollBackException(e.getMessage());
		}
	}

	//删除记录
	public int delete(List<${ent.entityName?cap_first}> _${ent.entityName?uncap_first}List) 
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		int count = 0;
		try {
			if(_${ent.entityName?uncap_first}List == null || _${ent.entityName?uncap_first}List.size() == 0)
				throw new RuntimeException("删除记录时输入的List不能为空，且长度不能为0！");
			//数据校验
			for (int i = 0; i < _${ent.entityName?uncap_first}List.size(); i++) {
				Field field =  ${ent.entityName?cap_first}.class.getDeclaredField("${fldList.COLUMN_NAME}");
				if(field.isAnnotationPresent(DeAnnotaion.class)){
					DeAnnotaion deannotaion = field.getAnnotation(DeAnnotaion.class);
					<#if fldList.SOURCE_DATA_TYPE=="java.lang.Integer">
					if(deannotaion.isPrimary() && (_${ent.entityName?uncap_first}List.get(i).get${fldList.COLUMN_NAME?cap_first}() == null || _${ent.entityName?uncap_first}List.get(i).get${fldList.COLUMN_NAME?cap_first}() == 0)) 
						throw new RuntimeException("删除记录时VO的ID主键不能为空！"); 
					<#elseif fldList.SOURCE_DATA_TYPE=="java.lang.String">	
					if(deannotaion.isPrimary() && Tools.StringIsNullOrSpace(_${ent.entityName?uncap_first}List.get(i).get${fldList.COLUMN_NAME?cap_first}()))
						throw new RuntimeException("删除记录时VO的ID主键不能为空！");
					</#if>
				}
			}
			List<String> sqlList = new ArrayList<String>();
			List<List> paramList = new ArrayList<List>();
			int sqlcount = this.classSQLWrite.batchClassSQLDelete(_${ent.entityName?uncap_first}List,sqlList,paramList);
			if(sqlList == null || sqlList.size() ==0) 
				throw new RuntimeException("删除的插入SQL为空，请检查");
			ClassSQLWrite.printSQL("成功生成 "+sqlcount+" 条，更新SQL语句。");
			for (int i = 0; i < sqlList.size(); i++) {
				count ++;
				this.jdbcTemplate.update(sqlList.get(i), paramList.get(i).toArray());
			}
			return count;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new AeMustRollBackException(e.getMessage());
		}
	}

	//更新记录
	public int update(List<${ent.entityName?cap_first}> _${ent.entityName?uncap_first}List) 
			throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		int count = 0;
		List<String> sqlList = null;
		try {
			if(_${ent.entityName?uncap_first}List == null || _${ent.entityName?uncap_first}List.size() == 0)
				throw new RuntimeException("更新记录时输入的List不能为空，且长度不能为0！");
			//数据校验
			for (int i = 0; i < _${ent.entityName?uncap_first}List.size(); i++) {
				Field field =  ${ent.entityName?cap_first}.class.getDeclaredField("${fldList.COLUMN_NAME}");
				if(field.isAnnotationPresent(DeAnnotaion.class)){
					DeAnnotaion deannotaion = field.getAnnotation(DeAnnotaion.class);
					<#if fldList.SOURCE_DATA_TYPE=="java.lang.Integer">
					if(deannotaion.isPrimary() && (_${ent.entityName?uncap_first}List.get(i).get${fldList.COLUMN_NAME?cap_first}() == null || _${ent.entityName?uncap_first}List.get(i).get${fldList.COLUMN_NAME?cap_first}() == 0)) 
						throw new RuntimeException("更新记录时VO的ID主键不能为空！"); 
					<#elseif fldList.SOURCE_DATA_TYPE=="java.lang.String">	
					if(deannotaion.isPrimary() && Tools.StringIsNullOrSpace(_${ent.entityName?uncap_first}List.get(i).get${fldList.COLUMN_NAME?cap_first}()))
						throw new RuntimeException("更新记录时VO的ID主键不能为空！");
					</#if>
				}
			}
			sqlList = this.classSQLWrite.BatchClassSQLUpdate(_${ent.entityName?uncap_first}List);
			if(sqlList == null || sqlList.size() ==0) 
				throw new RuntimeException("更新的插入SQL为空，请检查");
			ClassSQLWrite.printSQL("成功生成 "+sqlList.size()+" 条，更新SQL语句。");
			for (String sql : sqlList) {
				count ++;
				classSQLWrite.printSQL(sql); //输出SQL日志
				this.jdbcTemplate.execute(sql); //执行sql
			}
			return count;
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new AeMustRollBackException(e.getMessage());
		}
	}

	//使用对象检索返回List
	public List<${ent.entityName?cap_first}> select(${ent.entityName?cap_first} _${ent.entityName?uncap_first}) 
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		List<${ent.entityName?cap_first}> ${ent.entityName?uncap_first}List = null;
		String sql;

		sql = this.classSQLWrite.ClassSQLSelect(${ent.entityName?uncap_first}List);
		ClassRowsMapper<${ent.entityName?cap_first}> classRowsMapper = new ClassRowsMapper<${ent.entityName?cap_first}>(${ent.entityName?cap_first}.class);

		${ent.entityName?uncap_first}List = this.jdbcTemplate.query(sql, classRowsMapper);
		ClassSQLWrite.printSQL(sql);
		return ${ent.entityName?uncap_first}List;
	}

	//分页查询
	public List<${ent.entityName?cap_first}> selectByPage(${ent.entityName?cap_first} _${ent.entityName?cap_first}, Integer page, Integer rows) 
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		List<${ent.entityName?cap_first}> ${ent.entityName?cap_first}List = null;
		String sql; 
		
		sql = this.classSQLWrite.ClassSQLSelectByPage(${ent.entityName?cap_first}List);
		ClassRowsMapper<${ent.entityName?cap_first}> classRowsMapper = new ClassRowsMapper<${ent.entityName?cap_first}>(${ent.entityName?cap_first}.class);
		${ent.entityName?cap_first}List = this.jdbcTemplate.query(sql, classRowsMapper);
		ClassSQLWrite.printSQL(sql);
		
		return ${ent.entityName?cap_first}List;
	}

	//查询记录行数
	@SuppressWarnings("deprecation")
	public Integer selectCount(${ent.entityName?cap_first} _${ent.entityName?uncap_first}) 
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String sql;

		sql = this.classSQLWrite.ClassSQLSelectCount(_${ent.entityName?uncap_first});
		ClassSQLWrite.printSQL(sql);
		return this.jdbcTemplate.queryForInt(sql);
	}

	//自由条件查询
	public List<${ent.entityName?cap_first}> queryUserDefineCondition(String sql) 
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String sqlZ;
		List<${ent.entityName?cap_first}> ${ent.entityName?uncap_first}List;
		${ent.entityName?cap_first} ${ent.entityName?uncap_first} = new ${ent.entityName?cap_first}();
		
		sqlZ = this.classSQLWrite.ClassSQLSelectUserDefineCondition(${ent.entityName?uncap_first},sql);
		ClassRowsMapper<${ent.entityName?cap_first}> classRowsMapper = new ClassRowsMapper<${ent.entityName?cap_first}>(${ent.entityName?cap_first}.class);
		${ent.entityName?uncap_first}List = this.jdbcTemplate.query(sql,classRowsMapper);
		ClassSQLWrite.printSQL(sqlZ);
		
		return ${ent.entityName?uncap_first}List;
	}

	//自由条件查询分页
	public List<${ent.entityName?cap_first}> queryUserDefineConditionByPage(String sql, Integer page, Integer rows) 
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		String sqlZ;
		List<${ent.entityName?cap_first}> ${ent.entityName?uncap_first}List;
		${ent.entityName?cap_first} ${ent.entityName?uncap_first} = new ${ent.entityName?cap_first}();
		
		sqlZ = this.classSQLWrite.ClassSQLSelectUserDefineConditionByPage(${ent.entityName?uncap_first},sql,page,rows);
		ClassRowsMapper<${ent.entityName?cap_first}> classRowsMapper = new ClassRowsMapper<${ent.entityName?cap_first}>(${ent.entityName?cap_first}.class);
		${ent.entityName?uncap_first}List = this.jdbcTemplate.query(sql,classRowsMapper);
		ClassSQLWrite.printSQL(sqlZ);
		
		return ${ent.entityName?uncap_first}List;
	}

	// 自定义SQL查询,需传入一整个SQL，但需注意规则：select 列名 as 实体属性名,...... from 表名。
	//表名可不是该实体，但结果集别名必须与实体属性名称一致。
	public List<${ent.entityName?cap_first}> queryForUserDefineSQL(String sql, ${ent.entityName?cap_first} _${ent.entityName?uncap_first}) 
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		//TODO
		return null;
	}

	//自定义条件查询 查询记录条数 实现函数
	public Integer queryForUserDefineSQLCount(String sql) 
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		if (Tools.StringIsNullOrSpace(sql)) {
			sql = "select count(1) from ${ent.entityName?cap_first} ";
		} else {
			sql = "select count(1) from ${ent.entityName?cap_first} " + sql;
		}
		
		ClassSQLWrite.printSQL(sql);
		return jdbcTemplate.queryForInt(sql);
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}	