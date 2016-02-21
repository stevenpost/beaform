/*
 * This file is part of RCIRC
 * Copyright (C) 2009  Steven Post
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package beaform.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class About implements Runnable {
	private static Logger log = LoggerFactory.getLogger(About.class);
	private static JFrame frm = new JFrame("About...");
	private static JPanel pane = new JPanel();
	private static JLabel lblTitle = new JLabel();
	private static JLabel lblAuthor = new JLabel();
	private static JTextArea txtLicense = new JTextArea();

	@Override
	public void run() {
		createAndShowGUI();
	}

	private void init(){

		//Create pane and add components
		pane.setLayout(new GridBagLayout()); //create pane
		GridBagConstraints c = new GridBagConstraints();
		//c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;

		c.gridx = 0;
		c.gridy = 0;
		lblTitle.setText("BeaForm");
		pane.add(lblTitle, c);

		c.gridx = 0;
		c.gridy = 1;
		lblAuthor.setText("By Steven Post");
		pane.add(lblAuthor, c);

		c.gridx = 0;
		c.gridy = 2;
		txtLicense.setText("This program is distributed under the MIT License.\n You can re-distribute, and/or modify the program under the conditions of the MIT License.");
		txtLicense.setEditable(false);
		pane.add(txtLicense, c);

		pane.setBorder(BorderFactory.createEmptyBorder(20, /*top*/20, /*left*/20, /*bottom*/20)/*right*/);

	}

	public static void createAndShowGUI() {
		//Set the look and feel.
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e){
			log.error("Look'n feel: " + e.getMessage());
		}

		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		//Create and set up the window.
		//JFrame frame = new JFrame("GUI testing");
		frm.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		About app = new About();
		app.init();
		Component contents = pane;
		frm.getContentPane().add(contents, BorderLayout.CENTER);
		frm.setLocation(150, 150);

		//Display the window.
		frm.setMinimumSize(new Dimension(200, 100));
		frm.pack();
		//frame.setResizable(false);
		frm.setVisible(true);
		//frame.setSize(235, 165);
	}
}
