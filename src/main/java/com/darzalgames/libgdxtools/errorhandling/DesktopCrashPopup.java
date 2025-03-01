package com.darzalgames.libgdxtools.errorhandling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class DesktopCrashPopup extends JFrame {
	
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(false);
		CrashReport crashReport = new CrashReport("Test Game", "1.0.1", "linux", Instant.now(), UUID.randomUUID(), CrashHandler.getStackTraceString(new RuntimeException("Test Exception lol")));
		DesktopCrashPopup crashPopup = new DesktopCrashPopup(crashReport, ()-> DesktopCrashHandler.reportCrashToDarBot5000(crashReport.getGameName(), "file", crashReport.toJson()));
		crashPopup.setVisible(true);
	}
	
	public DesktopCrashPopup(CrashReport crashReport, Runnable sendErrorReport) {
		super(crashReport.getGameName() + " - crash reporting");
        setSize(1000, 750);
        setResizable(true);
        BorderLayout borderLayout = new BorderLayout(20, 20);
        setLayout(borderLayout);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JLabel situationLabel = new JLabel("Unfortunately the game has crashed :(", JLabel.CENTER);
        add(situationLabel, BorderLayout.NORTH);
        
        JTextArea crashReportArea = new JTextArea(crashReport.toString());
        crashReportArea.setEditable(false);
        crashReportArea.setCursor(null);
        crashReportArea.setOpaque(false);  
        crashReportArea.setFocusable(false);
        crashReportArea.setLineWrap(true);
        crashReportArea.setWrapStyleWord(true);

        add(crashReportArea, BorderLayout.CENTER);
        
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 20, 20));

        JButton buttonSendReport = new JButton("Send report to DarZal Games");
        buttonSendReport.setBackground(Color.ORANGE);
		Runnable sendcallbackRunnable = () -> {
			buttonSendReport.setEnabled(false);
			buttonSendReport.setText("Sending . . .");
			sendErrorReport.run();				
			buttonSendReport.setText("SENT!");
		};
		buttonSendReport.addActionListener(new SingleUseThreadedAction(sendcallbackRunnable));

		
		JButton buttonCopy = new JButton("Copy crash report");
		buttonCopy.addActionListener(e -> {
			copyTextToClipboard(crashReport.toString());
			buttonCopy.setText("Copied!");
		});

		
        JButton buttonClose = new JButton("Exit");
        buttonClose.addActionListener(e -> System.exit(1));
        
        buttonPanel.add(buttonSendReport);
        buttonPanel.add(buttonCopy);
        buttonPanel.add(buttonClose);
		
		add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private void copyTextToClipboard(String text) {
		StringSelection stringSelection = new StringSelection(text);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}
	
	private static class SingleUseThreadedAction implements ActionListener{
		private final AtomicBoolean notRun;
		private final Runnable singleUseRunnable;
		
		public SingleUseThreadedAction(Runnable runnable) {
			this.notRun = new AtomicBoolean(true);
			this.singleUseRunnable = runnable;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(notRun.getAndSet(false)) {
				Thread thread = new Thread(singleUseRunnable);
				thread.start();
			}
		}
		
		
	}

}
