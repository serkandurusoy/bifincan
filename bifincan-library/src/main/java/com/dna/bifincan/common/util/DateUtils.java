/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.common.util;

import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hantsy
 */
public class DateUtils {

    private static final Logger log = LoggerFactory.getLogger(DateUtils.class);

    public static Period lastWeekSpan() {
       // if (log.isDebugEnabled()) {
      //      log.debug("last weekspan@");
       // }
        
        Calendar cal = Calendar.getInstance();
        Date currentTime = cal.getTime();

      //  log.debug(" currentTime@" + currentTime);

        cal.add(Calendar.DAY_OF_MONTH, -1);
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        Date _sun = cal.getTime();
      //  log.debug("last sunday@" + _sun);

        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        Date _mon = cal.getTime();
      //  log.debug(" last monday@" + _mon);

        return new Period(_mon, _sun);
    }
}
