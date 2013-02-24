package net.rocrail.androc.interfaces;

import net.rocrail.androc.widgets.LocoImage;

import org.xml.sax.Attributes;

import android.graphics.Bitmap;

public interface Mobile extends ItemBase {
  public String getID();
  public Bitmap getFunctionIcon(int fn);
  public String getFunctionText(int fn);
  public boolean isFunction(int fn);
  public boolean isAutoStart();
  public void setAutoStart(boolean autostart);
  public boolean isLights();
  public int getAddr();
  public int getSpeed();
  public int getVMax();
  public int getSteps();
  public boolean isDir();
  public boolean isShow();
  public void updateFunctions(Attributes atts);
  public void flipLights();
  public void flipFunction(int fn);
  public void flipGo();
  public void flipDir();
  public void doRelease();
  public String getConsist();
  public Bitmap getBmp(LocoImage image);
  public String getDescription();
  public void setSpeed(int V, boolean force);
  public void Dispatch();
  public void addFunction(Attributes atts );
  public long getRunTime();
  public String getRoadname();
  public boolean isHalfAuto();
  public void setHalfAuto(boolean halfauto);
  public boolean isPlacing();
  public void setPlacing(boolean placing);
  public void swap();
  public LocoImage getImageView();
  public void setPicData(String filename, String data, int nr);
}
