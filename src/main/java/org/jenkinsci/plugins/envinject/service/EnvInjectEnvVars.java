package org.jenkinsci.plugins.envinject.service;

import hudson.EnvVars;
import hudson.Util;
import org.jenkinsci.plugins.envinject.EnvInjectLogger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gregory Boissinot
 */
public class EnvInjectEnvVars implements Serializable {

    EnvInjectLogger logger;

    public EnvInjectEnvVars(EnvInjectLogger logger) {
        this.logger = logger;
    }

    public void resolveVars(Map<String, String> variables, Map<String, String> env) {
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            entry.setValue(Util.replaceMacro(entry.getValue(), env));
        }
        EnvVars.resolve(variables);
    }

    public Map<String, String> removeUnsetVars(Map<String, String> envVars) {
        Map<String, String> result = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : envVars.entrySet()) {
            if (!isUnresolvedVar(entry.getValue())) {
                result.put(entry.getKey(), entry.getValue());
            } else {
                logger.info(String.format("Unset unresolved '%s' variable.", entry.getKey()));
            }
        }
        return result;
    }

    private boolean isUnresolvedVar(String value) {
        if (value == null) {
            return false;
        }
        return value.contains("$");
    }

}
