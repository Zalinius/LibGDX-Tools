package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import java.util.List;
import java.util.function.Supplier;

public interface SelectBoxContentManager {

	String getBoxLabelKey();

	List<SelectBoxButtonInfo> getOptionButtons();

	Supplier<String> getCurrentSelectedDisplayName();

	public record SelectBoxButtonInfo(Supplier<String> buttonTextSupplier, Runnable buttonPressRunnable) {}

}
