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
import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.pokesplash.moveshop.MoveShop;
import org.pokesplash.moveshop.util.Utils;

import java.util.ArrayList;

public class PokemonUI {

	public Page getPage(ServerPlayerEntity player) {

		PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);

		ArrayList<Button> pokemonButtons = new ArrayList<>();
		for (int x=0; x < 6; x++) {

			Pokemon pokemon = party.get(x);

			if (pokemon == null) {
				pokemonButtons.add(GooeyButton.builder()
						.display(new ItemStack(CobblemonItems.POKE_BALL))
						.title("§cNo Pokemon In Slot")
						.lore(new ArrayList<>())
						.hideFlags(FlagType.All)
						.build());
				continue;
			}

			pokemonButtons.add(GooeyButton.builder()
					.display(PokemonItem.from(pokemon, 1))
					.title(pokemon.getDisplayName())
					.onClick(e -> {
						UIManager.openUIForcefully(e.getPlayer(), new MainUI().getPage(pokemon));
					})
					.build());

		}

		pokemonButtons.add(3, GooeyButton.builder()
				.display(new ItemStack(CobblemonItems.MASTER_BALL))
						.title("§2Choose a Pokemon to learn a move.")
				.build());

		Button filler = GooeyButton.builder()
				.display(Utils.parseItemId(MoveShop.lang.getFillerMaterial()))
				.title("")
				.lore(new ArrayList<>())
				.hideFlags(FlagType.All)
				.build();

		PlaceholderButton placeholder = new PlaceholderButton();

		ChestTemplate template = ChestTemplate.builder(3)
				.rectangle(1, 1, 1, 7, placeholder)
				.fill(filler)
				.build();

		LinkedPage page = PaginationHelper.createPagesFromPlaceholders(template, pokemonButtons, null);
		page.setTitle(MoveShop.lang.getTitle());

		return page;
	}
}
