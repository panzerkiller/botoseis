/*
 * 
 */
package botoseis.mainGui.usrproject;

import java.io.*;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.IOException;

import botoseis.mainGui.utils.DefaultNode;
import static botoseis.mainGui.utils.DefaultNode.LINE_TYPE;
import static java.lang.System.getProperty;
import java.util.ArrayList;
import java.util.List;
import static javax.swing.JOptionPane.showMessageDialog;
import static org.w3c.dom.Node.COMMENT_NODE;
import static org.w3c.dom.Node.ELEMENT_NODE;
import static org.w3c.dom.Node.TEXT_NODE;

/**
 *
 */
public class UserProject {

    public UserProject(String home, String title) {
        m_prjHomeDir = home;
        m_title = title;

        String fsep = getProperty("file.separator");
        String path = m_prjHomeDir + fsep + "GlobalLine";
        java.io.File f = new java.io.File(path);
        if (f.mkdir()) {
            m_globalLine = new ProcessingLine(path, "GlobalLine");
            m_lines.add(m_globalLine);
            m_selectedLine = m_globalLine;
        }
    }

    public String getTitle() {
        return m_title;
    }

    public String getprojHomeDir() {
        return this.m_prjHomeDir;
    }

    public void open(String home) {
        String fsep = getProperty("file.separator");
        m_prjHomeDir = home;
        m_title = home.substring(home.lastIndexOf('/') + 1, home.length());
        String xmlFile = m_prjHomeDir + fsep + "info.xml";

        DOMParser parser = new DOMParser();

        m_lines.clear();

        try {
            parser.parse(xmlFile);

            Document document = parser.getDocument();
            NodeList nodes = document.getChildNodes();
            int len = (nodes != null) ? nodes.getLength() : 0;
            Node node;
            for (int i = 0; i < len; i++) {
                node = nodes.item(i);
                switch (node.getNodeType()) {
                    case COMMENT_NODE:
                        break;
                    case ELEMENT_NODE:
                        readElementNode(node);
                        break;
                }
            }
        } catch (SAXException se) {
        } catch (IOException ioe) {
            showMessageDialog(null, "Problem");
        }

    }

    private void readElementNode(Node pParent) {
        NodeList nodes = pParent.getChildNodes();
        int len = (nodes != null) ? nodes.getLength() : 0;
        Node node;
        for (int i = 0; i < len; i++) {
            node = nodes.item(i);
            switch (node.getNodeType()) {
                case TEXT_NODE:
                    break;
                case ELEMENT_NODE:
                    if (node.getNodeName().equalsIgnoreCase("title")) {
                        m_title = node.getChildNodes().item(0).getNodeValue();
                    }
                    if (node.getNodeName().equalsIgnoreCase("author")) {
                    }
                    if (node.getNodeName().equalsIgnoreCase("comments")) {
                    }
                    if (node.getNodeName().equalsIgnoreCase("line")) {
                        ProcessingLine newLine = new ProcessingLine("", "");
                        String s = node.getChildNodes().item(0).getNodeValue();
                        String fsep = getProperty("file.separator");
                        newLine.open(m_prjHomeDir + fsep + s);

                        m_lines.add(newLine);
                    }

                    break;
            }
        }
    }

    public void save() {
        String fsep = getProperty("file.separator");
        String xmlFile = m_prjHomeDir + fsep + "info.xml";
        File of = new File(xmlFile);
        FileWriter outF;
        try {
            outF = new FileWriter(of);

            outF.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            outF.write("<BotoSeisProject>\n");
            outF.write("    <title>" + m_title + "</title>\n");
            outF.write("    <author></author>\n");
            outF.write("    <comments></comments>\n");
            for (int i = 0; i < m_lines.size(); i++) {
                outF.write("    <line>" + m_lines.get(i).getTitle() + "</line>\n");
            }
            for (int i = 0; i < m_lines.size(); i++) {
                m_lines.get(i).save();
            }
            outF.write("</BotoSeisProject>");
            outF.close();
        } catch (IOException ex) {
            showMessageDialog(null, ex.toString());
        }
    }

    public void reloadLines() {
        String fsep = getProperty("file.separator");
        String xmlFile = m_prjHomeDir + fsep + "info.xml";
        File of = new File(xmlFile);
        FileWriter outF;
        try {
            outF = new FileWriter(of);

            outF.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            outF.write("<BotoSeisProject>\n");
            outF.write("    <title>" + m_title + "</title>\n");
            outF.write("    <author></author>\n");
            outF.write("    <comments></comments>\n");
            for (int i = 0; i < m_lines.size(); i++) {
                outF.write("    <line>" + m_lines.get(i).getTitle() + "</line>\n");
            }

            for (int i = 0; i < m_lines.size(); i++) {
                m_lines.get(i).reloadFlow();
            }
            outF.write("</BotoSeisProject>");
            outF.close();
        } catch (IOException ex) {
            showMessageDialog(null, ex.toString());
        }
    }

    public void reloadFlows(String path) {
        for (int i = 0; i < m_lines.size(); i++) {
            if (m_lines.get(i).getHomedir().equals(path)) {
                m_lines.get(i).save();
            }
        }
    }

    public void close() {
    }

    public void addLine(String pTitle) {
        // Test for duplicated line title
        boolean titleDuplicated = false;
        for (int i = 0; i < m_lines.size(); i++) {
            if (m_lines.get(i).getTitle().equalsIgnoreCase(pTitle)) {
                titleDuplicated = true;
                break;
            }

        }

        if (!titleDuplicated) {
            String fsep = getProperty("file.separator");
            String path = m_prjHomeDir + fsep + pTitle;
            java.io.File f = new java.io.File(path);
            if (f.mkdir()) {
                ProcessingLine newLine = new ProcessingLine(path, pTitle);
                m_lines.add(newLine);
                m_selectedLine = newLine;
            }
        }
    }

    public ProcessingLine selectLine(String s) {

        m_selectedLine = m_globalLine;
        for (int i = 0; i < m_lines.size(); i++) {
            if (m_lines.get(i).getTitle().equalsIgnoreCase(s)) {
                m_selectedLine = m_lines.get(i);
                break;
            }
        }
        return m_selectedLine;
    }

    public void removeLine(String pTitle) {

        for (int i = 0; i < m_lines.size(); i++) {
            if (m_lines.get(i).getTitle().equalsIgnoreCase(pTitle)) {
                m_lines.remove(i);
            }
        }

        String fsep = getProperty("file.separator");
        String path = m_prjHomeDir + fsep + pTitle;
        deleteDir(new File(path));
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String children1 : children) {
                boolean success = deleteDir(new File(dir, children1));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public boolean addWorkflow(String pTitle) {
        return m_selectedLine.addWorkflow(pTitle);
    }

    public botoseis.mainGui.workflows.WorkflowModel selectWorkflow(String pName) {
        final botoseis.mainGui.workflows.WorkflowModel ret;
        if (m_selectedLine != null) {
            ret = m_selectedLine.selectWorkflow(pName);
        } else {
            ret = null;
        }
        return ret;
    }

    public void fillLinesList(DefaultNode root) {
        for (int i = 0; i < m_lines.size(); i++) {
            ProcessingLine line = m_lines.get(i);
            if (line.getTitle().equalsIgnoreCase("GlobalLine")) {
                line.fillWorkflowsList(root);
            } else {
                DefaultNode node = new DefaultNode(line, LINE_TYPE);
                root.add(node);
                line.fillWorkflowsList(node);
            }
        }
    }

    @Override
    public String toString() {
        return m_title;
    }

    //
    private String m_prjHomeDir;
    private String m_title;
    public static final int ErrorIO = 1;
    public static final int ErrorDuplicatedTitle = 2;
    // Lines
    List<ProcessingLine> m_lines = new ArrayList<>();
    ProcessingLine m_globalLine;
    ProcessingLine m_selectedLine;
}
