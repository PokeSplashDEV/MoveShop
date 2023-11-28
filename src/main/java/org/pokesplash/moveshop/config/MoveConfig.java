package org.pokesplash.moveshop.config;

public class MoveConfig {
	private String move;
	private double price;

	public MoveConfig() {
		this.move = "hail";
		this.price = 5000;
	}

	public String getMove() {
		return move;
	}

	public double getPrice() {
		return price;
	}
}
