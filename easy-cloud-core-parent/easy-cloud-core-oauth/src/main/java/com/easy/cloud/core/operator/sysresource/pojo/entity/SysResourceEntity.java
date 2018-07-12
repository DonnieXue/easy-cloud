package com.easy.cloud.core.operator.sysresource.pojo.entity;

import com.easy.cloud.core.jdbc.base.entity.EcBaseEntity;

/**
 * 描述：资源表持久化类
 * 
 * @author THINK
 * @date 2018-05-30 16:24:17
 */
public class SysResourceEntity extends EcBaseEntity {
	/** 资源编号 */
	private Integer resourceNo;
	/** 资源名称 */
	private String name;
	/** 资源类型 */
	private String type;
	/** 资源url */
	private String url;
	/** 直接父编号 */
	private Integer parentNo;
	/** 权限字符串 */
	private String permission;

	public SysResourceEntity() {
		super();
	}

	public SysResourceEntity(Integer resourceNo, String name, String type, String url, Integer parentNo, String permission) {
		super();
		this.resourceNo = resourceNo;
		this.name = name;
		this.type = type;
		this.url = url;
		this.parentNo = parentNo;
		this.permission = permission;
	}

	/** 获取资源编号 */
	public Integer getResourceNo() {
		return this.resourceNo;
	}

	/** 设置资源编号 */
	public void setResourceNo(Integer resourceNo) {
		this.resourceNo = resourceNo;
	}

	/** 构建资源编号 */
	public SysResourceEntity buildResourceNo(Integer resourceNo) {
		this.resourceNo = resourceNo;
		return this;
	}

	/** 获取资源名称 */
	public String getName() {
		return this.name;
	}

	/** 设置资源名称 */
	public void setName(String name) {
		this.name = name;
	}

	/** 构建资源名称 */
	public SysResourceEntity buildName(String name) {
		this.name = name;
		return this;
	}

	/** 获取资源类型 */
	public String getType() {
		return this.type;
	}

	/** 设置资源类型 */
	public void setType(String type) {
		this.type = type;
	}

	/** 构建资源类型 */
	public SysResourceEntity buildType(String type) {
		this.type = type;
		return this;
	}

	/** 获取资源url */
	public String getUrl() {
		return this.url;
	}

	/** 设置资源url */
	public void setUrl(String url) {
		this.url = url;
	}

	/** 构建资源url */
	public SysResourceEntity buildUrl(String url) {
		this.url = url;
		return this;
	}

	/** 获取直接父编号 */
	public Integer getParentNo() {
		return this.parentNo;
	}

	/** 设置直接父编号 */
	public void setParentNo(Integer parentNo) {
		this.parentNo = parentNo;
	}

	/** 构建直接父编号 */
	public SysResourceEntity buildParentNo(Integer parentNo) {
		this.parentNo = parentNo;
		return this;
	}

	/** 获取权限字符串 */
	public String getPermission() {
		return this.permission;
	}

	/** 设置权限字符串 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

	/** 构建权限字符串 */
	public SysResourceEntity buildPermission(String permission) {
		this.permission = permission;
		return this;
	}

}
