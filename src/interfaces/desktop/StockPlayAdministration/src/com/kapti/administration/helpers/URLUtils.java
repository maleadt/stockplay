/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.administration.helpers;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Thijs
 */
public class URLUtils {
  public static boolean exists(String URLName){
    try {
      HttpURLConnection.setFollowRedirects(false);
      // note : you may also need
      //        HttpURLConnection.setInstanceFollowRedirects(false)
      HttpURLConnection con =
         (HttpURLConnection) new URL(URLName).openConnection();
      con.setRequestMethod("HEAD");
      return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
    }
    catch (Exception e) {
       e.printStackTrace();
       return false;
    }
  }

}
