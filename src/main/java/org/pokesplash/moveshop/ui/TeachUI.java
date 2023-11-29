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
import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.impactdev.impactor.api.economy.accounts.Account;
import net.impactdev.impactor.api.economy.transactions.EconomyTransaction;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.pokesplash.moveshop.MoveShop;
import org.pokesplash.moveshop.config.MoveConfig;
import org.pokesplash.moveshop.util.ImpactorUtils;
import org.pokesplash.moveshop.util.Utils;

import java.util.ArrayList;
import java.util.Collection;

public class TeachUI {
	public Page getPage(Pokemon pokemon, ServerPlayerEntity player, MoveConfig moveConfig, MoveTemplate move) {

		Collection<String> moves = new ArrayList<>();
		for (Move move1 : pokemon.getMoveSet().getMoves()) {
			if (move == null) {
				moves.add("No Move");
			} else {
				moves.add(move1.getName());
			}
		}

		GooeyButton pokemonButton = GooeyButton.builder()
				.display(new ItemStack(PokemonItem.from(pokemon).getItem()))
				.title(pokemon.getDisplayName())
				.lore(moves)
				.build();

		ArrayList<Button> slotButtons = new ArrayList<>();
		for (int x=0; x < 4; x++) {

			Move currentMove = pokemon.getMoveSet().get(x);

			int finalX = x;
			slotButtons.add(GooeyButton.builder()
					.title(
							currentMove == null ?
									Text.literal("No Move").setStyle(Style.EMPTY.withColor(TextColor.parse("blue"))) :
									currentMove.getDisplayName().setStyle(Style.EMPTY.withColor(TextColor.parse("blue"))))
							.display(Utils.parseItemId(MoveShop.lang.getMoveSlotItem()))
							.onClick(e -> {
								Account account = ImpactorUtils.getAccount(player.getUuid());

								assert account != null;
								EconomyTransaction transaction = ImpactorUtils.remove(account, moveConfig.getPrice());

								if (!transaction.successful()) {
									e.getPlayer().sendMessage(Text.literal("§c" + transaction.result().toString()));
									return;
								}

								pokemon.getMoveSet().setMove(finalX, move.create());


								UIManager.closeUI(e.getPlayer());
								e.getPlayer().sendMessage(Text.literal(
										Utils.formatPlaceholders(MoveShop.lang.getGiveMessage(),
												pokemon.getDisplayName().getString(),
												move.getDisplayName().getString())
								));
							})
					.build());
		}

		slotButtons.add(2, GooeyButton.builder()
				.display(new ItemStack(CobblemonItems.MASTER_BALL))
				.title("§3Choose a move to swap.")
				.build());

		GooeyButton filler = GooeyButton.builder()
				.display(Utils.parseItemId(MoveShop.lang.getFillerMaterial()))
				.title("")
				.lore(new ArrayList<>())
				.hideFlags(FlagType.All)
				.build();

		PlaceholderButton placeholderButton = new PlaceholderButton();

		ChestTemplate.Builder template = ChestTemplate.builder(3)
				.fill(filler)
				.rectangle(1, 2, 1, 5, placeholderButton);

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template.build(), slotButtons, null);
		page.setTitle(move.getDisplayName());

		return page;
	}
}
