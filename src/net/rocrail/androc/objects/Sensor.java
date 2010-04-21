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

import android.view.View;

public class Sensor extends Item implements View.OnClickListener {
  Boolean Curve = false;

  public Sensor(RocrailService rocrailService, Attributes atts) {
    super(atts);
    m_RocrailService = rocrailService;
    Curve = Item.getAttrValue(atts, "curve", false );
  }
  
  
  public String getImageName() {
    int orinr = getOriNr();
    String prefix = "";

    if (Curve)
      prefix = "c";
    else
      orinr = (orinr % 2 == 0) ? 2 : 1;

    if (State.equals("true")) {
      ImageName = String.format("%ssensor_on_%d", prefix, orinr);
    } else {
      ImageName = String.format("%ssensor_off_%d", prefix, orinr);
    }

    return ImageName;
  }

  public void onClick(View v) {
    m_RocrailService.sendMessage("fb", String.format( "<fb id=\"%s\" state=\"%s\"/>", ID, State.equals("true")?"false":"true" ) );
  }


}
