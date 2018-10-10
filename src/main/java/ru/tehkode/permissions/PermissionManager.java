/*
 * PermissionsEx - Permissions plugin for Bukkit
 * Copyright (C) 2011 t3hk0d3 http://www.tehkode.ru
  * Copyright (C) 2018 ClubObsidian https://www.clubobsidian.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package ru.tehkode.permissions;

import java.util.UUID;

import org.bukkit.entity.Player;

/**
 * @author t3hk0d3, virustotal
 */
public class PermissionManager {

	/**
	 * Return object of specified player
	 *
	 * @param uuid uuid object
	 * @return PermissionUser instance
	 */
	public PermissionUser getUser(UUID uuid)
	{
		return new PermissionUser(uuid);
	}
	
	/**
	 * Return object of specified player
	 *
	 * @param player player object
	 * @return PermissionUser instance
	 */
	public PermissionUser getUser(Player player) 
	{
		return this.getUser(player.getUniqueId());
	}
}