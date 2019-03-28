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
	
	private int speed;  //�ƶ��ٶ�
	/** ���췽�� */
	public Airplane(){
		super(49,36);
		speed = 2;
	}

	/** ��дstep()�ƶ� */
	public void step(){
		y+=speed; //y+(����)
	}
	int deadIndex = 1; //���˵�С�л�����ʼ�±�
	
	public BufferedImage getImage() {
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
		return this.y>=World.HEIGHT; //С�л���y>=���ڵĸߣ���ΪԽ����
	}
	
	/** ��дgetScore()�÷� */
	public int getScore(){
		return 1; //���һ��С�л�����ҵ�1��
	}
}
