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

import java.net.Socket;
import javax.xml.parsers.SAXParser;

import org.xml.sax.Attributes;

import net.rocrail.androc.objects.Item;
import net.rocrail.androc.objects.Loco;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class RocrailService extends Service {
  public String m_Host     = "rocrail.dyndns.org";
  public int    m_iPort    = 8080;
  public Model  m_Model    = null;
  public String m_DevideId = "andRoc";
  
  public int    m_iSelectedLoco = 0;
  public Loco   SelectedLoco = null;
  
  public boolean Power     = false;
  public boolean AutoMode  = false;
  public boolean AutoStart = false;
  public boolean Connected = false;
  
  andRoc        m_andRoc     = null;
  Socket        m_Socket     = null;
  Connection    m_Connection = null;
  
  SAXParser m_Parser = null;

  @Override
  public void onCreate() {
    m_Model = new Model(this);
  }

  
  private final IBinder rocrailBinder = new RocrailLocalBinder();
  public class RocrailLocalBinder extends Binder {
    public RocrailService getService() {
        return RocrailService.this;
    }
    public Model getModel() {
      return m_Model;
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return rocrailBinder;
  }
  
  
  public RocrailService() {
  }
  
  
  public void connect() throws Exception {
    try {
      m_Connection.stopReading();
      Thread.sleep(500);
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
    
    if( m_Socket != null && m_Socket.isConnected() && !m_Socket.isClosed() ) {
      m_Socket.close();
    }
    m_Socket = new Socket(m_Host, m_iPort);
    sendMessage("model","<model cmd=\"plan\" disablemonitor=\"true\"/>");
    if( m_Connection == null ) {
      m_Connection = new Connection(this, m_Model, m_Socket);
      m_Connection.start();
    }
    m_Connection.startReading();
    Connected = true;
  }
  
  
  public void onDestroy() {
    try {
      m_Connection.stopReading();
      m_Connection.stopRunning();
      Thread.sleep(500);
      if( m_Socket != null && m_Socket.isConnected() && !m_Socket.isClosed() ) {
        m_Socket.close();
      }
      Connected = false;

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public String getDeviceName() {
    return m_DevideId;
  }
  
  public synchronized void sendMessage(String name, String msg) {
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
        m_Socket = null;
      }
    }
  }

  public void event(String itemtype, Attributes atts) {
    if( itemtype.equals("state") ) {
      Power = Item.getAttrValue(atts, "power", false);
      return;
    }
    if( itemtype.equals("auto") ) {
      String cmd = Item.getAttrValue(atts, "cmd", "");
      if( cmd.equals("on") || cmd.equals("off") )
        AutoMode = cmd.equals("on");
      return;
    }
  }


}

