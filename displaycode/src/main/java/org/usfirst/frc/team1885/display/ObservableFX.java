package org.usfirst.frc.team1885.display;

import java.util.HashSet;
import java.util.Set;

import javafx.application.Platform;

public class ObservableFX <T> {
  private T mValue = null;
  private final Set<IUpdate<T>> mListeners = new HashSet<>();
  
  public ObservableFX(T pValue) {
    mValue = pValue;
  }
  
  public ObservableFX() {
    
  }
  
  public void setValue(T pValue) {
    mValue = pValue;
    
    // Runs on the FX UI update thread
    Platform.runLater(() -> mListeners.forEach(listener -> listener.update(mValue)));
  }
  
  public T getValue() {
    return mValue;
  }
  
  public void addListener(IUpdate<T> pListener) {
    mListeners.add(pListener);
  }
  
  public void removeListener(IUpdate<T> pListener) {
    mListeners.remove(pListener);
  }
}
