package modules.alarmclock;

import hochberger.utilities.eventbus.EventBus;
import hochberger.utilities.gui.ImageButton;
import hochberger.utilities.gui.font.FontLoader;
import hochberger.utilities.images.loader.ImageLoader;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import modules.CustosModuleWidget;
import net.miginfocom.swing.MigLayout;
import view.ColorProvider;
import edt.EDT;

public class AlarmClockWidget implements CustosModuleWidget {

	private boolean isBuilt;
	private JPanel panel;
	private final ColorProvider colorProvider;
	private final List<Alarm> alarms;
	private JPanel rightPanel;
	private final EventBus eventBus;

	public AlarmClockWidget(final EventBus eventBus, final ColorProvider colorProvider, final List<Alarm> alarms) {
		super();
		this.eventBus = eventBus;
		this.colorProvider = colorProvider;
		this.alarms = alarms;
	}

	@Override
	public void updateWidget() {
		EDT.performBlocking(new Runnable() {

			@Override
			public void run() {
				AlarmClockWidget.this.rightPanel.removeAll();
				for (Alarm alarm : AlarmClockWidget.this.alarms) {
					SingleAlarm singleAlarm = new SingleAlarm(AlarmClockWidget.this.colorProvider, alarm);
					singleAlarm.build();
					AlarmClockWidget.this.rightPanel.add(singleAlarm.getComponent(), "wrap");
				}
			}
		});
	}

	@Override
	public JComponent getComponent() {
		return this.panel;
	}

	@Override
	public void build() {
		if (this.isBuilt) {
			return;
		}
		this.isBuilt = true;
		this.panel = new JPanel(new MigLayout("", "2[64!]2[234!]2", "2[top]:push"));
		final Font baseFont = FontLoader.loadFromWithFallback("monofonto.ttf", new JPanel().getFont());
		this.panel.setOpaque(false);
		JPanel leftPanel = new JPanel(new MigLayout("", "0[64!]0", "0[]2[]:push"));
		leftPanel.setOpaque(false);
		leftPanel.add(new JLabel(ImageLoader.loadIcon("modules/alarmclock/alarm_clock.png")), "wrap");
		ImageButton newAlarmButton = new ImageButton(ImageLoader.loadImage("modules/alarmclock/plus.png"));
		newAlarmButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent arg0) {
				NewAlarmDialog dialog = new NewAlarmDialog(AlarmClockWidget.this.panel, AlarmClockWidget.this.colorProvider);
				dialog.build();
				dialog.show();
				if (dialog.wasClosedByCommit()) {
					AlarmClockWidget.this.eventBus.publishFromEDT(new NewAlarmEvent(dialog.getResult()));
				}
			}
		});
		leftPanel.add(newAlarmButton, "center");

		this.panel.add(leftPanel, "cell 0 0");

		this.rightPanel = new JPanel(new MigLayout("", "0[234!]0", "0[20]"));
		this.rightPanel.setOpaque(false);
		for (Alarm alarm : this.alarms) {
			SingleAlarm singleAlarm = new SingleAlarm(this.colorProvider, alarm);
			singleAlarm.build();
			this.rightPanel.add(singleAlarm.getComponent(), "wrap");
		}
		this.panel.add(this.rightPanel, "cell 1 0");
	}
}
