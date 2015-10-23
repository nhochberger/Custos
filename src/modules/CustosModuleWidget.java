package modules;

import javax.swing.JComponent;

public interface CustosModuleWidget {

    public void updateWidget();

    public JComponent getComponent();

    public void build();

    public String getLayoutConstraints();
}
