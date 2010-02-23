/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.bo;

import com.kapti.bo.interfaces.IOrder;
import java.util.Date;

/**
 *
 * @author Thijs
 */
public class Order extends Instruction implements IOrder{
    private Date creationTime;
    private Date expiryTimer;

    public Date getCreationTime() {
        return creationTime;
    }

    public Date getExpiryTimer() {
        return expiryTimer;
    }


}
