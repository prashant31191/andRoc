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
package net.rocrail.androc.objects;

import net.rocrail.androc.RocrailService;
import net.rocrail.androc.andRoc;

import org.xml.sax.Attributes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Loco {
  public String  ID      = "?";
  public String  PicName = null;
  public Bitmap  LocoBmp = null;
  public int     Speed   = 0;
  
  RocrailService  m_andRoc  = null;
  private boolean m_bLights = false;
  boolean m_bDir    = true;
  boolean[] m_Function = new boolean[32];
  String  mPicData  = null;
  
  public Attributes properties = null;

  public Loco( RocrailService rocrailService, String id, Attributes atts) {
    m_andRoc = rocrailService;
    ID = id;
    PicName = atts.getValue("image");
    properties = atts;
  }

  public void requestLocoImg() {
    if( PicName != null ) {
      // type 1 is for small images
      m_andRoc.sendMessage("datareq", 
          String.format("<datareq id=\"%s\" type=\"1\" filename=\"%s\"/>", ID, PicName) );
    }
  }

  
  static byte[] strToByte( String s ) {
    int i = 0;
    int len = s.length();
    byte[] b = new byte[len/2 + 1];
    for( i = 0; i < len; i+=2 ) {
      int val = Integer.parseInt(s.substring(i, i+2), 16);
      b[i/2] = (byte)(val & 0xFF);
    }
    return b;
  }

  public void setPicData(String data) {
    mPicData = data;
    // TODO: convert from HEXA to Bitmap
    byte[] rawdata = strToByte(mPicData);
    Bitmap bmp = BitmapFactory.decodeByteArray(rawdata, 0, rawdata.length);
    LocoBmp = bmp;
  }
  
  public void dir() {
    m_bDir = !m_bDir;
    speed(Speed);
  }
  
  public void lights() {
    m_bLights = !m_bLights;
    m_andRoc.sendMessage("lc", String.format( "<lc throttleid=\"%s\" id=\"%s\" fn=\"%s\"/>", 
        m_andRoc.getDeviceName(), ID, (m_bLights?"true":"false")) );
  }
  
  public void function(int fn) {
    m_Function[fn] = !m_Function[fn];
    m_andRoc.sendMessage("lc", String.format( "<fn id=\"%s\" fnchanged=\"%d\" group=\"%d\" f%d=\"%s\"/>", 
        ID, fn, (fn-1)/4+1, fn, (m_Function[fn]?"true":"false")) );
  }
  
  public void speed(int V) {
    Speed = V;
    m_andRoc.sendMessage("lc", String.format( "<lc throttleid=\"%s\" id=\"%s\" V=\"%d\" dir=\"%s\" fn=\"%s\"/>", 
        m_andRoc.getDeviceName(), ID, Speed, (m_bDir?"true":"false"), (m_bLights?"true":"false") ) );
    
  }
}
