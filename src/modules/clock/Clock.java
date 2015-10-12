package modules.clock;

import hochberger.utilities.application.session.BasicSession;
import modules.CustosModuleConfiguration;
import modules.CustosModuleWidget;
import modules.VisibleCustosModule;
import view.ColorProvider;
import controller.HeartbeatEvent;
import edt.EDT;

public class Clock extends VisibleCustosModule {

    private final ClockWidget widget;
    private final CustosModuleConfiguration configuration;

    public Clock(final BasicSession session, final ColorProvider colorProvider) {
        super(session, colorProvider);
        this.widget = new ClockWidget(colorProvider());
        this.configuration = new CustosModuleConfiguration.NoCustosModuleConfiguration();
    }

    @Override
    public void start() {
        EDT.perform(new Runnable() {

            @Override
            public void run() {
                Clock.this.widget.build();
            }
        });
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub

    }

    @Override
    public CustosModuleWidget getWidget() {
        return this.widget;
    }

    @Override
    public void receive(final HeartbeatEvent event) {
        // TODO Auto-generated method stub
    }

    @Override
    public CustosModuleConfiguration getConfiguration() {
        return this.configuration;
    }
}
