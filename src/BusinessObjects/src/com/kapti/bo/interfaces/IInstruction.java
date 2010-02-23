/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.bo.interfaces;

import com.kapti.bo.InstructionType;

/**
 *
 * @author Thijs
 */
public interface IInstruction {

    int getAmount();

    int getId();

    double getPrice();

    InstructionType getType();

}
