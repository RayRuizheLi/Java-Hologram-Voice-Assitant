package model;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;

public class Gif {

	static JFrame f = new JFrame("Animation");
	JLabel label = new JLabel();


	private int change = 1; 

	public void moveUp(){
		label.setBounds(label.getX(), label.getY() + 50, label.getWidth(), label.getHeight()); 
	}

	public void moveDown(){
		label.setBounds(label.getX(), label.getY() - 50, label.getWidth(), label.getHeight()); 
	}

	public void moveRight(){
		label.setBounds(label.getX() + 50, label.getY(), label.getWidth(), label.getHeight()); 
	}

	public void moveLeft(){
		label.setBounds(label.getX() + 50, label.getY(), label.getWidth(), label.getHeight()); 
	}

	public void origin(){
		label.setBounds(0, 0, label.getWidth(), label.getHeight());
	}


	public static void main(String[] args){

		Gif gif = new Gif();

		gif.moveUp();
		gif.origin();

	}

	public static void killWindow(){
		f.dispose();
	}

	public Gif (){


		URL url;
		//	try {
		//url = new URL("source.gif");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();

		f.setLayout(null);
		f.setSize(width, height);
		Icon icon = new ImageIcon(new ImageIcon("waiting.gif").getImage().getScaledInstance(f.getWidth(), f.getHeight(), Image.SCALE_DEFAULT));


		label.setIcon(icon);
		label.setBounds(0, 0, width, height);


		f.getContentPane().add(label);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		f.getContentPane().setBackground(Color.black);

		f.setVisible(true);
		//		} catch (MalformedURLException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}

	}

	public void change(String change ){


		if(change.contains("mouth")){
			Icon icon = new ImageIcon(new ImageIcon("mouth.gif").getImage().getScaledInstance(f.getWidth(), f.getHeight(), Image.SCALE_DEFAULT));
			label.setIcon(icon);
		}
		else if(change.contains("nood")){
			Icon icon = new ImageIcon(new ImageIcon("nood.gif").getImage().getScaledInstance(f.getWidth(), f.getHeight(), Image.SCALE_DEFAULT));
			label.setIcon(icon);
		}
		else if(change.contains("nose")){
			Icon icon = new ImageIcon(new ImageIcon("nose.gif").getImage().getScaledInstance(f.getWidth(), f.getHeight(), Image.SCALE_DEFAULT));
			label.setIcon(icon);
		}
		else if(change.contains("transition to screen")){
			Icon icon = new ImageIcon(new ImageIcon("transition to screen.gif").getImage().getScaledInstance(f.getWidth(), f.getHeight(), Image.SCALE_DEFAULT));
			label.setIcon(icon);
		}
		else if(change.contains("waiting")){
			Icon icon = new ImageIcon(new ImageIcon("waiting.gif").getImage().getScaledInstance(f.getWidth(), f.getHeight(), Image.SCALE_DEFAULT));
			label.setIcon(icon);
		}
		else if(change.contains("dramatic stare")){
			Icon icon = new ImageIcon(new ImageIcon("dramatic stare.gif").getImage().getScaledInstance(f.getWidth(), f.getHeight(), Image.SCALE_DEFAULT));
			label.setIcon(icon);
		}


		// f.pack();
		f.setLocationRelativeTo(null);


	}
}
