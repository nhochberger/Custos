package controller;

import hochberger.utilities.eventbus.Event;

import java.awt.Color;

import view.ColorProvider;

public class SystemMessage implements Event {

	public static enum MessageSeverity {

		NORMAL {
			@Override
			public Color getColorFrom(final ColorProvider colorProvider) {
				return colorProvider.foregroundColor();
			}
		},
		WARNING {
			@Override
			public Color getColorFrom(final ColorProvider colorProvider) {
				return colorProvider.highlightColor();
			}
		},
		SEVERE {
			@Override
			public Color getColorFrom(final ColorProvider colorProvider) {
				return colorProvider.warningColor();
			}
		},
		SUCCESS {
			@Override
			public Color getColorFrom(final ColorProvider colorProvider) {
				return colorProvider.everythingOkColor();
			}
		};

		public abstract Color getColorFrom(ColorProvider colorProvider);
	}

	private final MessageSeverity severity;
	private final String text;

	public SystemMessage(final MessageSeverity severity, final String text) {
		super();
		this.severity = severity;
		this.text = text;
	}

	public MessageSeverity getSeverity() {
		return this.severity;
	}

	public String getText() {
		return this.text;
	};

	@Override
	public void performEvent() {
		// TODO Auto-generated method stub
	}
}
