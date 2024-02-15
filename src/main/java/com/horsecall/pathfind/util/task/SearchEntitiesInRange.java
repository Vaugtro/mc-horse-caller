package com.horsecall.pathfind.util.task;


import com.horsecall.pathfind.networking.packet.HorseSearch;
import com.horsecall.pathfind.util.ID;
import com.horsecall.pathfind.util.data.SearchData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Runnable to search entities in a predefined box
 */
public class SearchEntitiesInRange implements Supplier<List<SearchData>> {
    Box searchRange;
    private final ServerWorld world;
    private final Predicate<Entity> predicate;
    private List<Entity> results;
    private final Entity tgtEntity;
    private final short pageSize;

    // Logger only for the search runnable
    public static final Logger LOGGER = LoggerFactory.getLogger(ID.MOD_ID + "/Server/Thread/Search");


    /**
     * Instanciate the search runnable
     * @param searchRange The box search Range
     * @param world The world being search
     * @param predicate The conditions being used as a predicate
     */
    public SearchEntitiesInRange(Box searchRange, ServerWorld world, Predicate<Entity> predicate, Entity tgtEntity){
        this.searchRange = searchRange;
        this.world = world;
        this.predicate = predicate;
        this.tgtEntity = tgtEntity;
    }

    // TODO: Develop this method
    private static String horseClassifier(Entity entity) {
        return ".png";
    }

    /**
     * Sort the result per distance related a certain entity
     * @return A sorted list, containing the entities from the result and their respective distances from the target
     */
    private List<SearchData> sortResultsPerDistance() {

        List<SearchData> entities = new ArrayList<>();

        // Object Literal AbstractHorseEntity Mapping
        // NOTE: I don't know if multiple instances of Identifiers with same ID going to cause problems later
        HashMap<String, Function<Entity,String>> entityTypes = new HashMap<>(){{
            put("HorseEntity", SearchEntitiesInRange::horseClassifier);
            put("Default", (entity) -> "unknown");
        }};

        // Create the list of EntitiesWithDistance
        for (Entity result: results){

            // Verify the subtype of entity to set the sprite path
            Function<Entity, String> sprite = entityTypes.containsKey(result.getClass().getName()) ? entityTypes.get(result.getClass().getName()) : entityTypes.get("Default");

            double distance = result.distanceTo(this.tgtEntity);
            entities.add(new SearchData(result.getUuid(), distance, sprite.apply(result)));
        }

        // Sort the entities per distance
        entities.sort(Comparator.comparingDouble(SearchData::getDistance));

        // Set the page iterator
        short page = 1;

        // Set the count iterator
        short count = 0;

        // Set the page for the GUI to each entity
        for (SearchData item: entities){
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
    public List<SearchData> get() {
        LOGGER.info("Searching Entities...");

        long startTime = System.currentTimeMillis();
        List<Entity> results = this.world.getEntitiesByClass(Entity.class, searchRange, predicate);
        long endTime = System.currentTimeMillis();

        LOGGER.info("Elapsed time: " + (endTime - startTime) + "ms | Num. of Entities found: " + this.results.size());

        startTime = System.currentTimeMillis();
        LOGGER.info("Building list and sorting Entities...");
        List<SearchData> sorted = sortResultsPerDistance();
        endTime = System.currentTimeMillis();
        LOGGER.info("Entities listed and sorted | Elapsed time: " + (endTime - startTime) + "ms");

        return sorted;
    }
}
