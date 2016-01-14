package com.noahsaso.timer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
//import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TTimer {
	
	//Define stuff
	public static JFrame frame = new JFrame("Timer");
	public static JPanel panel = new JPanel();
	public static JPanel fieldPanel = new JPanel();
	
	public static JButton startStopButton = new JButton("Start");
	public static JButton pausePlayButton = new JButton("Pause");
	
	public static JLabel label = new JLabel("0.0");
	public static JLabel paddingLabel = new JLabel(" ");
	public static JLabel paddingLabel2 = new JLabel(" ");
	public static JTextField fields = new JTextField();
	public static JTextField fieldm = new JTextField();
	public static JTextField fieldh = new JTextField();
	
	public static Timer timer = new Timer();
	
	public static boolean isPaused = false;
	public static boolean isRunning = false;
	//Stop defining stuff
	
	public static void main(String[] args) {
		
		//Setup stuff
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(200, 180));
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		fields.setPreferredSize(new Dimension(60, 25));
		fields.setHorizontalAlignment(JTextField.CENTER);
		fieldm.setPreferredSize(new Dimension(60, 25));
		fieldm.setHorizontalAlignment(JTextField.CENTER);
		fieldh.setPreferredSize(new Dimension(60, 25));
		fieldh.setHorizontalAlignment(JTextField.CENTER);
		
		//Set tooltips
		startStopButton.setToolTipText("Start timer");
		pausePlayButton.setToolTipText("Pause timer");
		fields.setToolTipText("Seconds for timer");
		fieldm.setToolTipText("Minutes for timer");
		fieldh.setToolTipText("Hours for timer");
		label.setToolTipText("Timer");
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		//panel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		//Center all fields
		fields.setAlignmentX(Component.CENTER_ALIGNMENT);
		fieldm.setAlignmentX(Component.CENTER_ALIGNMENT);
		fieldh.setAlignmentX(Component.CENTER_ALIGNMENT);
		startStopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		pausePlayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		paddingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		paddingLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//Make window exactly in center
		Point newLocation = frame.getLocation();
		newLocation.x-=100;
		newLocation.y-=90;
		frame.setLocation(newLocation);
		//Stop setting up stuff
		
		//Add listeners
		startStopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				startStopTimer();
			}
		});
		pausePlayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				pausePlayTimer();
			}
		});
		
		//Add stuff
		fieldPanel.setLayout(new GridLayout(1,3));
		fieldPanel.add(fieldh);
		fieldPanel.add(fieldm);
		fieldPanel.add(fields);
		
		panel.add(fieldPanel);
		panel.add(startStopButton);
		panel.add(pausePlayButton);
		panel.add(label);
		panel.add(paddingLabel);
		panel.add(paddingLabel2);
		//Done adding stuff
		
		//Add panel stuff
		frame.getContentPane().add(panel);
		
		//Show frame
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public static void startStopTimer() {
		System.out.println("Starting...");
		
		if(isRunning) {
			stopTimer();
			return;
		}
		
		if(isPaused) {
			isPaused = false;
			pausePlayButton.setText("Pause");
			pausePlayButton.setToolTipText("Pause timer");
			return;
		}
		
		if(timer == null)
			timer = new Timer();
		
		//Get string
		String SEC = fields.getText();
		if(SEC.equals(""))
			SEC = "0";
		String MIN = fieldm.getText();
		if(MIN.equals(""))
			MIN = "0";
		String HOU = fieldh.getText();
		if(HOU.equals(""))
			HOU = "0";
		
		//Check if string is number
		if(!isNumeric(SEC)||!isNumeric(MIN)||!isNumeric(HOU)) {
			label.setText("Please specify a number");
			return;
		}else {
			label.setText(String.valueOf(Integer.valueOf(SEC)+(Integer.valueOf(MIN)*60)+(Integer.valueOf(HOU)*3600)));
		}
		
		//Start timer
		timer.schedule(new TimerTask() {
			public void run() {
				count();
			}
		}, 0, 100);
		
		startStopButton.setText("Stop");
		startStopButton.setToolTipText("Stop timer");
	}
	
	public static void pausePlayTimer() {
		System.out.println("Toggling timer...");
		isPaused = !isPaused;
		if(isPaused) {
			pausePlayButton.setText("Play");
			pausePlayButton.setToolTipText("Play timer");
		}
		else {
			pausePlayButton.setText("Pause");
			pausePlayButton.setToolTipText("Pause timer");
		}
	}
	
	public static void stopTimer() {
		System.out.println("Stopping...");
		
		if(timer == null) {
			label.setText("0.0");
			return;
		}
		
		//Stop/cancel timer
		timer.cancel();
		timer = null;
		
		label.setText("0.0");
		
		isPaused = false;
		pausePlayButton.setText("Pause");
		pausePlayButton.setToolTipText("Pause timer");
		isRunning = false;
		
		startStopButton.setText("Start");
		startStopButton.setToolTipText("Start timer");
	}
	
	public static void count() {
		if(isPaused) return;
		
		//Check if is not number
		if(!isNumeric(label.getText())) {
			timer.cancel();
			timer = null;
			label.setText("Please specify a number");
			return;
		}
		
		//Subtract 0.1 and set the label
		double oldCount = Double.parseDouble(label.getText());
		oldCount -= 0.1;
		oldCount = Math.round(oldCount*100.0)/100.0;
		if(oldCount<=0.0) oldCount = 0.0;
		label.setText(String.valueOf(oldCount));
		
		//If timer is done, say Done! and shake window
		if(oldCount == 0.0) {
			label.setText("Done!");
			timer.cancel();
			timer = null;
			
			isRunning = false;
			
			try {
				//Shake
				vibrate(frame, 10, 2);
				Thread.sleep(650);
				vibrate(frame, 10, 2);
				Thread.sleep(650);
				vibrate(frame, 10, 2);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}else {
			isRunning = true;
		}
	}
	
	public static boolean isNumeric(String str) {
		//Attempt to parse string with numbers
		try {
			@SuppressWarnings("unused")
			double d = Double.parseDouble(str);
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public static void vibrate(Frame theFrame, int VIBRATION_LENGTH, int VIBRATION_VELOCITY) {
		//Vibrate!!!
		try {
			final int originalX = theFrame.getLocationOnScreen().x;
			final int originalY = theFrame.getLocationOnScreen().y;
			for(int i = 0; i < VIBRATION_LENGTH; i++) {
				Thread.sleep(10);
				theFrame.setLocation(originalX, originalY + VIBRATION_VELOCITY);
				Thread.sleep(10);
				theFrame.setLocation(originalX, originalY - VIBRATION_VELOCITY);
				Thread.sleep(10);
				theFrame.setLocation(originalX + VIBRATION_VELOCITY, originalY);
	  			Thread.sleep(10);
	  			theFrame.setLocation(originalX, originalY);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
