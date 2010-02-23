/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.bo;

import com.kapti.bo.Instruction;
import com.kapti.bo.interfaces.ITransaction;
import java.util.Date;

/**
 *
 * @author Thijs
 */
public class Transaction extends Instruction implements ITransaction {

    private Date time;

    public Date getTime() {
        return time;
    }
    
}
