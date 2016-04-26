package com.ntobler.space;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class GUI extends JFrame {
	
	private static GUI instance = null;
	
	private JLabel label;
	private JButton button;
	
	public static GUI getInstance() {
		
		if (instance == null) {
			instance = new GUI();
		}
		return instance;
	}
	
	public GUI() {
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new GUIDispatcher());
        
        
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        
        label = new JLabel("Label");
		
		button = new JButton("Button");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    button.setText(e.toString());
			  }
		});
		
		panel.add(label);
		panel.add(button);
		
		
		//add(panel);
		setSize(500,500);
		setVisible(true);
		
		setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
	}
	
	private class GUIDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
        	
        	if (e.getID() == KeyEvent.KEY_PRESSED) {
        		GUI.getInstance().onKeyPressed(e);
            }
            return false;
        }
    }
	
	private void onKeyPressed(KeyEvent e) {
		
	}

}
