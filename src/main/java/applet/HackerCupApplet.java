package applet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class HackerCupApplet extends JApplet {
    private static final Dimension HGAP5 = new Dimension(5, 1);
    private static final String BOLD_ITALIC = "BoldItalic";

    private STControlPanel controlPanel;
    private JTextPane textPane;

    @Override
    public void init() {
        String laf = UIManager.getCrossPlatformLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException exc) {
            System.err.println("Warning: UnsupportedLookAndFeel: " + laf);
        } catch (Exception exc) {
            System.err.println("Error loading " + laf + ": " + exc);
        }

        controlPanel = new STControlPanel();

        textPane = new JTextPane();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(BorderLayout.NORTH, controlPanel);
        getContentPane().add(BorderLayout.CENTER, textPane);
    }


    private class STControlPanel extends JPanel {
        private JButton uploadButton;
        private JButton downloadButton;

        public STControlPanel() {
            uploadButton = new JButton( "Upload input file" );
            downloadButton = new JButton( "Download output file" );
            downloadButton.setEnabled(false);

            uploadButton.addActionListener(
                    new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            JFileChooser fc = new JFileChooser();
                            //fc.setCurrentDirectory( new File( "C:\\" ) );
                            int returnVal = fc.showSaveDialog(HackerCupApplet.this);
                            String fileName = "*.*";
                            if (returnVal == JFileChooser.APPROVE_OPTION) {
                                File aFile = fc.getSelectedFile();
                                fileName = aFile.getName();
                            }
                            System.out.println(fileName);
                            downloadButton.setEnabled(true);
                        }
                    }
            );

            downloadButton.addActionListener(
                    new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            downloadButton.setEnabled(false);
                        }
                    }
            );

            JPanel panel1 = new JPanel();
            panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
            panel1.add(Box.createRigidArea(HGAP5));
            panel1.add(uploadButton, BorderLayout.WEST);
            panel1.add(Box.createRigidArea(HGAP5));
            panel1.add(downloadButton, BorderLayout.WEST);

            setLayout(new BorderLayout());
            add(panel1, BorderLayout.NORTH);
        }


    }
}