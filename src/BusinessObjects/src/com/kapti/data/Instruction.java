package com.kapti.data;

/**
 *
 * @author Thijs
 */
public class Instruction {

    private int id = -1;
    private int user;
    private String security;
    private int amount;
    private double price;
    private InstructionType type;

    public Instruction(int id) {
        this.id = id;
    }

    public Instruction(int id, int user, String security, int amount, double price, InstructionType type) {
        this.id = id;
        this.user = user;
        this.security = security;
        this.amount = amount;
        this.price = price;
        this.type = type;
    }

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

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setType(InstructionType type) {
        this.type = type;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }
}
