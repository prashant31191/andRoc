/*
 Rocrail - Model Railroad Software

 Copyright (C) 2002-2013 - Rob Versluis <r.j.versluis@rocrail.net>

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

public class Car extends MobileImpl implements Runnable {
  

  public Car( RocrailService rocrailService, Attributes atts) {
    this.rocrailService = rocrailService;
    ID = atts.getValue("id");
    PicName = atts.getValue("image");
    properties = atts;
    Description = Item.getAttrValue(atts, "desc","");
    Roadname    = Item.getAttrValue(atts, "roadname", "");
    Addr        = Item.getAttrValue(atts, "addr", 0);
    Show        = Item.getAttrValue(atts, "show", false);
    Era         = Item.getAttrValue(atts, "era", 0);

    updateWithAttributes(atts);

    if( !this.rocrailService.Prefs.ImagesOnDemand )
      new Thread(this).start();
  }
  
  @Override
  public void updateWithAttributes(Attributes atts) {
    Dir  = Item.getAttrValue(atts, "dir", Dir);
    Show = Item.getAttrValue(atts, "show", Show);
    Era  = Item.getAttrValue(atts, "era", Era);
  }
  
  @Override
  public boolean isAutoStart() {
    return false;
  }

  @Override
  public void flipGo() {
  }

  @Override
  public void doRelease() {
  }

  @Override
  public int getVMax() {
    return 100;
  }

  @Override
  public void Dispatch() {
  }


  @Override
  public boolean isHalfAuto() {
    return false;
  }


  @Override
  public void setAutoStart(boolean autostart) {
  }


  @Override
  public void setHalfAuto(boolean halfauto) {
  }


  @Override
  public void swap() {
  }


  @Override
  public void run() {
    try {
      Thread.sleep(100);
      Car.this.getBmp(null);
    }
    catch(Exception e) {
      
    }
  }

}


