package com.kapti.bo;

/**
 *
 * @author Thijs
 */
public class Exchange  {
    private String symbol = "";
    private String name ="";
    private String location = "";

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Exchange(){}

    public Exchange(String symbol) {
        this.symbol = symbol;
    }

    public Exchange(String symbol, String name){
        this.symbol = symbol;
        this.name = name;
    }

    public Exchange(String symbol, String name, String location){
        this.symbol = symbol;
        this.name = name;
        this.location = location;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Exchange other = (Exchange) obj;
        if ((this.symbol == null) ? (other.symbol != null) : !this.symbol.equals(other.symbol)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.symbol != null ? this.symbol.hashCode() : 0);
        return hash;
    }

    
    
}
