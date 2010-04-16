/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.backend.helpers;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 *
 * @author Thijs
 */
public class DateHelper {
    /**
     * Adapt calendar to client time zone.
     * @param calendar - adapting calendar
     * @param timeZone - client time zone
     * @return adapt calendar to client time zone
     */
    public static Calendar convertCalendar(final Calendar calendar, final TimeZone timeZone) {
        Calendar ret = new GregorianCalendar(timeZone);
        ret.setTimeInMillis(calendar.getTimeInMillis()
                + timeZone.getOffset(calendar.getTimeInMillis())
                - TimeZone.getDefault().getOffset(calendar.getTimeInMillis()));
        ret.getTime();
        return ret;
    }
}
