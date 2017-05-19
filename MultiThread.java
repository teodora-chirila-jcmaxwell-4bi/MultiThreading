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
        Schermi schermo = new Schermi();
        
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


class Schermi {

  String threadPrima = ""; // ultimo thread che ha scritto sullo schermo
  int punteggio = 0;

  public int punteggio() {  // fornisce il punteggio
    return this.punteggio;
  }

  public synchronized void scrivi(String thread, String msg) {
    int random=100+(int)(Math.random()*300); //numero casuale tra 100 e 400
    msg += ": " + random + " :";
    if( thread.equals("TOE") && threadPrima.equals("TAC")) {
        punteggio++;
        msg += "  <---------------- qui";
    }
    try {
        TimeUnit.MILLISECONDS.sleep(random); //casuale ora diventa un numero rappresentante il tempo il MILLISECONDI
    } catch (InterruptedException e) {} //Richiamo eccezione    this.ultimoTHREAD = thread;
    System.out.println(msg);
    threadPrima = thread;
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
    public void run() {
        for (int i = 10; i > 0; i--) {
            msg = "<" + t + "> " + t + ": " + i;
            schermo.scrivi(t, msg);
        }
    }
    
}
