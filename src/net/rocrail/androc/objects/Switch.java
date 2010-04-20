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

import org.xml.sax.Attributes;

import android.content.Intent;
import android.view.View;

import net.rocrail.androc.RocrailService;
import net.rocrail.androc.interfaces.LayoutItem;

public class Switch implements LayoutItem, View.OnClickListener {
  RocrailService m_RocrailService = null;
  public String ID = "?";
  public Attributes properties = null;
  public int X = 0;
  public int Y = 0;
  public int Z = 0;
  

  public Switch( RocrailService rocrailService, String id, Attributes atts) {
    m_RocrailService = rocrailService;
    ID = id;
    properties = atts;
    String sX = atts.getValue("x");
    if( sX != null ) X = Integer.parseInt(sX);
    String sY = atts.getValue("y");
    if( sY != null ) Y = Integer.parseInt(sY);
    String sZ = atts.getValue("z");
    if( sZ != null ) Z = Integer.parseInt(sZ);
  }

  public void onClick(View v) {
    m_RocrailService.sendMessage("sw", String.format( "<sw id=\"%s\" cmd=\"flip\"/>", ID ) );
  }


  @Override
  public int getX() {
    return X;
  }

  @Override
  public int getY() {
    return Y;
  }

  @Override
  public int getZ() {
    return Z;
  }

  @Override
  public String getID() {
    return ID;
  }

}
