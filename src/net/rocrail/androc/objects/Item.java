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
import net.rocrail.androc.interfaces.UpdateListener;
import net.rocrail.androc.widgets.LevelItem;

import org.xml.sax.Attributes;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

public class Item implements View.OnClickListener, View.OnLongClickListener, UpdateListener {
  RocrailService m_RocrailService = null;

  public Attributes Properties = null;
  public String ID = "?";
  public String Ori = "west";
  public int X = 0;
  public int Y = 0;
  public int Z = 0;

  public String Mod_Ori = "west";
  public int Mod_X = 0;
  public int Mod_Y = 0;

  public int cX = 1;
  public int cY = 1;
  public boolean Show = true;
  public boolean Background = false;
  public String Type = "";
  public String State = "";
  public String Text = "";
  public boolean textVertical = false;
  public String ImageName = "";
  public LevelItem imageView = null;
  public int colorName = 0;
  public String RouteIDs = "";
  public boolean RouteLocked = false;
  public String BlockID = "";
  public boolean Occupied = false;
  
  boolean Reserved = false;
  boolean Entering = false;
  boolean ModPlan = false;
  
  public Activity activity = null;
  
  public static final int COLOR_OPEN     = 0;
  public static final int COLOR_CLOSED   = 1;
  public static final int COLOR_FREE     = 2;
  public static final int COLOR_RESERVED = 3;
  public static final int COLOR_ENTER    = 4;
  public static final int COLOR_OCCUPIED = 5;
  public static final int COLOR_ACCEPTIDENT = 6;
  public static final int COLOR_RED         = 7;
  public static final int COLOR_NONE        = 8;


  public Item(RocrailService rocrailService, Attributes atts) {
    m_RocrailService = rocrailService;
    Properties = atts;
    __updateWithAttributes(atts);
    ID      = getAttrValue(atts, "id", "?"); 
    Mod_X   = getAttrValue(atts, "x", 0); 
    Mod_Y   = getAttrValue(atts, "y", 0); 
    X       = getAttrValue(atts, "prev_x", Mod_X); 
    Y       = getAttrValue(atts, "prev_y", Mod_Y); 
    Mod_Ori = getAttrValue(atts, "ori", Ori); 
    Ori     = getAttrValue(atts, "prev_ori", Mod_Ori); 
    Z       = getAttrValue(atts, "z", 0); 
    cX      = getAttrValue(atts, "cx", 1); 
    cY      = getAttrValue(atts, "cy", 1); 
    Show    = getAttrValue(atts, "show", true);
    RouteIDs= getAttrValue(atts, "routeids", ""); 
    BlockID = getAttrValue(atts, "blockid", ""); 
    
    if( cX < 1 ) cX = 1;
    if( cY < 1 ) cY = 1;
      
  }
  
  public String getImageName(boolean ModPlan){
    this.ModPlan = ModPlan;
    return ImageName;
  }
  
  public void Draw( Canvas canvas) {
    
  }
  
  public static String getAttrValue(Attributes atts, String key, String defval) {
    if( atts.getValue(key) == null )
      return defval;
    return atts.getValue(key);
  }
  
  public static int getAttrValue(Attributes atts, String key, int defval) {
    if( atts.getValue(key) == null || atts.getValue(key).length() == 0 )
      return defval;
    return Integer.parseInt(atts.getValue(key));
  }
  
  public static boolean getAttrValue(Attributes atts, String key, boolean defval) {
    String val = atts.getValue(key);
    if( val == null || val.length() == 0 )
      return defval;
    if( val.equals("false") || val.equals("true") )
      return Boolean.parseBoolean(atts.getValue(key));
    else
      return defval;
  }
  
  public static boolean hasAttribute(Attributes atts, String key) {
    String val = atts.getValue(key);
    if( val != null && val.length() > 0 )
      return true;
    return false;
  }
  
  void __updateWithAttributes(Attributes atts ) {
    Type  = getAttrValue(atts, "type", Type); 
    State = getAttrValue(atts, "state", State); 
    if( hasAttribute(atts, "ori") ) {
      Mod_Ori = getAttrValue(atts, "ori", Ori); 
      Ori     = getAttrValue(atts, "prev_ori", Mod_Ori);
    }
    Z       = getAttrValue(atts, "z", Z); 
    cX      = getAttrValue(atts, "cx", cX); 
    cY      = getAttrValue(atts, "cy", cY); 
    RouteIDs= getAttrValue(atts, "routeids", RouteIDs); 
    
    try {
      if( imageView != null && imageView.isShown() ) {
        imageView.post(new UpdateImage(this));
      }
    }
    catch( Exception e ) {
      // Probably not a valid ImageView...
    }
  }


  public void updateWithAttributes(Attributes atts ) {
    __updateWithAttributes(atts);
  }


  @Override
  public boolean update4Route(String routeid, boolean locked ) {
    if( RouteIDs.contains(routeid) ) {
      RouteLocked = locked;
      System.out.println("item " + ID + " locked=" + locked + " for route " + routeid);
      try {
        if( imageView != null && imageView.isShown() ) {
          imageView.post(new UpdateImage(this));
        }
      }
      catch( Exception e ) {
        // Probably not a valid ImageView...
      }
      return true;
    }
    return false;
  }
  
  @Override
  public boolean update4Block(String blockid, boolean occ ) {
    if( BlockID.equals(blockid) ) {
      Occupied = occ;
      System.out.println("item " + ID + " occ=" + occ + " for block " + blockid);
      
      try {
        if( imageView != null && imageView.isShown() ) {
          imageView.post(new UpdateImage(this));
        }
        else {
          System.out.println("item " + ID + " does not show");
        }
      }
      catch( Exception e ) {
        // Probably not a valid ImageView...
      }
      return true;
    }
    return false;
  }
  
public int getOriNr(boolean ModPlan) {
    if( ModPlan ) {
      if(Mod_Ori.equals("north"))
        return 2;
      if(Mod_Ori.equals("east"))
        return 3;
      if(Mod_Ori.equals("south"))
        return 4;
      return 1;
    }
    
    if(Ori.equals("north"))
      return 2;
    if(Ori.equals("east"))
      return 3;
    if(Ori.equals("south"))
      return 4;
    return 1;
  }

  
  @Override
  public void onClick(View view) {
    // TODO Auto-generated method stub
    
  }
  public void onClickUp(View view) {
    // TODO Auto-generated method stub
    
  }
  
  public void propertiesView() {
    
  }

  @Override
  public boolean onLongClick(View arg0) {
    // TODO Auto-generated method stub
    return false;
  }

}

class UpdateImage implements Runnable {
  Item item = null;
  
  public UpdateImage( Item item ) {
    this.item = item;
  }

  @Override
  public void run() {
    if( item.getImageName(item.ModPlan) == null || item.getImageName(item.ModPlan).length() == 0 ) {
      item.imageView.invalidate();
    }
    else {
      Bitmap bMap = BitmapFactory.decodeFile("/sdcard/androc/symbols/"+item.getImageName(item.ModPlan)+".png");
      if( bMap != null ) {
        item.imageView.setImageBitmap(bMap);
      }
      else {
        int resId = item.imageView.getContext().getResources().getIdentifier(item.getImageName(item.ModPlan), "raw", "net.rocrail.androc");
        if( resId != 0 ) {
          item.imageView.setImageResource(resId);
          if( item.Text != null && item.Text.length() > 0 ) {
            // update text
            item.imageView.invalidate();
          }
        }
      }
    }
  }
  
}
