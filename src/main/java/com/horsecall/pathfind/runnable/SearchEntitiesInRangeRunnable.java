package com.horsecall.pathfind.runnable;


import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

import java.util.function.Predicate;

public class SearchEntitiesInRangeRunnable implements Runnable{

    Box searchRange;
    private ServerWorld world;
    private Predicate<Entity> predicate;
    private Iterable<Entity> results;

    public SearchEntitiesInRangeRunnable(Box searchRange, ServerWorld world, Predicate<Entity> predicate){
        this.searchRange = searchRange;
        this.world = world;
        this.predicate = predicate;

    }

    public Iterable<Entity> getResults() {
        return results;
    }

    public void run(){
        this.results = this.world.getEntitiesByClass(Entity.class, searchRange, predicate);
    }
}
