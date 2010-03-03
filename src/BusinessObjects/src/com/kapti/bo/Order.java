

package com.kapti.bo;

import java.util.Date;

/**
 *
 * @author Thijs
 */
public class Order extends Instruction{

    private OrderStatus status;
    private Date creationTime;
    private Date expirationTime;
    private Date executionTime;


    public Order(int id){
        super(id);
    }

    public Order(int id, int user, String security, int amount, double price, InstructionType type, OrderStatus status, Date creationTime, Date expirationTime, Date executionTime) {
        super(id, user, security, amount, price, type);
        this.status =status;
        this.creationTime = creationTime;
        this.expirationTime = expirationTime;
        this.executionTime = executionTime;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }


    

    public Date getCreationTime() {
        return creationTime;
    }

    public Date getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }




}
