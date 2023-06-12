package io.github.thebusybiscuit.mobcapturer.adapters.mobs;

import org.bukkit.entity.Camel;
import org.bukkit.entity.Horse;

/**
 * This is an adapter for the {@link Camel}.
 * Camel is an extension of {@link Horse} with no additional info.
 */
public class CamelAdapter extends AbstractHorseAdapter<Camel> {

    public CamelAdapter() {
        super(Camel.class);
    }

}
