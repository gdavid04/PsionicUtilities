package gdavid.psionicutilities.mixin;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import gdavid.psionicutilities.PsionicUtilities;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.Material;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import gdavid.psionicutilities.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.psi.api.ClientPsiAPI;
import vazkii.psi.api.spell.IGenericRedirector;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

@Mixin(value = SpellPiece.class, remap = false)
public abstract class SpellPieceMixin {
	
	@OnlyIn(Dist.CLIENT)
	@Redirect(method = "drawParam(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;ILvazkii/psi/api/spell/SpellParam$Side;ILvazkii/psi/api/spell/SpellParam$ArrowType;F)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;color(IIII)Lcom/mojang/blaze3d/vertex/VertexConsumer;", remap = true))
	private VertexConsumer paramColor(VertexConsumer builder, int r, int g, int b, int a, PoseStack ms, VertexConsumer buffer, int light, SpellParam.Side side, int color, SpellParam.ArrowType arrowType, float percent) {
		SpellPiece self = (SpellPiece) (Object) this;
		// TODO Phi IWarpRedirector support
		if (self instanceof IGenericRedirector) {
			int[] t = Util.getPartialRedirect(self, side);
			float[] rgb = Util.getColor(t[0], t[1]);
			return builder.color(rgb[0], rgb[1], rgb[2], a / 255f);
		}
		return builder.color(r, g, b, a);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Inject(method = "drawBackground", at = @At("HEAD"))
	private void drawBackground(PoseStack ms, MultiBufferSource buffers, int light, CallbackInfo callback) {
		SpellPiece self = (SpellPiece) (Object) this;
		if (self.comment != null) {
			String prefix = "@color=";
			Optional<String> str = Arrays.stream(self.comment.split(";")).filter(ln -> ln.startsWith(prefix)).findFirst().map(ln -> ln.substring(prefix.length()));
			str.ifPresent(color -> {
				TextColor value = TextColor.parseColor(color);
				if (value != null) {
					drawHighlight(ms, buffers, light, value.getValue());
				}
			});
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	@Inject(method = "drawComment", at = @At("HEAD"), cancellable = true)
	private void drawComment(PoseStack ms, MultiBufferSource buffers, int light, CallbackInfo callback) {
		SpellPiece self = (SpellPiece) (Object) this;
		if (self.comment != null) {
			String prefix = "@color=";
			if (Arrays.stream(self.comment.split(";")).allMatch(ln ->
				ln.startsWith(prefix) && TextColor.parseColor(ln.substring(prefix.length())) != null
			)) callback.cancel();
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	@Inject(method = "drawCommentText", at = @At("HEAD"))
	private void visibleCommentText(PoseStack ms, int tooltipX, int tooltipY, List<Component> commentText, Screen screen, CallbackInfo callback) {
		String prefix = "@color=";
		commentText.stream().filter(ln -> ln.getString().startsWith(prefix)).findFirst().ifPresent(commentText::remove);
	}
	
	@OnlyIn(Dist.CLIENT)
	private void drawHighlight(PoseStack ms, MultiBufferSource buffers, int light, int value) {
		int r = (value >> 16) & 0xFF;
		int g = (value >> 8) & 0xFF;
		int b = value & 0xFF;
		Material material = new Material(ClientPsiAPI.PSI_PIECE_TEXTURE_ATLAS, new ResourceLocation(PsionicUtilities.modId, "spell/highlight"));
		VertexConsumer buf = material.buffer(buffers, ignore -> SpellPiece.getLayer());
		Matrix4f mat = ms.last().pose();
		buf.vertex(mat, -1, 17, 0).color(r, g, b, 128);
		buf.uv(0, 1).uv2(light).endVertex();
		buf.vertex(mat, 17, 17, 0).color(r, g, b, 128);
		buf.uv(1, 1).uv2(light).endVertex();
		buf.vertex(mat, 17, -1, 0).color(r, g, b, 128);
		buf.uv(1, 0).uv2(light).endVertex();
		buf.vertex(mat, -1, -1, 0).color(r, g, b, 128);
		buf.uv(0, 0).uv2(light).endVertex();
		ms.translate(0, 0, 0.1f);
	}
	
}
