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

public class Loco extends MobileImpl implements Runnable {
  public String  Mode    = "";
  public String  Engine  = "";
  public String  Cargo   = "";
  
  public boolean   AutoStart = false;
  public boolean   HalfAuto  = false;
  public boolean   Commuter  = false;
  
  
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
    Consist     = Item.getAttrValue(atts, "consist", "");
    Addr        = Item.getAttrValue(atts, "addr", 0);
    Steps       = Item.getAttrValue(atts, "spcnt", 0);
    Dir         = Item.getAttrValue(atts, "dir", Dir);
    Speed       = Item.getAttrValue(atts, "V", Speed);
    Lights      = Item.getAttrValue(atts, "fn", Lights );
    Engine      = Item.getAttrValue(atts, "engine", "");
    Cargo       = Item.getAttrValue(atts, "cargo", "");
    Commuter    = Item.getAttrValue(atts, "commuter", false);
    Show        = Item.getAttrValue(atts, "show", false);
    Era         = Item.getAttrValue(atts, "era", 0);
    
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
  

  
  public void updateWithAttributes(Attributes atts) {
    Dir    = Item.getAttrValue(atts, "dir", Dir);
    Speed  = Item.getAttrValue(atts, "V", Speed);
    Lights = Item.getAttrValue(atts, "fn", Lights );
    Mode   = Item.getAttrValue(atts, "mode", "");
    Vmax   = Item.getAttrValue(atts, "V_max", Vmax);
    Vmid   = Item.getAttrValue(atts, "V_mid", Vmid);
    Vmin   = Item.getAttrValue(atts, "V_min", Vmin);
    Show   = Item.getAttrValue(atts, "show", Show);
    Era    = Item.getAttrValue(atts, "era", Era);
    Cargo  = Item.getAttrValue(atts, "cargo", Cargo);

    RunTime = Item.getAttrValue(atts, "runtime", 0);
    Placing = Item.getAttrValue(atts, "placing", Placing);

    Consist = Item.getAttrValue(atts, "consist", Consist);
    
    if( Mode.length() > 0 ) {
      AutoStart = Mode.equals("auto");
      HalfAuto  = Mode.equals("halfauto");
    }
    
  }

  public void addConsistMember( String memberID ) {
    if( !Consist.contains(memberID) ) {
      if( Consist.length() > 0 )
        Consist = Consist + "," + memberID;
      else
        Consist = memberID;
      rocrailService.sendMessage("model", 
          String.format("<model cmd=\"modify\"><lc id=\"%s\" consist=\"%s\"/></model>", ID, Consist) );
    }
  }

  public void removeConsistMember( String memberID ) {
    if( Consist.contains(memberID) ) {
      Consist = Consist.replace( ","+memberID, "");
      if( Consist.contains(memberID) )
        Consist = Consist.replace( memberID+",", "");
      if( Consist.contains(memberID) )
        Consist = Consist.replace( memberID, "");
      rocrailService.sendMessage("model", 
          String.format("<model cmd=\"modify\"><lc id=\"%s\" consist=\"%s\"/></model>", ID, Consist) );
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
  

  @Override
  public void run() {
    try {
      Thread.sleep(100);
      Loco.this.getBmp(null);
    }
    catch(Exception e) {
      
    }
  }


  @Override
  public boolean isAutoStart() {
    return AutoStart;
  }


  @Override
  public int getVMax() {
    return Vmax;
  }



  @Override
  public boolean isHalfAuto() {
    return HalfAuto;
  }


  @Override
  public void setAutoStart(boolean autostart) {
    AutoStart = autostart;
    
  }


  @Override
  public void setHalfAuto(boolean halfauto) {
    HalfAuto = halfauto;
  }


  
}


class Function {
  int Nr = 0;
  String Text = "";
  String Icon = "";
}
