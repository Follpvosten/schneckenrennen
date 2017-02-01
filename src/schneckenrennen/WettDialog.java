package schneckenrennen;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;


public class WettDialog extends javax.swing.JDialog {

    Wettbuero.Wette result;

    /**
     * Creates a new WettDialog
     *
     * @param parent The Form opening this dialog
     * @param schneggen The array of snails to display
     */
    public WettDialog(java.awt.Frame parent, Rennschnecke[] schneggen) {
        super(parent, true);
        initComponents();
        NumberFormat format = NumberFormat.getCurrencyInstance();
        NumberFormatter nf = new NumberFormatter(format);
        nf.setMinimum(0.01);
        nf.setMaximum(2999999.99);
        nf.setAllowsInvalid(false);
        nf.setCommitsOnValidEdit(true);
        nf.setOverwriteMode(false);
        einsatzInput.setFormatterFactory(
                new DefaultFormatterFactory(nf)
        );
        result = null;
        snailList.setModel(new DefaultListModel<>());
        snailList.setListData(schneggen);
    }

    private class SnailBetCellRenderer extends JLabel implements ListCellRenderer<Rennschnecke> {

        @Override
        public Component getListCellRendererComponent(JList<? extends Rennschnecke> jlist, Rennschnecke e, int i, boolean bln, boolean bln1) {
            setText(e.toBetString());

            if (bln) {
                this.setBackground(jlist.getSelectionBackground());
                this.setForeground(jlist.getSelectionForeground());
            } else {
                this.setBackground(jlist.getBackground());
                this.setForeground(jlist.getForeground());
            }
            setEnabled(jlist.isEnabled());
            setFont(jlist.getFont());
            setOpaque(true);
            return this;
        }

    }

    public Wettbuero.Wette showDialog() {
        setVisible(true);
        return result;
    }

    private String getInputErrors() {
        StringBuilder builder = new StringBuilder();

        String enteredName = nameInput.getText();
        if (enteredName.trim().isEmpty()) {
            builder.append("Bitte einen gültigen Namen eingeben!\n");
        }
        
        if(snailList.getSelectedValue() == null) {
            builder.append("Bitte eine Schnecke auswählen!");
        }

        return builder.toString();
    }
    
    private void processReturn() {
        String errors = getInputErrors();
        if (errors.isEmpty()) {
            Number einsatz = (Number) einsatzInput.getValue();
            result = new Wettbuero.Wette(
                    einsatz.doubleValue(),
                    nameInput.getText(),
                    snailList.getSelectedValue()
            );
            setVisible(false);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, errors);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogMainPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        nameInput = new javax.swing.JTextField();
        einsatzLabel = new javax.swing.JLabel();
        snailListScroll = new javax.swing.JScrollPane();
        snailList = new javax.swing.JList<>();
        cancelButton = new javax.swing.JButton();
        confirmButton = new javax.swing.JButton();
        einsatzInput = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Wette Abgeben");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        nameLabel.setText("Name:");

        einsatzLabel.setText("Einsatz:");

        snailList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        snailList.setCellRenderer(new SnailBetCellRenderer());
        snailListScroll.setViewportView(snailList);

        cancelButton.setText("Abbrechen");
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelClickedHandler(evt);
            }
        });

        confirmButton.setText("OK");
        confirmButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                okClickedHandler(evt);
            }
        });

        einsatzInput.setValue(new Double(10.0));

        javax.swing.GroupLayout dialogMainPanelLayout = new javax.swing.GroupLayout(dialogMainPanel);
        dialogMainPanel.setLayout(dialogMainPanelLayout);
        dialogMainPanelLayout.setHorizontalGroup(
            dialogMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogMainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dialogMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(snailListScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
                    .addGroup(dialogMainPanelLayout.createSequentialGroup()
                        .addGroup(dialogMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameLabel)
                            .addComponent(einsatzLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dialogMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(einsatzInput)
                            .addComponent(nameInput)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialogMainPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cancelButton)
                        .addGap(18, 18, 18)
                        .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        dialogMainPanelLayout.setVerticalGroup(
            dialogMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogMainPanelLayout.createSequentialGroup()
                .addGroup(dialogMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dialogMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(einsatzLabel)
                    .addComponent(einsatzInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(snailListScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dialogMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(confirmButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dialogMainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dialogMainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelClickedHandler(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelClickedHandler
        result = null;
        setVisible(false);
        dispose();
    }//GEN-LAST:event_cancelClickedHandler

    private void okClickedHandler(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okClickedHandler
        processReturn();
    }//GEN-LAST:event_okClickedHandler

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            processReturn();
        }
    }//GEN-LAST:event_formKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton confirmButton;
    private javax.swing.JPanel dialogMainPanel;
    private javax.swing.JFormattedTextField einsatzInput;
    private javax.swing.JLabel einsatzLabel;
    private javax.swing.JTextField nameInput;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JList<Rennschnecke> snailList;
    private javax.swing.JScrollPane snailListScroll;
    // End of variables declaration//GEN-END:variables
}