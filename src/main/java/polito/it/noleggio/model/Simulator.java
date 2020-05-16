package polito.it.noleggio.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.PriorityQueue;

import polito.it.noleggio.model.Event.EventType;

public class Simulator {
	
	//CODA DEGLI EVENTI
	private PriorityQueue<Event> queue = new PriorityQueue<>(); 
	
	//PARAMETRI DI SIMULAZIONE
	//Questi devono essere impostati dall'esterno, allora uso dei setter, tuttavia metto valori di default
	private int NC = 10; //Number of Cars
	private Duration T_IN = Duration.of(10, ChronoUnit.MINUTES); //Intervallo tra i clienti
	
	private final LocalTime oraApertura = LocalTime.of(8, 00);
	private final LocalTime oraChiusura = LocalTime.of(17, 00);
	
	//MODELLO DEL MONDO
	//In questo caso molto semplice, dato solo dalle auto disponibili
	private int nAuto; //Auto disponibili nel deposito, numero compreso tra 0 e NC
	
	//VALORI IN USCITA CHE IL SIMULATORE DEVE CALCOLARE
	private int clienti;
	private int insoddisfatti;
	
	
	//METODI PER IMPOSTARE I PARAMETRI
	public void setNumCars(int N) {
		this.NC = N;
	}
	
	public void setClientFrequency(Duration d) {
		this.T_IN = d;
	}

	//METODI PER RESTITUIRE I RISULTATI
	public int getInsoddisfatti() {
		return insoddisfatti;
	}

	public int getClienti() {
		return clienti;
	}
	
	//SIMULAZIONE VERA E PROPRIA
	public void run() {
		//Preparazione iniziale, ovvero preparo sia le variabili del mondo che quelle della coda degli eventi
		this.nAuto = this.NC; //Inizalmente ovviamente le auto le ho tutte in deposito
		this.clienti = 0;
		this.insoddisfatti = 0;
		
		this.queue.clear(); //Per sicurezza pulisco la coda
		LocalTime oraArrivoCliente = this.oraApertura; //Ipotizzo che il primo cliente arrivi a quest'ora
		do {
			Event e = new Event(oraArrivoCliente, EventType.NEW_CLIENT); //Evento: alle 8 arriva un nuovo cliente
			this.queue.add(e);
			oraArrivoCliente = oraArrivoCliente.plus(this.T_IN); //Dopo 10 min arriva un altro
		} while(oraArrivoCliente.isBefore(this.oraChiusura)); //Fin quando non chiude
		
		//Esecuzione del ciclo di simulazione
		//Estraggo gli eventi in ordine di tempo, perche' l'oggetto evento ha il Comparator basato sul time
		//e la priority queue estrae sicuramente in ordine di marcatura temporale.
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			
			//Questo evento allora puo' essere di due tipi
			/*
			switch(e.getType()) {
			case NEW_CLIENT:
				break;
			
			case CAR_RETURNED:
				break;
			
			}
			*/
			
			//System.out.println(e);
			this.processEvent(e);
		}
	}
	
	private void processEvent(Event e) {
		switch(e.getType()) {
		case NEW_CLIENT: //All'arrivo di un nuovo cliente puo' essere che ci sia un auto oppure no
			
			if(this.nAuto > 0) {
				//Cliente servito, auto noleggiata -> quindi 1 - aggiorno modello del mondo, 
				//2 - aggiorno risultati simulazione
				//3 - eventualmente inserire nuovi eventi
				
				//1. Aggiorno stato mondo
				this.nAuto--;
				
				//2. Aggiorno i risultati
				this.clienti++;
				
				//3. Genera nuovi eventi (in questo caso la restituzione dell'auto)
				double numero = Math.random(); // [0,1)
				Duration travel;
				if(numero < 1.0/3.0)
					travel = Duration.of(1, ChronoUnit.HOURS);
				else if(numero < 2.0/3.0)
					travel = Duration.of(2, ChronoUnit.HOURS);
				else
					travel = Duration.of(3, ChronoUnit.HOURS);
				
				//Il tempo e' il tempo corrente piu' la durata del viaggio, l'evento la restituzione della macchina
				Event nuovo = new Event(e.getTime().plus(travel), EventType.CAR_RETURNED);
				this.queue.add(nuovo);
			}
			else {
				//3 domande
				//Cliente insoddisfatto, se non ci sono auto e arriva un cliente, lo stato de mondo diceva di avere
				//zero auto e continua ad essere cosi', quindi non cambia niente
				this.clienti++;
				this.insoddisfatti++;
				
			}
			break;
		
		case CAR_RETURNED:
			//3 domande
			//Il deposito aumenta, avro' di nuovo un auto (cioe' aggiorno modello mondo)
			this.nAuto++;
			break;
		
		}
	}
}
