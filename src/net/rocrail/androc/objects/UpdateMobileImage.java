package net.rocrail.androc.objects;

import net.rocrail.androc.interfaces.Mobile;

public class UpdateMobileImage implements Runnable {
  Mobile mobile = null;
  
  public UpdateMobileImage( Mobile mobile ) {
    this.mobile = mobile;
  }
  @Override
  public void run() {
    if( mobile.getBmp(null) != null && mobile.getImageView() != null ) {
      try {
        if( mobile.getID().equals(mobile.getImageView().ID))
          mobile.getImageView().setImageBitmap(mobile.getBmp(null));
      }
      catch( Exception e ) {
        // invalid imageView 
      }
    }
  }
  
}
