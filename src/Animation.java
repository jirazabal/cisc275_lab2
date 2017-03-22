
//J Irazabal 
//with assistance from T Harvey :)
//based loosely on http://www.java2s.com/Code/JavaAPI/java.awt/GraphicsdrawImageImageimgintxintyImageObserverob.htm

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import animationEnums.DIRECTION;

public class Animation extends JPanel {

	

	final int frameCount = 10;
	int picNum = 0;
	BufferedImage[][] pics;
	static int dir = DIRECTION.SOUTHEAST.ordinal(); // assume walking SOUTHEAST
													// initially
	static boolean atTheWall = false;
	int xloc = 0;
	int yloc = 0;
	final int xIncr = 8;
	final int yIncr = 2;
	final static int frameWidth = 800;
	final static int frameHeight = 600;
	final static int imgWidth = 165;
	final static int imgHeight = 165;
	

	public static int changeDirection(int xloc, int yloc) {
		//figures out which direction to travel when at a wall 
		
		if (xloc + imgWidth >= frameWidth && dir == DIRECTION.SOUTHEAST.ordinal()) // right wall
			return DIRECTION.SOUTHWEST.ordinal();
		if (xloc + imgWidth >= frameWidth && dir == DIRECTION.NORTHEAST.ordinal()) // right wall
			return DIRECTION.NORTHWEST.ordinal();
		if (yloc + imgHeight >= frameHeight && dir == DIRECTION.SOUTHWEST.ordinal()) // bottom wall
			return DIRECTION.NORTHWEST.ordinal();
		if (yloc + imgHeight >= frameHeight && dir == DIRECTION.SOUTHEAST.ordinal()) // bottom wall
			return DIRECTION.NORTHEAST.ordinal();
		if (xloc <= 0 && dir == DIRECTION.SOUTHWEST.ordinal()) // left wall
			return DIRECTION.SOUTHEAST.ordinal();
		if (xloc <= 0 && dir == DIRECTION.NORTHWEST.ordinal()) // left wall
			return DIRECTION.NORTHEAST.ordinal();
		if (yloc <= 0 && dir == DIRECTION.NORTHEAST.ordinal()) // top wall
			return DIRECTION.SOUTHEAST.ordinal();
		if (yloc <= 0 && dir == DIRECTION.NORTHWEST.ordinal()) // top wall
			return DIRECTION.SOUTHWEST.ordinal();
		else
			throw new RuntimeException();
	}

	// Override this JPanel's paint method to cycle through picture array and
	// draw images
	public void paint(Graphics g) {
		// if we are at a wall, change direction, then just keep walking.
	
	    
		if (atTheWall()) {
			try {
				dir = changeDirection(xloc, yloc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		keepWalking(g, dir);
	}
    // this function figures out if we hit a wall
	public boolean atTheWall() {
		if (xloc < 0 || yloc < 0 || xloc + imgWidth >= frameWidth || yloc + imgHeight >= frameHeight)
			return atTheWall = true;
		else
			return atTheWall = false;
	}
	
    // this function figures out which way to walk based on current direction and point of origin
	public void keepWalking(Graphics g, int dir) {
		picNum = (picNum + 1) % frameCount;

		if (dir == DIRECTION.SOUTHWEST.ordinal()) {
			 xloc -= xIncr; yloc += yIncr;
		}
		if (dir == DIRECTION.SOUTHEAST.ordinal()) {
			 xloc += xIncr; yloc += yIncr;
		}
		if (dir == DIRECTION.NORTHWEST.ordinal()) {
			 xloc -= xIncr; yloc -= yIncr;
		}
		if (dir == DIRECTION.NORTHEAST.ordinal()) {
			 xloc += xIncr; yloc -= yIncr;
		}
		g.drawImage(pics[dir][picNum], xloc, yloc, Color.gray, this);
	}

	// Make frame, loop on repaint and wait
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new Animation());
		frame.setBackground(Color.gray);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(frameWidth, frameHeight);
		frame.setVisible(true);
		
		JButton b2 = new JButton("ORC SPAWN");
	    b2.setVerticalTextPosition(AbstractButton.BOTTOM);
	    b2.setHorizontalTextPosition(AbstractButton.CENTER);
	    b2.setMnemonic(KeyEvent.VK_M);
	    
		for (int i = 0; i < 1000; i++) {
			frame.repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// Constructor: get image, segment and store in array
	public Animation() {
		BufferedImage[] img = new BufferedImage[8];

		// clockwise starting at NW = 0...
		img[0] = createImage("images/orc/orc_forward_northwest.png");
		img[1] = createImage("images/orc/orc_forward_north.png");
		img[2] = createImage("images/orc/orc_forward_northeast.png");
		img[3] = createImage("images/orc/orc_forward_east.png");
		img[4] = createImage("images/orc/orc_forward_southeast.png");
		img[5] = createImage("images/orc/orc_forward_south.png");
		img[6] = createImage("images/orc/orc_forward_southwest.png");
		img[7] = createImage("images/orc/orc_forward_west.png");

		pics = new BufferedImage[8][10];
		for (int i = 0; i < frameCount; i++)
			for (int j = 0; j < 7; j++)
				pics[j][i] = img[j].getSubimage(imgWidth * i, 0, imgWidth, imgHeight);
	}

	// Read image from file and return
	private BufferedImage createImage(String orcImage) {
		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(new File(orcImage));
			return bufferedImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}