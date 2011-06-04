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




public class Track extends Item {

  public Track(RocrailService rocrailService, Attributes atts) {
    super(rocrailService, atts);
    // TODO Auto-generated constructor stub
  }
  
  
  public String getImageName(boolean ModPlan) {
    this.ModPlan = ModPlan;
    int orinr = getOriNr(ModPlan);

    if (Type.equals("curve")) {
      ImageName = String.format("curve_%d", orinr);
    } 
    else if (Type.equals("buffer") || Type.equals("connector") || Type.equals("dir") ) {
      // symbol naming fix (see rocrail/impl/pclient.c line 250)
      if (orinr == 1)
        orinr = 3;
      else if (orinr == 3)
        orinr = 1;
      ImageName = String.format("%s_%d", Type, orinr);
    } 
    else if( Type.equals("dirall") ) {
      ImageName = String.format("%s_%d", Type, (orinr % 2 == 0 ? 2 : 1));
    } 
    else {
      ImageName = String.format("track_%d", (orinr % 2 == 0 ? 2 : 1));
    }

    return ImageName;
  }

}
