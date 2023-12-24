package org.modder.modid.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.modder.modid.ModIdMain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class ExampleMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void dummyMixinInject(GameConfig gameConfig, CallbackInfo ci) {
        ModIdMain.LOGGER.info("Hello from Example Mixin!");
    }

}
