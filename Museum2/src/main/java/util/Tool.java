package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class Tool {

	public static void main(String[] args) {
		
		/*Tool.saveFile(new Member("abc","def","789","taipei","000"), "c:/ABC/member.txt");
		
		System.out.println(Tool.readFile("c:/ABC/member.txt"));
		
		/*Member m=(Member)Tool.readFile("c:/ABC/member.txt");
		System.out.println(m.getName()+"\t"+m.getUsername()+"\t"+m.getPassword());*/

	}
	
	
	//存物件檔
	public static void saveFile(Object object,String fileName)
	{
		try {
			FileOutputStream fos=new FileOutputStream(fileName);
			ObjectOutputStream oos=new ObjectOutputStream(fos);
			oos.writeObject(object);
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}


	
	
	//讀取物件檔
	public static Object readFile(String fileName)
	{
		Object object=null;
		
		try {
			FileInputStream fis=new FileInputStream(fileName);
			ObjectInputStream ois=new ObjectInputStream(fis);
			object=ois.readObject();
			
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		return object;
	}

}
