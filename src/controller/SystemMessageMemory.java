package controller;

import hochberger.utilities.eventbus.EventReceiver;

import java.util.LinkedList;
import java.util.List;

public class SystemMessageMemory implements EventReceiver<SystemMessage> {

	private static final int MAX_MESSAGES = 250;
	private final List<SystemMessage> messages;

	public SystemMessageMemory() {
		super();
		this.messages = new LinkedList<>();
	}

	@Override
	public void receive(final SystemMessage message) {
		while (MAX_MESSAGES < this.messages.size()) {
			this.messages.remove(0);
		}
		this.messages.add(message);
	}

	public List<SystemMessage> getMessages() {
		return this.messages;
	}
}
