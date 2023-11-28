package org.pokesplash.moveshop.config;

import com.google.gson.Gson;
import org.pokesplash.moveshop.MoveShop;
import org.pokesplash.moveshop.util.Utils;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Config {

	private boolean learnTM;
	private boolean learnEgg;
	private boolean learnTutor;
	private ArrayList<CategoryConfig> categories;

	public Config() {
		learnTM = true;
		learnEgg = false;
		learnTutor = true;
		categories = new ArrayList<>();
		categories.add(new CategoryConfig());
	}

	public void init() {
		CompletableFuture<Boolean> futureRead = Utils.readFileAsync(MoveShop.BASE_PATH,
				"config.json", el -> {
					Gson gson = Utils.newGson();
					Config cfg = gson.fromJson(el, Config.class);
					categories = cfg.getCategories();
				});

		if (!futureRead.join()) {
			MoveShop.LOGGER.info("No config.json file found for " + MoveShop.MOD_ID + ". Attempting to generate" +
					" " +
					"one");
			Gson gson = Utils.newGson();
			String data = gson.toJson(this);
			CompletableFuture<Boolean> futureWrite = Utils.writeFileAsync(MoveShop.BASE_PATH,
					"config.json", data);

			if (!futureWrite.join()) {
				MoveShop.LOGGER.fatal("Could not write config for " + MoveShop.MOD_ID + ".");
			}
			return;
		}
		MoveShop.LOGGER.info(MoveShop.MOD_ID + " config file read successfully");
	}

	public ArrayList<CategoryConfig> getCategories() {
		return categories;
	}

	public boolean isLearnTM() {
		return learnTM;
	}

	public boolean isLearnEgg() {
		return learnEgg;
	}

	public boolean isLearnTutor() {
		return learnTutor;
	}
}
