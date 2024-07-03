package gdavid.psionicutilities.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellParam.Any;
import vazkii.psi.common.spell.other.PieceCrossConnector;

@Mixin(PieceCrossConnector.class)
public interface CrossConnectorPieceAccessor {
	
	@Accessor
	SpellParam<Any> getIn1();
	
	@Accessor
	SpellParam<Any> getIn2();
	
	@Accessor
	SpellParam<Any> getOut1();
	
	@Accessor
	SpellParam<Any> getOut2();
	
}
