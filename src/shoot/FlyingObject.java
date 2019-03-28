package shoot;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;



public abstract  class FlyingObject {
	public static final int LIFE = 0;   //活着的
	public static final int DEAD = 1;   //死了的
	public static final int REMOVE = 2; //删除的
	protected int state = LIFE; //当前状态(默认为活着的)
	
	protected int width;  //宽
	protected int height; //高
	protected int x;      //x坐标
	protected int y;      //y坐标
	
	//专门给英雄机、天空、子弹准备的
	public FlyingObject(int x,int y,int width,int height){
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
	}
	//专门给小敌机、大敌机、小蜜蜂准备的
	public FlyingObject(int width,int height){
		this.width = width;
		this.height = height;
		Random rand = new Random(); //随机数对象
		x = rand.nextInt(World.WIDTH-this.width); //0到(窗口宽-敌人宽)之间的随机数
		y = -this.height; //负的敌人的高
	}
	
	/** 加载/读取图片 */
	public static BufferedImage loadImage(String fileName) {
		try {
			BufferedImage img = ImageIO.read(FlyingObject.class.getResource(fileName));
			return img;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	/** 飞行物移动 */
	public abstract void step();
	
	public abstract BufferedImage getImage();
	
	/** 画对象 g:画笔 */
	public void paintObject(Graphics g) {
		g.drawImage(getImage(), x, y,null);
	}
	
	/** 判断对象是否是活着的 */
	public boolean isLife(){
		return state==LIFE;
	}
	
	public boolean isDead(){
		return state==DEAD;
	}
	
	/** 判断对象是否是删除了的 */
	public boolean isRemove(){
		return state==REMOVE;
	}
	
	/** 飞行物越界检查 */
	public abstract boolean outOfBounds();
	
	/** 检测碰撞  this:敌人  other:子弹或英雄机  */
	public boolean hit(FlyingObject other){
		int x1 = this.x-other.width;  //x1:敌人的x-子弹的宽
		int x2 = this.x+this.width;   //x2:敌人的x+敌人的宽
		int y1 = this.y-other.height; //y1:敌人的y-子弹的高
		int y2 = this.y+this.height;  //y2:敌人的y+敌人的高
		int x = other.x;              //x:子弹的x
		int y = other.y;              //y:子弹的y
		
		return x>=x1 && x<=x2 && y>=y1 && y<=y2; //x在x1与x2之间，并且，y在y1与y2之间，即为撞上了
	}
	
	/** 飞行物去死 */
	public void goDead(){
		state = DEAD; //修改对象状态为Dead状态
	}
}













