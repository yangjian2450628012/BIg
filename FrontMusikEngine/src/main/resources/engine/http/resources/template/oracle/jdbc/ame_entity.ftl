package com.xiaoyang.entity.${ent.packageN};

import java.io.Serializable;
import com.xiaoyang.util.system.DeAnnotaion;

/**
 * 此文件由应用引擎生成，严禁修改，如重复生成时，该文件将被覆盖。
 * ${ent.entityName?cap_first} 实体信息 
 * <#if ent.remark ??>${ent.remark}<#else>实体</#if>
 * @author 应用引擎自动生成
 * @date ${nowDate}
 *	
 */

public class ${ent.entityName?cap_first} implements Serializable{
	private static final long serialVersionUID = 1L;

<#list fldList as fld>
<#if (fld.PRIMARYKEYS=="YES")&&(fld.IS_AUTOINCREMENT=="YES")>
	//${fld.REMARKS}
	@DeAnnotaion(isPrimary=true,is_autoincrement=true)//自增,主键
	private ${fld.SOURCE_DATA_TYPE} ${fld.COLUMN_NAME};
<#elseif (fld.PRIMARYKEYS=="YES")&&(fld.IS_AUTOINCREMENT=="NO")>	 
	//${fld.REMARKS}
	@DeAnnotaion(isPrimary=true)//自增
	private ${fld.SOURCE_DATA_TYPE} ${fld.COLUMN_NAME};	
<#elseif (fld.PRIMARYKEYS=="NO")&&(fld.IS_AUTOINCREMENT=="YES")>	
	//${fld.REMARKS}
	@DeAnnotaion(is_autoincrement=true)//自增
	private ${fld.SOURCE_DATA_TYPE} ${fld.COLUMN_NAME};	
<#else>
	//${fld.REMARKS}
	private ${fld.SOURCE_DATA_TYPE} ${fld.COLUMN_NAME};
</#if>
</#list>

<#list fldList as fld>
<#if (fld.IS_NULLABLE=="NO")>
	@DeAnnotaion(isNull=false) 
	public ${fld.SOURCE_DATA_TYPE} get${fld.COLUMN_NAME?cap_first}(){
		return ${fld.COLUMN_NAME};
	}
	@DeAnnotaion(isNull=false) 
	public void set${fld.COLUMN_NAME?cap_first}(${fld.SOURCE_DATA_TYPE} ${fld.COLUMN_NAME}){
		this.${fld.COLUMN_NAME} = ${fld.COLUMN_NAME};
	}	
<#else>
	public ${fld.SOURCE_DATA_TYPE} get${fld.COLUMN_NAME?cap_first}(){
		return ${fld.COLUMN_NAME};
	}	
	public void set${fld.COLUMN_NAME?cap_first}(${fld.SOURCE_DATA_TYPE} ${fld.COLUMN_NAME}){
		this.${fld.COLUMN_NAME} = ${fld.COLUMN_NAME};
	}
</#if>
</#list>

}