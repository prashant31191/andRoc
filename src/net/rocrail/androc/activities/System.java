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
package net.rocrail.androc.activities;

import net.rocrail.androc.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class System extends Base {
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MenuSelection = Base.MENU_THROTTLE | Base.MENU_MENU | Base.MENU_LAYOUT;
    connectWithService();
  }
  
  public void connectedWithService() {
    initView();
    updateTitle();
  }

  public void initView() {
    setContentView(R.layout.system);

    final Button powerON = (Button) findViewById(R.id.systemPowerON);
    powerON.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_RocrailService.sendMessage("sys", "<sys cmd=\"go\"/>");
        }
    });

    final Button powerOFF = (Button) findViewById(R.id.systemPowerOFF);
    powerOFF.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_RocrailService.sendMessage("sys", "<sys cmd=\"stop\"/>");
        }
    });
    
    final Button initField = (Button) findViewById(R.id.systemInitField);
    initField.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          m_RocrailService.sendMessage("model", "<model cmd=\"initfield\"/>");
        }
    });
    
  }

}
