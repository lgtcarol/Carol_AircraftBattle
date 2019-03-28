package shoot;

import javax.swing.JFrame;
import javax.swing.JPanel;


import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;



public class World extends JPanel{
	public static final int WIDTH = 400;  //窗口的宽
	public static final int HEIGHT = 700; //窗口的高
	
	public static final int START = 0;     //启动状态
	public static final int RUNNING = 1;   //运行状态
	public static final int PAUSE = 2;     //暂停状态
	public static final int GAME_OVER = 3; //游戏结束状态
	private int state = START; //当前状态(默认为启动状态)
	
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;

	static{ //初始化静态资源
		start = FlyingObject.loadImage("start.png");
		pause = FlyingObject.loadImage("pause.png");
		gameover = FlyingObject.loadImage("gameover.png");
	}
	private Sky sky = new Sky(); //天空
	private Hero hero = new Hero(); //英雄机
	private FlyingObject[] enemies = {}; //敌人(小敌机、大敌机、小蜜蜂)数组
	private Bullet[] bullets = {}; //子弹数组

	//子弹数组的创建不是独立存在的，依附于Hero对象
	/** 创建敌人(小敌机、大敌机、小蜜蜂)对象 */
	public FlyingObject nextOne() {
		Random rand = new Random();
		int type = rand.nextInt(20);
		if(type < 6) {
			return new Airplane();
		}else if(type < 12) {
			return new BigAirplane();
		}else {
			return new Bee();
		}
	}

	int flyEnterIndex = 0;
	public void enterAction() { //每10毫秒走一次
		flyEnterIndex++;
		if(flyEnterIndex%40 == 0) {//每400毫秒生成一个对象
			FlyingObject obj = nextOne();
			enemies = Arrays.copyOf(enemies,enemies.length+1);
			enemies[enemies.length-1] = obj;
		}
	}
	
	/**飞行物移动*/
	public void stepAction() {
		sky.step();
		for(int i=0; i<enemies.length; i++)
		{
			enemies[i].step();
		}
		for(int i=0; i<bullets.length; i++)
		{
			bullets[i].step();
		}
	}
	
	int shootIndex = 0; //射击计数
	/** 子弹入场(英雄机发射子弹) */
	public void shootAction(){ //每10毫秒走一次
		shootIndex++; //每10毫秒增1
		if(shootIndex%30==0){ //每300(10*30)毫秒走一次
			Bullet[] bs = hero.shoot(); //获取子弹数组对象
			bullets = Arrays.copyOf(bullets,bullets.length+bs.length); //扩容(bs有几个元素就扩大几个容量)
			System.arraycopy(bs,0,bullets,bullets.length-bs.length,bs.length); //数组的追加
		}
	}
	
	/** 删除越界的飞行物 */
	public void outOfBoundsAction(){ //每10毫秒走一次
		int index = 0; //1)不越界敌人数组下标  2)不越界敌人个数
		FlyingObject[] enemyLives = new FlyingObject[enemies.length]; //不越界敌人数组
		for(int i=0;i<enemies.length;i++){ //遍历敌人数组
			FlyingObject f = enemies[i]; //获取每一个敌人
			if(!f.outOfBounds() && !f.isRemove()){ //不越界并且不是删除状态的
				enemyLives[index] = f; //将不越界敌人添加到不越界敌人数组中
				index++; //1)不越界敌人数组下标增一  2)不越界敌人个数增一
			}
		}
		enemies = Arrays.copyOf(enemyLives,index); //将不越界敌人数组复制到enemies中，index有几个则enemies的长度为几
		
		index = 0;
		Bullet[] bulletLives = new Bullet[bullets.length];
		for(int i=0;i<bullets.length;i++){
			Bullet b = bullets[i];
			if(!b.outOfBounds() && !b.isRemove()){
				bulletLives[index] = b;
				index++;
			}
		}
		bullets = Arrays.copyOf(bulletLives,index);
		
	}
	
	int score = 0; //玩家的得分
	/** 子弹与敌人的碰撞 */
	public void bulletBangAction(){ //每10毫秒走一次
		for(int i=0;i<bullets.length;i++){ //遍历所有子弹
			Bullet b = bullets[i]; //获取每一个子弹
			for(int j=0;j<enemies.length;j++){ //遍历所有敌人
				FlyingObject f = enemies[j]; //获取每一个敌人
				if(b.isLife() && f.isLife() && f.hit(b)){ //撞上了
					b.goDead(); //子弹去死
					f.goDead(); //敌人去死
					if(f instanceof Enemy){ //若被撞对象是敌人
						Enemy e = (Enemy)f; //将被撞对象强转为敌人类型
						score += e.getScore(); //玩家得分
					}
					if(f instanceof Award){ //若被撞对象是奖励
						Award a = (Award)f; //将被撞对象强转为奖励类型
						int type = a.getType(); //获取奖励类型
						switch(type){ //根据奖励类型来获取不同的奖励
						case Award.DOUBLE_FIRE:   //奖励类型为火力值
							hero.addDoubleFire(); //则英雄机增火力
							break;
						case Award.LIFE:    //奖励类型为命
							hero.addLife(); //则英雄机增命
							break;
						}
					}
				}
			}
		}
	}
	
	/** 英雄机与敌人的碰撞 */
	public void heroBangAction(){ //每10毫秒走一次
		for(int i=0;i<enemies.length;i++){ //遍历所有敌人
			FlyingObject f = enemies[i]; //获取每一个敌人
			if(f.isLife() && hero.isLife() && f.hit(hero)){ //撞上了
				f.goDead(); //敌人去死
				hero.subtractLife(); //英雄机减命
				hero.clearDoubleFire(); //英雄机清空火力值
			}
		}
	}
	
	/** 检测游戏结束 */
	public void checkGameOverAction(){ //每10毫秒走一次
		if(hero.getLife()<=0){ //游戏结束了
			state=GAME_OVER; //修改当前状态为游戏结束状态
		}
	}
	
	/** 启动程序的执行 */
	public void action(){
		MouseAdapter l = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				if(state == RUNNING) {
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}
			}
			public void mouseClicked(MouseEvent e) {
				switch(state) {
					case START:
						state = RUNNING;
						break;
					case GAME_OVER:
						score = 0;
						sky = new Sky();
						hero = new Hero();
						enemies = new FlyingObject[0];
						bullets = new Bullet[0];
						state=START; //修改为启动状态
						break;
				}
			}
			
			public void mouseExited(MouseEvent e) {
				if(state == RUNNING) {
					state = PAUSE;
				}
			}
			public void mouseEntered(MouseEvent e) {
				if(state == PAUSE ) {
					state = RUNNING;
				}
			}
		};
		this.addMouseListener(l);
		this.addMouseMotionListener(l);//鼠标滑动事件
		
		Timer timer = new Timer(); //定时器对象
		int interval = 10; //时间间隔(以毫秒为单位)
		timer.schedule(new TimerTask(){
			public void run(){ //每10毫秒走一次(定时干的那个事)
				if(state==RUNNING){ //运行状态时执行
					enterAction(); //敌人(小敌机、大敌机、小蜜蜂)入场
					stepAction();  //飞行物移动
					shootAction();
					outOfBoundsAction();;
					bulletBangAction();
					heroBangAction();
					checkGameOverAction();
				}
				repaint();     //重画(重新调用paint())
			}
		},interval,interval); //日程表
	}
	
	/** 重写paint()画 */
	public void paint(Graphics g){
		sky.paintObject(g);  //画天空
		hero.paintObject(g); //画英雄机
		for(int i=0;i<enemies.length;i++){ //遍历所有敌人(小敌机、大敌机、小蜜蜂)
			enemies[i].paintObject(g); //画敌人对象
		}
		for(int i=0;i<bullets.length;i++){ //遍历所有子弹
			bullets[i].paintObject(g); //画子弹对象
		}
		g.drawString("SCORE:"+score,10,25); //画分
		g.drawString("LIFE:"+hero.getLife(),10,45); //画命

		switch(state){ //根据当前状态画不同的图
		case START: //启动状态时画启动图
			g.drawImage(start,0,0,null);
			break;
		case PAUSE: //暂停状态时画暂停图
			g.drawImage(pause,0,0,null);
			break;
		case GAME_OVER: //游戏结束状态时画游戏结束图
			g.drawImage(gameover,0,0,null);
			break;
		}
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame(); //相框
		World world = new World(); //相板
		frame.setSize(WIDTH,HEIGHT); //设置窗口大小
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //设置窗口关闭时退出程序
		frame.setLocationRelativeTo(null); //设置窗口居中显示
		frame.setVisible(true); //1)设置窗口可见  2)尽快调用paint()
	}
}
