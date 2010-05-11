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

import net.rocrail.androc.Preferences;
import net.rocrail.androc.R;
import net.rocrail.androc.widgets.LEDButton;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ActAccessory extends ActBase {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MenuSelection = ActBase.MENU_THROTTLE | ActBase.MENU_MENU | ActBase.MENU_LAYOUT | ActBase.MENU_PREFERENCES | ActBase.MENU_ACCESSORY;
    connectWithService();
  }
  
  public void connectedWithService() {
    super.connectedWithService();
    initView();
    updateTitle(getText(R.string.Accessory).toString());
  }
 
  public void initView() {
    setContentView(R.layout.accessory);
    
    EditText et = (EditText)findViewById(R.id.accAddress);
    et.setText(""+m_RocrailService.Prefs.AccNr);
    
    Button bt = (Button) findViewById(R.id.accAddressing);
    bt.setText(m_RocrailService.Prefs.AccType);

    updateAddress();
    
    bt.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        Button btType = (Button)v;
        String Type = btType.getText().toString();
        if( Type.equals(Preferences.ACCTYPE_NMRA))
          btType.setText(Preferences.ACCTYPE_FADA);
        else if( Type.equals(Preferences.ACCTYPE_FADA))
          btType.setText(Preferences.ACCTYPE_PADA);
        else if( Type.equals(Preferences.ACCTYPE_PADA))
          btType.setText(Preferences.ACCTYPE_NMRA);
        updateAddress();
      }
    });
    
    
    bt = (Button) findViewById(R.id.accM);
    bt.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        EditText et = (EditText)findViewById(R.id.accAddress);
        int addr = Integer.parseInt(et.getText().toString());
        if( addr > 1 ) {
          addr--;
          et.setText(""+addr);
          updateAddress();
        }
      }
    });

    bt = (Button) findViewById(R.id.accMM);
    bt.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        EditText et = (EditText)findViewById(R.id.accAddress);
        int addr = Integer.parseInt(et.getText().toString());
        if( addr > 1 ) {
          addr -= getGroupSize();
          if( addr < 1 )
            addr = 1;
          et.setText(""+addr);
          updateAddress();
        }
      }
    });

    bt = (Button) findViewById(R.id.accP);
    bt.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        EditText et = (EditText)findViewById(R.id.accAddress);
        int addr = Integer.parseInt(et.getText().toString());
        if( addr < 64*1024 ) {
          addr++;
          et.setText(""+addr);
          updateAddress();
        }
      }
    });

    bt = (Button) findViewById(R.id.accPP);
    bt.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        EditText et = (EditText)findViewById(R.id.accAddress);
        int addr = Integer.parseInt(et.getText().toString());
        if( addr < 64*1024 ) {
          addr += getGroupSize();
          if( addr > 64*1024 )
            addr = 64*1024;
          et.setText(""+addr);
          updateAddress();
        }
      }
    });
    
    bt = (Button)findViewById(R.id.acc1G);
    bt.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        switchCmd(0,0);
      }
    });
    bt = (Button)findViewById(R.id.acc1R);
    bt.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        switchCmd(0,1);
      }
    });

    bt = (Button)findViewById(R.id.acc2G);
    bt.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        switchCmd(1,0);
      }
    });
    bt = (Button)findViewById(R.id.acc2R);
    bt.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        switchCmd(1,1);
      }
    });

    bt = (Button)findViewById(R.id.acc3G);
    bt.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        switchCmd(2,0);
      }
    });
    bt = (Button)findViewById(R.id.acc3R);
    bt.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        switchCmd(2,1);
      }
    });

    bt = (Button)findViewById(R.id.acc4G);
    bt.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        switchCmd(3,0);
      }
    });
    bt = (Button)findViewById(R.id.acc4R);
    bt.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        switchCmd(3,1);
      }
    });

    
  }
  
  
  void switchCmd(int row, int col ) {
    // send the command
    Button bt = (Button)findViewById(R.id.accAddressing);
    String type = bt.getText().toString();
    
    EditText et = (EditText)findViewById(R.id.accAddress);
    int addr = Integer.parseInt(et.getText().toString());
    int port = row + 1;

    if( type.equals(Preferences.ACCTYPE_NMRA)) {
      port = row + 1;
    }
    else if( type.equals(Preferences.ACCTYPE_FADA)) {
      addr = addr + row * 2 + col;
      port = 0;
    }
    else if( type.equals(Preferences.ACCTYPE_PADA)) {
      port = addr + row * 2;
      addr = 0;
    }
    
    String cmd = col == 0 ? "turnout":"straight";
    m_RocrailService.sendMessage("sw", 
        String.format("<sw cmd=\"%s\" addr1=\"%d\" port1=\"%d\"/>", cmd, addr, port));
  }
  
  
  int getGroupSize() {
    Button bt = (Button)findViewById(R.id.accAddressing);
    String type = bt.getText().toString();
    if( type.equals(Preferences.ACCTYPE_NMRA)) {
     return 4; 
    }
    else if( type.equals(Preferences.ACCTYPE_FADA)) {
      return 8; 
    }
    else if( type.equals(Preferences.ACCTYPE_PADA)) {
      return 4; 
    }
    return 4;
  }
  
  int makeButtonAddr(String type, int addr, int row, int col) {
    int btAddr = 0;
    if( type.equals(Preferences.ACCTYPE_NMRA)) {
      btAddr = row + 1; 
    }
    else if( type.equals(Preferences.ACCTYPE_FADA)) {
      btAddr = addr + row * 2 + col; 
    }
    else if( type.equals(Preferences.ACCTYPE_PADA)) {
      btAddr = addr + row * 2; 
    }
    return btAddr;
  }
  
  void updateAddress() {
    EditText et = (EditText)findViewById(R.id.accAddress);
    int addr = Integer.parseInt(et.getText().toString());
    Button bt = (Button)findViewById(R.id.accAddressing);
    String type = bt.getText().toString();
    
    bt = (Button)findViewById(R.id.acc1G);
    bt.setText(""+makeButtonAddr(type, addr, 0, 0)+"G");
    bt = (Button)findViewById(R.id.acc1R);
    bt.setText(""+makeButtonAddr(type, addr, 0, 1)+"R");
    
    bt = (Button)findViewById(R.id.acc2G);
    bt.setText(""+makeButtonAddr(type, addr, 1, 0)+"G");
    bt = (Button)findViewById(R.id.acc2R);
    bt.setText(""+makeButtonAddr(type, addr, 1, 1)+"R");
    
    bt = (Button)findViewById(R.id.acc3G);
    bt.setText(""+makeButtonAddr(type, addr, 2, 0)+"G");
    bt = (Button)findViewById(R.id.acc3R);
    bt.setText(""+makeButtonAddr(type, addr, 2, 1)+"R");
    
    bt = (Button)findViewById(R.id.acc4G);
    bt.setText(""+makeButtonAddr(type, addr, 3, 0)+"G");
    bt = (Button)findViewById(R.id.acc4R);
    bt.setText(""+makeButtonAddr(type, addr, 3, 1)+"R");
  }
  
  
  @Override
  protected void onPause() {
    super.onPause();
    EditText et = (EditText)findViewById(R.id.accAddress);
    Button bt = (Button)findViewById(R.id.accAddressing);

    m_RocrailService.Prefs.saveAccessory(bt.getText().toString(), Integer.parseInt(et.getText().toString()));
  }


}
