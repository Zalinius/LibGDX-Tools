package com.darzalgames.libgdxtools.ui.input.universaluserinput;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface SelectBoxContentManager {

	Supplier<String> getBoxLabelSupplier();
	Collection<Supplier<String>> getAllDisplayNames();
	Consumer<String> getChoiceResponder();
	Supplier<String> getCurrentSelectedDisplayName();

}
