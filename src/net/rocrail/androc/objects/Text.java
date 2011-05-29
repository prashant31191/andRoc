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
import android.view.View;

public class Text extends Item implements View.OnClickListener {
  int m_cX = 0;
  int m_cY = 0;

  public Text(RocrailService rocrailService, Attributes atts) {
    super(rocrailService, atts);
    Text = Item.getAttrValue(atts, "text", Text );
    cX = Item.getAttrValue(atts, "cx", 3 );
    m_cX = cX;
    m_cY = cY;
  }
  
  public void updateTextColor() {
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
    return null;
  }

  public void updateWithAttributes(Attributes atts ) {
    Text = Item.getAttrValue(atts, "text", Text); 
    updateTextColor();
    super.updateWithAttributes(atts);
  }


}
