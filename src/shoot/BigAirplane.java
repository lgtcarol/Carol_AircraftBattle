package shoot;

import java.awt.image.BufferedImage;


public class BigAirplane extends FlyingObject{
	private static BufferedImage[] images; //图片数组
	static{
		images = new BufferedImage[5]; //5张图片
		for(int i=0;i<images.length;i++){ //遍历图片数组
			images[i] = loadImage("bigplane"+i+".png");
		}
	}
	
	private int speed;  //移动速度
	/** 构造方法 */
	public BigAirplane(){
		super(69,99);
		speed = 2;
	}
	

	
	/** 重写step()移动 */
	public void step(){
		y+=speed; //y+(向下)
	}
	int deadIndex = 1; //死了的大敌机的起始下标
	/** 重写getImage()获取图片 */
	public BufferedImage getImage(){
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
		return this.y>=World.HEIGHT; //大敌机的y>=窗口的高，即为越界了
	}
	
	/** 重写getScore()得分 */
	public int getScore(){
		return 3; //打掉一个大敌机，玩家得3分
	}
}
