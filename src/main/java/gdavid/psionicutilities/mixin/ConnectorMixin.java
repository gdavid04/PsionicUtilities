package gdavid.psionicutilities.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import gdavid.psionicutilities.ConnectorColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vazkii.psi.api.spell.IRedirector;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.spell.other.PieceConnector;

@Mixin(value = PieceConnector.class, remap = false)
public abstract class ConnectorMixin extends SpellPiece implements IRedirector {
	
	private ConnectorMixin(Spell spell) {
		super(spell);
	}
	
	@OnlyIn(Dist.CLIENT)
	@WrapOperation(method = "drawSide", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;", remap = true))
	private VertexConsumer sideColor(VertexConsumer builder, float r, float g, float b, float a, Operation<VertexConsumer> original) {
		float[] rgb = ConnectorColor.colorFor(this, getRedirectionSide());
		return original.call(builder, rgb[0], rgb[1], rgb[2], a);
	}
	
}
