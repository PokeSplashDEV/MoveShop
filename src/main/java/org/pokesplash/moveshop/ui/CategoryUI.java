package org.pokesplash.moveshop.ui;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.FlagType;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.page.Page;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.api.moves.Moves;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.server.network.ServerPlayerEntity;
import org.pokesplash.moveshop.MoveShop;
import org.pokesplash.moveshop.config.CategoryConfig;
import org.pokesplash.moveshop.config.MoveConfig;
import org.pokesplash.moveshop.util.ImpactorUtils;
import org.pokesplash.moveshop.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CategoryUI {

	private final LinkedPageButton nextPage = LinkedPageButton.builder()
			.display(Utils.parseItemId(MoveShop.lang.getNextPageMaterial()))
			.title("§7Next Page")
			.linkType(LinkType.Next)
			.build();

	private final LinkedPageButton previousPage = LinkedPageButton.builder()
			.display(Utils.parseItemId(MoveShop.lang.getPreviousPageMaterial()))
			.title("§7Previous Page")
			.linkType(LinkType.Previous)
			.build();

	public Page getPage(CategoryConfig category, ServerPlayerEntity player, Pokemon pokemon) {
		PlaceholderButton placeholderButton = new PlaceholderButton();

		List<Button> itemButtons = new ArrayList<>();
		for (MoveConfig moveConfig : category.getMoves()) {

			MoveTemplate move = Moves.INSTANCE.getByName(moveConfig.getMove());

			if (move == null) {
				MoveShop.LOGGER.error("Move " + moveConfig.getMove() + " can not be found.");
				continue;
			}

			Collection<String> lore = new ArrayList<>();
			lore.add("§b" + move.getDescription().getString());
			lore.add("§aPrice: §e" + moveConfig.getPrice());
			lore.add("§6Current Balance: " + ImpactorUtils.getAccount(player.getUuid()).balanceAsync().join());


			List<MoveTemplate> availableMoves = new ArrayList<>();

			if (MoveShop.config.isLearnTM()) {
				availableMoves.addAll(pokemon.getForm().getMoves().getTmMoves());
			}

			if (MoveShop.config.isLearnEgg()) {
				availableMoves.addAll(pokemon.getForm().getMoves().getEggMoves());
			}

			if (MoveShop.config.isLearnTutor()) {
				availableMoves.addAll(pokemon.getForm().getMoves().getTutorMoves());
			}

			for (Move move1 : pokemon.getMoveSet().getMoves()) {
				availableMoves.remove(move1.getTemplate());
			}

			if (!availableMoves.contains(move)) {
				continue;
			}

			Button button = GooeyButton.builder()
					.display(Utils.parseItemId(category.getDisplayItem()))
					.title(move.getDisplayName())
					.lore(lore)
					.onClick(el -> {
						UIManager.openUIForcefully(el.getPlayer(), new MoveUI().getPage(move, moveConfig,
								category,	el.getPlayer(), pokemon));
					})
					.build();

			itemButtons.add(button);
		}

		GooeyButton filler = GooeyButton.builder()
				.display(Utils.parseItemId(MoveShop.lang.getFillerMaterial()))
				.title("")
				.lore(new ArrayList<>())
				.hideFlags(FlagType.All)
				.build();

		GooeyButton back = GooeyButton.builder()
				.display(Utils.parseItemId(MoveShop.lang.getBackButtonMaterial()))
				.title("§cBack")
				.lore(new ArrayList<>())
				.hideFlags(FlagType.All)
				.onClick(e -> {
					UIManager.openUIForcefully(e.getPlayer(), new MainUI().getPage(pokemon));
				})
				.build();

		int rows = category.getMoves().size() <= 28 ? (int) Math.ceil((double) category.getMoves().size() / 7) + 2 : 6;

		ChestTemplate.Builder template = ChestTemplate.builder(rows)
				.rectangle(1, 1, rows - 2, 7, placeholderButton)
				.fill(filler)
				.set(0, 0, back);

		if (category.getMoves().size() > 28) {
			template.set(45, previousPage);
			template.set(53, nextPage);
		}

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template.build(), itemButtons, null);
		page.setTitle(category.getName());

		return page;
	}
}