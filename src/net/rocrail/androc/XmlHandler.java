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

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

class XmlHandler extends DefaultHandler {
  int m_iXmlSize = 0;
  
  public int getXmlSize() {
    return m_iXmlSize;
  }
  
  
  public void startDocument () {
  
  }
  
  public void endDocument () {
    
  }

  public void startElement (String uri, String localName, String qName, Attributes atts) {
    if( localName.equals("xmlh") ) {
      // xmlh handling
    }
    else if( localName.equals("xml") ) {
      // xml handling
      String val = atts.getValue("size");
      m_iXmlSize = Integer.parseInt(val);
    }
  }

  public void endElement (String uri, String localName, String qName) {
    
  }
}
