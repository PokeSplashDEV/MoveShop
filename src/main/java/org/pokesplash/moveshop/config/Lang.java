package org.pokesplash.moveshop.config;

import com.google.gson.Gson;
import org.pokesplash.moveshop.MoveShop;
import org.pokesplash.moveshop.util.Utils;

import java.util.concurrent.CompletableFuture;

public class Lang {
	private String title;
	private String fillerMaterial;
	private String nextPageMaterial;
	private String previousPageMaterial;
	private String backButtonMaterial;
	private String purchaseMaterial;
	private String insufficientBalanceMaterial;
	private String cancelMaterial;
	private String giveMessage;
	private String moveSlotItem;


	public Lang() {
		title = MoveShop.MOD_ID;
		fillerMaterial = "minecraft:white_stained_glass_pane";
		nextPageMaterial = "minecraft:arrow";
		previousPageMaterial = "cobblemon:poison_barb";
		backButtonMaterial = "minecraft:barrier";
		purchaseMaterial = "minecraft:lime_stained_glass_pane";
		insufficientBalanceMaterial = "minecraft:red_stained_glass_pane";
		cancelMaterial = "minecraft:orange_stained_glass_pane";
		giveMessage = "§2Successfully taught %pokemon% %move%";
		moveSlotItem = "minecraft:gold_nugget";
	}

	public String getTitle() {
		return title;
	}

	public String getFillerMaterial() {
		return fillerMaterial;
	}

	public String getNextPageMaterial() {
		return nextPageMaterial;
	}

	public String getPreviousPageMaterial() {
		return previousPageMaterial;
	}

	public String getBackButtonMaterial() {
		return backButtonMaterial;
	}

	public String getPurchaseMaterial() {
		return purchaseMaterial;
	}

	public String getInsufficientBalanceMaterial() {
		return insufficientBalanceMaterial;
	}

	public String getMoveSlotItem() {
		return moveSlotItem;
	}

	public String getCancelMaterial() {
		return cancelMaterial;
	}

	public String getGiveMessage() {
		return giveMessage;
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
					nextPageMaterial = lang.getNextPageMaterial();
					previousPageMaterial = lang.getPreviousPageMaterial();
					backButtonMaterial = lang.getBackButtonMaterial();
					purchaseMaterial = lang.getPurchaseMaterial();
					insufficientBalanceMaterial = lang.getInsufficientBalanceMaterial();
					cancelMaterial = lang.getCancelMaterial();
					giveMessage = lang.getGiveMessage();
					moveSlotItem = lang.getMoveSlotItem();
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
