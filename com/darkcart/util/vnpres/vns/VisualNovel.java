package com.darkcart.util.vnpres.vns;

import java.util.ArrayList;

import net.arikia.dev.drpc.DiscordRichPresence;

public abstract class VisualNovel {

	ArrayList<DiscordRichPresence> routes = new ArrayList<DiscordRichPresence>();
	String name, assetID, icon;

	public VisualNovel(String name, String assetID) {
		this(name, assetID, "");
	}
	
	public VisualNovel(String name, String assetID, String icon) {
		this.name = name;
		this.assetID = assetID;
		this.icon = icon;
	}

	public String getAssetID() {
		return assetID;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<DiscordRichPresence> getRoutes() {
		return routes;
	}

	public void addRoute(String route, String image) {
		routes.add(new DiscordRichPresence.Builder("").setStartTimestamps(System.currentTimeMillis()).setDetails(route).setBigImage(image, "")
				.setSmallImage(icon, "").build());
	}
}
