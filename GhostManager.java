package com.totempopchams;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.EquipmentSlot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Erzeugt beim Totem-Pop eine rein clientseitige Kopie des Spielers.
 * Die Kopie wird ganz normal von Minecraft gerendert (kein eigener Render-Code nötig)
 * und bekommt den Glow-Effekt: eine leuchtende Umrandung, die auch durch Wände sichtbar ist.
 */
public final class GhostManager {

    /** Wie lange ein Geist stehen bleibt (in Ticks, 20 Ticks = 1 Sekunde). */
    public static final int LIFETIME_TICKS = 80;

    /** Maximale Anzahl gleichzeitiger Geister (Schutz gegen Spam). */
    private static final int MAX_GHOSTS = 32;

    /** Eigene, negative Entity-IDs, damit sie nie mit Server-Entities kollidieren. */
    private static int nextId = -1_000_000;

    private static final class Ghost {
        final RemotePlayer entity;
        int age = 0;

        Ghost(RemotePlayer entity) {
            this.entity = entity;
        }
    }

    private static final List<Ghost> GHOSTS = new ArrayList<>();

    private GhostManager() {
    }

    public static void spawn(ClientLevel level, AbstractClientPlayer player) {
        if (GHOSTS.size() >= MAX_GHOSTS) {
            GHOSTS.remove(0).entity.discard();
        }

        RemotePlayer copy = new RemotePlayer(level, player.getGameProfile());
        copy.setId(nextId--);
        if (nextId < -2_000_000) {
            nextId = -1_000_000;
        }

        copy.snapTo(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
        copy.setYBodyRot(player.yBodyRot);
        copy.setYHeadRot(player.yHeadRot);
        copy.yBodyRotO = player.yBodyRot;
        copy.yHeadRotO = player.yHeadRot;
        copy.setPose(player.getPose());

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            copy.setItemSlot(slot, player.getItemBySlot(slot).copy());
        }

        copy.setNoGravity(true);
        copy.noPhysics = true;
        copy.setGlowingTag(true);

        level.addEntity(copy);
        GHOSTS.add(new Ghost(copy));
    }

    public static void tick() {
        Iterator<Ghost> it = GHOSTS.iterator();
        while (it.hasNext()) {
            Ghost g = it.next();
            if (++g.age >= LIFETIME_TICKS) {
                g.entity.discard();
                it.remove();
            }
        }
    }

    /** Beim Verlassen der Welt: nur die Liste leeren, die Welt räumt ihre Entities selbst auf. */
    public static void clear() {
        GHOSTS.clear();
    }
}
