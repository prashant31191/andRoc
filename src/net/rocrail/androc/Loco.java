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

import org.xml.sax.Attributes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Loco {
  public String  ID      = "?";
  public String  PicName = null;
  public Bitmap  LocoBmp = null;
  
  andRoc  m_andRoc  = null;
  private boolean m_bLights = false;
  boolean m_bDir    = true;
  int     m_iSpeed  = 0;
  boolean[] m_Function = new boolean[32];
  String  mPicData  = null;
  
  public Attributes properties = null;

  public Loco( andRoc androc, String id, Attributes atts) {
    m_andRoc = androc;
    ID = id;
    properties = atts;
  }

  public void requestLocoImg() {
    if( PicName != null ) {
      // type 1 is for small images
      m_andRoc.getSystem().sendMessage("datareq", 
          String.format("<datareq id=\"%s\" type=\"1\" filename=\"%s\"/>", ID, PicName) );
    }
  }

  
  static byte[] strToByte( String s ) {
    int i = 0;
    int len = s.length();
    byte[] b = new byte[len/2 + 1];
    for( i = 0; i < len; i+=2 ) {
      b[i/2] = (byte)(Integer.getInteger(s.substring(i, i+2)) & 0xFF);
    }
    return b;
  }

  public void setPicData(String data) {
    mPicData = data;
    // TODO: convert from HEXA to Bitmap
    byte[] rawdata = strToByte(mPicData);
    LocoBmp = BitmapFactory.decodeByteArray(rawdata, 0, rawdata.length);
  }
  
  public void dir() {
    m_bDir = !m_bDir;
    speed(m_iSpeed);
  }
  
  public void lights() {
    m_bLights = !m_bLights;
    m_andRoc.getSystem().sendMessage("lc", String.format( "<lc throttleid=\"%s\" id=\"%s\" fn=\"%s\"/>", 
        m_andRoc.getSystem().getDeviceName(), ID, (m_bLights?"true":"false")) );
  }
  
  public void function(int fn) {
    m_Function[fn] = !m_Function[fn];
    m_andRoc.getSystem().sendMessage("lc", String.format( "<fn id=\"%s\" fnchanged=\"%d\" group=\"%d\" f%d=\"%s\"/>", 
        ID, fn, (fn-1)/4+1, fn, (m_Function[fn]?"true":"false")) );
  }
  
  public void speed(int V) {
    m_iSpeed = V;
    m_andRoc.getSystem().sendMessage("lc", String.format( "<lc throttleid=\"%s\" id=\"%s\" V=\"%d\" dir=\"%s\" fn=\"%s\"/>", 
        m_andRoc.getSystem().getDeviceName(), ID, m_iSpeed, (m_bDir?"true":"false"), (m_bLights?"true":"false") ) );
    
  }
}
