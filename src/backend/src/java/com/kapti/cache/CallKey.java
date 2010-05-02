/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.cache;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 *
 * @author tim
 */


public class CallKey implements Serializable {

    public final Method method;
    public final Object[] args;

    public CallKey(Method method, Object[] args) {
        this.method = method;
        this.args = args;
    }

    @Override
    public int hashCode() {
        // Hash method name
        int code = method.getName().hashCode();

        // Hash arguments
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                code = (31 * code) + args[i].hashCode();
            }
        }

        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof CallKey)) {
            return false;
        }

        final CallKey callKey = (CallKey) o;

        if (!method.equals(callKey.method)) {
            return false;
        }

        if (!Arrays.equals(args, callKey.args)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return Integer.toString(hashCode());
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(method.getName());
        //if (args != null)
        //    stream.writeObject(args);
    }
}
