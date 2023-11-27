package org.pokesplash.moveshop.config;

import com.google.gson.Gson;
import org.pokesplash.moveshop.MoveShop;
import org.pokesplash.moveshop.util.Utils;

import java.util.concurrent.CompletableFuture;

public class Lang {
	private String title;
	private String fillerMaterial;

	public Lang() {
		title = MoveShop.MOD_ID;
		fillerMaterial = "minecraft:white_stained_glass_pane";
	}

	public String getTitle() {
		return title;
	}

	public String getFillerMaterial() {
		return fillerMaterial;
	}

	/**
	 * Method to initialize the config.
	 */
	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(MoveShop.BASE_PATH, "lang.json",
				el -> {
					Gson gson = Utils.newGson();
					Lang lang = gson.fromJson(el, Lang.class);
					title = lang.getTitle();
					fillerMaterial = lang.getFillerMaterial();
				});

		if (!futureRead.join()) {
			MoveShop.LOGGER.info("No lang.json file found for " + MoveShop.MOD_ID + ". Attempting to " +
					"generate " +
					"one.");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(MoveShop.BASE_PATH, "lang.json", data);

			if (!futureWrite.join()) {
				MoveShop.LOGGER.fatal("Could not write lang.json for " + MoveShop.MOD_ID + ".");
			}
			return;
		}
		MoveShop.LOGGER.info(MoveShop.MOD_ID + " lang file read successfully.");
	}
}
