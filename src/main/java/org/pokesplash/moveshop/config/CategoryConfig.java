package org.pokesplash.moveshop.config;

import java.util.ArrayList;

public class CategoryConfig {
	private String name;
	private String displayItem;
	private ArrayList<MoveConfig> moves;

	public CategoryConfig() {
		name = "Ice";
		displayItem = "minecraft:ice";
		moves = new ArrayList<>();
		moves.add(new MoveConfig());
	}

	public String getName() {
		return name;
	}

	public String getDisplayItem() {
		return displayItem;
	}


	public ArrayList<MoveConfig> getMoves() {
		return moves;
	}
}
