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
package ru.tehkode.permissions.bukkit;

import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.lucko.luckperms.api.LuckPermsApi;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;

/**
 * @author t3hk0d3, virustotal
 */
public class PermissionsEx extends JavaPlugin {

	private static PermissionsEx instance;
	private static LuckPermsApi luckPermsApi;
	
	private PermissionManager permissionManager;

	@Override
	public void onEnable()
	{	
		RegisteredServiceProvider<LuckPermsApi> provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
		if (provider != null) 
		{
		    luckPermsApi = provider.getProvider();
		}
		else
		{
			this.getLogger().log(Level.SEVERE, "Unable to obtain LuckPermsApi, shutting down.");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
		}
		PermissionsEx.instance = this;
		this.permissionManager = new PermissionManager();
	}

    public static PermissionManager getPermissionManager() 
    {
        return PermissionsEx.instance.getPermissionsManager();
    }
	
    public PermissionManager getPermissionsManager() 
    {
        return this.permissionManager;
    }
    
    public static PermissionUser getUser(UUID uuid) 
    {
        return getPermissionManager().getUser(uuid);
    }

    public static PermissionUser getUser(Player player) 
    {
        return getPermissionManager().getUser(player);
    }

    @SuppressWarnings("deprecation")
	public static PermissionUser getUser(String name) 
    {
        return getPermissionManager().getUser(Bukkit.getServer().getPlayer(name));
    }
    
    public static LuckPermsApi getLuckPermsApi()
    {
    	return PermissionsEx.luckPermsApi;
    }
}