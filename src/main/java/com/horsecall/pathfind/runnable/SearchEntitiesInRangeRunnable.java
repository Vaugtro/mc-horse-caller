package com.horsecall.pathfind.runnable;


import com.horsecall.pathfind.helper.EntityWithDistance;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Runnable to search entities in a predefined box
 */
public class SearchEntitiesInRangeRunnable implements Runnable{
    Box searchRange;
    private final ServerWorld world;
    private final Predicate<Entity> predicate;
    private List<Entity> results;

    // Logger only for the search runnable
    public static final Logger LOGGER = LoggerFactory.getLogger("HorseOverhaul/Search");


    /**
     * Instanciate the search runnable
     * @param searchRange The box search Range
     * @param world The world being search
     * @param predicate The conditions being used as a predicate
     */
    public SearchEntitiesInRangeRunnable(Box searchRange, ServerWorld world, Predicate<Entity> predicate){
        this.searchRange = searchRange;
        this.world = world;
        this.predicate = predicate;
    }

    public List<Entity> getResults() {
        return results;
    }

    /**
     * Sort the result per distance related a certain entity
     * @param entity The entity being used to calculate the distances
     * @return A sorted list, containing the entities from the result and their respective distances from the target
     */
    public List<EntityWithDistance> sortResultsPerDistance(Entity entity) {
        List<EntityWithDistance> entitiesWithDistances = new ArrayList<>();

        for (Entity result: results){
            double distance = result.distanceTo(entity);
            entitiesWithDistances.add(new EntityWithDistance(result, distance));
        }

        entitiesWithDistances.sort(Comparator.comparingDouble(EntityWithDistance::getDistance));

        return entitiesWithDistances;
    }

    @Override
    public void run(){
        LOGGER.info("Searching Entities...");

        long startTime = System.currentTimeMillis();
        this.results = this.world.getEntitiesByClass(Entity.class, searchRange, predicate);
        long endTime = System.currentTimeMillis();

        LOGGER.info("Elapsed time: " + (endTime - startTime) + "ms; Num. of Entities found: " + this.results.size());
    }
}
