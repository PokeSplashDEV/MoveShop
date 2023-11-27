package org.pokesplash.moveshop.event;

import org.pokesplash.moveshop.event.events.ExampleEvent;
import org.pokesplash.moveshop.event.obj.Event;

/**
 * Class that holds all of the events.
 */
public abstract class Events {
	public static Event<ExampleEvent> EXAMPLE = new Event<>();

}