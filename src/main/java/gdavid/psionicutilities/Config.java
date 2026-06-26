package gdavid.psionicutilities;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
	
	public static final ModConfigSpec spec;
	public static final Config instance;
	
	public final ModConfigSpec.BooleanValue disableGuideBookHotkey;
	public final ModConfigSpec.BooleanValue foucsPreviousWithShift;
	
	public Config(ModConfigSpec.Builder builder) {
		disableGuideBookHotkey = builder.comment("Disable showing guide book with the Psi keybind").define("disableGuideBookHotkey", true);
		foucsPreviousWithShift = builder.comment("Use shift+tab instead of alt+tab to focus previous piece in the catalog").define("foucsPreviousWithShift", true);
	}
	
	static {
		var p = new ModConfigSpec.Builder().configure(Config::new);
		spec = p.getRight();
		instance = p.getLeft();
	}
	
}
