package shoot;

import java.awt.image.BufferedImage;

public class Hero extends FlyingObject{
	
	private static BufferedImage[] images;
	static {
		images = new BufferedImage[6];
		for(int i=0; i < images.length; i++) {
			images[i] = loadImage("hero"+i+".png");
		}
	}
	private int life;   //命
	private int doubleFire; //火力值
	public Hero() {
		super(97,124,140,400);
		life = 3; //默认3条命
		doubleFire = 0; //默认火力值为0(单倍火力)
	}
	
	/** 英雄机随着鼠标动  x/y:鼠标的x坐标和y坐标*/
	public void moveTo(int x,int y){
		this.x = x-this.width/2;  //英雄机的x=鼠标的x-1/2英雄机的宽
		this.y = y-this.height/2; //英雄机的y=鼠标的y-1/2英雄机的高
	}
	
	/** 英雄机发射子弹(创建子弹对象) */
	public Bullet[] shoot() {
		int xStep = this.width/4;
		int yStep = 20;
		if(doubleFire > 0) {
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x+1*xStep, this.y-yStep);
			bs[1] = new Bullet(this.x+3*xStep,this.y-yStep); //x:英雄机的x+3/4英雄机的宽 y:英雄机的y-固定的20
			doubleFire -= 2;
			return bs;
		}else {
			Bullet[] bs = new Bullet[1];
			bs[0] = new Bullet(this.x+2*xStep,this.y-yStep); //x:英雄机的x+2/4英雄机的宽 y:英雄机的y-固定的20
			return bs;
		}
	}
	
	/** 重写step()移动 */
	public void step(){
	}
	
	int index = 0; //活着的英雄机的起始下标
	int deadIndex = 2; //死了的英雄机的起始下标
	/** 重写getImage()获取图片 */
	public BufferedImage getImage(){ //每10毫秒走一次
		if(isLife()){ //若活着呢
			return images[index++%2]; //images[0]和images[1]切换
		}else if(isDead()){ //若死了的
			BufferedImage img = images[deadIndex++]; //从第3张图片开始获取
			if(deadIndex==images.length){ //若到了最后一张图片
				state=REMOVE; //当前状态修改为删除
			}
			return img; 
		}
		return null;
	}
	
	/** 重写outOfBounds()越界检查 */
	public boolean outOfBounds(){
		return false; //永不越界
	}
	
	/** 英雄机增命 */
	public void addLife(){
		life++; //命数增1
	}
	
	/** 获取英雄机的命 */
	public int getLife(){
		return life; //返回命数
	}
	
	/** 英雄机减命 */
	public void subtractLife(){
		life--; //命数减1
	}
	
	/** 英雄机增火力 */
	public void addDoubleFire(){
		doubleFire+=40; //火力值增40
	}
	
	/** 清空火力值 */
	public void clearDoubleFire(){
		doubleFire = 0; //火力值归零
	}
}








