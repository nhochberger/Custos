package modules;

import hochberger.utilities.application.Lifecycle;

public interface CustosModule extends Lifecycle {
	public CustosModuleWidget getWidget();

	public void updateWidget();
}
