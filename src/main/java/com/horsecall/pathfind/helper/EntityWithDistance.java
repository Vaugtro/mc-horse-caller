package com.horsecall.pathfind.helper;

import net.minecraft.entity.Entity;

import java.util.UUID;

/**
 * This class provides utility methods for mathematical operations involving entity distances
 */

// TODO: Replace entity to UUID
// TODO: add page short int
// TODO: add png path name
public class EntityWithDistance {
    private final UUID entityUUID;
    private final double distance;
    private short page;

    public EntityWithDistance (UUID entityUUID, double distance){
        this.entityUUID = entityUUID;
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
     * Get an entity UUID
     * @return The entity UUID {@code entityUUID}
     */
    public UUID getEntityUUID() {
        return entityUUID;
    }

    /**
     * Get the sorted page
     * @return The sorted page {@code page}
     */
    public short getPage() {
        return page;
    }

    /**
     * Set the sorted page
     * @param page The page where the entity is
     */
    public void setPage(short page){
        this.page = page;
    }
}
