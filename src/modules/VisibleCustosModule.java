package modules;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import view.ColorProvider;

public abstract class VisibleCustosModule extends SessionBasedObject implements CustosModule {

	private final ColorProvider colorProvider;

	protected VisibleCustosModule(final BasicSession session, final ColorProvider colorProvider) {
		super(session);
		this.colorProvider = colorProvider;
	}

	protected ColorProvider colorProvider() {
		return this.colorProvider;
	}
}
