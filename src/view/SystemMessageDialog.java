package view;

import hochberger.utilities.eventbus.EventReceiver;

import java.awt.Dialog.ModalExclusionType;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;
import controller.SystemMessage;
import controller.SystemMessageMemory;
import edt.EDT;

public class SystemMessageDialog implements EventReceiver<SystemMessage> {

    private boolean isBuilt;
    private JDialog dialog;
    private final ColorProvider colorProvider;
    private final SystemMessageMemory messageMemory;
    private JPanel messagesPanel;

    public SystemMessageDialog(final ColorProvider colorProvider, final SystemMessageMemory messageMemory) {
        super();
        this.colorProvider = colorProvider;
        this.messageMemory = messageMemory;
        this.isBuilt = false;
    }

    public void build() {
        if (this.isBuilt) {
            return;
        }
        this.isBuilt = true;
        this.dialog = new JDialog();
        this.dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.dialog.setTitle("System Message History");
        this.dialog.setModal(true);
        this.dialog.setAlwaysOnTop(true);
        this.dialog.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
        this.dialog.setSize(600, 300);
        this.dialog.setLocationRelativeTo(null);
        this.dialog.setResizable(false);
        this.messagesPanel = new JPanel(new MigLayout());
        this.messagesPanel.setBackground(this.colorProvider.backgroundColor());
        JScrollPane scrollPane = new JScrollPane(this.messagesPanel);
        scrollPane.setAutoscrolls(true);
        this.dialog.setContentPane(scrollPane);
    }

    public void show() {
        this.messagesPanel.setBackground(this.colorProvider.backgroundColor());
        this.dialog.setVisible(true);
    }

    public void hide() {
        this.dialog.setVisible(false);
    }

    @Override
    public void receive(final SystemMessage event) {
        EDT.performBlocking(new Runnable() {

            @Override
            public void run() {
                SystemMessageDialog.this.messagesPanel.removeAll();
                for (SystemMessage message : SystemMessageDialog.this.messageMemory.getMessages()) {
                    JLabel label = new JLabel();
                    label.setForeground(message.getSeverity().getColorFrom(SystemMessageDialog.this.colorProvider));
                    label.setText(message.toString());
                    SystemMessageDialog.this.messagesPanel.add(label, "wrap");
                }
                SystemMessageDialog.this.messagesPanel.doLayout();
                SystemMessageDialog.this.dialog.getContentPane().doLayout();
            }
        });
    }
}
