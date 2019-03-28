package shoot;

import java.awt.image.BufferedImage;


public class BigAirplane extends FlyingObject{
	private static BufferedImage[] images; //ͼƬ����
	static{
		images = new BufferedImage[5]; //5��ͼƬ
		for(int i=0;i<images.length;i++){ //����ͼƬ����
			images[i] = loadImage("bigplane"+i+".png");
		}
	}
	
	private int speed;  //�ƶ��ٶ�
	/** ���췽�� */
	public BigAirplane(){
		super(69,99);
		speed = 2;
	}
	

	
	/** ��дstep()�ƶ� */
	public void step(){
		y+=speed; //y+(����)
	}
	int deadIndex = 1; //���˵Ĵ�л�����ʼ�±�
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
		return this.y>=World.HEIGHT; //��л���y>=���ڵĸߣ���ΪԽ����
	}
	
	/** ��дgetScore()�÷� */
	public int getScore(){
		return 3; //���һ����л�����ҵ�3��
	}
}
