package org.pokesplash.moveshop.ui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.pokesplash.moveshop.MoveShop;
import org.pokesplash.moveshop.config.CategoryConfig;
import org.pokesplash.moveshop.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainUI {
	public Page getPage(Pokemon pokemon) {
		PlaceholderButton placeholderButton = new PlaceholderButton();

		List<Button> categoryButtons = new ArrayList<>();
		for (CategoryConfig category : MoveShop.config.getCategories()) {
			Button button = GooeyButton.builder()
					.display(Utils.parseItemId(category.getDisplayItem()))
					.title(Text.literal(category.getName())
							.setStyle(Style.EMPTY.withColor(TextColor.parse(category.getPrefix()))))
					.onClick(el -> {
						ServerPlayerEntity player = el.getPlayer();

						UIManager.openUIForcefully(player, new CategoryUI().getPage(category, el.getPlayer(), pokemon));
					})
					.build();

			categoryButtons.add(button);
		}

		GooeyButton back = GooeyButton.builder()
				.display(Utils.parseItemId(MoveShop.lang.getBackButtonMaterial()))
				.title("§cBack")
				.lore(new ArrayList<>())
				.hideFlags(FlagType.All)
				.onClick(e -> {
					UIManager.openUIForcefully(e.getPlayer(), new PokemonUI().getPage(e.getPlayer()));
				})
				.build();

		GooeyButton filler = GooeyButton.builder()
			.display(Utils.parseItemId(MoveShop.lang.getFillerMaterial()))
			.title("")
			.lore(new ArrayList<>())
			.hideFlags(FlagType.All)
			.build();

		int rows = (int) Math.ceil((double) MoveShop.config.getCategories().size() / 7) + 2;

		ChestTemplate template = ChestTemplate.builder(rows)
				.rectangle(1, 1, rows - 2, 7, placeholderButton)
				.fill(filler)
				.set(0, 0, back)
				.build();

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, categoryButtons, null);
		page.setTitle(MoveShop.lang.getTitle());

		return page;
	}
}
