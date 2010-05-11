/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.helpers;

import be.belgium.eid.*;

/**
 *
 * @author Thijs
 */
public class eIDService {

  
    private long RRN = -1;

    public long getRijksRegisterNummer() {
        return RRN;
    }
    public boolean authenticated = false;
    private BEID_ReaderContext reader = null;
    private BEID_EIDCard card = null;

    public eIDService() throws Exception {
        if (-1 != System.getProperty("os.name").indexOf("Windows")) {
            //Windows systeem --> andere libnaam
            System.loadLibrary("beid35libJava_Wrapper");
        } else {
            System.loadLibrary("beidlibJava_Wrapper");
        }
        BEID_ReaderSet.initSDK();

        reader = BEID_ReaderSet.instance().getReader();

        if (reader == null) {
            throw new Exception("No cardreader found!");
        }
    }

    @Override
    protected void finalize() throws Throwable {

        BEID_ReaderSet.releaseSDK();

        super.finalize();

    }

    public boolean authenticate() throws Exception {


        try {

            if (-1 != System.getProperty("os.name").indexOf("Windows")) {
                //Windows systeem --> andere libnaam
                System.loadLibrary("beid35libJava_Wrapper");
            } else {
                System.loadLibrary("beidlibJava_Wrapper");
            }

            BEID_ulwrapper ulRemaining = new BEID_ulwrapper(-1);

            BEID_ReaderContext reader = BEID_ReaderSet.instance().getReader();
            BEID_EIDCard card = reader.getEIDCard();



            if (card.getPins().getPinByNumber(0).verifyPin("", ulRemaining)) {
                String nn = card.getID().getNationalNumber();

                StringBuilder nummer = new StringBuilder();

                for (char c : card.getID().getNationalNumber().toCharArray()) {
                    if (Character.isDigit(c)) {
                        nummer.append(c);
                    }
                }

                Long.parseLong(nummer.toString());
                authenticated = true;
                return true;
            } else {

                return false;
//                if (ulRemaining.m_long == -1) {
//                    System.out.println("s");
//                } else {
//                    throw new Exception("verify pin failed (" + ulRemaining.m_long + " tries left)");
//                }
            }


        } catch (BEID_ExCardBadType ex) {
            throw new Exception("[Exception] This is not an eid card", ex);
        } catch (BEID_ExNoCardPresent ex) {
            throw new Exception("[Exception] No card present", ex);
        } catch (BEID_ExNoReader ex) {
            throw new Exception("[Exception] No reader found", ex);
        } catch (BEID_Exception ex) {
            throw new Exception("BEID_Exception exception", ex);
        } catch (Exception ex) {
            throw new Exception("[Exception] Other exception", ex);
        }


    }

    public void findCard() throws Exception {

        while(!reader.isCardPresent()){
            Thread.sleep(1000);
        }
        card = reader.getEIDCard();
    }

    public void readCard() throws Exception {

        BEID_EId eid = card.getID();

        StringBuilder nummer = new StringBuilder();
        for (char c : card.getID().getNationalNumber().toCharArray()) {
            if (Character.isDigit(c)) {
                nummer.append(c);
            }
        }

        RRN = Long.parseLong(nummer.toString());



    }

    public int getTriesLeft() throws Exception {
        return card.getPins().getPinByNumber(0).getTriesLeft();
    }
    private boolean userCancelled = false;

    public boolean isUserCancelled() {
        return userCancelled;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public boolean verifyPIN() throws Exception {
        BEID_ulwrapper ulRemaining = new BEID_ulwrapper(-1);
        if (card.getPins().getPinByNumber(0).verifyPin("", ulRemaining)) {
            authenticated = true;
            return true;
        } else if (ulRemaining.m_long == -1) {
            userCancelled = true;
        }
        return false;

    }
}
