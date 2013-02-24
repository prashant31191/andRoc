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

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import net.rocrail.androc.RocrailService;
import net.rocrail.androc.interfaces.Mobile;
import net.rocrail.androc.widgets.LocoImage;

public class Car extends MobileImpl implements Runnable {
  

  public Car( RocrailService rocrailService, Attributes atts) {
    this.rocrailService = rocrailService;
    ID = atts.getValue("id");
    PicName = atts.getValue("image");
    properties = atts;
    Description = Item.getAttrValue(atts, "desc","");
    Roadname    = Item.getAttrValue(atts, "roadname", "");
    Addr        = Item.getAttrValue(atts, "addr", 0);
    
    updateWithAttributes(atts);

    if( !this.rocrailService.Prefs.ImagesOnDemand )
      new Thread(this).start();
  }
  
  @Override
  public void updateWithAttributes(Attributes atts) {
    Dir = Item.getAttrValue(atts, "dir", Dir);
  }
  
  @Override
  public boolean isAutoStart() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void flipGo() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void doRelease() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void flipDir() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String getConsist() {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public String getDescription() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getVMax() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void setSpeed(int V, boolean force) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void Dispatch() {
    // TODO Auto-generated method stub
    
  }


  @Override
  public long getRunTime() {
    // TODO Auto-generated method stub
    return 0;
  }


  @Override
  public int getSteps() {
    // TODO Auto-generated method stub
    return 0;
  }


  @Override
  public boolean isHalfAuto() {
    // TODO Auto-generated method stub
    return false;
  }


  @Override
  public void setAutoStart(boolean autostart) {
    // TODO Auto-generated method stub
    
  }


  @Override
  public void setHalfAuto(boolean halfauto) {
    // TODO Auto-generated method stub
    
  }


  @Override
  public boolean isPlacing() {
    // TODO Auto-generated method stub
    return false;
  }


  @Override
  public void setPlacing(boolean placing) {
    // TODO Auto-generated method stub
    
  }


  @Override
  public void swap() {
    // TODO Auto-generated method stub
    
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


