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
public interface ITransaction extends IInstruction {

    Date getTime();

}
