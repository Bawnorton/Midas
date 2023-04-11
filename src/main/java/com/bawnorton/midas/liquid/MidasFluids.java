package com.bawnorton.midas.liquid;

import com.bawnorton.midas.Midas;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class MidasFluids {
    public static FlowableFluid STILL_PACTOLUS_WATER = Registry.register(Registries.FLUID, Midas.id("pactolus_water"), new PactolusFluid.Still());
    public static FlowableFluid FLOWING_PACTOLUS_WATER = Registry.register(Registries.FLUID, Midas.id("flowing_pactolus_water"), new PactolusFluid.Flowing());

    public static void init() {
        Midas.LOGGER.debug("Initializing Midas Fluids");
    }
}
