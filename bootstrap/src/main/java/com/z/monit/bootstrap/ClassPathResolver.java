/*
 * Copyright 2014 NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.z.monit.bootstrap;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.z.monit.bootstrap.core.config.MonitConfigRegister;

/**
 * bootstrap jar,bootstrap-core jar路径解析
 * 
 * @author zhaozhenzuo
 *
 */
public class ClassPathResolver {

	private final Logger logger = LoggerFactory.getLogger(ClassPathResolver.class);

	private static final Pattern DEFAULT_BOOTSTRAP_PATTERN = Pattern
			.compile("bootstrap(-[0-9]+\\.[0-9]+\\.[0-9]+(\\-SNAPSHOT)?)?\\.jar");

	private static final Pattern DEFAULT_BOOTSTRAP_CORE_PATTERN = Pattern
			.compile("bootstrap-core(-[0-9]+\\.[0-9]+\\.[0-9]+(\\-SNAPSHOT)?)?\\.jar");

	private static final Pattern PLUGIN_PATTERN = Pattern
			.compile("[a-z]*-plugin(-[0-9]+\\.[0-9]+\\.[0-9]+(\\-SNAPSHOT)?)?\\.jar");

	/**
	 * java.class.path,类目录,java agent启动时agent jar也会在这个目录里
	 */
	private String classPath;

	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public ClassPathResolver() {
		this(getClassPathFromSystemProperty());
	}

	public ClassPathResolver(String classPath) {
		this.classPath = classPath;
	}

	public List<String> getDefaultFileExtensionList() {
		List<String> extensionList = new ArrayList<String>();
		extensionList.add("jar");
		extensionList.add("xml");
		extensionList.add("properties");
		return extensionList;
	}

	public void setClassPathFromSystemProperty() {
		this.classPath = getClassPathFromSystemProperty();
	}

	public static String getClassPathFromSystemProperty() {
		return System.getProperty("java.class.path");
	}

	public boolean initBootStrapJar() {
		Matcher matcher = DEFAULT_BOOTSTRAP_PATTERN.matcher(classPath);
		if (!matcher.find()) {
			return false;
		}
		String bootstrapJarName = parseBootstrapName(matcher);
		String bootstrapFullPath = parseBootstrapPath(classPath, bootstrapJarName);
		if (bootstrapFullPath == null) {
			return false;
		}
		String bootstrapDirPath = parseBootstrapDirPath(bootstrapFullPath);

		/**
		 * 将bootstrap相关url全部放入MonitConfigRegistry
		 */
		MonitConfigRegister.monitConfig.setBootstrapDir(bootstrapDirPath);
		MonitConfigRegister.monitConfig.setBootstrapFullUrl(bootstrapFullPath);
		return true;
	}

	public boolean initBootStrapCore() {
		String bootstrapCoreFullUrl = parseBootstrapCoreFullUrl();
		MonitConfigRegister.monitConfig.setBootstrapCoreFullUrl(bootstrapCoreFullUrl);
		return true;
	}

	public boolean initPlugin() {
		URL[] plugins = this.parsePluginUrls();
		MonitConfigRegister.monitConfig.setPluginUrls(plugins);
		if (plugins == null || plugins.length <= 0) {
			return false;
		}
		return true;
	}

	public boolean initDefaultAgentLibs() {
		URL[] defaultAgentUrls = this.parseDefaultAgentUrls();
		MonitConfigRegister.monitConfig.setDefaultAgentUrls(defaultAgentUrls);
		if (defaultAgentUrls == null || defaultAgentUrls.length <= 0) {
			return false;
		}
		return true;
	}

	private String parseBootstrapCoreFullUrl() {
		if (MonitConfigRegister.monitConfig.getBootstrapDir() == null) {
			logger.error(">>boostrap dir not parse");
			return null;
		}

		String bootstrapCoreDir = MonitConfigRegister.monitConfig.getBootstrapDir() + "/" + "core";
		File coreDirFile = new File(bootstrapCoreDir);
		if (!coreDirFile.isDirectory()) {
			logger.error(
					">>boostrap dir is not dir,bootstraopDir:" + MonitConfigRegister.monitConfig.getBootstrapDir());
			return null;
		}

		MonitConfigRegister.monitConfig.setBootstrapCoreDir(bootstrapCoreDir);

		File[] files = coreDirFile.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				Matcher matcher = DEFAULT_BOOTSTRAP_CORE_PATTERN.matcher(name);
				if (matcher.matches()) {
					return true;
				}
				return false;
			}
		});

		if (files == null) {
			logger.error(">>not found bootstrap core jar");
		} else if (files.length != 1) {
			logger.error(">>too many bootstrap core jars found");
		}
		return files[0].getAbsolutePath();
	}

	private URL[] parsePluginUrls() {
		if (MonitConfigRegister.monitConfig.getBootstrapDir() == null) {
			logger.error(">>boostrap dir not parse");
			return null;
		}

		String pluginDir = MonitConfigRegister.monitConfig.getBootstrapDir() + "/" + "plugin";
		File pluginDirFile = new File(pluginDir);
		if (!pluginDirFile.isDirectory()) {
			logger.error(">>plugin dir is not dir,pluginDir:" + pluginDir);
			return null;
		}

		MonitConfigRegister.monitConfig.setPluginDir(pluginDir);

		File[] files = pluginDirFile.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				Matcher matcher = PLUGIN_PATTERN.matcher(name);
				if (matcher.matches()) {
					return true;
				}
				return false;
			}
		});

		URL[] urls = new URL[files.length];
		for (int i = 0; i < files.length; i++) {
			urls[i] = toURI(files[i]);
		}
		return urls;
	}

	private URL[] parseDefaultAgentUrls() {
		if (MonitConfigRegister.monitConfig.getBootstrapDir() == null) {
			logger.error(">>boostrap dir not parse");
			return null;
		}

		String defaultAgentDir = MonitConfigRegister.monitConfig.getBootstrapDir() + "/" + "libs";
		File defaultAgentDirFile = new File(defaultAgentDir);
		if (!defaultAgentDirFile.isDirectory()) {
			logger.error(">>defaultAgentDir is not dir,defaultAgentDir:" + defaultAgentDir);
			return null;
		}

		MonitConfigRegister.monitConfig.setDefaultAgentDir(defaultAgentDir);

		File[] files = defaultAgentDirFile.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return true;
			}
		});

		URL[] urls = new URL[files.length];
		for (int i = 0; i < files.length; i++) {
			urls[i] = toURI(files[i]);
		}
		return urls;
	}

	private URL toURI(File file) {
		URI uri = file.toURI();
		try {
			return uri.toURL();
		} catch (MalformedURLException e) {
			logger.error(".toURL() failed. Error:" + e.getMessage(), e);
			return null;
		}
	}

	private String parseBootstrapName(Matcher matcher) {
		int start = matcher.start();
		int end = matcher.end();
		return this.classPath.substring(start, end);
	}

	private String parseBootstrapPath(String classPath, String bootstrapJar) {
		String[] classPathList = classPath.split(File.pathSeparator);
		for (String findPath : classPathList) {
			boolean find = findPath.contains(bootstrapJar);
			if (find) {
				return findPath;
			}
		}
		return null;
	}

	public String parseBootstrapDirPath(String bootstrapJarFullPath) {
		int index1 = bootstrapJarFullPath.lastIndexOf("/");
		int index2 = bootstrapJarFullPath.lastIndexOf("\\");
		int max = Math.max(index1, index2);
		if (max == -1) {
			return null;
		}
		return bootstrapJarFullPath.substring(0, max);
	}

}
