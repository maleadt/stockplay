/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.bo.system;

import com.kapti.administration.bo.XmlRpcClientFactory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.event.EventListenerList;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

/**
 *
 * @author Thijs
 */
public class StatusObject implements ActionListener {

    private static Logger logger = Logger.getLogger(StatusObject.class);

    private Timer updateTimer = new Timer(2500, this);
    private EventListenerList listenerList = new EventListenerList();
    private String functionCall;

    public StatusObject(String name, String functionCall) {
        this.functionCall = functionCall;
        this.name = name;

        fetchStatus();

    }

    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    public void removeActionListener(ActionListener listener) {
        listenerList.remove(ActionListener.class, listener);
    }

    private void fireActionEvent(ActionEvent evt) {
        Object[] listeners = listenerList.getListenerList();

        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == ActionListener.class) {
                ((ActionListener) listeners[i + 1]).actionPerformed(evt);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        //Timer loopt, we verversen onze status

        fetchStatus();
    }

    private void fetchStatus(){

        XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();

        Status newStatus = Status.UNKNOWN;
        try {
            Integer result = (Integer)client.execute(functionCall, new Object[]{});

            newStatus = Status.getStatusFromId(result);

        } catch (XmlRpcException ex) {
            logger.error("Error while fetching status with function " + functionCall, ex);
        }


        if(newStatus != status){
            status = newStatus;
            fireActionEvent(new ActionEvent(this,0,"STATUS_CHANGED"));
        }
    }

    public void fetchUpdates(boolean fetchUpdates){
        if(fetchUpdates)
            updateTimer.start();
        else
            updateTimer.stop();
    }

    protected String name;

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }
    protected Status status = Status.UNKNOWN;

    /**
     * Get the value of status
     *
     * @return the value of status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Set the value of status
     *
     * @param status new value of status
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    public StatusObject(String name) {
        this.name = name;
    }

    public enum Status {

        UNKNOWN(-1),
        STOPPED(0),
        STARTED(1),
        STARTING(2),
        STOPPING(3),
        ERROR(4);
        private int id;

        private Status(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Status getStatusFromId(int id) {
            for (Status s : Status.values()) {
                if (s.getId() == id) {
                    return s;
                }
            }
            return Status.UNKNOWN;
        }
    }
}
