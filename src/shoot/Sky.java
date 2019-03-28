package shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sky extends FlyingObject{
	private static BufferedImage image; //ͼƬ
	static{
		image = loadImage("background.png");
	}
	private int speed;//�ƶ��ٶ�
	private int y1;     //y����(���ű����л�)
	public Sky() {
		super(World.WIDTH,World.HEIGHT,0,0);
		speed = 1;
		y1 = -height;
	}
	/** ��дstep()�ƶ� */
	public void step() {
		y += speed;
		y1 += speed;
		if(y >= this.height) {
			y = -this.height;
		}
		if(y1 >= this.height) {
			y1 = -this.height;
		}
	}
	

	/** ��дgetImage()��ȡͼƬ */
	public BufferedImage getImage(){
		return image;
	}
	
	/** ������ g:���� */
	public void paintObject(Graphics g){
		g.drawImage(getImage(),x,y,null); //��ͼƬ
		g.drawImage(getImage(),x,y1,null); //��ͼƬ
	}
	
	/** ��дoutOfBounds()Խ���� */
	public boolean outOfBounds(){
		return false; //����Խ��
	}
}
