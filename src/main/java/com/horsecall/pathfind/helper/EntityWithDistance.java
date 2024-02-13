package com.horsecall.pathfind.helper;

import net.minecraft.entity.Entity;

/**
 * This class provides utility methods for mathematical operations involving entity distances
 */
public class EntityWithDistance {
    private final Entity entity;
    private final double distance;

    public EntityWithDistance (Entity entity, double distance){
        this.entity = entity;
        this.distance = distance;
    }

    /**
     * Get distance of the entity to another entity
     * @return The eucledian distance stored on {@code distance}
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Get an entity
     * @return The entity {@code entitya}
     */
    public Entity getEntity() {
        return entity;
    }
}
