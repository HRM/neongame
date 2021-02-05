package Game.EngieParts.UiEngine.ListLayout;

import Game.EngieParts.UiEngine.Positionable;

public interface UIList {
    void addElement(Positionable p);
    void removeElement(Positionable p);
    void removeFirst();
    void removeLast();
}
