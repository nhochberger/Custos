package modules;

import hochberger.utilities.application.Lifecycle;
import hochberger.utilities.eventbus.EventReceiver;
import controller.HeartbeatEvent;

public interface CustosModule extends Lifecycle, EventReceiver<HeartbeatEvent> {

    public CustosModuleWidget getWidget();

    public CustosModuleConfiguration getConfiguration();
}
