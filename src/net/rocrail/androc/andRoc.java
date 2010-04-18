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
package net.rocrail.androc;

import android.os.Bundle;
import net.rocrail.androc.R;
import net.rocrail.androc.activities.Base;


/**
 * Application entry activity representing a splash screen.
 * @author rob
 *
 */
public class andRoc extends Base {
  
  final static int MENU_CONNECT  = 1;
  final static int MENU_THROTTLE = 2;
  final static int MENU_SYSTEM   = 3;
  final static int MENU_LAYOUT   = 4;
  final static int MENU_MENU     = 5;
  final static int MENU_QUIT     = 6;
  

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.androc);

    connectWithService();
  }
  
  public void connectedWithService() {
    restorePreferences();
    connectView();
  }

  
  public void Connected() {
  }

  public void onPause() {
    super.onPause();
    // 1 also received at orientation change
    return;
  }

  public void onStop() {
    super.onStop();
    // 2 also received at orientation change
    return;
  }

}