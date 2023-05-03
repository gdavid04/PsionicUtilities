package gdavid.psionicutilities.mixin;

import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorExtractZ;

@Mixin(value = PieceOperatorVectorExtractZ.class, remap = false)
public abstract class ExtractZMixin extends SpellPiece {
	
	private ExtractZMixin(Spell spell) {
		super(spell);
	}
	
	@Override public boolean interceptKeystrokes() { return true; }
	
	@Override public boolean onCharTyped(char character, int keyCode, boolean doit) {
		char ch = Character.toLowerCase(character);
		if (ch < 'x' || ch > 'z') return false;
		if (doit && ch != 'z') {
			CompoundTag nbt = new CompoundTag();
			writeToNBT(nbt);
			nbt.putString("key", "psi:operator_vector_extract_" + ch);
			spell.grid.gridData[x][y] = SpellPiece.createFromNBT(spell, nbt);
		}
		return true;
	}
	
}
