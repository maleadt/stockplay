/*
 * Copyright (c) 2010 StockPlay development team
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.kapti.data;

import java.io.Serializable;

/**
 * \brief   basisklasse voor instructie-gerelateerde data
 *
 * Deze klasse vormt de basis voor andere dataobjecten die een instructie vormen
 * die pas in een later stadium moeten verwerkt worden.
 */
public abstract class Instruction implements Serializable {
    //
    // Member data
    //
    
    private int id = -1;
    private int user;
    private String isin = "";
    private int amount;
    private double price;
    private double secondairyLimit;
    private InstructionType type;

    public Instruction(int user) {
        this.user = user;
    }


    //
    // Construction
    //



    public Instruction(int id, int user, String isin) {
        this(user, isin);
        this.id = id;
    }

    public Instruction (int user, String isin) {
        this.user = user;
        this.isin = isin;
    }

    //
    // Methods
    //

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

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    /**
     * @return the secondairyLimit
     */
    public double getSecondairyLimit() {
        return secondairyLimit;
    }

    /**
     * @param secondairyLimit the secondairyLimit to set
     */
    public void setSecondairyLimit(double secondairyLimit) {
        this.secondairyLimit = secondairyLimit;
    }
}