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
import net.rocrail.androc.widgets.LocoImage;

import org.xml.sax.Attributes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Loco {
  RocrailService  rocrailService = null;

  public String  ID      = "?";
  public String  PicName = null;
  private Bitmap LocoBmp = null;
  public int     Speed   = 0;
  public int     Vmax    = 0;
  public int     Vprev   = 0;
  
  public static final int VDelta = 5;
  
  public boolean   AutoStart = false;
  public boolean   HalfAuto  = false;
  public boolean   Lights    = false;
  public boolean   Dir       = true;
  public boolean   Go        = false;
  public boolean[] Function  = new boolean[32];
  
  private boolean   ImageRequested = false;
  public  LocoImage imageView      = null;
  
  public Attributes properties = null;
  

  public Loco( RocrailService rocrailService, Attributes atts) {
    this.rocrailService = rocrailService;
    ID = atts.getValue("id");
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
    Vmax  = Item.getAttrValue(atts, "V_max", 100);
    int fx = Item.getAttrValue(atts, "fx", 0 );
    
    for(int i = 1; i < 32; i++) {
      int mask = 1 << (i-1);
      Function[i] = ( (fx & mask) == mask ) ? true:false; 
    }

    updateWithAttributes(atts);
  }

  public void updateWithAttributes(Attributes atts) {
    //Dir    = Item.getAttrValue(atts, "dir", Dir);
    //Speed  = Item.getAttrValue(atts, "V", Speed);
    //Lights = Item.getAttrValue(atts, "fn", Lights );
  }

  public Bitmap getLocoBmp(LocoImage image) {
    if( LocoBmp == null ) {
      requestLocoImg(image);
    }
    return LocoBmp;
      
  }
  
  public void requestLocoImg(LocoImage image) {
    if( image != null )
      imageView = image;
    
    if(!ImageRequested) {
      ImageRequested = true;
      if( PicName != null && PicName.length() > 0 ) {
        // type 1 is for small images
        rocrailService.sendMessage("datareq", 
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
      // convert from HEXA to Bitmap
      byte[] rawdata = strToByte(data);
      Bitmap bmp = BitmapFactory.decodeByteArray(rawdata, 0, rawdata.length);
      LocoBmp = bmp;
      if( imageView != null ) {
        imageView.post(new UpdateLocoImage(this));
      }
    }
  }
  
  public void flipDir() {
    Dir = !Dir;
    Speed = 0;
    setSpeed();
  }
  
  public void flipLights() {
    Lights = !Lights;
    setSpeed();
  }
  
  public void flipGo() {
    if( rocrailService.AutoMode ) {
      Go = !Go; // TODO: use the reported mode of the loco
      rocrailService.sendMessage("lc", String.format("<lc id=\"%s\" cmd=\"%s\"/>", 
          ID, (Go?"go":"stop") ) );
    }
  }
  public void doRelease() {
    rocrailService.sendMessage("lc", String.format( "<lc throttleid=\"%s\" cmd=\"release\" id=\"%s\"/>",
        rocrailService.getDeviceName(), ID ) );
  }
  
  public void flipFunction(int fn) {
    Function[fn] = !Function[fn];
    rocrailService.sendMessage("lc", String.format( "<fn id=\"%s\" fnchanged=\"%d\" group=\"%d\" f%d=\"%s\"/>", 
        ID, fn, (fn-1)/4+1, fn, (Function[fn]?"true":"false")) );
  }
  
  public void setSpeed(int V, boolean force) {
    int vVal = (int)(V * (Vmax/100.00));
    
    if( force || StrictMath.abs( Vprev - vVal) >= VDelta ) {
      Speed = vVal;
      setSpeed();
    }
  }

  public void setSpeed() {
    Vprev = Speed;
    rocrailService.sendMessage("lc", 
        String.format( "<lc throttleid=\"%s\" id=\"%s\" V=\"%d\" dir=\"%s\" fn=\"%s\"/>", 
            rocrailService.getDeviceName(), ID, Speed, (Dir?"true":"false"), (Lights?"true":"false") ) );
    
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
        if( loco.ID.equals(loco.imageView.ID))
          loco.imageView.setImageBitmap(loco.getLocoBmp(null));
      }
      catch( Exception e ) {
        // invalid imageView 
      }
    }
  }
  
}
