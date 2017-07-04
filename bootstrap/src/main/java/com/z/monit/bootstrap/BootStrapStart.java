package com.z.monit.bootstrap;

import java.lang.instrument.Instrumentation;

import com.z.monit.bootstrap.core.Agent;
import com.z.monit.bootstrap.core.agent.AgentParam;
import com.z.monit.bootstrap.core.agent.DefaultAgentParam;
import com.z.monit.bootstrap.core.config.MonitConfigRegister;

public class BootStrapStart {

	private static final String DEFAULT_AGENT = "com.z.monit.agent.DefaultAgent";

	private Instrumentation instrumentation;
	
	/**
	 * TEST<br/>
	 * TODO
	 */
	public static Agent agent;

	public BootStrapStart(Instrumentation instrumentation) {
		this.instrumentation = instrumentation;
	}

	public void startInit() {
		/**
		 * 创建AgentClassLoader用于加载Agent子类,并通过它实例化
		 */
		AgentClassLoader agentClassLoader = new AgentClassLoader(MonitConfigRegister.monitConfig.getDefaultAgentUrls());
		String bootClaz = DEFAULT_AGENT;
		agentClassLoader.setBootClass(bootClaz);

		/**
		 * 实例化Agent子类
		 */
		AgentParam agentParam = new DefaultAgentParam(instrumentation);
		agent = agentClassLoader.boot(agentParam);

		/**
		 * 启动agent
		 */
		agent.start();
	}

}
