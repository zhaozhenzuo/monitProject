package com.z.monit.bootstrap.core.config;

import java.io.Serializable;
import java.net.URL;

/**
 * monit配置对象
 * 
 * @author zhaozhenzuo
 *
 */
public class MonitConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 启动boostrap jar目录
	 */
	private String bootstrapDir;

	/**
	 * bootstrap及bootstrap-core jar包所在目录,
	 */
	private String bootstrapCoreDir;

	/**
	 * default agent dir
	 */
	private String defaultAgentDir;

	/**
	 * plugin目录
	 */
	private String pluginDir;

	/**
	 * 启动jar url
	 */
	private String bootstrapFullUrl;

	/**
	 * monit核心jar包url
	 */
	private String bootstrapCoreFullUrl;

	/**
	 * monit agent实现jar包url
	 */
	private URL[] defaultAgentUrls;

	/**
	 * 插件url集合
	 */
	private URL[] pluginUrls;

	public String getBootstrapDir() {
		return bootstrapDir;
	}

	public void setBootstrapDir(String bootstrapDir) {
		this.bootstrapDir = bootstrapDir;
	}

	public String getBootstrapCoreDir() {
		return bootstrapCoreDir;
	}

	public void setBootstrapCoreDir(String bootstrapCoreDir) {
		this.bootstrapCoreDir = bootstrapCoreDir;
	}

	public String getDefaultAgentDir() {
		return defaultAgentDir;
	}

	public void setDefaultAgentDir(String defaultAgentDir) {
		this.defaultAgentDir = defaultAgentDir;
	}

	public String getPluginDir() {
		return pluginDir;
	}

	public void setPluginDir(String pluginDir) {
		this.pluginDir = pluginDir;
	}

	public String getBootstrapFullUrl() {
		return bootstrapFullUrl;
	}

	public void setBootstrapFullUrl(String bootstrapFullUrl) {
		this.bootstrapFullUrl = bootstrapFullUrl;
	}

	public String getBootstrapCoreFullUrl() {
		return bootstrapCoreFullUrl;
	}

	public void setBootstrapCoreFullUrl(String bootstrapCoreFullUrl) {
		this.bootstrapCoreFullUrl = bootstrapCoreFullUrl;
	}

	public URL[] getDefaultAgentUrls() {
		return defaultAgentUrls;
	}

	public void setDefaultAgentUrls(URL[] defaultAgentUrls) {
		this.defaultAgentUrls = defaultAgentUrls;
	}

	public URL[] getPluginUrls() {
		return pluginUrls;
	}

	public void setPluginUrls(URL[] pluginUrls) {
		this.pluginUrls = pluginUrls;
	}

}
