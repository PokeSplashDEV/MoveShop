package org.pokesplash.moveshop.event.events;

public class ExampleEvent {
	private String message;

	public ExampleEvent(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
