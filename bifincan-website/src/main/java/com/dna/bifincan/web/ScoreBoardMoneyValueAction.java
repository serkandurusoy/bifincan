/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.web;

import com.dna.bifincan.service.OrderService;
import com.dna.bifincan.util.spring.ScopeType;
import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;

@Named("scoreBoardMVAction")
@Scope(ScopeType.VIEW)
public class ScoreBoardMoneyValueAction implements Serializable {
	private static final long serialVersionUID = 4912630392753068677L;

	private static final Logger log = LoggerFactory.getLogger(ScoreBoardMoneyValueAction.class);

    @Inject
    OrderService  orderService;

    List scores;

    public List getScores() {
        return scores;
    }

    public void init() {
        if (log.isDebugEnabled()) {
            log.debug("init ....@");
        }

        this.scores = orderService.findLatestMoneyValuesStatistics();
        
        if (log.isDebugEnabled()) {
            log.debug("init ....scores@" + scores);
        }

    }
}
