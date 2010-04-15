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
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.net.Socket;
import java.net.SocketException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;

public class System extends Thread implements Runnable {
  public String m_Host   = "rocrail.dyndns.org";
  public int    m_iPort  = 8080;

  Activity      m_andRoc    = null;
  Socket        m_Socket    = null;
  boolean       m_bRun      = true;
  
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
    m_bRun = false;
    SharedPreferences settings = m_andRoc.getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString("host", m_Host);
    editor.putInt("port", m_iPort);
    editor.commit();
    try {
      Thread.sleep(500);
      if( m_Socket != null && m_Socket.isConnected() && !m_Socket.isClosed() ) {
        m_Socket.close();
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public void sendMessage(String name, String msg) {
    if( m_Socket != null && m_Socket.isConnected() && !m_Socket.isClosed() ) {
      try {
        int msgLen = msg.getBytes("UTF-8").length;
        String stringToSend = String.format("<xmlh><xml size=\"%d\" name=\"%s\"/></xmlh>%s", msgLen, name, msg);
        byte[] msgToSend = stringToSend.getBytes("UTF-8");
        msgLen = msgToSend.length;
        m_Socket.getOutputStream().write(msgToSend);
      }
      catch( Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void run() {
    SAXParser saxparser = null;
    try {
      saxparser = SAXParserFactory.newInstance().newSAXParser();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    
    XmlHandler xmlhandler = new XmlHandler();
    String hdr = "";
    boolean readHdr = true;
    int xmlSize = 0;
    byte[] buffer = null;
    int read = 0;

    
    while(saxparser != null && m_bRun) {
      try {
        if( m_Socket != null && m_Socket.isConnected() && !m_Socket.isClosed() ) {
          InputStream is = m_Socket.getInputStream();
          
          if( is.available() > 0 ) {
            
            // read header byte by byte
            if( readHdr ) {
              if( !hdr.endsWith("</xmlh>") ) {
                // read next byte
                hdr = hdr + String.valueOf((char) is.read());
              }

              // check if the header end is read
              if( hdr.endsWith("</xmlh>") ) {
                // find the start of the header
                if( hdr.indexOf("<?xml") != -1 ) {
                  // disregard al leading bytes
                  hdr = hdr.substring(hdr.indexOf("<?xml"));
                  // parse the header
                  saxparser.parse(new StringBufferInputStream(hdr), xmlhandler);
                  xmlSize = xmlhandler.getXmlSize();
                  // reset header string and signal reading data
                  hdr = "";
                  readHdr = false;
                  // initialize for reading data
                  buffer = new byte[xmlSize+1];
                  read = 0;
                }
                else {
                  hdr = "";
                  xmlSize = 0;
                  readHdr = true;
                }
              }
            }
            
            // read the xml data string at the given length
            else if( xmlSize > 0 ) {
              int avail = is.available();
              if( read + avail > xmlSize ) {
                // do not read more than wanted
                avail = xmlSize - read;
              }
              // read the available bytes
              is.read(buffer, read, avail);
              read = read + avail;
              
              // all bytes are read
              if( read == xmlSize ) {
                // create the xml string from the byte with utf-8 encoding
                String xml = new String(buffer, "UTF-8").trim();
                // parse the xml
                saxparser.parse(new StringBufferInputStream(xml), xmlhandler);
                // reset for next header
                read = 0;
                xmlSize = 0;
                readHdr = true;
              }
            }
            
            // no valid header or zero xmlsize
            else {
              hdr = "";
              xmlSize = 0;
              readHdr = true;
            }
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
        read = 0;
        xmlSize = 0;
        readHdr = true;
      } catch (IOException ioe) {
        // TODO Auto-generated catch block
        ioe.printStackTrace();
      } catch (InterruptedException inte) {
        // TODO Auto-generated catch block
        inte.printStackTrace();
        m_bRun = false;
      }
    }
  }

  
  public void initView() {
    final Button powerON = (Button) m_andRoc.findViewById(R.id.systemPowerON);
    powerON.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          System.this.sendMessage("sys", "<sys cmd=\"go\"/>");
        }
    });

    final Button powerOFF = (Button) m_andRoc.findViewById(R.id.systemPowerOFF);
    powerOFF.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          System.this.sendMessage("sys", "<sys cmd=\"stop\"/>");
        }
    });
    
    final Button initField = (Button) m_andRoc.findViewById(R.id.systemInitField);
    initField.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          System.this.sendMessage("model", "<model cmd=\"initfield\"/>");
        }
    });
    
  }

}

