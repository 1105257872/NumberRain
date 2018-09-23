package com.Dgx.rain;

import java.awt.*;
import java.util.Random;
import javax.swing.JFrame;
 
class RainCanvas extends Canvas implements Runnable {
 
	private int width, height;
	private Image offScreen; // 缓冲图片
	private char[][] charset; // 随机字符集合
	private int[] pos; // 列的起始位置
	private Color[] colors = new Color[30]; // 列的渐变颜色
 
	public RainCanvas(int width, int height) {
		this.width = width;
		this.height = height;
 
		// 生成ASCII可见字符集合
 
		Random rand = new Random();
		charset = new char[width / 5][height / 5];
		for (int i = 0; i < charset.length; i++) {
			for (int j = 0; j < charset[i].length; j++) {
				charset[i][j] = (char) (rand.nextInt(93) + 33);
			}
		}
 
		// 随机化列起始位置
		pos = new int[charset.length];
		for (int i = 0; i < pos.length; i++) {
			pos[i] = rand.nextInt(pos.length);
		}
 
		// 生成从黑色到绿色的渐变颜色，最后一个保持为白色
		for (int i = 0; i < colors.length - 1; i++) {
			colors[i] = new Color(0, 255 / colors.length * (i + 1), 0);
		}
		colors[colors.length - 1] = new Color(255, 255, 255);
 
		setBackground(Color.BLACK);
 
		setSize(width, height);
		setVisible(true);
	}
 
	public void startRain() {
		new Thread(this).start();
	}
 
	public void drawRain() {
 
		if (offScreen == null) {
			return;
		}
		Random rand = new Random();
		Graphics g = offScreen.getGraphics();
		g.clearRect(0, 0, width, height);
		g.setFont(new Font("Arial", Font.PLAIN, 16));
 
		//
 
		for (int i = 0; i < charset.length; i++) {
			int speed = rand.nextInt(5);
			for (int j = 0; j < colors.length; j++) {
				int index = (pos[i] + j) % charset[i].length;
				g.setColor(colors[j]);
 
				g.drawChars(charset[i], index, 1, i * 10, index * 10);
			}
			pos[i] = (pos[i] + 1) % charset[i].length;
		}
	}
 
	@Override
	public void update(Graphics g) {
		paint(g);
	}
 
	public void run() {
		while (true) {
			drawRain();
			repaint();
 
			try {
				Thread.sleep(50); // 可改变睡眠时间以调节速度
			}
 
			catch (InterruptedException e) {
				System.out.println(e);
 
			}
		}
	}
 
	@Override
	public void paint(Graphics g) {
		// 当组件显示时检测是否要创建缓冲图片，在组件还不可见时调用createImage将返回null
		if (offScreen == null) {
			offScreen = createImage(width, height);
		}
 
		g.drawImage(offScreen, 0, 0, this);
	}
}

public class NumRain extends JFrame {
	 
	Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();//获取屏幕大小
	
	private RainCanvas canvas = new RainCanvas(screenSize.width, screenSize.height);
 
	public NumRain() {
		super("NumRain");
		getContentPane().add(canvas);
		setSize(screenSize.width, screenSize.height);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
 
	public static void main(String[] args) {
		NumRain test = new NumRain();
		test.canvas.startRain();
	}
}
