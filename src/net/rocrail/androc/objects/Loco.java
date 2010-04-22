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

import org.xml.sax.Attributes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class Loco {
  public String  ID      = "?";
  public String  PicName = null;
  private Bitmap LocoBmp = null;
  public int     Speed   = 0;
  
  RocrailService  m_andRoc  = null;
  public boolean m_bLights = false;
  boolean m_bDir    = true;
  public boolean[] m_Function = new boolean[32];
  String  mPicData  = null;
  private boolean m_bImageRequested = false;
  ImageView imageView = null;
  
  public Attributes properties = null;

  public Loco( RocrailService rocrailService, String id, Attributes atts) {
    m_andRoc = rocrailService;
    ID = id;
    PicName = atts.getValue("image");
    properties = atts;
    /*
    Vmax     = [Globals getAttribute:@"V_max" fromDict:attributeDict withDefault:@""];
    vmaxstr  = [Globals getAttribute:@"V_max" fromDict:attributeDict withDefault:@""];
    Vmid     = [Globals getAttribute:@"V_mid" fromDict:attributeDict withDefault:@""];
    Vmin     = [Globals getAttribute:@"V_min" fromDict:attributeDict withDefault:@""];
    Vmode    = [Globals getAttribute:@"V_mode" fromDict:attributeDict withDefault:@""];
    Fn       = [Globals getAttribute:@"fn" fromDict:attributeDict withDefault:@""];
    Fx       = [Globals getAttribute:@"fx" fromDict:attributeDict withDefault:@""];
    SpCnt    = [Globals getAttribute:@"spcnt" fromDict:attributeDict withDefault:@""];
    Placing  = [Globals getAttribute:@"placing" fromDict:attributeDict withDefault:@""];
    Mode     = [Globals getAttribute:@"mode" fromDict:attributeDict withDefault:@""];
    imgname  = [Globals getAttribute:@"image" fromDict:attributeDict withDefault:@""];
    desc     = [Globals getAttribute:@"desc" fromDict:attributeDict withDefault:@""];
    roadname = [Globals getAttribute:@"roadname" fromDict:attributeDict withDefault:@""];
    dir      = [Globals getAttribute:@"dir" fromDict:attributeDict withDefault:@""];
    vstr     = [Globals getAttribute:@"V" fromDict:attributeDict withDefault:@"0"];
    */
    updateWithAttributes(atts);
  }

  public void updateWithAttributes(Attributes atts) {
    m_bDir = Item.getAttrValue(atts, "dir", m_bDir);
    Speed  = Item.getAttrValue(atts, "V", Speed);
  }

  public Bitmap getLocoBmp(ImageView image) {
    if( LocoBmp == null ) {
      requestLocoImg(image);
    }
    return LocoBmp;
      
  }
  
  public void requestLocoImg(ImageView image) {
    if( image != null )
      imageView = image;
    
    if(!m_bImageRequested) {
      m_bImageRequested = true;
      if( PicName != null && PicName.length() > 0 ) {
        // type 1 is for small images
        m_andRoc.sendMessage("datareq", 
            String.format("<datareq id=\"%s\" type=\"1\" filename=\"%s\"/>", ID, PicName) );
      }
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
    if( data != null && data.length() > 0 ) {
      mPicData = data;
      // convert from HEXA to Bitmap
      byte[] rawdata = strToByte(mPicData);
      Bitmap bmp = BitmapFactory.decodeByteArray(rawdata, 0, rawdata.length);
      LocoBmp = bmp;
      if( imageView != null ) {
        imageView.post(new UpdateLocoImage(this));
      }
    }
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

class UpdateLocoImage implements Runnable {
  Loco loco = null;
  
  public UpdateLocoImage( Loco loco ) {
    this.loco = loco;
  }
  @Override
  public void run() {
    if( loco.getLocoBmp(null) != null && loco.imageView != null ) {
      try {
        loco.imageView.setImageBitmap(loco.getLocoBmp(null));
      }
      catch( Exception e ) {
        // invalid imageView 
      }
    }
  }
  
}
