package gdavid.psionicutilities;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class Config {
	
	public static final ForgeConfigSpec spec;
	public static final Config instance;
	
	public final BooleanValue disableGuideBookHotkey;
	
	public Config(ForgeConfigSpec.Builder builder) {
		disableGuideBookHotkey = builder.comment("Disable showing guide book with the Psi keybind").define("disableGuideBookHotkey", false);
	}
	
	static {
		var p = new ForgeConfigSpec.Builder().configure(Config::new);
		spec = p.getRight();
		instance = p.getLeft();
	}
	
}
