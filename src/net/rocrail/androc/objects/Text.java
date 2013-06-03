/*
 Rocrail - Model Railroad Software

 Copyright (C) 2002-2011 - Rob Versluis <r.j.versluis@rocrail.net>

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
import java.util.Iterator;

import net.rocrail.androc.RocrailService;
import net.rocrail.androc.interfaces.Mobile;
import net.rocrail.androc.widgets.LocoImage;

import org.xml.sax.Attributes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class Text extends Item implements View.OnClickListener {
  int m_cX = 0;
  int m_cY = 0;
  protected Bitmap TextBmp = null;

  public Text(RocrailService rocrailService, Attributes atts) {
    super(rocrailService, atts);
    Text = Item.getAttrValue(atts, "text", Text );
    cX = Item.getAttrValue(atts, "cx", 3 );
    m_cX = cX;
    m_cY = cY;
  }
  
  public void updateTextColor() {
  }
  
  public void Draw( Canvas canvas) {
    if( !imageRequested )
      update4Text();
    else {
      if( getBmp() != null ) {
        try {
          imageView.setImageBitmap(getBmp());
        }
        catch( Exception e ) {
          // invalid imageView 
        }
      }
    }
  }
  
  public String getImageName(boolean ModPlan) {
    this.ModPlan = ModPlan;
    int orinr = getOriNr(ModPlan);

    if (orinr % 2 == 0) {
      // vertical
      textVertical = true;
      cX = m_cY;
      cY = m_cX;
    }
    else {
      textVertical = false;
      cX = m_cX;
      cY = m_cY;
    }

    if( Text.endsWith(".png") ) {
      ImageName = Text.substring(0, Text.indexOf(".png"));
      return ImageName;
    }
    return null;
  }

  public void updateWithAttributes(Attributes atts ) {
    Text = Item.getAttrValue(atts, "text", Text); 
    imageRequested = false;
    updateTextColor();
    super.updateWithAttributes(atts);
  }

  
  public void setPicData(String filename, String data) {
    if( data != null && data.length() > 0 ) {
      // convert from HEXA to Bitmap
      byte[] rawdata = MobileImpl.strToByte(data);
      System.out.println("text image: " + filename);
      File dir = new File("/sdcard/androc/");
      if( !dir.exists() )
        dir.mkdirs();
      
      File file = null;
      file = new File("/sdcard/androc/" + filename );
      
      if( file != null ) {
        try {
          FileOutputStream fos = new FileOutputStream(file);
          fos.write(rawdata);
          fos.close();
        }
        catch(Exception e) {
          e.printStackTrace();
        }
  
        Bitmap bmp = BitmapFactory.decodeByteArray(rawdata, 0, rawdata.length);
        TextBmp = bmp;
        HideText = true;
        if( imageView != null ) {
          System.out.println("setting text image...");
          imageView.post(new UpdateTextImage(this));
        }
        else {
          System.out.println("no text imageview...");
        }
      }
    }
    else {
      System.out.println("no raw data for text "+ID);
    }
  }
  
  
  public Bitmap getBmp() {
    return TextBmp;
  }

}


class UpdateTextImage implements Runnable {
  Text text = null;
  
  public UpdateTextImage( Text text ) {
    this.text = text;
  }
  @Override
  public void run() {
    if( text.getBmp() != null ) {
      try {
        text.imageView.setImageBitmap(text.getBmp());
      }
      catch( Exception e ) {
        // invalid imageView 
      }
    }
  }
  
}
