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


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.rocrail.androc.RocrailService;
import net.rocrail.androc.widgets.LocoImage;

import org.xml.sax.Attributes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Loco implements Runnable {
  RocrailService  rocrailService = null;

  public List<Function> Functions = new ArrayList<Function>();
  public String  ID      = "?";
  public String  Description = "";
  public String  Roadname = "";
  public String  Mode    = "";
  public String  PicName = null;
  public String  Engine  = "";
  public String  Cargo   = "";
  private Bitmap LocoBmp = null;
  public int     Addr    = 0;
  public int     Steps   = 0;
  public long    RunTime = 0;
  public int     Speed   = 0;
  public int     Vmax    = 0;
  public int     Vmid    = 0;
  public int     Vmin    = 0;
  public String  Vmode   = "";
  public int     Vprev   = 0;
  
  public static final int VDelta = 5;
  
  public boolean   AutoStart = false;
  public boolean   HalfAuto  = false;
  public boolean   Commuter  = false;
  public boolean   Lights    = false;
  public boolean   Dir       = true;
  public boolean   Placing   = true;
  public boolean[] Function  = new boolean[32];
  public boolean   Show      = true;
  
  private boolean   ImageRequested = false;
  public  LocoImage imageView      = null;
  
  public Attributes properties = null;
  
  int fx = 0;
  

  public Loco( RocrailService rocrailService, Attributes atts) {
    this.rocrailService = rocrailService;
    ID = atts.getValue("id");
    PicName = atts.getValue("image");
    properties = atts;
    /*
    Vmid     = [Globals getAttribute:@"V_mid" fromDict:attributeDict withDefault:@""];
    Vmin     = [Globals getAttribute:@"V_min" fromDict:attributeDict withDefault:@""];
    Vmode    = [Globals getAttribute:@"V_mode" fromDict:attributeDict withDefault:@""];
    Placing  = [Globals getAttribute:@"placing" fromDict:attributeDict withDefault:@""];
    */
    Description = Item.getAttrValue(atts, "desc","");
    Roadname    = Item.getAttrValue(atts, "roadname", "");
    Addr        = Item.getAttrValue(atts, "addr", 0);
    Steps       = Item.getAttrValue(atts, "spcnt", 0);
    Dir         = Item.getAttrValue(atts, "dir", Dir);
    Speed       = Item.getAttrValue(atts, "V", Speed);
    Lights      = Item.getAttrValue(atts, "fn", Lights );
    Engine      = Item.getAttrValue(atts, "engine", "");
    Cargo       = Item.getAttrValue(atts, "cargo", "");
    Commuter    = Item.getAttrValue(atts, "commuter", false);
    Show        = Item.getAttrValue(atts, "show", false);
    
    Vmax  = Item.getAttrValue(atts, "V_max", 100);
    Vmid  = Item.getAttrValue(atts, "V_mid", 50);
    Vmin  = Item.getAttrValue(atts, "V_min", 10);
    Vmode = Item.getAttrValue(atts, "V_mode", "");

    fx = Item.getAttrValue(atts, "fx", fx );
    
    for(int i = 1; i < 32; i++) {
      int mask = 1 << (i-1);
      Function[i] = ( (fx & mask) == mask ) ? true:false; 
    }
    updateWithAttributes(atts);
    
    if( !this.rocrailService.Prefs.ImagesOnDemand )
      new Thread(this).start();

  }
  

  public void updateFunctions(Attributes atts) {
    for( int i = 1; i <= 24; i++ ) {
      Function[i] = Item.getAttrValue(atts, "f"+i, Function[i]);
    }
  }
  
  
  public void updateWithAttributes(Attributes atts) {
    Dir    = Item.getAttrValue(atts, "dir", Dir);
    Speed  = Item.getAttrValue(atts, "V", Speed);
    Lights = Item.getAttrValue(atts, "fn", Lights );
    Mode   = Item.getAttrValue(atts, "mode", "");
    Vmax   = Item.getAttrValue(atts, "V_max", Vmax);
    Vmid   = Item.getAttrValue(atts, "V_mid", Vmid);
    Vmin   = Item.getAttrValue(atts, "V_min", Vmin);

    RunTime = Item.getAttrValue(atts, "runtime", 0);
    Placing = Item.getAttrValue(atts, "placing", Placing);
    
    if( Mode.length() > 0 ) {
      AutoStart = Mode.equals("auto");
      HalfAuto  = Mode.equals("halfauto");
    }
    
  }
  
  public boolean isPercentMode() {
    return Vmode.equals("percent");
  }
  
  public boolean isAutoMode() {
    return Mode.equals("auto");
  }
  
  public boolean isHalfAutoMode() {
    return Mode.equals("halfauto");
  }
  
  public String toString() {
    if( Description.length() > 0 ) {
      return ID + ", " + Description;
    }
    return ID;
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
    
    
    File dir = new File("/sdcard/androc/");
    if( !dir.exists() )
      dir.mkdirs();
    File file = new File("/sdcard/androc/" + PicName );
    if( file.exists()) {
      try {
        byte rawdata[] = new byte[(int)file.length()]; 
        FileInputStream fis = new FileInputStream(file);
        fis.read(rawdata);
        fis.close();
        Bitmap bmp = BitmapFactory.decodeByteArray(rawdata, 0, rawdata.length);
        LocoBmp = bmp;
        if( imageView != null ) {
          imageView.post(new UpdateLocoImage(this));
        }
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }
    
    if(LocoBmp == null && !ImageRequested) {
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
      
      File dir = new File("/sdcard/androc/");
      if( !dir.exists() )
        dir.mkdirs();
      File file = new File("/sdcard/androc/" + PicName );
      try {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(rawdata);
        fos.close();
      }
      catch(Exception e) {
        e.printStackTrace();
      }

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
    setSpeed(true);
  }
  
  public void flipLights() {
    Lights = !Lights;
    setSpeed(true);
    rocrailService.sendMessage("lc", String.format( "<fn id=\"%s\" fnchanged=\"%d\" group=\"%d\" f%d=\"%s\"/>",
        ID, 0, 1, 0, (Lights?"true":"false")) );
  }
  
  public void flipGo() {
    if( rocrailService.AutoMode ) {
      AutoStart = !AutoStart;
      rocrailService.sendMessage("lc", String.format("<lc id=\"%s\" cmd=\"%s\"/>", 
          ID, (AutoStart?"go":"stop") ) );
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
    if( force || V == Vmax || V == 0 || StrictMath.abs( Vprev - V) >= VDelta || Steps < 50 ) {
      Speed = V;
      System.out.println("set Speed="+Speed);
      setSpeed(false);
    }
  }

  public void setSpeed(boolean force) {
    if(force || Vprev != Speed) {
      Vprev = Speed;
      rocrailService.sendMessage("lc", 
          String.format( "<lc throttleid=\"%s\" id=\"%s\" V=\"%d\" dir=\"%s\" fn=\"%s\"/>", 
              rocrailService.getDeviceName(), ID, Speed, (Dir?"true":"false"), (Lights?"true":"false") ) );
    }
  }
  
  public void CVWrite(int cv, int val) {
    boolean longAddr = Addr > 127;
    rocrailService.sendMessage("program", 
        String.format("<program cmd=\"1\" addr=\"%d\" decaddr=\"%d\" cv=\"%d\" value=\"%d\" longaddr=\"%s\" pom=\"true\"/>", 
            Addr, Addr, cv, val, longAddr?"true":"false" ) 
            );
  }

  public void CVRead(int cv) {
    boolean longAddr = Addr > 127;
    rocrailService.sendMessage("program", 
        String.format("<program cmd=\"0\" addr=\"%d\" cv=\"%d\" longaddr=\"%s\" pom=\"true\"/>", 
            Addr, cv, longAddr?"true":"false" ) 
            );
  }

  public void setVmin(int v) {
    Vmin = v;
    rocrailService.sendMessage("model",
        String.format("<model cmd=\"modify\"><lc id=\"%s\" V_min=\"%d\"/></model>", ID, v ) );
  }
  public void setVmid(int v) {
    Vmid = v;
    rocrailService.sendMessage("model",
        String.format("<model cmd=\"modify\"><lc id=\"%s\" V_mid=\"%d\"/></model>", ID, v ) );
  }
  public void setVmax(int v) {
    Vmax = v;
    rocrailService.sendMessage("model",
        String.format("<model cmd=\"modify\"><lc id=\"%s\" V_max=\"%d\"/></model>", ID, v ) );
  }
  
  public void swap() {
    rocrailService.sendMessage("lc",
        String.format("<lc id=\"%s\" cmd=\"swap\"/>", ID ) );
  }
  
  public void Dispatch() {
    rocrailService.sendMessage("lc",
        String.format("<lc id=\"%s\" cmd=\"dispatch\"/>", ID ) );
  }
  
  public void addFunction(Attributes atts ) {
    Function function = new Function();
    function.Nr = Item.getAttrValue(atts, "fn", 0 );
    function.Text = Item.getAttrValue(atts, "text", "F"+function.Nr );
    Functions.add(function);
  }

  public String getFunctionText(int nr) {
    if( Functions != null ) {
      Iterator<Function> it = Functions.iterator();
      while(it.hasNext()) {
        Function function = it.next();
        if( function.Nr == nr && function.Text.length() > 0 )
          return function.Text;
      }
    }
    return "F"+nr;
  }


  @Override
  public void run() {
    try {
      Thread.sleep(100);
      Loco.this.getLocoBmp(null);
    }
    catch(Exception e) {
      
    }
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


class Function {
  int Nr = 0;
  String Text = "";
}
