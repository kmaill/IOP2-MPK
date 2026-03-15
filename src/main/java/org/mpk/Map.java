package org.mpk;

public class Map {
    private MapRenderStrategy strategy;

    public Map(MapRenderStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(MapRenderStrategy strategy) {
        this.strategy = strategy;
    }

    public void render(MapElement element) {
        if (strategy != null) {
            strategy.render(element);
        } else {
            element.display();
        }
    }
}
