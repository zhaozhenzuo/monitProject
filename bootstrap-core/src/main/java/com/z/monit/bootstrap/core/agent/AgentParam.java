package com.z.monit.bootstrap.core.agent;

import java.lang.instrument.Instrumentation;

import com.z.monit.bootstrap.core.config.MonitConfig;

/**
 * 给agent的参数
 * 
 * @author zhaozhenzuo
 *
 */
public interface AgentParam {

	public Instrumentation getInstrument();

	public MonitConfig getMonitConfig();

}
