package presentation;

import java.io.IOException;

import data.ClassManager;

public class main {
	public static void main(String[] args) {
		ClassManager c = new ClassManager();
		try {
			c.getClasses();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(c.assessClasses());
	}
}
