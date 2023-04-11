package com.bawnorton.midas;

import com.bawnorton.midas.block.MidasBlocks;
import com.bawnorton.midas.command.CommandHandler;
import com.bawnorton.midas.entity.MidasEntities;
import com.bawnorton.midas.item.MidasItems;
import com.bawnorton.midas.liquid.MidasFluids;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Midas implements ModInitializer {
	public static final String MOD_ID = "midas";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Midas");
		MidasBlocks.init();
		MidasItems.init();
		MidasFluids.init();
		MidasEntities.init();

		CommandHandler.init();
	}
}