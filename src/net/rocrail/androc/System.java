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

import android.app.Activity;
import android.content.SharedPreferences;

public class System {
  public String m_Host   = "rocrail.dyndns.org";
  public int    m_iPort  = 8080;

  Activity      m_andRoc = null;
  Socket        m_Socket = null;

  public static final String PREFS_NAME = "andRoc.ini";

  public System(Activity androc) {
    m_andRoc = androc;
    // Restore preferences
    SharedPreferences settings = androc.getSharedPreferences(PREFS_NAME, 0);
    m_Host  = settings.getString("host", "rocrail.dyndns.org");
    m_iPort = settings.getInt("port", 8080);
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

}
