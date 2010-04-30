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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.rocrail.androc.interfaces.SystemListener;

import org.xml.sax.SAXException;

public class Connection extends Thread {
  RocrailService  rocrailService = null;
  Model   m_Model  = null;
  boolean m_bRun   = true;
  boolean m_bRead  = true;
  
  public Connection( RocrailService rocrailService, Model model, Socket socket ) {
    this.rocrailService = rocrailService;
    m_Model  = model;
  }
  
  public void stopReading() {
    m_bRead = false;
  }
  
  
  public void startReading() {
    m_bRead = true;
  }
  
  public void stopRunning() {
    m_bRun = false;
  }
  
  public void run() {
    SAXParser saxparser = null;
    try {
      saxparser = SAXParserFactory.newInstance().newSAXParser();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    
    XmlHandler xmlhandler = new XmlHandler(rocrailService, m_Model);
    String hdr = "";
    boolean readHdr = true;
    int xmlSize = 0;
    byte[] buffer = null;
    int read = 0;

    
    while(saxparser != null && m_bRun) {
      try {
        if( m_bRead && rocrailService.m_Socket != null && rocrailService.m_Socket.isConnected() && !rocrailService.m_Socket.isClosed() ) {
          InputStream is = rocrailService.m_Socket.getInputStream();
          
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
                  hdr = hdr.trim();
                  // parse the header
                  saxparser.parse(new ByteArrayInputStream(hdr.getBytes("UTF-8")), xmlhandler);
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
              int actualRead = is.read(buffer, read, avail);
              if( actualRead != -1 )
                read = read + actualRead;
              
              // all bytes are read
              if( read == xmlSize ) {
                // create the xml string from the byte with utf-8 encoding
                String xml = new String(buffer, "UTF-8").trim();
                // parse the xml
                if( xml != null && xml.length() > 0 ) {
                  ByteArrayInputStream bai = new ByteArrayInputStream(xml.getBytes("UTF-8"));
                  saxparser.parse(bai, xmlhandler);
                }
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
          else {
            Thread.sleep(10);            
          }
        }
        else {
          Thread.sleep(10);
        }
        
      } catch (SocketException soce) {
        // TODO: Inform the system
        soce.printStackTrace();
        rocrailService.informListeners(SystemListener.EVENT_DISCONNECTED);
      } catch (SAXException saxe) {
        // TODO Auto-generated catch block
        saxe.printStackTrace();
        read = 0;
        xmlSize = 0;
        readHdr = true;
      } catch (IOException ioe) {
        // TODO Auto-generated catch block
        ioe.printStackTrace();
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  

}
