package com.z.monit.bootstrap.core.agent;

import java.lang.instrument.Instrumentation;

import com.z.monit.bootstrap.core.config.MonitConfig;
import com.z.monit.bootstrap.core.config.MonitConfigRegister;

public class DefaultAgentParam implements AgentParam {

	private Instrumentation instrumentation;

	public DefaultAgentParam(Instrumentation inst) {
		instrumentation = inst;
	}

	public Instrumentation getInstrument() {
		return instrumentation;
	}

	public MonitConfig getMonitConfig() {
		return MonitConfigRegister.monitConfig;
	}

}
