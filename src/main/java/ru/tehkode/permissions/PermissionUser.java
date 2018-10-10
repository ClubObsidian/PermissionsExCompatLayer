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

import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.manager.UserManager;

import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author t3hk0d3, virustotal
 */
public class PermissionUser extends PermissionEntity {

	private UUID uuid;
	public PermissionUser(UUID uuid) 
	{
		this.uuid = uuid;
	}

	public String getName()
	{
		Player player = Bukkit.getServer().getPlayer(this.uuid);
		if(player == null)
			return null;
		
		return player.getName();
	}
	
	public Type getType() 
	{
		return Type.USER;
	}
	
	
	/**
	 * Add user to group
	 *
	 * @param groupName group's name as String
	 */
	public void addGroup(String groupName) 
	{
		Group group = PermissionsEx.getLuckPermsApi().getGroupManager().getGroup(groupName);
		if(group != null)
		{
			try 
			{
				LuckPermsApi api = PermissionsEx.getLuckPermsApi();
				UserManager userManager = api.getUserManager();
				User user = userManager.loadUser(this.uuid).get();
				user.setPermission(api.getNodeFactory().makeGroupNode(groupName).build());
				userManager.saveUser(user);
			} 
			catch (InterruptedException | ExecutionException e) 
			{
				e.printStackTrace();
			}
		}
	}


	/**
	 * Remove user from group
	 *
	 * @param groupName group's name as String
	 */
	public void removeGroup(String groupName) 
	{
		Group group = PermissionsEx.getLuckPermsApi().getGroupManager().getGroup(groupName);
		if(group != null)
		{
			try 
			{
				LuckPermsApi api = PermissionsEx.getLuckPermsApi();
				UserManager userManager = api.getUserManager();
				User user = userManager.loadUser(this.uuid).get();
				user.unsetPermission(api.getNodeFactory().makeGroupNode(groupName).build());
				userManager.saveUser(user);
			} 
			catch (InterruptedException | ExecutionException e) 
			{
				e.printStackTrace();
			}
		}
	}


	public List<String> getPermissions(String worldName) 
	{
		List<String> permissions = new ArrayList<>();
		try 
		{
			LuckPermsApi api = PermissionsEx.getLuckPermsApi();
			UserManager userManager = api.getUserManager();
			User user = userManager.loadUser(this.uuid).get();
			if(worldName == null || worldName.equals("") || worldName.length() == 0)
			{
				Iterator<? extends Node> it = user.getPermissions().iterator();
				while(it.hasNext())
				{
					Node node = it.next();
					permissions.add(node.getPermission());
				}
			}
			else
			{
				Iterator<? extends Node> it = user.getPermissions().iterator();
				while(it.hasNext())
				{
					Node node = it.next();
					if(node.getWorld().isPresent())
					{
						if(node.getWorld().get().equals(worldName))
						{
							permissions.add(node.getPermission());
						}
					}
				}
			}
		}
		catch (InterruptedException | ExecutionException e) 
		{
			e.printStackTrace();
		}
		return permissions;
	}

	/**
	 * Get group for this user, global inheritance only
	 *
	 * @return
	 */
	public PermissionGroup[] getGroups()
	{
		String[] groupNames = this.getGroupsNames();
		PermissionGroup[] groups = new PermissionGroup[groupNames.length];
		for(int i = 0; i < groups.length; i++)
		{
			groups[i] = new PermissionGroup(groupNames[i]);
		}
		return groups;
	}
	
	/**
	 * Get group names
	 *
	 * @return
	 */
	public String[] getGroupsNames() 
	{
		List<String> groupNames = new ArrayList<>();
		try 
		{
			LuckPermsApi api = PermissionsEx.getLuckPermsApi();
			UserManager userManager = api.getUserManager();
			User user = userManager.loadUser(this.uuid).get();
			Iterator<? extends Node> it = user.getPermissions().iterator();
			while(it.hasNext())
			{
				Node node = it.next();
				if(node.isGroupNode())
				{
					groupNames.add(node.getGroupName());
				}
			}
		} 
		catch (InterruptedException | ExecutionException e) 
		{
			e.printStackTrace();
		}
		return groupNames.toArray(new String[groupNames.size()]);
	}

	/**
	 * Set parent groups for user
	 *
	 * @param groups array of parent Strings
	 */
	public void setGroups(String[] groups) 
	{
		try 
		{
			LuckPermsApi api = PermissionsEx.getLuckPermsApi();
			UserManager userManager = api.getUserManager();
			User user = userManager.loadUser(this.uuid).get();
			for(String groupName : this.getGroupsNames())
			{
				user.unsetPermission(api.getNodeFactory().makeGroupNode(groupName).build());
			}
			for(String groupName : groups)
			{
				user.setPermission(api.getNodeFactory().makeGroupNode(groupName).build());
			}
			userManager.saveUser(user);
		} 
		catch (InterruptedException | ExecutionException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public boolean has(String permission)
	{
		try 
		{
			LuckPermsApi api = PermissionsEx.getLuckPermsApi();
			UserManager userManager = api.getUserManager();
			User user = userManager.loadUser(this.uuid).get();
			return user.hasPermission(api.buildNode(permission).build()).asBoolean();
		} 
		catch (InterruptedException | ExecutionException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
}