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

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Try to get all available Rocrail server client connections by the UDP RRNet protocol.
 * @author rob
 *
 */
public class R2RNet extends Thread {
  Preferences Prefs = null;
  String Host = "";
  int Port = 0;
  public boolean Run = true;

  
  public R2RNet( Preferences Prefs ) {
    this.Prefs = Prefs;  
  }
  
  public void set(String Host, int Port) {
    this.Host = Host;
    this.Port = Port;
    start();
  }
  
  @Override
  public void run() {
    try {
      byte[] buf = null;
      String msg = "<NetReq req=\"clientconn\"/>";
      buf = msg.getBytes("UTF-8");
      
      MulticastSocket socket = new MulticastSocket(Port);
      InetAddress group = InetAddress.getByName(Host);
      socket.joinGroup(group);

      
      DatagramPacket packet = new DatagramPacket(buf, buf.length, group, Port);
      socket.send(packet);
      
      int rrcnt = 0;
      do {
        buf = new byte[4096];
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String clientConn = new String(packet.getData(), 0, packet.getLength());
        // Inform the recent list...
        if( clientConn.contains("rsp=\"clientconn\"") ) {
          // format: <NetReq title="Plan-F" rsp="clientconn" host="192.168.100.65" port="4711"/>
          int idx = clientConn.indexOf("host");
          String host = clientConn.substring(idx+"host".length()+2);
          idx = host.indexOf('"');
          Prefs.Host = host.substring(0, idx);

          idx = clientConn.indexOf("port");
          String port = clientConn.substring(idx+"port".length()+2);
          idx = port.indexOf('"');
          Prefs.Port = Integer.parseInt(port.substring(0, idx));

          rrcnt++;
        }
        Thread.sleep(10);
      } while(Run && rrcnt == 0 );
     
      socket.leaveGroup(group);
      socket.close();

    }
    catch( Exception e ) {
      e.printStackTrace(); 
    }
  }
  
}
