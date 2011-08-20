package net.rocrail.androc.activities;

import java.util.Comparator;

import net.rocrail.androc.objects.Loco;

public class LocoSort implements Comparator<Loco>{

  @Override
  public int compare(Loco loco1, Loco loco2) {
    return loco1.ID.toLowerCase().compareTo(loco2.ID.toLowerCase());
  }
 }
