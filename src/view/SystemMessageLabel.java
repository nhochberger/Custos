package view;

import hochberger.utilities.eventbus.EventReceiver;

import java.awt.Cursor;

import javax.swing.JLabel;

import controller.SystemMessage;
import edt.EDT;

public class SystemMessageLabel implements EventReceiver<SystemMessage> {

	private JLabel label;
	private final ColorProvider colorProvider;
	private boolean isBuilt;

	public SystemMessageLabel(final ColorProvider colorProvider) {
		super();
		this.colorProvider = colorProvider;
		this.isBuilt = false;
	}

	public void build() {
		EDT.only();
		if (this.isBuilt) {
			return;
		}
		this.isBuilt = true;
		this.label = new JLabel();
		this.label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	public JLabel getLabel() {
		return this.label;
	}

	@Override
	public void receive(final SystemMessage event) {
		EDT.performBlocking(new Runnable() {

			@Override
			public void run() {
				SystemMessageLabel.this.label.setForeground(event.getSeverity().getColorFrom(SystemMessageLabel.this.colorProvider));
				SystemMessageLabel.this.label.setText(event.getText());
			}
		});
	}
}
