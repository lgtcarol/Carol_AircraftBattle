package shoot;

import java.awt.image.BufferedImage;
import java.util.Random;


public class Bee extends FlyingObject{
	private static BufferedImage[] images; //图片数组
	static {
		images = new BufferedImage[5];
		for(int i=0;i<images.length;i++){ //遍历图片数组
			images[i] = loadImage("bee"+i+".png");
		}
	}
	
	private int xSpeed;    //x坐标移动速度
	private int ySpeed;    //y坐标移动速度
	private int awardType; //奖励类型(0或1)
	/** 构造方法 */
	public Bee(){
		super(60,50);
		xSpeed = 1;
		ySpeed = 2;
		Random rand = new Random(); //随机数对象
		awardType = rand.nextInt(2); //0到1之间的随机数
	}

	
	public void step() {
		x += xSpeed;
		y += ySpeed;
		if(x<=0 || x >=World.WIDTH-this.width) {
			xSpeed *= -1;
		}
	}
	
	int deadIndex = 1; //死了的小蜜蜂的起始下标
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
		return this.y>=World.HEIGHT; //小蜜蜂的y>=窗口的高，即为越界了
	}
	
	/** 重写getType()获取奖励类型 */
	public int getType(){
		return awardType; //返回奖励类型(0或1)
	}
	
}
