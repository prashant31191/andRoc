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

import net.rocrail.androc.RocrailService;

import org.xml.sax.Attributes;

public class StageBlock extends Block {

  public StageBlock(RocrailService rocrailService, Attributes atts) {
    super(rocrailService, atts);
    // TODO Auto-generated constructor stub
  }

  public String getImageName(boolean ModPlan) {
    this.ModPlan = ModPlan;
    int orinr = getOriNr(ModPlan);

    if (orinr % 2 == 0) {
      // vertical
      textVertical = true;
      cX = 1;
      cY = 4;
    } else {
      // horizontal
      cX = 4;
      cY = 1;
      textVertical = false;
    }
    ImageName = "stageblock_"+orinr;
    
    updateTextColor();

    return ImageName;
  }

  
  
  
}
