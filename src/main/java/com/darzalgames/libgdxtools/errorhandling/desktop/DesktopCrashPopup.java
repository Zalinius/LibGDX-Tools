package com.darzalgames.libgdxtools.errorhandling.desktop;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import javax.swing.*;
import javax.swing.border.Border;

import com.darzalgames.libgdxtools.errorhandling.*;

@SuppressWarnings("serial") // Same-version serialization only
public class DesktopCrashPopup extends JFrame {
	
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(false);
		CrashReport crashReport = new CrashReport("Test Game", "1.0.1", "linux", Instant.now(), UUID.randomUUID(), CrashHandler.getMessageAndStackTraceArray(new RuntimeException("Test Exception lol")));
		CrashLocalization crashLocalization = CrashLocalization.getLocalizationFromCode(Locale.getDefault().getLanguage());
		DesktopCrashPopup crashPopup = new DesktopCrashPopup(crashReport, ()-> DesktopCrashHandler.reportCrashToDarBot5000(crashReport.getGameName(), "file.err.json", crashReport.toJson()), "localfile.err.json", crashLocalization);
		crashPopup.setVisible(true);
		crashPopup.requestFocusOnSendButton();
	}
	
	public static final double PADDING_TO_HEIGHT_RATIO = 0.025;
	public static final double FONT_TO_HEIGHT_RATIO = 0.015;
	public static final double SCREEN_TO_HEIGHT_RATIO = 0.75;
	
	private final JButton buttonSendReport;
	
	public DesktopCrashPopup(CrashReport crashReport, Supplier<ReportStatus> sendErrorReport, String localCrashReportFile, CrashLocalization crashLocalization) {
		super(crashLocalization.getTitleSuffixString(crashReport.getGameName()));
		int initialWindowHeight = getInitialWindowHeight();
        setSize(initialWindowHeight, initialWindowHeight);
        setLocationRelativeTo(null);
        setResizable(true);
        BorderLayout borderLayout = new BorderLayout(getSmallPadding(), getSmallPadding());
        setLayout(borderLayout);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel informationPanel = new JPanel();
        Border topPadding = BorderFactory.createEmptyBorder(getSmallPadding(), getLargePadding(), 0, getLargePadding());
        informationPanel.setBorder(topPadding);
        GridLayout gridLayout = new GridLayout(3, 1);
        informationPanel.setLayout(gridLayout);
        
        JLabel situationLabel = makeLabel(crashLocalization.getReportSituationLabelString(crashReport.getGameName()));
        situationLabel.setFont(getRegularFont());
        informationPanel.add(situationLabel);
        JTextArea crashReportFileArea = new JTextArea(crashLocalization.getReportFileLabelString(localCrashReportFile));
        crashReportFileArea.setFont(getRegularFont());
        crashReportFileArea.setOpaque(false);
        crashReportFileArea.setEditable(false);
        crashReportFileArea.setLineWrap(true);
        crashReportFileArea.setWrapStyleWord(true);
        informationPanel.add(crashReportFileArea);
        JLabel crashReportHeader = makeLabel(crashLocalization.getReportHeaderLabelString());
        informationPanel.add(crashReportHeader);
        
        add(informationPanel, BorderLayout.NORTH);

        JTextArea crashReportArea = new JTextArea(crashReport.toString());
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

        this.buttonSendReport = new JButton(crashLocalization.getSendButtonString());
        buttonSendReport.setFont(getRegularFont());
        buttonSendReport.setBackground(Color.ORANGE);
		Runnable sendcallbackRunnable = () -> {
			buttonSendReport.setEnabled(false);
			buttonSendReport.setText(crashLocalization.getSendingButtonString());
			ReportStatus reportStatus = sendErrorReport.get();
			if(reportStatus.isSuccessful()) {
				buttonSendReport.setText(crashLocalization.getSentSuccessButtonString());
				buttonSendReport.setBackground(Color.GREEN);				
			}
			else {
				buttonSendReport.setText(crashLocalization.getSentFailedButtonString(reportStatus.getShortMessage()));
				buttonSendReport.setBackground(Color.RED.darker());
			}
		};
		buttonSendReport.addActionListener(new SingleUseThreadedAction(sendcallbackRunnable));
		
		JButton buttonCopy = new JButton(crashLocalization.getCopyButtonString());
		buttonCopy.setFont(getRegularFont());
		buttonCopy.addActionListener(e -> {
			copyTextToClipboard(crashReport.toString());
			buttonCopy.setText(crashLocalization.getCopiedButtonString());
		});

		
        JButton buttonClose = new JButton(crashLocalization.getExitButtonString());
        buttonClose.setFont(getRegularFont());
        buttonClose.addActionListener(e -> System.exit(1));
        
        buttonPanel.add(buttonSendReport);
        buttonPanel.add(buttonCopy);
        buttonPanel.add(buttonClose);
		
		add(buttonPanel, BorderLayout.SOUTH);
		
	}
	
	public void requestFocusOnSendButton() {
		buttonSendReport.requestFocusInWindow();
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
		JLabel jLabel = new JLabel(text, JLabel.LEFT);
		jLabel.setFont(getRegularFont());
		return jLabel;
	}
	
	private int getInitialWindowHeight() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		return (int) (SCREEN_TO_HEIGHT_RATIO * gd.getDisplayMode().getHeight());
	}

}
