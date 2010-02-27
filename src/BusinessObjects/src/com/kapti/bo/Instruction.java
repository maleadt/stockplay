
package com.kapti.bo;

/**
 *
 * @author Thijs
 */
public class Instruction  {
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
