/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.bo.interfaces;

import java.util.Date;

/**
 *
 * @author Thijs
 */
public interface IOrder extends IInstruction {

    Date getCreationTime();

    Date getExpiryTimer();

}
