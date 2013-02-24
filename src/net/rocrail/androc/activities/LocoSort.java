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

package net.rocrail.androc.activities;

import java.util.Comparator;

import net.rocrail.androc.interfaces.Mobile;

public class LocoSort implements Comparator<Mobile>{
  boolean sortbyaddr = false;
  
  public LocoSort(boolean sortbyaddr) {
    this.sortbyaddr = sortbyaddr;
  }
  @Override
  public int compare(Mobile loco1, Mobile loco2) {
    if( sortbyaddr ) {
      if( loco1.getAddr() == loco2.getAddr() )
        return 0;
      if( loco1.getAddr() > loco2.getAddr() )
        return 1;
      return -1;
    }
    else
      return loco1.getID().toLowerCase().compareTo(loco2.getID().toLowerCase());
  }
 }
