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

import java.util.List;

public class ZLevel {
  public String Title = "";
  public int Z = 0;
  public int X = 0;
  public int Y = 0;
  public int cX = 0;
  public int cY = 0;
  public int progressIdx = 0;
  public String ModID = "";
  public List<Item> itemList = null;
  public ZLevel( String title, int z ) {
    Title = title;
    Z = z;
  }
}
