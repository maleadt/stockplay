/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.helpers;

import java.lang.*;
import java.util.*;
import java.io.*;
import be.belgium.eid.*;
import org.jdesktop.swingx.auth.LoginService;

/**
 *
 * @author Thijs
 */
public class eIDService {

    //-----------------------------------------------------------------
    // verify the pin for the card in the reader
    //-----------------------------------------------------------------
//    static void verifyPin() {
//        try {
//            BEID_ulwrapper ulRemaining = new BEID_ulwrapper(-1);
//
//            BEID_ReaderContext reader = BEID_ReaderSet.instance().getReader();
//            BEID_EIDCard card = reader.getEIDCard();
//
//            if (card.getPins().getPinByNumber(0).verifyPin("", ulRemaining)) {
//                System.out.println("verify pin succeeded");
//            } else {
//                if (ulRemaining.m_long == -1) {
//                    System.out.println("verify pin canceled");
//                } else {
//                    System.out.println("verify pin failed (" + ulRemaining.m_long + " tries left)");
//                }
//            }
//        } catch (BEID_ExCardBadType ex) {
//            System.out.println("[Exception] This is not an eid card");
//        } catch (BEID_ExNoCardPresent ex) {
//            System.out.println("[Exception] No card present");
//        } catch (BEID_ExNoReader ex) {
//            System.out.println("[Exception] No reader found");
//        } catch (BEID_Exception ex) {
//            System.out.println("BEID_Exception exception");
//        } catch (Exception ex) {
//            System.out.println("[Exception] Other exception");
//        }
//    }
    //-----------------------------------------------------------------
    // Main entry point
    //-----------------------------------------------------------------
//	public static void main(String[] args)
//	{
//		String osName = System.getProperty("os.name");
//
//		if (-1 != osName.indexOf("Windows"))
//		{
//			System.out.println("[Info]  Windows system!!");
//			System.loadLibrary("beid35libJava_Wrapper");
//		}
//		else
//		{
//			System.loadLibrary("beidlibJava_Wrapper");
//		}
//
//		System.out.println("eID SDK sample program: pin_eid");
//
//        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//
//		try
//		{
//			boolean bStop = false;
//
//			while(!bStop)
//			{
//				System.out.println("   Hit v<CR> to verify the pin");
//				System.out.println("       c<CR> to change the pin");
//				System.out.println("       q<CR> to quit");
//
//				String input = "";
//				input = in.readLine();
//
//				if (input.equalsIgnoreCase("v"))
//				{
//					verifyPin();
//				}
//				else if (input.equalsIgnoreCase("c"))
//				{
//					changePin();
//				}
//				else if (input.equalsIgnoreCase("q"))
//				{
//					bStop=true;
//				}
//			}
//
//			BEID_ReaderSet.releaseSDK();
//		}
//		catch (IOException ex)
//		{
//			System.out.println("[Exception] IOException");
//		}
//		catch (Exception ex)
//		{
//			System.out.println("[Exception] Exception");
//		}
//	}

    private long RRN = -1;

    public long getRijksRegisterNummer() {
        return RRN;
    }


    public boolean authenticated = false;


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
                RRN = Long.parseLong(card.getID().getNationalNumber());
                authenticated= true;
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
}
