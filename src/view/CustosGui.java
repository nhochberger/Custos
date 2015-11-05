package view;

import hochberger.utilities.application.session.BasicSession;
import hochberger.utilities.application.session.SessionBasedObject;
import hochberger.utilities.eventbus.EventReceiver;
import hochberger.utilities.gui.ApplicationGui;

import java.util.LinkedList;

import modules.VisibleCustosModule;
import controller.CustosConfiguration;
import controller.HeartbeatEvent;
import controller.SystemMessageMemory;

public class CustosGui extends SessionBasedObject implements ApplicationGui, EventReceiver<HeartbeatEvent> {

    private final LinkedList<VisibleCustosModule> modules;
    private final ColorProvider colorProvider;
    private CustosMainFrame mainFrame;
    private final SystemMessageMemory messageMemory;
    private final CustosConfiguration custosConfiguration;

    public CustosGui(final BasicSession session, final ColorProvider colorProvider, final SystemMessageMemory messageMemory, final CustosConfiguration custosConfiguration) {
        super(session);
        this.colorProvider = colorProvider;
        this.messageMemory = messageMemory;
        this.custosConfiguration = custosConfiguration;
        this.modules = new LinkedList<>();
    }

    @Override
    public void activate() {
        logger().info("GUI activated");
        this.mainFrame = new CustosMainFrame(session(), this.colorProvider, this.messageMemory, this.custosConfiguration);
        for (final VisibleCustosModule module : this.modules) {
            this.mainFrame.addModuleToView(module);
        }
        this.mainFrame.show();
    }

    @Override
    public void deactivate() {
        this.mainFrame.hide();
        logger().info("GUI deactivated");
    }

    public void addModule(final VisibleCustosModule module) {
        this.modules.add(module);
    }

    @Override
    public void receive(final HeartbeatEvent event) {
        if (null == this.mainFrame) {
            return;
        }
        this.mainFrame.update();
    }
}
