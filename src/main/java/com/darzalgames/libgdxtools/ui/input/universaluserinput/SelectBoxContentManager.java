package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import java.util.List;
import java.util.function.Supplier;

import com.darzalgames.libgdxtools.ui.input.universaluserinput.button.UniversalButton;

public interface SelectBoxContentManager {

	Supplier<String> getBoxLabelSupplier();
	List<UniversalButton> getOptionButtons();
	Supplier<String> getCurrentSelectedDisplayName();

}
