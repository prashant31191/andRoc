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

public class Item {
  RocrailService m_RocrailService = null;

  public Attributes Properties = null;
  public String ID = "?";
  public String Ori = "west";
  public int X = 0;
  public int Y = 0;
  public int Z = 0;

  public int cX = 1;
  public int cY = 1;
  public boolean Show = true;
  public String Type = "";
  public String State = "";
  public String Text = "";
  public boolean textVertical = false;
  public String ImageName = "";
  
  public Item(Attributes atts) {
    Properties = atts;
    updateWithAttributes(atts);
    ID   = getAttrValue(atts, "id", "?"); 
    X    = getAttrValue(atts, "x", 0); 
    Y    = getAttrValue(atts, "y", 0); 
    Z    = getAttrValue(atts, "z", 0); 
    cX   = getAttrValue(atts, "cx", 1); 
    cY   = getAttrValue(atts, "cy", 1); 
    Show = getAttrValue(atts, "show", true);
    
    if( cX < 1 ) cX = 1;
    if( cY < 1 ) cY = 1;
      
  }
  
  public String getImgName(){
    return ImageName;
  }
  
  public static String getAttrValue(Attributes atts, String key, String defval) {
    if( atts.getValue(key) == null )
      return defval;
    return atts.getValue(key);
  }
  
  public static int getAttrValue(Attributes atts, String key, int defval) {
    if( atts.getValue(key) == null )
      return defval;
    return Integer.parseInt(atts.getValue(key));
  }
  
  public static boolean getAttrValue(Attributes atts, String key, boolean defval) {
    if( atts.getValue(key) == null )
      return defval;
    return Boolean.parseBoolean(atts.getValue(key));
  }
  
  public void updateWithAttributes(Attributes atts ) {
    Ori   = getAttrValue(atts, "ori", Ori); 
    Type  = getAttrValue(atts, "type", Type); 
    State = getAttrValue(atts, "state", State); 
  }


  
  public int getOriNr() {
    if(Ori.equals("north"))
      return 2;
    if(Ori.equals("east"))
      return 3;
    if(Ori.equals("south"))
      return 4;
    return 1;
  }

}
