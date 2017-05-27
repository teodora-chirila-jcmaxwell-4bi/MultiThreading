/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author teodora.chirila
 */
public class TicTacToe {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
     System.out.println("Main Thread iniziata...");
        Schermi schermo = new Schermi(); //creazione di un nuovo oggetto di nome schermo di tipo schermi (monitor)
        
        // Posso creare un THREAD e avviarlo immediatamente
        Thread tic = new Thread (new TXY("TIC", schermo));
        tic.start();
        Thread tac = new Thread (new TXY("TAC", schermo));
        tac.start();
        Thread toe = new Thread (new TXY("TOE", schermo));
        toe.start();
        
        try {
            toe.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
        }
         try {
            tic.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
        }
          try {
            tac.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Main THREAD terminata. Punteggio: " + schermo.punteggio());
    }
    
}


class Schermi { //Schermi è la nostra classe monitor che ci aiuterà a gestire "i movimenti" del thread, (simile a un semaforo) il che significa che solo un thread può "entrare" in un monitor in un determinato istante.

  String threadPrima = ""; // ultimo thread che ha scritto sullo schermo
  int punteggio = 0; //variabile che conta quante volte toe viene dopo di tac

  public int punteggio() {  // fornisce il punteggio
    return this.punteggio;
  }

  public synchronized void scrivi(String thread, String msg) { //parola chiave synchronized applicata al metodo scrivi per gestire la sincronizzazione dei thred e far in modo che uesti non vadano in conflitto dato che Solo un thread alla volta può eseguire un metodo synchronized su uno stesso oggetto
    int random=100+(int)(Math.random()*300); //numero casuale tra 100 e 300
    msg += ": " + random + " :";
    if( thread.equals("TOE") && threadPrima.equals("TAC")) { //confronto tra il thread attuale e quello precedente, se quello attuale corrisponde a TOE e quello precendete a TAC si incrementa il punteggiio nella riga successiva.
        punteggio++; //incremento del punteggio se la condizione prima risulta vera
        msg += "  <---------------- qui"; // messaggio che mostra dove effettivamente toe è capitato dopo tac
    }
    try {
        TimeUnit.MILLISECONDS.sleep(random); //random ora diventa un numero rappresentante il tempo(generato casualmente dall'istruzione precedente) il MILLISECONDI 
    } catch (InterruptedException e) {} //Richiamo eccezione    this.ultimoTHREAD = thread;
    System.out.println(msg); //stampa del messaggio "<-------qui"
    threadPrima = thread; // ogni volta che un thread scrive cambia il contenuto delle variabili "threadPrima" e "thread" nella variabile "threadPrima" ci va l'ultimo thread che aveva scritto e quello nuovo va nella variabile "Thread" e cosi via 
  }
}

// Ci sono vari (troppi) metodi per creare un THREAD in Java questo e' il mio preferito per i vantaggi che offre
// +1 si puo estendere da un altra classe
// +1 si possono passare parametri (usando il Costruttore)
// +1 si puo' controllare quando un THREAD inizia indipendentemente da quando e' stato creato
class TXY implements Runnable {
    
    // non essesndo "static" c'e' una copia delle seguenti variabili per ogni THREAD 
    private String t;
    private String msg;
    Schermi schermo;

    // Costruttore, possiamo usare il costruttore per passare dei parametri al THREAD
    public TXY (String s, Schermi schermo) {
        this.t = s;
        this.schermo = schermo;
    }
    
    @Override // Annotazione per il compilatore
    // se facessimo un overloading invece di un override il copilatore ci segnalerebbe l'errore
    // per approfondimenti http://lancill.blogspot.it/2012/11/annotations-override.html
    public void run() { //metodo eseguito dai thread che in questo caso faranno un contdown partendo da 10
        for (int i = 10; i > 0; i--) {
            msg = "<" + t + "> " + t + ": " + i;
            schermo.scrivi(t, msg); //richiamo della procedura scrivi che si trova nella classe schermi
        }
    }
    
}
