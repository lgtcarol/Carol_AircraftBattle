package shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Sky extends FlyingObject{
	private static BufferedImage image; //图片
	static{
		image = loadImage("background.png");
	}
	private int speed;//移动速度
	private int y1;     //y坐标(两张背景切换)
	public Sky() {
		super(World.WIDTH,World.HEIGHT,0,0);
		speed = 1;
		y1 = -height;
	}
	/** 重写step()移动 */
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
	

	/** 重写getImage()获取图片 */
	public BufferedImage getImage(){
		return image;
	}
	
	/** 画对象 g:画笔 */
	public void paintObject(Graphics g){
		g.drawImage(getImage(),x,y,null); //画图片
		g.drawImage(getImage(),x,y1,null); //画图片
	}
	
	/** 重写outOfBounds()越界检查 */
	public boolean outOfBounds(){
		return false; //永不越界
	}
}
