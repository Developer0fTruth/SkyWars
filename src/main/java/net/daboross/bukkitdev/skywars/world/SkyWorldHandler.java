/*
 * Copyright (C) 2013-2016 Dabo Ross <http://www.daboross.net/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.daboross.bukkitdev.skywars.world;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import net.daboross.bukkitdev.skywars.api.SkyStatic;
import net.daboross.bukkitdev.skywars.api.SkyWars;
import net.daboross.bukkitdev.skywars.api.arenaconfig.SkyArena;
import net.daboross.bukkitdev.skywars.api.arenaconfig.SkyArenaConfig;
import net.daboross.bukkitdev.skywars.api.game.SkyGame;
import net.daboross.bukkitdev.skywars.api.location.SkyBlockLocation;
import net.daboross.bukkitdev.skywars.api.location.SkyPlayerLocation;
import net.daboross.bukkitdev.skywars.events.events.GameEndInfo;
import net.daboross.bukkitdev.skywars.events.events.GameStartInfo;
import net.daboross.bukkitdev.skywars.game.ArenaGame;
import net.daboross.bukkitdev.skywars.world.providers.ProtobufStorageProvider;
import net.daboross.bukkitdev.skywars.world.providers.WorldEditProtobufStorageProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

public class SkyWorldHandler {

    private final SkyWars plugin;
    private final WorldProvider provider;
    private World arenaWorld;

    public SkyWorldHandler(SkyWars plugin) {
        this.plugin = plugin;
        if (plugin.getConfiguration().isWorldeditHookEnabled() && plugin.getServer().getPluginManager().isPluginEnabled("WorldEdit")) {
            boolean upToDate;
            try {
                Class.forName("com.sk89q.worldedit.world.AbstractWorld");
                upToDate = true;
            } catch (ClassNotFoundException ignored) {
                upToDate = false;
            }
            if (upToDate) {
                plugin.getLogger().info("Using WorldEdit hook for arena creation.");
                this.provider = new WorldEditProtobufStorageProvider(plugin);
            } else {
                plugin.getLogger().info("WorldEdit outdated! Using slower non-worldedit backend for arena creation.");
                this.provider = new ProtobufStorageProvider(plugin);
            }
        } else {
            plugin.getLogger().info("Using slower non-worldedit backend for arena creation.");
            this.provider = new ProtobufStorageProvider(plugin);
        }
    }

    public void loadArenas() {
        for (SkyArenaConfig arena : plugin.getConfiguration().getEnabledArenas()) {
            try {
                provider.loadArena(arena, false);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to load arena '" + arena.getArenaName() + "':", e);
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
        }
    }

    /**
     * This method loads a new arena into the current cache - useful for creating caches for new arenas after saving the
     * .yml files.
     */
    public void loadNewArena(SkyArenaConfig arena, boolean forceReload) throws IOException {
        provider.loadArena(arena, forceReload);
    }

    public void create() {
        arenaWorld = plugin.getServer().getWorld(Statics.ARENA_WORLD_NAME);
        if (arenaWorld == null) {
            plugin.getLogger().info("Loading world '" + Statics.ARENA_WORLD_NAME + "'.");
            WorldCreator arenaWorldCreator = new WorldCreator(Statics.ARENA_WORLD_NAME);
            arenaWorldCreator.generateStructures(false);
            arenaWorldCreator.generator(new VoidGenerator());
            arenaWorldCreator.type(WorldType.FLAT);
            arenaWorldCreator.seed(0);
            arenaWorld = arenaWorldCreator.createWorld();
            plugin.getLogger().info("Done loading world '" + Statics.ARENA_WORLD_NAME + "'.");
        } else {
            plugin.getLogger().info("The world '" + Statics.ARENA_WORLD_NAME + "' was already loaded.");
        }
        arenaWorld.setAutoSave(false);
        arenaWorld.getBlockAt(-5000, 45, -5000).setType(Material.STONE);
        arenaWorld.setSpawnLocation(-5000, 50, -5000);
        for (Map.Entry<String, String> entry : plugin.getConfiguration().getArenaGamerules().entrySet()) {
            arenaWorld.setGameRuleValue(entry.getKey(), entry.getValue());
        }
        arenaWorld.setTime(4000);
    }

    public void destroyArenaWorld() {
        Path worldFolder = arenaWorld.getWorldFolder().toPath();
        Bukkit.unloadWorld(arenaWorld, false);
        arenaWorld = null;
        if (Files.exists(worldFolder)) {
            plugin.getLogger().info("Cleaning up: Deleting " + worldFolder);
            try {
                deletePath(worldFolder);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to delete " + worldFolder, e);
            }
        }
    }

    public void onGameStart0(GameStartInfo info) {
        // Copy arena *first* before doing anything else.
        ArenaGame game = info.getGame();
        SkyBlockLocation min = getMinLocation(game);
        game.setMin(min);
        provider.copyArena(arenaWorld, game.getArena(), min);
    }

    public void onGameStart1(GameStartInfo info) {
        ArenaGame game = info.getGame();
        SkyBlockLocation min = getMinLocation(game);
        List<SkyPlayerLocation> spawns = new ArrayList<>(game.getArena().getSpawns());
        Collections.shuffle(spawns);
        if (game.areTeamsEnabled()) {
            int numTeams = game.getNumTeams();
            for (int i = 0, currentSpawn = 0; i < numTeams; i++) {
                Location spawn = min.add(spawns.get(currentSpawn++)).toLocation();
                SkyStatic.debug("Starting spawning team #%s to spawn %s", i, spawn);
                for (UUID uuid : game.getAllPlayersInTeam(i)) {
                    Player p = Bukkit.getPlayer(uuid);
                    if (p != null) {
                        SkyStatic.debug("Sending %s (uuid: %s) to that spawn", p.getName(), uuid);
                        p.teleport(spawn);
                    }
                }
                if (currentSpawn >= spawns.size()) {
                    currentSpawn = 0;
                }
            }
        } else {
            List<Player> players = info.getPlayers();
            for (int i = 0, currentSpawn = 0; i < players.size(); i++) {
                players.get(i).teleport(min.add(spawns.get(currentSpawn++)).toLocation());
                if (currentSpawn >= spawns.size()) {
                    currentSpawn = 0;
                }
            }
        }
    }

    public void onGameEnd(GameEndInfo info) {
        SkyBlockLocation min = info.getGame().getMin();
        provider.destroyArena(arenaWorld, info.getGame().getArena(), min);
    }

    private SkyBlockLocation getMinLocation(SkyGame game) {
        return getMinLocation(game.getId(), game.getArena());
    }

    private SkyBlockLocation getMinLocation(int id, SkyArena arena) {
        int distanceApart = plugin.getConfiguration().getArenaDistanceApart();
        int modX = (id % 2) * distanceApart;
        int modZ = (id / 2) * distanceApart;
        int modY = arena.getPlacementY();
        return new SkyBlockLocation(modX, modY, modZ, Statics.ARENA_WORLD_NAME);
    }

    public static void deletePath(final Path path) throws IOException {
        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            cleanDirectory(path);
        }
        Files.deleteIfExists(path);
    }

    public static void cleanDirectory(final Path path) throws IOException {
        if (!Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            return;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                deletePath(entry);
            }
        }
    }
}
