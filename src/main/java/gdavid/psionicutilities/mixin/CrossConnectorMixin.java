package gdavid.psionicutilities.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import gdavid.psionicutilities.ConnectorColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.spell.IGenericRedirector;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellParam.Any;
import vazkii.psi.api.spell.SpellParam.Side;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.spell.other.PieceCrossConnector;

@Mixin(value = PieceCrossConnector.class, remap = false)
public abstract class CrossConnectorMixin extends SpellPiece implements IGenericRedirector {
	
	@Shadow SpellParam<Any> in1, in2, out1, out2;
	
	private CrossConnectorMixin(Spell spell) {
		super(spell);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Redirect(method = "drawSide", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;color(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;", remap = true))
	private VertexConsumer sideColor(VertexConsumer builder, float r, float g, float b, float a, PoseStack ms, MultiBufferSource buffers, Side side, int light, int color) {
		boolean which = side == paramSides.get(in2) || side == paramSides.get(out2);
		float[] rgb = ConnectorColor.colorFor(this, which ? paramSides.get(in2) : paramSides.get(in1));
		return builder.color(rgb[0], rgb[1], rgb[2], a);
	}
	
}
