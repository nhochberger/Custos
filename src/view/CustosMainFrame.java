package view;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.gui.UndecoratedEDTSafeFrame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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

    public CustosMainFrame(final BasicSession session,
            final ColorProvider colorProvider,
            final SystemMessageMemory messageMemory) {
        super(session.getProperties().title());
        this.colorProvider = colorProvider;
        this.modules = new CopyOnWriteArrayList<>();
        this.systemMessageLabel = new SystemMessageLabel(colorProvider);
        session.getEventBus().register(this.systemMessageLabel,
                SystemMessage.class);
        this.systemMessageDialog = new SystemMessageDialog(colorProvider,
                messageMemory);
        session.getEventBus().register(this.systemMessageDialog,
                SystemMessage.class);
    }

    @Override
    protected void buildUI() {
        exitOnClose();
        center();
        frame().getContentPane().setBackground(
                this.colorProvider.backgroundColor());
        useLayoutManager(new MigLayout("debug, wrap 3", "20[]:push[]:push[]20",
                "20[]20[]20[]push"));
        frame().setAlwaysOnTop(true);
        this.systemMessageLabel.build();
        this.systemMessageDialog.build();

        this.systemMessageLabel.getLabel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent arg0) {
                CustosMainFrame.this.systemMessageDialog.show();
            }
        });

        for (final CustosModule module : this.modules) {
            add(module.getWidget().getComponent());
        }
        add(this.systemMessageLabel.getLabel(),
                "dock south, gapleft 5, gapright 5, gapbottom 5");
        maximize();
    }

    public void addModuleToView(final VisibleCustosModule module) {
        this.modules.add(module);
    }

    public void update() {
        if (!isBuilt()) {
            return;
        }
        frame().getContentPane().setBackground(
                this.colorProvider.backgroundColor());
        for (final VisibleCustosModule custosModule : this.modules) {
            custosModule.updateWidget();
        }
    }
}
