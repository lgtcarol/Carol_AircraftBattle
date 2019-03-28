package shoot;

import java.awt.image.BufferedImage;
import java.util.Random;


public class Bee extends FlyingObject{
	private static BufferedImage[] images; //ͼƬ����
	static {
		images = new BufferedImage[5];
		for(int i=0;i<images.length;i++){ //����ͼƬ����
			images[i] = loadImage("bee"+i+".png");
		}
	}
	
	private int xSpeed;    //x�����ƶ��ٶ�
	private int ySpeed;    //y�����ƶ��ٶ�
	private int awardType; //��������(0��1)
	/** ���췽�� */
	public Bee(){
		super(60,50);
		xSpeed = 1;
		ySpeed = 2;
		Random rand = new Random(); //���������
		awardType = rand.nextInt(2); //0��1֮��������
	}

	
	public void step() {
		x += xSpeed;
		y += ySpeed;
		if(x<=0 || x >=World.WIDTH-this.width) {
			xSpeed *= -1;
		}
	}
	
	int deadIndex = 1; //���˵�С�۷����ʼ�±�
	/** ��дgetImage()��ȡͼƬ */
	public BufferedImage getImage(){
		if(isLife()){ //�������أ��򷵻ص�1��ͼƬ
			return images[0];
		}else if(isDead()){ //�����˵�
			BufferedImage img = images[deadIndex++]; //�ӵ�2��ͼƬ��ʼ��ȡ
			if(deadIndex==images.length){ //���������һ��ͼƬ
				state=REMOVE; //��ǰ״̬�޸�Ϊɾ��
			}
			return img;
		}
		return null;
	}
	
	/** ��дoutOfBounds()Խ���� */
	public boolean outOfBounds(){
		return this.y>=World.HEIGHT; //С�۷��y>=���ڵĸߣ���ΪԽ����
	}
	
	/** ��дgetType()��ȡ�������� */
	public int getType(){
		return awardType; //���ؽ�������(0��1)
	}
	
}
