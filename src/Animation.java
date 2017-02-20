
//T Harvey
//based loosely on http://www.java2s.com/Code/JavaAPI/java.awt/GraphicsdrawImageImageimgintxintyImageObserverob.htm

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Animation extends JPanel {

	public enum DIRECTION {
		NORTHWEST(0), NORTH(1), NORTHEAST(2), EAST(3), SOUTHEAST(4), SOUTH(5), SOUTHWEST(6), WEST(7);

		private int direction;

		DIRECTION(int dir) {
			direction = dir;
		}

		public static int getDirection(int oldXLoc, int xLoc, int oldYLoc, int yLoc) {
			System.out.println("oldXLoc: " + oldXLoc);
			System.out.println("xLoc: " + xLoc);
			System.out.println("oldYLoc: " + oldYLoc);
			System.out.println("yLoc: " + yLoc);

			if (xLoc > oldXLoc && yLoc > oldYLoc)
				return DIRECTION.SOUTHEAST.ordinal();
			if (xLoc < oldXLoc && yLoc < oldYLoc)
				return DIRECTION.NORTHWEST.ordinal();
			if (xLoc > oldXLoc && yLoc < oldYLoc)
				return DIRECTION.NORTHEAST.ordinal();
			if (xLoc < oldXLoc && yLoc > oldYLoc)
				return DIRECTION.SOUTHWEST.ordinal();
			if (xLoc > oldXLoc)
				return DIRECTION.EAST.ordinal();
			if (xLoc < oldXLoc)
				return DIRECTION.WEST.ordinal();
			if (yLoc > oldYLoc)
				return DIRECTION.SOUTH.ordinal();
			if (yLoc < oldYLoc)
				return DIRECTION.NORTH.ordinal();
			else
				return 0;

		}
	}

	final int frameCount = 10;
	int picNum = 0;
	BufferedImage[][] pics;
	int dir = 0; // clockwise starting at NW = 0...
	boolean atTheWall = false;
	int xloc = 0;
	int yloc = 0;
	final int xIncr = 8;
	final int yIncr = 2;
	final static int frameWidth = 800;
	final static int frameHeight = 600;
	final static int imgWidth = 165;
	final static int imgHeight = 165;

	// Override this JPanel's paint method to cycle through picture array and
	// draw images
	public void paint(Graphics g) {
		picNum = (picNum + 1) % frameCount;
		int oldXLoc = xloc;
		int oldYLoc = yloc;
		if (!(yloc + imgHeight > frameHeight) && !(xloc + imgWidth > frameWidth)) {
			if (!atTheWall && xloc < frameHeight) {
				dir = DIRECTION.getDirection(oldXLoc, xloc + xIncr, oldYLoc, yloc + yIncr);
				g.drawImage(pics[dir][picNum], xloc += xIncr, yloc += yIncr, Color.gray, this);
			} 
			if (!atTheWall && xloc >= frameHeight) {
				dir = DIRECTION.getDirection(oldXLoc, xloc + xIncr, oldYLoc, yloc - yIncr);
				g.drawImage(pics[dir][picNum], xloc += xIncr, yloc -= yIncr, Color.gray, this);
			} 
			if (atTheWall && xloc > 0) {
				dir = DIRECTION.getDirection(oldXLoc, xloc - xIncr, oldYLoc, yloc + yIncr);
				g.drawImage(pics[dir][picNum], xloc -= xIncr, yloc += yIncr, Color.gray, this);
			}
			
			if (atTheWall && xloc <= 0) {
				dir = DIRECTION.getDirection(oldXLoc, xloc - xIncr, oldYLoc, yloc - yIncr);
				g.drawImage(pics[dir][picNum], xloc -= xIncr, yloc -= yIncr, Color.gray, this);
			}

			if ((yloc + imgHeight > frameHeight) || (xloc + imgWidth > frameWidth)) {
				atTheWall=true;
				xloc-=xIncr;
				yloc-=yIncr;
			}
			if ((xloc <= 0 && yloc <= 0) || (xloc < 0 || yloc < 0))   {
				atTheWall = false;

			}
		}
	}

	// TODO: Keep the orc from walking off-screen, turn around when bouncing off
	// walls.
	// Be sure that animation picture direction matches what is happening on
	// screen.

	// Make frame, loop on repaint and wait
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new Animation());
		frame.setBackground(Color.gray);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(frameWidth, frameHeight);
		frame.setVisible(true);
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

		// TODO: Change this constructor so that at least eight orc animation
		// pngs are loaded
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

		// TODO: Change this method so you can load other orc animation bitmaps
	}
}