package com.tutk.kalayvsaas.jenkins;

import java.util.Random;

public class test {

	public static void main(String[] args) {
		Random rand = new Random();
		boolean items[] = { true, false };

		// boolean x = items[rand.nextInt(items.length)];

		for (int i = 0; i < 10; i++) {
			if (items[rand.nextInt(items.length)]) {
				System.out.println("true");
			} else {
				System.out.println("false");
			}
		}

	}

}
