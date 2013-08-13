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

import net.rocrail.androc.R;
import net.rocrail.androc.RocrailService;
import net.rocrail.androc.interfaces.UpdateListener;

import org.xml.sax.Attributes;


import android.view.View;
import android.widget.Toast;


public class Switch extends Item implements View.OnClickListener {
  boolean Dir = false;
  boolean RectCrossing = true;
  int AccNr = 0;
  String SWType = "";
  boolean Raster = false;

  public Switch( RocrailService rocrailService, Attributes atts) {
    super(rocrailService, atts);
    Dir   = Item.getAttrValue(atts, "dir", false );
    RectCrossing = Item.getAttrValue(atts, "rectcrossing", true );
    AccNr = Item.getAttrValue(atts, "accnr", 1 );
    SWType = Item.getAttrValue(atts, "swtype", "default" );
    Raster = SWType.equals("raster");
  }

  public void onClick(View v) {
    flip();
  }

  
  public void flip() {
    m_RocrailService.sendMessage("sw", String.format( "<sw id=\"%s\" cmd=\"flip\"/>", ID ) );
  }

  
  public String getImageName(boolean ModPlan) {
    this.ModPlan = ModPlan;
    
    System.out.println("sw "+this.ID+" ModPlan="+ModPlan+ " ori="+this.Ori + " modori="+this.Mod_Ori);
    
    int orinr = getOriNr(ModPlan);
    String rasterStr = (Raster?"_r":"");
    
    if (orinr == 1)
      orinr = 3;
    else if (orinr == 3)
      orinr = 1;
    
    if( BlockID.length() > 0 ) {
      Block bk = m_RocrailService.m_Model.m_BlockMap.get(BlockID);
      if( bk != null)
        Occupied = bk.isOccupied();
      else {
        Sensor fb = m_RocrailService.m_Model.m_SensorMap.get(BlockID);
        if( fb != null)
          Occupied = fb.State.equals("true");
      }
    }

    String suffix = "";
    if( Occupied )
      suffix = "_occ";
    
    if (Type.equals("accessory")) {
      if (getOriNr(ModPlan) % 2 == 0)
        orinr = 2;
      else
        orinr = 1;

      switch (AccNr) {
      case 1:
        cX = (orinr == 1 ? 1 : 2);
        cY = (orinr == 1 ? 2 : 1);
        break;
      case 40:
        cX = (orinr == 1 ? 4 : 2);
        cY = (orinr == 1 ? 2 : 4);
        break;
      case 51:
        cX = (orinr == 1 ? 4 : 2);
        cY = (orinr == 1 ? 2 : 4);
        break;
      case 52:
        cX = (orinr == 1 ? 4 : 1);
        cY = (orinr == 1 ? 1 : 4);
        break;
      case 53:
        cX = 2;
        cY = 2;
        break;
      case 54:
        cX = (orinr == 1 ? 3 : 2);
        cY = (orinr == 1 ? 2 : 3);
        break;
      }

      if (State.equals("turnout"))
        ImageName = String.format("accessory_%d_off_%d", AccNr, orinr);
      else
        ImageName = String.format("accessory_%d_on_%d", AccNr, orinr);
    }
    else if (Type.equals("right")) {
      if (State.equals("straight"))
        ImageName = String.format("turnout%s_rs%s_%d", rasterStr, suffix, orinr);
      else
        ImageName = String.format("turnout%s_rt%s_%d", rasterStr, suffix, orinr);

    }
    else if (Type.equals("left")) {
      if (State.equals("straight"))
        ImageName = String.format("turnout%s_ls%s_%d", rasterStr, suffix, orinr);
      else
        ImageName = String.format("turnout%s_lt%s_%d", rasterStr, suffix, orinr);

    }
    else if (Type.equals("threeway")) {
      if (State.equals("straight"))
        ImageName = String.format("threeway_s%s_%d", suffix, orinr);
      else if (State.equals("left"))
        ImageName = String.format("threeway_l%s_%d", suffix, orinr);
      else
        ImageName = String.format("threeway_r%s_%d", suffix, orinr);

    }
    else if (Type.equals("twoway")) {
      if (State.equals("straight"))
        ImageName = String.format("twoway_tr%s_%d", suffix, orinr);
      else
        ImageName = String.format("twoway_tl%s_%d", suffix, orinr);

    }
    else if (Type.equals("dcrossing")) {
      char st = 's';

      if (State.equals("straight"))
        st = 's';
      else if (State.equals("turnout"))
        st = 't';
      else if (State.equals("left"))
        st = 'l';
      else if (State.equals("right"))
        st = 'r';

      ImageName = String.format("dcrossing%s_%c%s_%d", (Dir ? "left" : "right"), st, suffix, orinr);

      cX = orinr % 2 == 0 ? 1 : 2;
      cY = orinr % 2 == 0 ? 2 : 1;
    }
    else if (Type.equals("crossing")) {
      if( RectCrossing ) {
		    ImageName = "cross";
      }
      else {
		    char st = 's';

		    if (State.equals("straight"))
		      st = 's';
		    else if (State.equals("turnout"))
		      st = 't';

		    ImageName = String.format("crossing%s_%c%s_%d", (Dir ? "left" : "right"), st, suffix, orinr);

		    cX = orinr % 2 == 0 ? 1 : 2;
		    cY = orinr % 2 == 0 ? 2 : 1;
      }
    }
    else if (Type.equals("ccrossing")) {
      ImageName = String.format("ccrossing%s_%d", suffix, (orinr % 2 == 0 ? 2 : 1));
      cX = orinr % 2 == 0 ? 1 : 2;
      cY = orinr % 2 == 0 ? 2 : 1;
    }
    else if (Type.equals("decoupler")) {
      String st = "off";
      if( suffix.length() == 0 && RouteLocked )
        suffix = "_route";

      if (State.equals("straight"))
        st = "on";
      ImageName = String.format("decoupler_%s%s_%d", st, suffix, (orinr % 2 == 0 ? 2 : 1));
    }

    System.out.println("switch type=" + Type + " img="+ImageName);


    return ImageName;

  }

  @Override
  public void propertiesView() {
    Toast.makeText(super.m_RocrailService.getApplicationContext(), R.string.Unlock,
        Toast.LENGTH_SHORT).show();
    m_RocrailService.sendMessage("sw", String.format( "<sw id=\"%s\" cmd=\"unlock\"/>", ID ) );
  }

}
