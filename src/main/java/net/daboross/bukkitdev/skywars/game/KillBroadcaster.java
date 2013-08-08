/*
 * Copyright (C) 2013 Dabo Ross <http://www.daboross.net/>
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
package net.daboross.bukkitdev.skywars.game;

import net.daboross.bukkitdev.skywars.Messages;

/**
 *
 * @author daboross
 */
public class KillBroadcaster {

    public static String getMessage(String player, String damager, boolean causedVoid) {
        if (damager == null) {
            if (causedVoid) {
                return String.format(Messages.SUICIDE_VOID, player);
            } else {
                return String.format(Messages.SUICIDE, player);
            }
        } else {
            if (causedVoid) {
                return String.format(Messages.KILLED_VOID, damager, player);
            } else {
                return String.format(Messages.KILLED, damager, player);
            }
        }
    }
}