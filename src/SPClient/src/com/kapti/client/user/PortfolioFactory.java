package com.kapti.client.user;

import com.kapti.client.XmlRpcClientFactory;
import com.kapti.exceptions.RequestError;
import com.kapti.exceptions.StockPlayException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

/**
 *
 * @author Dieter
 */
public class PortfolioFactory {

    private static PortfolioFactory instance = new PortfolioFactory();

    public static PortfolioFactory getInstance() {
        return instance;
    }

    private PortfolioFactory() {};

    public Collection<UserSecurity> getPortfolioByUser(User user) throws StockPlayException {

        ArrayList<UserSecurity> result = new ArrayList<UserSecurity>();
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Object[] userSecurities = (Object[]) client.execute("User.Portfolio.List", new Object[]{"USERID == " + user.id});

            for (Object obj : userSecurities) {
                HashMap geg = (HashMap) obj;
                geg.put("USEROBJECT", user);
                result.add(UserSecurity.fromStruct(geg));
            }
            return result;

        } catch (XmlRpcException ex) {
            throw new RequestError(ex);
        }
    }
}
