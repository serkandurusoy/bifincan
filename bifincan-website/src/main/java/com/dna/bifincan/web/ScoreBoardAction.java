/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.web;

import com.dna.bifincan.service.PointTransactionAccountingService;
import com.dna.bifincan.util.spring.ScopeType;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

@Named("scoreBoardAction")
@Scope(ScopeType.VIEW)
public class ScoreBoardAction implements Serializable {
	private static final long serialVersionUID = -1857460052160357067L;

	private static final Logger log = LoggerFactory.getLogger(ScoreBoardAction.class);

    @Inject
    PointTransactionAccountingService pointTransactionAccountingService;

    List scores;

    public List getScores() {
        return scores;
    }

    public void init() {
        if (log.isDebugEnabled()) {
            log.debug("init ....@");
        }

        this.scores = pointTransactionAccountingService.findLatestPointsStatistics();
        
        if (log.isDebugEnabled()) {
            log.debug("init ....scores@" + scores);
        }

    }
}
