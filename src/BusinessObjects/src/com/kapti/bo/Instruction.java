/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.bo;

import com.kapti.bo.interfaces.IInstruction;

/**
 *
 * @author Thijs
 */
public class Instruction implements IInstruction {
    private int id=-1;
    private int amount;
    private double price;
    private InstructionType type;

    public int getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public InstructionType getType() {
        return type;
    }

    
}
