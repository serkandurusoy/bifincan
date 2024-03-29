/*
 * Copyright 2004-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.faces.support;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link PhaseListener} that logs the execution of the individual phases of the
 * JSF lifecycle. Useful during JSF application development in order to detect
 * unreported JSF errors that cause the lifecycle to short-circuit. Turn logging
 * level to DEBUG to see its output.
 *
 * @author Jeremy Grelle
 */
public class RequestLoggingPhaseListener implements PhaseListener {

    private Logger logger = LoggerFactory.getLogger(RequestLoggingPhaseListener.class);

    @Override
    public void afterPhase(PhaseEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Leaving JSF Phase: " + event.getPhaseId());
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("Entering JSF Phase: " + event.getPhaseId());
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
}
