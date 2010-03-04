
package com.kapti.data;

/**
 *
 * @author Thijs
 */
public class Index {

    private int id;
    private String name;
    private String exchange;

    public Index(int id, String name, String exchange) {
        this.id = id;
        this.name = name;
        this.exchange = exchange;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }


    

}
