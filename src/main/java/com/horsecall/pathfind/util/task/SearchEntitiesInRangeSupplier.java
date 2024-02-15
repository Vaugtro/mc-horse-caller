package com.horsecall.pathfind.util.task;


import com.horsecall.pathfind.util.data.EntityData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Runnable to search entities in a predefined box
 */
public class SearchEntitiesInRangeSupplier implements Supplier<List<EntityData>> {
    Box searchRange;
    private final ServerWorld world;
    private final Predicate<Entity> predicate;
    private List<Entity> results;

    private final Entity tgtEntity;

    // Logger only for the search runnable
    public static final Logger LOGGER = LoggerFactory.getLogger("HorseOverhaul/Search");


    /**
     * Instanciate the search runnable
     * @param searchRange The box search Range
     * @param world The world being search
     * @param predicate The conditions being used as a predicate
     */
    public SearchEntitiesInRangeSupplier(Box searchRange, ServerWorld world, Predicate<Entity> predicate, Entity tgtEntity){
        this.searchRange = searchRange;
        this.world = world;
        this.predicate = predicate;
        this.tgtEntity = tgtEntity;
    }

    /**
     * Sort the result per distance related a certain entity
     * @param entity The entity being used to calculate the distances
     * @return A sorted list, containing the entities from the result and their respective distances from the target
     */
    private List<EntityData> sortResultsPerDistance(Entity entity) {
        List<EntityData> entities = new ArrayList<>();

        // Create the list of EntitiesWithDistance
        for (Entity result: results){

            String sprite;

            // Verify the sub-type of entity to set the sprite path
            if (result instanceof HorseEntity horse) {

            } else if (result instanceof CamelEntity) {

            } else if (result instanceof DonkeyEntity) {

            } else if (result instanceof MuleEntity) {

            } else if (result instanceof SkeletonHorseEntity) {

            } else if (result instanceof ZombieHorseEntity) {

            } else {
                continue;
            }


            double distance = result.distanceTo(entity);
            entities.add(new EntityData(result.getUuid(), distance));
        }

        // Sort the entities per distance
        entities.sort(Comparator.comparingDouble(EntityData::getDistance));

        // Set the page iterator
        short page = 1;

        // Set the count iterator
        short count = 0;

        // Set the page for the GUI to each entity
        for (EntityData item: entities){
            item.setPage(page);

            count++;

            // If reached the max quantity per page, set the next page
            if (count % 9 == 0) {
                page++;
            }
        }

        return entities;
    }

    @Override
    public List<EntityData> get() {
        LOGGER.info("Searching Entities...");

        long startTime = System.currentTimeMillis();
        this.results = this.world.getEntitiesByClass(Entity.class, searchRange, predicate);
        long endTime = System.currentTimeMillis();

        LOGGER.info("Elapsed time: " + (endTime - startTime) + "ms | Num. of Entities found: " + this.results.size());

        startTime = System.currentTimeMillis();
        LOGGER.info("Building list and sorting Entities...");
        List<EntityData> sorted = sortResultsPerDistance(tgtEntity);
        endTime = System.currentTimeMillis();
        LOGGER.info("Entities listed and sorted | Elapsed time: " + (endTime - startTime) + "ms");

        return sorted;
    }
}
