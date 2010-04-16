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

public class Loco {
  andRoc m_andRoc = null;
  boolean m_bLights = false;
  boolean m_bDir = true;
  int m_iSpeed = 0;
  public String m_ID = "?";
  
  public Loco( andRoc androc, String id) {
    m_andRoc = androc;
    m_ID = id;
  }

  public void dir() {
    m_bDir = m_bDir?false:true;
    speed(m_iSpeed);
  }
  
  public void lights() {
    if( m_bLights ) 
      m_bLights = false;
    else
      m_bLights = true;
    m_andRoc.getSystem().sendMessage("lc", String.format( "<lc throttleid=\"%s\" id=\"%s\" fn=\"%s\"/>", 
        m_andRoc.getSystem().getDeviceName(), m_ID, m_bLights?"true":"false") );
  }
  
  public void speed(int V) {
    m_iSpeed = V;
    m_andRoc.getSystem().sendMessage("lc", String.format( "<lc throttleid=\"%s\" id=\"%s\" V=\"%d\" dir=\"%s\" fn=\"%s\"/>", 
        m_andRoc.getSystem().getDeviceName(), m_ID, m_iSpeed, m_bDir ? "true":"false", m_bLights ? "true":"false" ) );
    
  }
}