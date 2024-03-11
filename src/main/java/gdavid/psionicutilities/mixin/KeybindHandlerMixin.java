package gdavid.psionicutilities.mixin;

import gdavid.psionicutilities.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.psi.client.core.handler.KeybindHandler;

@Mixin(value = KeybindHandler.class, remap = false)
public class KeybindHandlerMixin {

	@Inject(method = "keyDown", at = @At(value = "INVOKE", target = "Lvazkii/patchouli/api/PatchouliAPI$IPatchouliAPI;openBookGUI(Lnet/minecraft/resources/ResourceLocation;)V"), cancellable = true)
	private static void disableGuideBookHotkey(CallbackInfo callback) {
		if (Config.instance.disableGuideBookHotkey.get()) callback.cancel();
	}

}
