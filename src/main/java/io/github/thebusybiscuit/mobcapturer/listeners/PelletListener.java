package io.github.thebusybiscuit.mobcapturer.listeners;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.mobcapturer.MobCapturer;
import io.github.thebusybiscuit.mobcapturer.items.MobEgg;
import io.github.thebusybiscuit.mobcapturer.nms.entity.EntityUtil;
import io.github.thebusybiscuit.mobcapturer.setup.Keys;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.libraries.dough.protection.Interaction;

/**
 * Listener for pellet hit.
 *
 * @author TheBusyBiscuit
 * @author levtey
 * @author ybw0014
 */
public class PelletListener implements Listener {

    private final MobCapturer plugin;

    public PelletListener(@Nonnull MobCapturer plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.plugin = plugin;
    }

    /*
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onEntityDamageByEntity(@Nonnull EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Snowball pellet
            && e.getEntity() instanceof LivingEntity entity
            && pellet.hasMetadata(Keys.MOB_CAPTURING_PELLET)
            && pellet.getShooter() instanceof Player player
            && canCapture(player, entity)
        ) {
            Optional<ItemStack> optional = capture(entity);

            if (optional.isPresent()) {
                pellet.removeMetadata(Keys.MOB_CAPTURING_PELLET, plugin);
                entity.remove();
                entity.getWorld().dropItemNaturally(entity.getEyeLocation(), optional.get());
            }
        }
    }
    */

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onProjectileHit(@Nonnull ProjectileHitEvent e) {
        if (e.getEntity() instanceof Snowball pellet
            && e.getHitEntity() instanceof LivingEntity entity
            && pellet.hasMetadata(Keys.MOB_CAPTURING_PELLET)
            && pellet.getShooter() instanceof Player player
            && canCapture(player, entity)
        ) {
            Optional<ItemStack> optional = capture(entity);

            if (optional.isPresent()) {
                pellet.removeMetadata(Keys.MOB_CAPTURING_PELLET, plugin);
                entity.remove();
                entity.getWorld().dropItemNaturally(entity.getEyeLocation(), optional.get());
            }
        }
    }
    

    /**
     * Check if {@link Player} can capture the specified {@link LivingEntity}.
     *
     * @param p
     *     The {@link Player} to be checked.
     * @param entity
     *     The specified {@link LivingEntity}.
     *
     * @return If {@link Player} can capture the specified {@link LivingEntity}.
     */
    @ParametersAreNonnullByDefault
    protected boolean canCapture(Player p, LivingEntity entity) {

        if (!Slimefun.getProtectionManager().hasPermission(p, entity.getLocation(), Interaction.ATTACK_ENTITY)) {
            // no permission check
            return false;
        }

        if (!MobCapturer.getRegistry().getConfig().getBoolean("options.capture-named-mobs") 
            && entity.getCustomName() != null) { 
        	return false;
        }

        List<String> ignoredMobTypes = MobCapturer.getRegistry().getConfig().getStringList("options.ignored-mob-types");
        if (ignoredMobTypes.size() > 0) {
            String entityTypeName = EntityUtil.getTypeName(entity);
            Bukkit.getLogger().info("Comparing " + entityTypeName + " to " + ignoredMobTypes.toString());
            for (String ignoredMobType : ignoredMobTypes) {
                if (ignoredMobType.equalsIgnoreCase(entityTypeName)) {
                    return false;
                }
            }
        }

        List<String> ignoredMobNames = MobCapturer.getRegistry().getConfig().getStringList("options.ignored-mob-names");
        if (ignoredMobNames.size() > 0) {
            String strippedEntityName = ChatColor.stripColor(entity.getCustomName());
            for (String ignoredMobName : ignoredMobNames) {
                if (ignoredMobName.equalsIgnoreCase(strippedEntityName)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Capture the specified {@link LivingEntity}.
     *
     * @param entity
     *     The specified {@link LivingEntity}.
     *
     * @return The egg {@link ItemStack}.
     */
    @Nonnull
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Optional<ItemStack> capture(@Nonnull LivingEntity entity) {
        MobEgg egg = MobCapturer.getRegistry().getAdapters().get(entity.getType());

        if (egg != null) {
            ItemStack item = egg.getEggItem(entity);
            return Optional.of(item);
        } else {
            return Optional.empty();
        }
    }
}
