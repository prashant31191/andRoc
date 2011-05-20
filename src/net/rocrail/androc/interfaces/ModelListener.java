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
package net.rocrail.androc.interfaces;

public interface ModelListener {
  public static int MODELLIST_PLAN_START = 100;
  public static int MODELLIST_PLAN = 0;
  public static int MODELLIST_LC = 1;
  public static int MODELLIST_TK = 2;
  public static int MODELLIST_BK = 3;
  public static int MODELLIST_FB = 4;
  public static int MODELLIST_ST = 5;
  public static int MODELLIST_SC = 6;
  public static int MODELLIST_SW = 7;
  public static int MODELLIST_SG = 8;
  public static int MODELLIST_CO = 9;
  public static int MODELLIST_TX = 10;
  public void modelListLoaded(int MODELLIST);
  public void modelUpdate(int MODELLIST, String ID);
}
