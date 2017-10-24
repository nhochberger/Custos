package view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.SystemMessage;
import controller.SystemMessageMemory;
import hochberger.utilities.application.ApplicationShutdownEvent;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.gui.ImageButton;
import hochberger.utilities.gui.PanelWrapper;
import hochberger.utilities.gui.UndecoratedEDTSafeFrame;
import hochberger.utilities.gui.WindowClosedApplicationShutdownEventPublisher;
import hochberger.utilities.images.loader.ImageLoader;
import hochberger.utilities.text.i18n.DirectI18N;
import model.configuration.CustosConfiguration;
import modules.CustosModule;
import modules.VisibleCustosModule;
import net.miginfocom.swing.MigLayout;

public class CustosMainFrame extends UndecoratedEDTSafeFrame {

	private final List<VisibleCustosModule> modules;
	private final ColorProvider colorProvider;
	private final SystemMessageLabel systemMessageLabel;
	private final SystemMessageDialog systemMessageDialog;
	private final BasicSession session;
	private final CustosConfiguration custosConfiguration;

	public CustosMainFrame(final BasicSession session, final ColorProvider colorProvider, final SystemMessageMemory messageMemory, final CustosConfiguration custosConfiguration) {
		super(session.getProperties().title());
		this.session = session;
		this.colorProvider = colorProvider;
		this.custosConfiguration = custosConfiguration;
		this.modules = new CopyOnWriteArrayList<>();
		this.systemMessageLabel = new SystemMessageLabel(colorProvider);
		session.getEventBus().register(this.systemMessageLabel, SystemMessage.class);
		this.systemMessageDialog = new SystemMessageDialog(colorProvider, messageMemory);
		session.getEventBus().register(this.systemMessageDialog, SystemMessage.class);
	}

	@Override
	protected void buildUI() {
		frame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame().addWindowListener(new WindowClosedApplicationShutdownEventPublisher(this.session));
		center();
		frame().getContentPane().setBackground(this.colorProvider.backgroundColor());
		useLayoutManager(new MigLayout("wrap 3", ":push[400!, left]30![400!, center]30![400!, right]:push", "20![200!, top]30[200!, center]30[200!, bottom]push"));
		frame().setAlwaysOnTop(true);
		this.systemMessageLabel.build();
		this.systemMessageDialog.build();

		this.systemMessageLabel.getLabel().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent arg0) {
				CustosMainFrame.this.systemMessageDialog.show();
			}
		});
		final ImageButton closeApplicationButton = new ImageButton(ImageLoader.loadImage("close.png"));
		closeApplicationButton.setToolTipText(new DirectI18N("Shutdown Custos").toString());
		closeApplicationButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				CustosMainFrame.this.session.getEventBus().publishFromEDT(new ApplicationShutdownEvent());
			}
		});
		final ImageButton settingsButton = new ImageButton(ImageLoader.loadImage("settings.png"));
		settingsButton.setToolTipText(new DirectI18N("Settings").toString());
		settingsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final ConfigurationDialog dialog = new ConfigurationDialog(CustosMainFrame.this.custosConfiguration, CustosMainFrame.this.colorProvider, CustosMainFrame.this.session);
				dialog.build();
				dialog.show();
			}
		});
		final JPanel buttonPanel = PanelWrapper.wrap(settingsButton, closeApplicationButton);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setOpaque(false);
		add(buttonPanel, "north");
		for (final CustosModule module : this.modules) {
			add(module.getWidget().getComponent(), module.getWidget().getLayoutConstraints());
		}
		add(this.systemMessageLabel.getLabel(), "dock south, gapleft 5, gapright 5, gapbottom 5");
		maximize();
	}

	public void addModuleToView(final VisibleCustosModule module) {
		this.modules.add(module);
	}

	public void update() {
		if (!isBuilt()) {
			return;
		}
		frame().getContentPane().setBackground(this.colorProvider.backgroundColor());
		for (final VisibleCustosModule custosModule : this.modules) {
			custosModule.updateWidget();
		}
	}
}
