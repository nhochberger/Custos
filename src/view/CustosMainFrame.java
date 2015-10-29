package view;

import hochberger.utilities.application.ApplicationShutdownEvent;
import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.gui.ImageButton;
import hochberger.utilities.gui.UndecoratedEDTSafeFrame;
import hochberger.utilities.images.loader.ImageLoader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;

import modules.CustosModule;
import modules.VisibleCustosModule;
import net.miginfocom.swing.MigLayout;
import controller.SystemMessage;
import controller.SystemMessageMemory;

public class CustosMainFrame extends UndecoratedEDTSafeFrame {

    private final List<VisibleCustosModule> modules;
    private final ColorProvider colorProvider;
    private final SystemMessageLabel systemMessageLabel;
    private final SystemMessageDialog systemMessageDialog;
    private final BasicSession session;

    public CustosMainFrame(final BasicSession session, final ColorProvider colorProvider, final SystemMessageMemory messageMemory) {
        super(session.getProperties().title());
        this.session = session;
        this.colorProvider = colorProvider;
        this.modules = new CopyOnWriteArrayList<>();
        this.systemMessageLabel = new SystemMessageLabel(colorProvider);
        session.getEventBus().register(this.systemMessageLabel, SystemMessage.class);
        this.systemMessageDialog = new SystemMessageDialog(colorProvider, messageMemory);
        session.getEventBus().register(this.systemMessageDialog, SystemMessage.class);
    }

    @Override
    protected void buildUI() {
        frame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent e) {
                CustosMainFrame.this.session.getEventBus().publishFromEDT(new ApplicationShutdownEvent());
            }
        });
        center();
        frame().getContentPane().setBackground(this.colorProvider.backgroundColor());
        useLayoutManager(new MigLayout("wrap 3", ":push[400!, left]40![400!, center]40![400!, right]:push", "40[200!, top]20[200!, center]20[200!, bottom]push"));
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
        closeApplicationButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                CustosMainFrame.this.session.getEventBus().publishFromEDT(new ApplicationShutdownEvent());
            }
        });
        add(closeApplicationButton, "east");
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
