package io.github.thebusybiscuit.mobcapturer.adapters.mobs;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.gson.JsonObject;

import org.bukkit.ChatColor;
import org.bukkit.entity.Bee;

/**
 * This is an adapter for the {@link Bee}.
 * It stores the entity's nectar, stung, and anger status.
 */
public class BeeAdapter extends AnimalsAdapter<Bee> {

    public BeeAdapter() {
        super(Bee.class);
    }

    @Nonnull
    @Override
    public List<String> getLore(@Nonnull JsonObject json) {
        List<String> lore = super.getLore(json);

        if (json.get("anger").getAsInt() > 0) {
            lore.add(ChatColor.GRAY + "Angry: " + ChatColor.WHITE + "Yes");
        }
        if (json.get("nectar").getAsBoolean()) {
            lore.add(ChatColor.GRAY + "Nectar: " + ChatColor.WHITE + "Yes");
        }
        if (json.get("stung").getAsBoolean()) {
            lore.add(ChatColor.GRAY + "Stung: " + ChatColor.WHITE + "Yes");
        }

        return lore;
    }


    @Override
    @ParametersAreNonnullByDefault
    public void apply(Bee entity, JsonObject json) {
        super.apply(entity, json);

        entity.setAnger(json.get("anger").getAsInt());
        entity.setCannotEnterHiveTicks(json.get("cannotEnterHiveTicks").getAsInt());
        entity.setHasNectar(json.get("nectar").getAsBoolean());
        entity.setHasStung(json.get("stung").getAsBoolean());
    }

    @Nonnull
    @Override
    public JsonObject saveData(@Nonnull Bee entity) {
        JsonObject json = super.saveData(entity);

        json.addProperty("anger", entity.getAnger());
        json.addProperty("cannotEnterHiveTicks", entity.getCannotEnterHiveTicks());
        json.addProperty("nectar", entity.hasNectar());
        json.addProperty("stung", entity.hasStung());

        return json;
    }

}
