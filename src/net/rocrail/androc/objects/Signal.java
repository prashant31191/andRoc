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

public class Signal extends Item implements View.OnClickListener {
  int Aspects = 3;
  String Signal = "main";
  boolean Distant = false;

  public Signal(RocrailService rocrailService, Attributes atts) {
    super(rocrailService, atts);
    Aspects = Item.getAttrValue(atts, "aspects", Aspects );
    Signal = Item.getAttrValue(atts, "signal", Signal );
    Distant = Signal.equals("distant");
  }

  public void updateWithAttributes(Attributes atts ) {
    Aspects = Item.getAttrValue(atts, "aspects", Aspects );
    Signal = Item.getAttrValue(atts, "signal", Signal );
    Distant = Signal.equals("distant");
    super.updateWithAttributes(atts);
  }
  
  public String getImageName(boolean ModPlan) {
    this.ModPlan = ModPlan;
    int orinr = getOriNr(ModPlan);
    if (orinr == 1)
      orinr = 3;
    else if (orinr == 3)
      orinr = 1;
    
    if(Aspects < 2)
      Aspects = 2;
    if(Aspects > 3)
      Aspects = 3;
    
    if(Distant) {
      if (State.equals("red"))
        ImageName = String.format("signaldistant_r_%d", orinr);
      else if (State.equals("green"))
        ImageName = String.format("signaldistant_g_%d", orinr);
      else if (State.equals("yellow"))
        ImageName = String.format("signaldistant_y_%d", orinr);
      else
        ImageName = String.format("signaldistant_w_%d", orinr);
    }
    else {
      if (State.equals("red"))
        ImageName = String.format("signal%d_r_%d", Aspects, orinr);
      else if (State.equals("green"))
        ImageName = String.format("signal%d_g_%d", Aspects, orinr);
      else if (State.equals("yellow"))
        ImageName = String.format("signal%d_y_%d", Aspects, orinr);
      else
        ImageName = String.format("signal%d_w_%d", Aspects, orinr);
    }
    
    return ImageName;
  }  

  public void onClick(View v) {
    m_RocrailService.sendMessage("sg", String.format( "<sg id=\"%s\" cmd=\"flip\"/>", ID ) );
  }
}
