/*
 * Con questo programma voglio illustrare i seguenti concetti:
 * 1. MAIN e' un thread come gli altri e quindi puo' terminare prima che gli altri
 * 2. THREADs vengono eseguiti allo stesso tempo
 * 3. THREADs possono essere interrotti e hanno la possibilita' di interrompersi in modo pulito
 * 4. THREADs possono essere definiti mediante una CLASSE che implementa un INTERFACCIA Runnable
 * 5. THREADs possono essere avviati in modo indipendente da quando sono stati definiti
 * 6. posso passare parametri al THREADs tramite il costruttore della classe Runnable
 */
package multithread;

import java.util.concurrent.TimeUnit;
/**
 *
 * @author Matteo Palitto
 */
public class MultiThread {
    
    /**
     * @param args the command line arguments
     */
    // "main" e' il THREAD principale da cui vengono creati e avviati tutti gli altri THREADs
    // i vari THREADs poi evolvono indipendentemente dal "main" che puo' eventualmente terminare prima degli altri
    public static void main(String[] args) {
        System.out.println("Main Thread iniziata...");
        long start = System.currentTimeMillis();
        
        // Posso creare un THREAD e avviarlo immediatamente
        Thread tic = new Thread (new TicTacToe("TIC"));
        tic.start(); // avvio del primo THREAD
        // Posso creare un 2ndo THREAD e avviarlo immediatamente
        Thread tac = new Thread(new TicTacToe("TAC"));
          tac.start();  // avvio del secondo THREAD
           // Posso creare un 2ndo THREAD e avviarlo immediatamente
        Thread toe = new Thread (new TicTacToe("TOE"));
        toe.start(); // avvio del terzo THREAD
         
        try
        {  tic.join(); }
        catch (InterruptedException exc){}
        
        try
        {  tac.join(); }
        catch (InterruptedException exc){}
         
        try
        { toe.join(); }
        catch (InterruptedException exc){}
        
        long end = System.currentTimeMillis();
        System.out.println("Main Thread completata! tempo di esecuzione: " + (end - start) + "ms");
        System.out.println("Toe viene dopo Tac:" + TicTacToe.punteggio + " volte");
    }
    
}

// Ci sono vari (troppi) metodi per creare un THREAD in Java questo e' il mio preferito per i vantaggi che offre
// +1 si puo estendere da un altra classe
// +1 si possono passare parametri (usando il Costruttore)
// +1 si puo' controllare quando un THREAD inizia indipendentemente da quando e' stato creato
class TicTacToe implements Runnable {
    
    // non essesndo "static" c'e' una copia delle seguenti variabili per ogni THREAD 
    private String t;
    private String msg;
    public static int punteggio=0; //dichiarazione di una variabile statica che può essere condivisa da tutti e tre i thread che incrementeranno ogni volta che la condizione posta sarà soddisfatta.
    public static String threadPrima = " "; //dichiarazione di una variabile statica che sarà condivisa da tutti e tre i thread. Essa conterrà i nome del thread che dovrà essere confrontata con il nome del thread attuale.
    int random=100+(int)(Math.random()*300); //creazione di un numero random che va da 100 a 300
    // Costruttore, possiamo usare il costruttore per passare dei parametri al THREAD
    public TicTacToe (String s) {
        this.t = s;
    }
    
    @Override // Annotazione per il compilatore
    // se facessimo un overloading invece di un override il copilatore ci segnalerebbe l'errore
    // per approfondimenti http://lancill.blogspot.it/2012/11/annotations-override.html
    public void run() {
        
        for (int i = 10; i > 0; i--) {
            msg = "<" + t + "> ";
            //System.out.print(msg);
            
            try {
                TimeUnit.MILLISECONDS.sleep(random); //utilizzato per rappresentare intervalli di tempo specificati in una determinata unità di misura(millisecondi nel nostro caso) e per la gestione di tempi di ritardo.
            } catch (InterruptedException e) {
                System.out.println("THREAD " + t + " e' stata interrotta! bye bye...");
                return; //me ne vado = termino il THREAD
            }
            msg += t + ": " + i;
            System.out.println(msg);
          
            if(threadPrima.equals("TAC") && t.equals("TOE")) //condizione della quale si mettono a confronto il nome del thread attuale con quello subito precedente a lui.
            {punteggio++;} //incremento della variabile statica punteggio;
            threadPrima=t; // assegnazione del thread attuale a quello precedente;
        }
           
            
    }
    
    
}
