/**
 * Copyright � Siemens AG, 2016
 * 
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package at.siemens.ct.jmz.ui;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JOptionPane;

import at.siemens.ct.jmz.diag.FastDiag;
import at.siemens.ct.jmz.diag.hsdag.ConflictDetectionAlgorithm;
import at.siemens.ct.jmz.diag.hsdag.HSDAG;
import at.siemens.ct.jmz.elements.constraints.Constraint;
import at.siemens.ct.jmz.mznparser.MiniZincCP;

public class VariableDialog {

	private List<Displayable> decisionVariables;
	ArrayList<DecisionVariableGUI> mapWithControls;
	private static File mznFile;
	private MiniZincCP mznCp;
	private List<Constraint> userConstraints;

	private TextArea textLog;
	private Button generateSolution;
	private Frame mainFrame;
	private Panel controlPanel;
	private Choice algorithmType;
	private TextComponentLogger logger;
	private ScrollPane scrollPane;

	private final String SCD_HSDAG = "Simple Conflict Detection - HSDAG";
	private final String QUICKXPLAIN_HSDAG = "QuickXPlain - HSDAG";
	private final String FAST_DIAG = "FastDiag";

	public VariableDialog(File mznFile) throws Exception {
		VariableDialog.mznFile = mznFile;
		mznCp = new MiniZincCP(mznFile);
		decisionVariables = mznCp.getElementsFromFile();
		mapWithControls = new ArrayList<DecisionVariableGUI>();
		userConstraints = new ArrayList<Constraint>();
		controlPanel = new Panel();

		prepareGUI();
		displayDecisionVariables();
		addComponents();

		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);

	}

	public static void main(String[] args) {

		try {

			String mznpath;

			if (args.length > 0) {
				mznpath = args[0];
			} else {
				FileChooserDialog fcd = new FileChooserDialog();
				mznpath = fcd.getFile();
			}

			if (!mznpath.isEmpty())

			{
				mznFile = new File(mznpath);
				if (!mznFile.exists()) {
					JOptionPane.showMessageDialog(null, String.format("The file \"%s\" does not exist!", args[0]),
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				new VariableDialog(mznFile);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "An error occured", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

	}

	private void prepareGUI() {

		final String FRAME_TITLE = "Minimal diagnoses";
		mainFrame = new Frame(FRAME_TITLE);
		mainFrame.setLayout(new FlowLayout());

		controlPanel.setLayout(new FlowLayout());
		mainFrame.add(controlPanel);

		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				mainFrame.dispose();
			}
		});

	}

	private void addComponents() {
		Panel panel = new Panel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		Box algorithmChosing = Box.createHorizontalBox();
		algorithmChosing.setAlignmentX(Box.CENTER_ALIGNMENT);
		Label choseTheAlgorithm = new Label("Chose the algorithm:");
		algorithmChosing.add(choseTheAlgorithm);

		algorithmType = new Choice();
		algorithmType.add(QUICKXPLAIN_HSDAG);
		algorithmType.add(SCD_HSDAG);
		algorithmType.add(FAST_DIAG);

		algorithmChosing.add(algorithmType);
		algorithmChosing.add(Box.createHorizontalGlue());

		generateSolution = new Button("Start diagnosis ...");
		algorithmChosing.add(generateSolution);
		algorithmChosing.setAlignmentX(Box.RIGHT_ALIGNMENT);
		panel.add(algorithmChosing);

		generateSolution.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				generateSolution.setEnabled(false);
				generateSolution();
				generateSolution.setEnabled(true);

			}

		});

		textLog = new TextArea();
		textLog.setEditable(false);
		textLog.setPreferredSize(new Dimension(500, 700));

		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(textLog);

		controlPanel.add(panel);
	}

	private void displayDecisionVariables() {

		Panel dvPanel = new Panel();

		scrollPane = new ScrollPane();

		GroupLayout dvlayout = new GroupLayout(dvPanel);
		dvlayout.setAutoCreateGaps(true);
		dvlayout.setAutoCreateContainerGaps(true);

		ParallelGroup groupForElementsInLine = dvlayout.createParallelGroup(Alignment.LEADING);
		SequentialGroup groupForElementsInColumns = dvlayout.createSequentialGroup();

		for (Displayable displayable : decisionVariables) {

			List<InfoGUI> infos = displayable.getInfo();

			for (InfoGUI infoGUI : infos) {
				DecisionVariableGUI dvGui1 = new DecisionVariableGUI(infoGUI);
				groupForElementsInLine.addGroup((dvlayout.createSequentialGroup().addComponent(dvGui1.getLabel())
						.addComponent(dvGui1.getComponent())));
				groupForElementsInColumns.addGroup((dvlayout.createParallelGroup(Alignment.CENTER)
						.addComponent(dvGui1.getLabel()).addComponent(dvGui1.getComponent())));
				mapWithControls.add(dvGui1);
			}

		}

		dvlayout.setHorizontalGroup(groupForElementsInLine);
		dvlayout.setVerticalGroup(groupForElementsInColumns);
		dvPanel.setLayout(dvlayout);
		dvPanel.setBackground(Color.lightGray);

		Collections.sort(mapWithControls);

		Dimension scrollPaneDimension = new Dimension(dvPanel.getPreferredSize().width + 30,
				dvPanel.getPreferredSize().height + 35);

		scrollPane.setPreferredSize(new Dimension(250, 700));

		if (scrollPaneDimension.height < scrollPane.getPreferredSize().height) {
			scrollPane.setPreferredSize(scrollPaneDimension);
		}

		scrollPane.add(dvPanel);
		controlPanel.add(scrollPane);
	}

	private List<Constraint> getAllValuesFromTheInterface() throws Exception {

		ArrayList<Constraint> userConstraints = new ArrayList<Constraint>();

		for (Displayable dv : decisionVariables) {

			List<DecisionVariableGUI> controls = getVariableControls(dv.getName());

			String values = getControlsValues(controls);

			List<Constraint> constraints = dv.createConstraint(values);

			if (constraints != null) {
				userConstraints.addAll(constraints);
			}

		}

		return userConstraints;

	}

	private String getControlsValues(List<DecisionVariableGUI> controls) {

		StringBuilder values = new StringBuilder();

		for (DecisionVariableGUI decisionVariableGUI : controls) {
			values.append(decisionVariableGUI.getVariableValue());
			if (controls.indexOf(decisionVariableGUI) != controls.size() - 1) {
				values.append(",");
			}
		}

		return values.toString();
	}

	private List<DecisionVariableGUI> getVariableControls(String variableName) {

		if (mapWithControls.isEmpty() || mapWithControls == null) {
			return null;

		}
		List<DecisionVariableGUI> controls = new ArrayList<DecisionVariableGUI>();

		for (DecisionVariableGUI decisionVariableGUI : mapWithControls) {

			if (decisionVariableGUI.getVariableName() == variableName) {
				controls.add(decisionVariableGUI);
			}

		}
		return controls;

	}

	private void generateSolution() {
		textLog.setText(null);

		try {

			userConstraints = getAllValuesFromTheInterface();

			String selectedAlgorithm = algorithmType.getSelectedItem();
			HSDAG hsdag;
			logger = new TextComponentLogger(textLog, userConstraints);
			switch (selectedAlgorithm) {
			case SCD_HSDAG:
				hsdag = new HSDAG(mznFile.getAbsolutePath(), userConstraints, logger,
						ConflictDetectionAlgorithm.SimpleConflictDetection);
				logger.displayStartMessage(mznFile);
				hsdag.diagnose();
				break;
			case QUICKXPLAIN_HSDAG:
				hsdag = new HSDAG(mznFile.getAbsolutePath(), userConstraints, logger,
						ConflictDetectionAlgorithm.QuickXPlain);
				logger.displayStartMessage(mznFile);
				hsdag.diagnose();
				break;
			case FAST_DIAG:
				FastDiag fastDiag = new FastDiag(mznFile.getAbsolutePath(), userConstraints, logger);
				logger.displayStartMessage(mznFile);
				fastDiag.diagnose();
			}

		} catch (Exception ex) {

			JOptionPane.showMessageDialog(controlPanel, String.format("An error occured! %s", ex.getMessage()), "Error",
					JOptionPane.ERROR_MESSAGE);

			ex.printStackTrace();
		}

	}

}
