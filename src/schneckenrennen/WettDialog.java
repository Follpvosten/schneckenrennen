package schneckenrennen;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 * A dialog for selecting a snail and betting an amount of money on it.
 * @author Follpvosten
 */
public class WettDialog extends JDialog {

    Wettbuero.Wette result;

    /**
     * Creates a new WettDialog and displays the given array of snails.
     * @param parent The Form opening this dialog
     * @param schneggen The array of snails to display
     * @param wettbueroFactor The factor the Wettbuero uses.
     */
    public WettDialog(Frame parent, ArrayList<Rennschnecke> schneggen, double wettbueroFactor) {
        super(parent, true);
        initComponents();
        NumberFormat format = NumberFormat.getCurrencyInstance();
	format.setMinimumFractionDigits(2);
	format.setMaximumFractionDigits(2);
        NumberFormatter nf = new NumberFormatter(format);
        nf.setMinimum(0.02);
	// The maximum bet value is, well, pretty high.
        nf.setMaximum(Double.MAX_VALUE / wettbueroFactor);
        nf.setAllowsInvalid(false);
        nf.setCommitsOnValidEdit(true);
        nf.setOverwriteMode(false);
        einsatzInput.setFormatterFactory(
                new DefaultFormatterFactory(nf)
        );
        result = null;
        snailList.setModel(new DefaultListModel<>());
        snailList.setListData(schneggen.toArray(new Rennschnecke[schneggen.size()]));
    }
    
    /**
     * The {@link ListCellRenderer} used to display a snail in the list.
     */
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

    /**
     * Shows the Dialog and returns the bet to place when it is closed.
     * @return A new Wette with the entered data.
     */
    public Wettbuero.Wette showDialog() {
        setVisible(true);
        return result;
    }

    /**
     * Checks the enteres values for validity.
     * @return "" if there are no errors; error messages otherwise.
     */
    private String getInputErrors() {
        StringBuilder builder = new StringBuilder();

        String enteredName = nameInput.getText();
        if (enteredName.trim().isEmpty()) {
            builder.append(Translations.get("WettDialog.error.nameEmpty"));
            builder.append('\n');
        }
        
        if(snailList.getSelectedValue() == null) {
            builder.append(Translations.get("WettDialog.error.noSnailSelected"));
        }

        return builder.toString();
    }
    
    /**
     * Performs a check of the entered values. If they are valid, the Dialog
     * is closed and the entered Wette is returned; otherwise, an informative
     * error message will be displayed.
     */
    private void processReturn() {
        String errors = getInputErrors();
        if (errors.isEmpty()) {
            Number einsatz = (Number) einsatzInput.getValue();
            result = new Wettbuero.Wette(
                    einsatz.doubleValue(),
                    nameInput.getText(),
                    snailList.getSelectedValue()
            );
	    if(snailList.getSelectedValue().getName().equals("Blümchen")) {
		if(snailList.getSelectedValue().strengen(nameInput.getText())) {
		    JOptionPane.showMessageDialog(this, "Blümchen wird sich ganz doll strengen!");
		}
	    }
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
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("schneckenrennen/AutoBundle"); // NOI18N
        setTitle(bundle.getString("WettDialog.title")); // NOI18N
        setName("Form"); // NOI18N

        dialogMainPanel.setName("dialogMainPanel"); // NOI18N

        nameLabel.setText(bundle.getString("WettDialog.nameLabel.text")); // NOI18N
        nameLabel.setName("nameLabel"); // NOI18N

        nameInput.setName("nameInput"); // NOI18N
        nameInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nameInputKeyReleased(evt);
            }
        });

        einsatzLabel.setText(bundle.getString("WettDialog.einsatzLabel.text")); // NOI18N
        einsatzLabel.setName("einsatzLabel"); // NOI18N

        snailListScroll.setName("snailListScroll"); // NOI18N

        snailList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        snailList.setCellRenderer(new SnailBetCellRenderer());
        snailList.setName("snailList"); // NOI18N
        snailList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                snailListKeyReleased(evt);
            }
        });
        snailListScroll.setViewportView(snailList);

        cancelButton.setText(bundle.getString("WettDialog.cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelClickedHandler(evt);
            }
        });

        confirmButton.setText(bundle.getString("WettDialog.confirmButton.text")); // NOI18N
        confirmButton.setName("confirmButton"); // NOI18N
        confirmButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                okClickedHandler(evt);
            }
        });

        einsatzInput.setName("einsatzInput"); // NOI18N
        einsatzInput.setValue(new Double(10.0));
        einsatzInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                einsatzInputKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout dialogMainPanelLayout = new javax.swing.GroupLayout(dialogMainPanel);
        dialogMainPanel.setLayout(dialogMainPanelLayout);
        dialogMainPanelLayout.setHorizontalGroup(
            dialogMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogMainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dialogMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(snailListScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
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
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(snailListScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
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

    private void nameInputKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameInputKeyReleased
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
	    processReturn();
	}
    }//GEN-LAST:event_nameInputKeyReleased

    private void einsatzInputKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_einsatzInputKeyReleased
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
	    processReturn();
	}
    }//GEN-LAST:event_einsatzInputKeyReleased

    private void snailListKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_snailListKeyReleased
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
	    processReturn();
	}
    }//GEN-LAST:event_snailListKeyReleased

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
