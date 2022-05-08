package com.kotakotik.ponderjs;

import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderStoryBoardEntry;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class PonderStoriesManager {
    private final Map<ResourceLocation, List<PonderStoryBoardEntry>> stories = Collections.synchronizedMap(new HashMap<>());

    public void add(PonderStoryBoardEntry entry) {
        List<PonderStoryBoardEntry> list = stories.computeIfAbsent(entry.getComponent(), $ -> new ArrayList<>());
        list.add(entry);
    }

    public void clear() {
        synchronized (PonderRegistry.ALL) {
            Set<ResourceLocation> toRemove = new HashSet<>();

            for (var thisStoryEntry : stories.entrySet()) {
                List<PonderStoryBoardEntry> existingStories = PonderRegistry.ALL.get(thisStoryEntry.getKey());
                existingStories.removeIf(story -> thisStoryEntry.getValue().contains(story));
                if (existingStories.isEmpty()) {
                    toRemove.add(thisStoryEntry.getKey());
                }
            }

            toRemove.forEach(PonderRegistry.ALL::remove);
        }

        stories.clear();
    }

    public void compileLang() {
        stories.values()
                .stream()
                .flatMap(Collection::stream)
                .forEach(entry -> PonderRegistry.compileScene(0, entry, null));
    }
}
