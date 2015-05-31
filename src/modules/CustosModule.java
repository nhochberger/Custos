package modules;

import hochberger.utilities.application.Lifecycle;

import javax.swing.JComponent;

public interface CustosModule extends Lifecycle {

	JComponent getWidget();
}
