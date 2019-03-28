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
	private int life;   //��
	private int doubleFire; //����ֵ
	public Hero() {
		super(97,124,140,400);
		life = 3; //Ĭ��3����
		doubleFire = 0; //Ĭ�ϻ���ֵΪ0(��������)
	}
	
	/** Ӣ�ۻ�������궯  x/y:����x�����y����*/
	public void moveTo(int x,int y){
		this.x = x-this.width/2;  //Ӣ�ۻ���x=����x-1/2Ӣ�ۻ��Ŀ�
		this.y = y-this.height/2; //Ӣ�ۻ���y=����y-1/2Ӣ�ۻ��ĸ�
	}
	
	/** Ӣ�ۻ������ӵ�(�����ӵ�����) */
	public Bullet[] shoot() {
		int xStep = this.width/4;
		int yStep = 20;
		if(doubleFire > 0) {
			Bullet[] bs = new Bullet[2];
			bs[0] = new Bullet(this.x+1*xStep, this.y-yStep);
			bs[1] = new Bullet(this.x+3*xStep,this.y-yStep); //x:Ӣ�ۻ���x+3/4Ӣ�ۻ��Ŀ� y:Ӣ�ۻ���y-�̶���20
			doubleFire -= 2;
			return bs;
		}else {
			Bullet[] bs = new Bullet[1];
			bs[0] = new Bullet(this.x+2*xStep,this.y-yStep); //x:Ӣ�ۻ���x+2/4Ӣ�ۻ��Ŀ� y:Ӣ�ۻ���y-�̶���20
			return bs;
		}
	}
	
	/** ��дstep()�ƶ� */
	public void step(){
	}
	
	int index = 0; //���ŵ�Ӣ�ۻ�����ʼ�±�
	int deadIndex = 2; //���˵�Ӣ�ۻ�����ʼ�±�
	/** ��дgetImage()��ȡͼƬ */
	public BufferedImage getImage(){ //ÿ10������һ��
		if(isLife()){ //��������
			return images[index++%2]; //images[0]��images[1]�л�
		}else if(isDead()){ //�����˵�
			BufferedImage img = images[deadIndex++]; //�ӵ�3��ͼƬ��ʼ��ȡ
			if(deadIndex==images.length){ //���������һ��ͼƬ
				state=REMOVE; //��ǰ״̬�޸�Ϊɾ��
			}
			return img; 
		}
		return null;
	}
	
	/** ��дoutOfBounds()Խ���� */
	public boolean outOfBounds(){
		return false; //����Խ��
	}
	
	/** Ӣ�ۻ����� */
	public void addLife(){
		life++; //������1
	}
	
	/** ��ȡӢ�ۻ����� */
	public int getLife(){
		return life; //��������
	}
	
	/** Ӣ�ۻ����� */
	public void subtractLife(){
		life--; //������1
	}
	
	/** Ӣ�ۻ������� */
	public void addDoubleFire(){
		doubleFire+=40; //����ֵ��40
	}
	
	/** ��ջ���ֵ */
	public void clearDoubleFire(){
		doubleFire = 0; //����ֵ����
	}
}








