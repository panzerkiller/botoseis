/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package botoseis.mainGui.prmview;

import static botoseis.mainGui.prmview.ProcessParameter.KEYVALUEPAIR;

public class NumericParameterView extends ParameterView {

    public NumericParameterView(ProcessParameter p) {
        super(p);

        m_input.setMinimumSize(new java.awt.Dimension(70, 20));
        m_input.setMaximumSize(new java.awt.Dimension(70, 20));
        m_input.setPreferredSize(new java.awt.Dimension(70, 20));

        add(m_input);

        m_labelBrief.setText(m_prm.brief);
        add(m_labelBrief);

    }

    @Override
    public void setValue(String v) {
        m_input.setText(v);
    }

    @Override
    public String getValue() {
        return m_input.getText();
    }

    @Override
    public String getCommandLine() {
        final String ret;
        if (m_prm.keyvaluePair.equalsIgnoreCase(KEYVALUEPAIR)) {
            ret = m_prm.name + "=" + m_input.getText();
        } else {
            ret = m_input.getText();
        }
        return ret;
    }

    @Override
    public String getKeyValuePair() {
        return m_prm.name + "=" + m_input.getText();
    }
    // Variables declaration
    private final javax.swing.JTextField m_input = new javax.swing.JTextField();
}
