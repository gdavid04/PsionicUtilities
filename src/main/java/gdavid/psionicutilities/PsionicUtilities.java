package gdavid.psionicutilities;

import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.network.NetworkConstants;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(PsionicUtilities.modId)
public class PsionicUtilities {
	
	public static final String modId = "psionicutilities";
	
	public PsionicUtilities() {
		ModLoadingContext.get().registerExtensionPoint(DisplayTest.class,
			() -> new DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
	}
	
}
