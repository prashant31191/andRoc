/*
 Rocrail - Model Railroad Software

 Copyright (C) 2002-2010 - Rob Versluis <r.j.versluis@rocrail.net>

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package net.rocrail.androc;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.content.SharedPreferences;

public class System extends Thread implements Runnable {
  public String m_Host   = "rocrail.dyndns.org";
  public int    m_iPort  = 8080;

  Activity      m_andRoc = null;
  Socket        m_Socket = null;
  boolean       m_bRun   = true;
  
  SAXParser m_Parser = null;

  public static final String PREFS_NAME = "andRoc.ini";

  public System(Activity androc) {
    m_andRoc = androc;
    // Restore preferences
    SharedPreferences settings = androc.getSharedPreferences(PREFS_NAME, 0);
    m_Host  = settings.getString("host", "rocrail.dyndns.org");
    m_iPort = settings.getInt("port", 8080);

    //m_Parser = SAXParser;
    m_bRun = true;
    start();
  }
  
  public void connect() throws Exception {
    m_Socket = new Socket(m_Host, m_iPort);
  }
  
  public void exit() {
    SharedPreferences settings = m_andRoc.getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString("host", m_Host);
    editor.putInt("port", m_iPort);
    editor.commit();
  }

  public void run() {
    SAXParser saxparser = null;
    try {
      saxparser = SAXParserFactory.newInstance().newSAXParser();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    
    xmlHandler xmlhandler = new xmlHandler();
    while(saxparser != null && m_bRun) {
      try {
        if( m_Socket != null && m_Socket.isConnected() && !m_Socket.isClosed() ) {
          InputStream is = m_Socket.getInputStream();
          if( is.available() > 0 ) {
            saxparser.parse(is, xmlhandler);
          }
        }
        
        Thread.sleep(10);
      } catch (SocketException soce) {
        // TODO Auto-generated catch block
        soce.printStackTrace();
        m_bRun = false;
      } catch (SAXException saxe) {
        // TODO Auto-generated catch block
        saxe.printStackTrace();
      } catch (IOException ioe) {
        // TODO Auto-generated catch block
        ioe.printStackTrace();
      } catch (InterruptedException inte) {
        // TODO Auto-generated catch block
        inte.printStackTrace();
      }
    }
  }


}


class xmlHandler extends DefaultHandler {
  public void startDocument () {
  
  }
  
  public void endDocument () {
    
  }

  public void startElement (String uri, String localName, String qName, Attributes atts) {
    if( localName.equals("xmlh") ) {
      // TODO: xmlh handling
    }
    else if( localName.equals("xml") ) {
      // TODO: xml handling
    }
  }

  public void endElement (String uri, String localName, String qName) {
    
  }
}
