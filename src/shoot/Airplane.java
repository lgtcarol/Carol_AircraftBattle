package shoot;

import java.awt.image.BufferedImage;


public class Airplane extends FlyingObject{
	private static BufferedImage[] images;
	static {
		images = new BufferedImage[5];
		for(int i=0; i<images.length; i++) {
			images[i] = loadImage("airplane"+i+".png");
		}
	}
	
	private int speed;  //移动速度
	/** 构造方法 */
	public Airplane(){
		super(49,36);
		speed = 2;
	}

	/** 重写step()移动 */
	public void step(){
		y+=speed; //y+(向下)
	}
	int deadIndex = 1; //死了的小敌机的起始下标
	
	public BufferedImage getImage() {
		if(isLife()){ //若活着呢，则返回第1张图片
			return images[0];
		}else if(isDead()){ //若死了的
			BufferedImage img = images[deadIndex++]; //从第2张图片开始获取
			if(deadIndex==images.length){ //若到了最后一张图片
				state=REMOVE; //当前状态修改为删除
			}
			return img;
		}
		return null;
	}
	
	/** 重写outOfBounds()越界检查 */
	public boolean outOfBounds(){
		return this.y>=World.HEIGHT; //小敌机的y>=窗口的高，即为越界了
	}
	
	/** 重写getScore()得分 */
	public int getScore(){
		return 1; //打掉一个小敌机，玩家得1分
	}
}
