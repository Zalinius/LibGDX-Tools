package com.darzalgames.libgdxtools.errorhandling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import com.darzalgames.darzalcommon.functional.Do;
import com.darzalgames.darzalcommon.functional.Runnables;

public class DesktopCrashPopup extends JFrame {
	
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(false);
		CrashReport crashReport = new CrashReport("Test Game", "1.0.1", "linux", Instant.now(), UUID.randomUUID(), CrashHandler.getStackTraceString(new RuntimeException("Test Exception lol")));
		DesktopCrashPopup crashPopup = new DesktopCrashPopup(crashReport, ()-> DesktopCrashHandler.reportCrashToDarBot5000(crashReport.getGameName(), "file", crashReport.toJson()));
		crashPopup.setVisible(true);
	}
	
	public static final double PADDING_TO_HEIGHT_RATIO = 0.025;
	public static final double FONT_TO_HEIGHT_RATIO = 0.015;
	public static final int BASE_HEIGHT = 800; // in pixels
	
	public DesktopCrashPopup(CrashReport crashReport, Runnable sendErrorReport) {
		
		super(crashReport.getGameName() + " - crash reporting");
        setSize(BASE_HEIGHT, BASE_HEIGHT);
        setLocationRelativeTo(null);
        setResizable(true);
        BorderLayout borderLayout = new BorderLayout(getSmallPadding(), getSmallPadding());
        setLayout(borderLayout);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        String reportFileLocation = "REPLACEME";
        
        JPanel informationPanel = new JPanel();
        Border topPadding = BorderFactory.createEmptyBorder(getSmallPadding(), getLargePadding(), 0, getLargePadding());
        informationPanel.setBorder(topPadding);
        BoxLayout boxLayout = new BoxLayout(informationPanel, BoxLayout.Y_AXIS);
        informationPanel.setLayout(boxLayout);
        
        JLabel situationLabel = makeLabel("Unfortunately the game has crashed :(");
        situationLabel.setFont(getRegularFont());
        informationPanel.add(situationLabel);
        JLabel crashReportFileLabel = makeLabel("The following report was saved to: " + reportFileLocation);
        informationPanel.add(crashReportFileLabel);
        JLabel crashReportHeader = makeLabel("Crash Report:");
        informationPanel.add(crashReportHeader);
        add(informationPanel, BorderLayout.NORTH);

        String crString = crashReport.toString();
        for (int i = 0; i < 100; i++) {
			crString += "\n" + i + " some text and stuff";
			if(i % 10 == 0) {
				crString += "\n" + i + " some text and stuff and a really long line that will have to wrap around to the next line because it is so darn long you know what I mean?";
			}
		}
        
        JTextArea crashReportArea = new JTextArea(crString);
//        JTextArea crashReportArea = new JTextArea(crashReport.toString());
        crashReportArea.setFont(getMonoSpaceFont());
        crashReportArea.setEditable(false);
        crashReportArea.setLineWrap(true);
        crashReportArea.setWrapStyleWord(true);
        
        JScrollPane crashReportScrollPane = new JScrollPane(crashReportArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        crashReportScrollPane.setBorder(BorderFactory.createEmptyBorder(0, getLargePadding(), 0, getLargePadding()));

        add(crashReportScrollPane, BorderLayout.CENTER);
        
        
        JPanel buttonPanel = new JPanel();
        Border buttonPadding = BorderFactory.createEmptyBorder(getSmallPadding(), getLargePadding(), getLargePadding(), getLargePadding());
        buttonPanel.setBorder(buttonPadding);
        buttonPanel.setLayout(new GridLayout(1, 3, getSmallPadding(), 0));

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
	
	private int getSmallPadding() {
		return (int) (PADDING_TO_HEIGHT_RATIO * getHeight());
	}

	private int getLargePadding() {
		return 2 * getSmallPadding();
	}
	
	private Font getRegularFont() {
		int size = (int) (FONT_TO_HEIGHT_RATIO * getHeight());
		return new Font(Font.SANS_SERIF, Font.BOLD, size);
		
	}
	
	private Font getMonoSpaceFont() {
		int size = (int) (FONT_TO_HEIGHT_RATIO * getHeight());
		return new Font(Font.MONOSPACED, Font.PLAIN, size);
	}
	
	private JLabel makeLabel(String text) {
		JLabel jLabel = new JLabel(text);
		jLabel.setFont(getRegularFont());
		return jLabel;
	}

	private JLabel makeLabel(String text, int alignment) {
		JLabel jLabel = new JLabel(text, alignment);
		jLabel.setFont(getRegularFont());
		return jLabel;
	}

}
