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

import java.util.concurrent.Callable;

/**
 * 
 * @author zhaozhenzuo
 *
 * @param <V>
 */
public class ContextClassLoaderExecuteTemplate<V> {
	private final ClassLoader classLoader;

	public ContextClassLoaderExecuteTemplate(ClassLoader classLoader) {
		if (classLoader == null) {
			throw new NullPointerException("classLoader must not be null");
		}
		this.classLoader = classLoader;
	}

	public V execute(Callable<V> callable) throws RuntimeException {
		try {
			final Thread currentThread = Thread.currentThread();
			final ClassLoader before = currentThread.getContextClassLoader();
			currentThread.setContextClassLoader(ContextClassLoaderExecuteTemplate.this.classLoader);
			try {
				return callable.call();
			} finally {
				currentThread.setContextClassLoader(before);
			}
		} catch (BootStrapException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new BootStrapException("execute fail. Caused:" + ex.getMessage(), ex);
		}
	}
}
