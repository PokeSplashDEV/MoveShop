package org.pokesplash.moveshop.ui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.impactdev.impactor.api.economy.accounts.Account;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.pokesplash.moveshop.MoveShop;
import org.pokesplash.moveshop.config.CategoryConfig;
import org.pokesplash.moveshop.config.MoveConfig;
import org.pokesplash.moveshop.util.ImpactorUtils;
import org.pokesplash.moveshop.util.Utils;

import java.util.ArrayList;
import java.util.Collection;

public class MoveUI {
	public Page getPage(MoveTemplate move, MoveConfig moveConfig, CategoryConfig category,
	                    ServerPlayerEntity player, Pokemon pokemon) {
		Collection<String> lore =
				new ArrayList<>(Utils.cutString(move.getDescription().getString(), 45, "§9"));

		GooeyButton itemButton = GooeyButton.builder()
				.display(Utils.parseItemId(category.getDisplayItem()))
				.title(Text.literal(move.getDisplayName().getString())
						.setStyle(Style.EMPTY.withColor(TextColor.parse(category.getPrefix()))))
				.lore(lore)
				.build();

		Collection<String> purchaseLore = new ArrayList<>();
		purchaseLore.add("§aBuy: §e" + moveConfig.getPrice());
		purchaseLore.add("§6Current Balance: " + ImpactorUtils.getAccount(player.getUuid()).balanceAsync().join());

		GooeyButton purchase = GooeyButton.builder()
				.display(Utils.parseItemId(MoveShop.lang.getPurchaseMaterial()))
				.title("§aPurchase")
				.lore(purchaseLore)
				.onClick(e -> {
					UIManager.openUIForcefully(e.getPlayer(), new TeachUI().getPage(pokemon, player, moveConfig, move));
				})
				.build();

		Account account = ImpactorUtils.getAccount(player.getUuid());

		Collection<String> poorLore = new ArrayList<>();
		poorLore.add("§cYou need " + moveConfig.getPrice() + " to purchase this.");
		poorLore.add("§cYour current balance is " + account.balanceAsync().join());
		GooeyButton poor = GooeyButton.builder()
				.display(Utils.parseItemId(MoveShop.lang.getInsufficientBalanceMaterial()))
				.title("§cInsufficient Funds")
				.lore(poorLore)
				.build();

		GooeyButton cancel = GooeyButton.builder()
				.display(Utils.parseItemId(MoveShop.lang.getCancelMaterial()))
				.title("§cCancel")
				.onClick(e -> {
					UIManager.openUIForcefully(e.getPlayer(), new CategoryUI().getPage(category, e.getPlayer(),
							pokemon));
				})
				.build();

		GooeyButton filler = GooeyButton.builder()
				.display(Utils.parseItemId(MoveShop.lang.getFillerMaterial()))
				.title("")
				.lore(new ArrayList<>())
				.hideFlags(FlagType.All)
				.build();

		ChestTemplate template = ChestTemplate.builder(3)
				.fill(filler)
				.set(11, purchase)
				.set(13, itemButton)
				.set(15, cancel)
				.build();


		if (account.balanceAsync().join().doubleValue() < moveConfig.getPrice()) {
			template.set(11, poor);
		}

		return GooeyPage.builder()
				.title(move.getDisplayName())
				.template(template)
				.build();
	}
}
