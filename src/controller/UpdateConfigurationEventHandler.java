package controller;

import hochberger.utilities.eventbus.EventReceiver;

import java.util.List;

import modules.CustosModule;

public class UpdateConfigurationEventHandler implements EventReceiver<UpdateConfigurationEvent> {

	private final List<CustosModule> modules;

	public UpdateConfigurationEventHandler(final List<CustosModule> modules) {
		super();
		this.modules = modules;
	}

	@Override
	public void receive(final UpdateConfigurationEvent event) {
		for (CustosModule custosModule : this.modules) {
			custosModule.applyConfiguration();
		}
	}
}
