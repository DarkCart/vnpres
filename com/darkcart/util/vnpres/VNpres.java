package com.darkcart.util.vnpres;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import com.darkcart.util.vnpres.vns.EverlastingSummer;
import com.darkcart.util.vnpres.vns.KatawaShoujo;
import com.darkcart.util.vnpres.vns.VisualNovel;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class VNpres {

	HashMap<String, String> imageSets = new HashMap<String, String>();
	ArrayList<VisualNovel> visualNovels = new ArrayList<VisualNovel>();
	JList<String> routeList = new JList<String>(new String[] { "nothing yet" });
	static VisualNovel selected = null;

	public static void main(String[] args) {
		new VNpres();
	}

	public VNpres() {
		visualNovels.add(new EverlastingSummer());
		visualNovels.add(new KatawaShoujo());

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 800);
		frame.setTitle("VNpres");
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		JComboBox<String> selector = new JComboBox<String>(getNames());

		JButton getButton = new JButton("Select");
		getButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				VisualNovel selected = getVisualNovelByName(selector.getSelectedItem());
				initDiscord(selected.getAssetID());
				System.out.println("initialized");
				VNpres.selected = selected;
				System.out.println(Arrays.toString(getRouteNames(selected)));
				routeList = new JList<String>(getRouteNames(selected));
				frame.add(routeList, BorderLayout.CENTER);
				frame.revalidate();
			}

		});

		JPanel selectorPanel = new JPanel();
		selectorPanel.add(selector);
		selectorPanel.add(getButton);

		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DiscordRPC.discordUpdatePresence(getRouteByName(selected, routeList.getSelectedValue()));
			}

		});

		frame.add(selectorPanel, BorderLayout.NORTH);
		frame.add(updateButton, BorderLayout.SOUTH);

		frame.revalidate();
		/*
		 * 
		 * initDiscord("546841579344756737"); while (true) { System.out.println("boom");
		 * setPresence("test", "alisa"); try { Thread.sleep(150000); } catch
		 * (InterruptedException e) { e.printStackTrace(); } }
		 */
	}

	private String[] getNames() {
		String[] names = new String[visualNovels.size()];
		for (int i = 0; i < visualNovels.size(); i++) {
			names[i] = visualNovels.get(i).getName();
		}
		return names;
	}

	private VisualNovel getVisualNovelByName(Object name) {
		for (int i = 0; i < visualNovels.size(); i++) {
			if (visualNovels.get(i).getName().equals(name)) {
				return visualNovels.get(i);
			}
		}
		return null;
	}

	private String[] getRouteNames(VisualNovel v) {
		String[] routeNames = new String[v.getRoutes().size()];
		ArrayList<DiscordRichPresence> routes = v.getRoutes();
		for (int i = 0; i < routes.size(); i++) {
			routeNames[i] = routes.get(i).details;
		}
		return routeNames;
	}

	private DiscordRichPresence getRouteByName(VisualNovel selected, String name) {
		ArrayList<DiscordRichPresence> routes = selected.getRoutes();
		for (int i = 0; i < routes.size(); i++) {
			if (routes.get(i).details.equals(name)) {
				return routes.get(i);
			}
		}
		return null;
	}

	
	private void initDiscord(String id) {
		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().build();
		DiscordRPC.discordInitialize(id, handlers, false);
		DiscordRPC.discordRegister(id, "");
	}

	/*
	private void setPresence(String routeName, String image) {
		DiscordRichPresence.Builder p = new DiscordRichPresence.Builder("Test");
		p.setDetails(routeName);
		p.setBigImage(image, routeName);
		DiscordRPC.discordUpdatePresence(p.build());
	}
	*/
}
