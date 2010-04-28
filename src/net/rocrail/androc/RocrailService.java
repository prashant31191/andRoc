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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParser;

import org.xml.sax.Attributes;

import net.rocrail.androc.interfaces.MessageListener;
import net.rocrail.androc.interfaces.SystemListener;
import net.rocrail.androc.objects.Item;
import net.rocrail.androc.objects.Loco;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class RocrailService extends Service {
  public String m_Recent   = "rocrail.dyndns.org:8080;";
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
  
  Socket        m_Socket     = null;
  Connection    m_Connection = null;
  
  SAXParser m_Parser = null;
  private List<SystemListener>  m_Listeners = new ArrayList<SystemListener>();
  public List<String>  MessageList = new ArrayList<String>();

  MessageListener messageListener = null;
  
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
  
  public void addListener( SystemListener listener ) {
    m_Listeners.add(listener);
  }
  

  public void connect() throws Exception {
    // TODO: clean up all activities and model
    
    try {
      if( m_Connection != null ) {
        m_Connection.stopReading();
        Thread.sleep(500);
      }
    } 
    catch (Exception e) {
      e.printStackTrace();
    }
    
    m_Socket = new Socket(m_Host, m_iPort);
    sendMessage("model","<model cmd=\"plan\" disablemonitor=\"false\"/>");
    if( m_Connection == null ) {
      m_Connection = new Connection(this, m_Model, m_Socket);
      m_Connection.start();
    }
    m_Connection.startReading();
    Connected = true;
  }
  
  
  public void onDestroy() {
    disConnect(true);
  }
  
  public void disConnect(boolean stop) {
    try {
      m_Connection.stopReading();
      if( stop )
        m_Connection.stopRunning();
      Thread.sleep(500);
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      if( m_Socket != null ) {
        m_Socket.close();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    Connected = false;
    m_Socket = null;

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
        informListeners(SystemListener.EVENT_DISCONNECTED);
      }
    }
  }

  public void informListeners(String event) {
    if( SystemListener.EVENT_SHUTDOWN.equals(event) ) {
      disConnect(false);
      // TODO: remove all views and clean up the model...
      
      // set the connection view active again
      Iterator<SystemListener> it = m_Listeners.iterator();
      while( it.hasNext() ) {
        SystemListener listener = it.next();
        listener.SystemShutdown();
      }
    }
    else if( SystemListener.EVENT_DISCONNECTED.equals(event) ) {
      m_Socket = null;
      Connected = false;
      
      // set the connection view active again
      Iterator<SystemListener> it = m_Listeners.iterator();
      while( it.hasNext() ) {
        SystemListener listener = it.next();
        listener.SystemDisconnected();
      }
    }
  }
  
  public void event(String itemtype, Attributes atts) {
    if( itemtype.equals("sys") ) {
      informListeners(Item.getAttrValue(atts, "cmd", ""));
      return;
    }
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
    if( itemtype.equals("exception") ) {
      String text = Item.getAttrValue(atts, "text", null);
      if( text != null && text.length() > 0 ) {
        MessageList.add(0, text);
        if( MessageList.size() > 100 ) {
          MessageList.remove(MessageList.size()-1);
        }
        if( messageListener != null )
          messageListener.newMessages();
      }
      return;
    }
  }

  public void setMessageListener(MessageListener listener) {
    messageListener = listener;
    
  }

}

