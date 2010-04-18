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

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

public class RocrailService extends Service {
  public String m_Host   = "rocrail.dyndns.org";
  public int    m_iPort  = 8080;
  public Model  m_Model  = null;
  
  
  andRoc        m_andRoc     = null;
  Socket        m_Socket     = null;
  boolean       m_bRun       = true;
  Connection    m_Connection = null;
  
  SAXParser m_Parser = null;

  public static final String PREFS_NAME = "andRoc.ini";

  
  @Override
  public void onCreate() {
    // Restore preferences
    /*
    SharedPreferences settings = m_andRoc.getSharedPreferences(PREFS_NAME, 0);
    m_Host  = settings.getString("host", "rocrail.dyndns.org");
    m_iPort = settings.getInt("port", 8080);
    */
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
    m_Socket = new Socket(m_Host, m_iPort);
    sendMessage("model","<model cmd=\"plan\" disablemonitor=\"true\"/>");
    m_Connection = new Connection(this, m_Model, m_Socket);
    m_Connection.start();
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
  
  public String getDeviceName() {
    return "andRoc";
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



}

