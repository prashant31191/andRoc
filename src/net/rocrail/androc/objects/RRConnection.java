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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;


public class RRConnection {
  public String Title    = "";
  public String HostName = "";
  public int    Port     = 0;
  public String ControlCode = "";
  
  public RRConnection(String Title, String HostName, int Port, String ControlCode) {
    this.Title    = Title;
    this.HostName = HostName;
    this.Port     = Port;
    this.ControlCode = ControlCode;
  }
  public boolean equals( RRConnection con ) {
    if( con.HostName.equals(this.HostName) && con.Port == this.Port )
      return true;
    return false;
  }
  public String toString() {
    return Title+":"+HostName+":"+Port+":"+ControlCode;
  }

  /*
   * Add a connection to the list.
   * An existing connection with the same key will be removed first. 
   */
  public static void addToList(RRConnection con, List<RRConnection> conList) {
    for (Iterator<RRConnection> it = conList.iterator(); it.hasNext();) {
      RRConnection c = it.next();
      if( c.equals(con) ) {
        conList.remove(c);
        break;
      }
    }
    conList.add(0,con);
  }

  public static void addToList(String alias, String host, int port, String ctrlcode, List<RRConnection> conList) {
    RRConnection con = new RRConnection(alias, host, port, ctrlcode);
    addToList(con, conList);
  }

  public static List<RRConnection> parse(String recent ) {
    List<RRConnection> conList = new ArrayList<RRConnection>();
    // parse the recent string and instantiate a class for each
    try {
      StringTokenizer tok = new StringTokenizer( recent, ";");
      while( tok.hasMoreTokens()) {
        String s = tok.nextToken();
        StringTokenizer constr = new StringTokenizer( s, ":");
        RRConnection con = null;
        if( constr.countTokens() == 4)
          con = new RRConnection(constr.nextToken(), constr.nextToken(), Integer.parseInt(constr.nextToken()), constr.nextToken() );
        else if( constr.countTokens() == 3)
          con = new RRConnection(constr.nextToken(), constr.nextToken(), Integer.parseInt(constr.nextToken()), "");
        else if( constr.countTokens() == 2)
          con = new RRConnection("", constr.nextToken(), Integer.parseInt(constr.nextToken()), "");
        else
          continue;

        addToList(con, conList);
      }
    }
    catch( Exception e ) {
      e.printStackTrace();
    }
    return conList;
  }
    
  public static String serialize(List<RRConnection> conList ) {
    // construct the recent string new
    String recent = "";
    for (Iterator<RRConnection> it = conList.iterator(); it.hasNext();) {
      RRConnection con = it.next();
      recent = recent.concat(con.toString()+";");
    }
    return recent;
  }
    

}
