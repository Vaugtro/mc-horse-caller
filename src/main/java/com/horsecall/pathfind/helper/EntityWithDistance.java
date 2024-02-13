package com.horsecall.pathfind.helper;

import net.minecraft.entity.Entity;

import java.io.*;
import java.util.UUID;

/**
 * This class provides utility methods for mathematical operations involving entity distances
 */

// TODO: Replace entity to UUID
// TODO: add page short int
// TODO: add png path name
public class EntityWithDistance implements Serializable {
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

    public class Serializer {
        public static byte[] serialize(EntityWithDistance obj){

            byte[] byteArray = null;

            try {
                ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bOut);
                out.writeObject(obj);
                out.flush();
                byteArray = bOut.toByteArray();
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return byteArray;
        }

        public static EntityWithDistance desserialize(byte[] byteArray){
            EntityWithDistance obj = null;
            try {
                ByteArrayInputStream bIn = new ByteArrayInputStream(byteArray);
                ObjectInputStream in = new ObjectInputStream(bIn);
                obj = (EntityWithDistance) in.readObject();

                in.close();
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }

            return obj;
        }
    }

}
