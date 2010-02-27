

package com.kapti.bo;

import java.util.Date;

/**
 *
 * @author Thijs
 */
public class Order extends Instruction{
    private Date creationTime;
    private Date expiryTimer;

    public Date getCreationTime() {
        return creationTime;
    }

    public Date getExpiryTimer() {
        return expiryTimer;
    }


}
