package polito.it.noleggio.model;

import java.time.LocalTime;

/**
 * Classe che rappresenta il singolo evento di simulazione, sicuramente contengono il tempo e il tipo di evento, che
 * poi puo' essere di due tipi (il cliente che arriva e l'auto che viene restituita)
 * @author eugenioprincipi
 *
 */
public class Event implements Comparable<Event>{

	/**
	 * Semplicemente una classe che fa la dichiarazione di costanti, messa qua perche' serve solo qui alla fine
	 * Identifica i due tipi di eventi possibili, ovvero un nuovo cliente e la macchina restituita
	 * 
	 * Fondamentale implementare l'interfaccia Comparable, perche' sara' inserita in una coda prioritaria
	 * @author eugenioprincipi
	 *
	 */
	public enum EventType {
		NEW_CLIENT, CAR_RETURNED
	}
	
	//Due attributi che avremo sempre all'interno dell'evento
	private LocalTime time; //Tempo di simulazione
	private EventType type;
	
	/**
	 * @param time
	 * @param type
	 */
	public Event(LocalTime time, EventType type) {
		super();
		this.time = time;
		this.type = type;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	@Override
	public int compareTo(Event o) {
		return this.time.compareTo(o.time);
	}

	@Override
	public String toString() {
		return "Event [time=" + time + ", type=" + type + "]";
	}
	
}
