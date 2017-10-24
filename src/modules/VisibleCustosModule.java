package modules;

import controller.CustosSession;
import edt.EDT;
import hochberger.utilities.application.session.SessionBasedObject;
import view.ColorProvider;

public abstract class VisibleCustosModule extends SessionBasedObject implements CustosModule {

	private final ColorProvider colorProvider;

	protected VisibleCustosModule(final CustosSession session) {
		super(session);
		this.colorProvider = session.getColorProvider();
	}

	protected ColorProvider colorProvider() {
		return this.colorProvider;
	}

	public void updateWidget() {
		EDT.performBlocking(new Runnable() {
			@Override
			public void run() {
				getWidget().updateWidget();
			}
		});
	}
}
