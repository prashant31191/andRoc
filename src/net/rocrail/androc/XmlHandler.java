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

import net.rocrail.androc.objects.Loco;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

class XmlHandler extends DefaultHandler {
  RocrailService rocrailService = null;
  int m_iXmlSize = 0;
  Model m_Model = null;
  boolean m_bParsingPlan = false;
  
  public XmlHandler(RocrailService rocrailService, Model model) {
    this.rocrailService = rocrailService;
    m_Model = model;
  }
  
  public int getXmlSize() {
    int size = m_iXmlSize;
    // reset size: read once
    m_iXmlSize = 0;
    return size;
  }
  
  
  public void startDocument () {
  
  }
  
  public void endDocument () {
    
  }

  public void startElement (String uri, String localName, String qName, Attributes atts) {
    if( localName.equals("xmlh") ) {
      // xmlh handling
      return;
    }
    
    if( localName.equals("xml") ) {
      // xml handling
      String val = atts.getValue("size");
      m_iXmlSize = Integer.parseInt(val);
      return;
    }
    
    if( localName.equals("datareq") ) {
      // loco handling
      String id = atts.getValue("id");
      Loco loco = m_Model.getLoco(id);
      if( loco != null ) {
        String data = atts.getValue("data");
        loco.setPicData(data);
      }
      return;
    }
    
    if(!m_bParsingPlan && localName.equals("plan")) {
      m_bParsingPlan = true;
      m_Model.setup(atts);
      return;
    }
    
    if( m_bParsingPlan ) {
      m_Model.addObject(localName, atts);
      return;
    }
    else {
      if( localName.equals("state") || localName.equals("auto") || 
          localName.equals("sys") || localName.equals("exception") || localName.equals("program") ) {
        rocrailService.event(localName, atts);
      }
      else {
        m_Model.updateItem(localName, atts);
      }
      
    }
    
  }

  public void endElement (String uri, String localName, String qName) {
    if( localName.equals("plan") ) {
      m_bParsingPlan = false;
      m_Model.planLoaded();
    }
    else if( m_bParsingPlan ) {
      // signal end of list
      m_Model.listLoaded(localName);
    }
  }
}
