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
	public static final int WIDTH = 400;  //���ڵĿ�
	public static final int HEIGHT = 700; //���ڵĸ�
	
	public static final int START = 0;     //����״̬
	public static final int RUNNING = 1;   //����״̬
	public static final int PAUSE = 2;     //��ͣ״̬
	public static final int GAME_OVER = 3; //��Ϸ����״̬
	private int state = START; //��ǰ״̬(Ĭ��Ϊ����״̬)
	
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;

	static{ //��ʼ����̬��Դ
		start = FlyingObject.loadImage("start.png");
		pause = FlyingObject.loadImage("pause.png");
		gameover = FlyingObject.loadImage("gameover.png");
	}
	private Sky sky = new Sky(); //���
	private Hero hero = new Hero(); //Ӣ�ۻ�
	private FlyingObject[] enemies = {}; //����(С�л�����л���С�۷�)����
	private Bullet[] bullets = {}; //�ӵ�����

	//�ӵ�����Ĵ������Ƕ������ڵģ�������Hero����
	/** ��������(С�л�����л���С�۷�)���� */
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
	public void enterAction() { //ÿ10������һ��
		flyEnterIndex++;
		if(flyEnterIndex%40 == 0) {//ÿ400��������һ������
			FlyingObject obj = nextOne();
			enemies = Arrays.copyOf(enemies,enemies.length+1);
			enemies[enemies.length-1] = obj;
		}
	}
	
	/**�������ƶ�*/
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
	
	int shootIndex = 0; //�������
	/** �ӵ��볡(Ӣ�ۻ������ӵ�) */
	public void shootAction(){ //ÿ10������һ��
		shootIndex++; //ÿ10������1
		if(shootIndex%30==0){ //ÿ300(10*30)������һ��
			Bullet[] bs = hero.shoot(); //��ȡ�ӵ��������
			bullets = Arrays.copyOf(bullets,bullets.length+bs.length); //����(bs�м���Ԫ�ؾ����󼸸�����)
			System.arraycopy(bs,0,bullets,bullets.length-bs.length,bs.length); //�����׷��
		}
	}
	
	/** ɾ��Խ��ķ����� */
	public void outOfBoundsAction(){ //ÿ10������һ��
		int index = 0; //1)��Խ����������±�  2)��Խ����˸���
		FlyingObject[] enemyLives = new FlyingObject[enemies.length]; //��Խ���������
		for(int i=0;i<enemies.length;i++){ //������������
			FlyingObject f = enemies[i]; //��ȡÿһ������
			if(!f.outOfBounds() && !f.isRemove()){ //��Խ�粢�Ҳ���ɾ��״̬��
				enemyLives[index] = f; //����Խ�������ӵ���Խ�����������
				index++; //1)��Խ����������±���һ  2)��Խ����˸�����һ
			}
		}
		enemies = Arrays.copyOf(enemyLives,index); //����Խ��������鸴�Ƶ�enemies�У�index�м�����enemies�ĳ���Ϊ��
		
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
	
	int score = 0; //��ҵĵ÷�
	/** �ӵ�����˵���ײ */
	public void bulletBangAction(){ //ÿ10������һ��
		for(int i=0;i<bullets.length;i++){ //���������ӵ�
			Bullet b = bullets[i]; //��ȡÿһ���ӵ�
			for(int j=0;j<enemies.length;j++){ //�������е���
				FlyingObject f = enemies[j]; //��ȡÿһ������
				if(b.isLife() && f.isLife() && f.hit(b)){ //ײ����
					b.goDead(); //�ӵ�ȥ��
					f.goDead(); //����ȥ��
					if(f instanceof Enemy){ //����ײ�����ǵ���
						Enemy e = (Enemy)f; //����ײ����ǿתΪ��������
						score += e.getScore(); //��ҵ÷�
					}
					if(f instanceof Award){ //����ײ�����ǽ���
						Award a = (Award)f; //����ײ����ǿתΪ��������
						int type = a.getType(); //��ȡ��������
						switch(type){ //���ݽ�����������ȡ��ͬ�Ľ���
						case Award.DOUBLE_FIRE:   //��������Ϊ����ֵ
							hero.addDoubleFire(); //��Ӣ�ۻ�������
							break;
						case Award.LIFE:    //��������Ϊ��
							hero.addLife(); //��Ӣ�ۻ�����
							break;
						}
					}
				}
			}
		}
	}
	
	/** Ӣ�ۻ�����˵���ײ */
	public void heroBangAction(){ //ÿ10������һ��
		for(int i=0;i<enemies.length;i++){ //�������е���
			FlyingObject f = enemies[i]; //��ȡÿһ������
			if(f.isLife() && hero.isLife() && f.hit(hero)){ //ײ����
				f.goDead(); //����ȥ��
				hero.subtractLife(); //Ӣ�ۻ�����
				hero.clearDoubleFire(); //Ӣ�ۻ���ջ���ֵ
			}
		}
	}
	
	/** �����Ϸ���� */
	public void checkGameOverAction(){ //ÿ10������һ��
		if(hero.getLife()<=0){ //��Ϸ������
			state=GAME_OVER; //�޸ĵ�ǰ״̬Ϊ��Ϸ����״̬
		}
	}
	
	/** ���������ִ�� */
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
						state=START; //�޸�Ϊ����״̬
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
		this.addMouseMotionListener(l);//��껬���¼�
		
		Timer timer = new Timer(); //��ʱ������
		int interval = 10; //ʱ����(�Ժ���Ϊ��λ)
		timer.schedule(new TimerTask(){
			public void run(){ //ÿ10������һ��(��ʱ�ɵ��Ǹ���)
				if(state==RUNNING){ //����״̬ʱִ��
					enterAction(); //����(С�л�����л���С�۷�)�볡
					stepAction();  //�������ƶ�
					shootAction();
					outOfBoundsAction();;
					bulletBangAction();
					heroBangAction();
					checkGameOverAction();
				}
				repaint();     //�ػ�(���µ���paint())
			}
		},interval,interval); //�ճ̱�
	}
	
	/** ��дpaint()�� */
	public void paint(Graphics g){
		sky.paintObject(g);  //�����
		hero.paintObject(g); //��Ӣ�ۻ�
		for(int i=0;i<enemies.length;i++){ //�������е���(С�л�����л���С�۷�)
			enemies[i].paintObject(g); //�����˶���
		}
		for(int i=0;i<bullets.length;i++){ //���������ӵ�
			bullets[i].paintObject(g); //���ӵ�����
		}
		g.drawString("SCORE:"+score,10,25); //����
		g.drawString("LIFE:"+hero.getLife(),10,45); //����

		switch(state){ //���ݵ�ǰ״̬����ͬ��ͼ
		case START: //����״̬ʱ������ͼ
			g.drawImage(start,0,0,null);
			break;
		case PAUSE: //��ͣ״̬ʱ����ͣͼ
			g.drawImage(pause,0,0,null);
			break;
		case GAME_OVER: //��Ϸ����״̬ʱ����Ϸ����ͼ
			g.drawImage(gameover,0,0,null);
			break;
		}
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame(); //���
		World world = new World(); //���
		frame.setSize(WIDTH,HEIGHT); //���ô��ڴ�С
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //���ô��ڹر�ʱ�˳�����
		frame.setLocationRelativeTo(null); //���ô��ھ�����ʾ
		frame.setVisible(true); //1)���ô��ڿɼ�  2)�������paint()
	}
}
