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

import java.util.Iterator;

import net.rocrail.androc.R;
import net.rocrail.androc.objects.Item;
import net.rocrail.androc.objects.Switch;
import net.rocrail.androc.objects.ZLevel;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.AbsoluteLayout.LayoutParams;

@SuppressWarnings("deprecation")
public class Level extends Base {

  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MenuSelection = 0;
    connectWithService();
  }
  
  public void connectedWithService() {
    initView();
  }

  public void initView() {
    setContentView(R.layout.level);
    
    int Z = 0;

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      int level = extras.getInt("level", 0);
      ZLevel zlevel = m_RocrailService.m_Model.m_ZLevelList.get(level);
      setTitle(zlevel.Title);
      Z = zlevel.Z;
    }

    /*
      The AbsoluteLayout is needed for drawing the Layout levels.
      The API is flagged as deprecated but, as it seems, with another meaning than 'obsolete':
      
      “I'll say again: we are not going to remove AbsoluteLayout from a future
      release, but we strongly discourage people from using it.”
      Dianne Hackborn
      Android framework engineer 
     */
    AbsoluteLayout levelView = (AbsoluteLayout)findViewById(R.id.levelView);
    ScrollView scrollView = (ScrollView)findViewById(R.id.levelScrollView);
    
    Iterator<Item> itemIt = m_RocrailService.m_Model.m_ItemList.iterator();
    while( itemIt.hasNext() ) {
      Item item = itemIt.next();
      if( item.Z == Z && item.Show ) {
        ImageView image = new ImageView(this);
        String imgname = item.getImageName();
        int resId = getResources().getIdentifier(imgname, "raw", "net.rocrail.androc");
        if( resId != 0 ) {
          image.setImageResource(resId);
          image.setOnClickListener(item);
          item.imageView = image;
          LayoutParams lp = new LayoutParams(item.cX*32, item.cY*32, item.X*32, item.Y*32);
          levelView.addView(image, lp);
        }
      }
      
    }
    
    scrollView.requestLayout();

  }

}
