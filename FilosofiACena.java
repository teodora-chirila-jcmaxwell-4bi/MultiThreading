/*
 * Con questo programma voglio illustrare i seguenti concetti:
 * 1. MONITOR gestiscono attività di coordinamento e di accesso ai dati tra più THREADs. 
 * 2. Le variabili dell'OGGETTO MONITOR sono le risorse da condividere tra THREADs
 * 3. Le attivita' CRITICHE che vengono fatte sulle variabili del MONITOR sono metodi SINCRONIZZATI della classe MONITOR
 * 4. i THREADs ricevono il puntatore all'OGGETTO MONITOR e ne usano i metodi per accedere alle risorse
 * 5. THREADs possono essere avviati in modo indipendente da quando sono stati definiti
 * 6. posso passare parametri al THREADs tramite il costruttore della classe Runnable
 */
package filosofiacena;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matteo Palitto
 */

// Monitor 
class Tavola {
    // A questa tavola ci sono delle regole: ogni persona puo' solo fare una delle seguenti cose
    // in JAVA enum sono piu' evoluti che in C++ possono contenere COSTRUTTORE e METODI
    public enum Azione {
        PENSA                   (0),  // ognuna di queste dichiarazioni invoca il costruttore (che in questo caso assegna il valore passato come argomento
        vuolePRENDEREforchette  (1),  // e' come se ognuno di queste COSTANTI fossero degli oggetti della classe enum...
        ASPETTAforchette        (2),
        MANGIA                  (3),
        seNEandatoVIA           (4);
        private final int idx;      // per ogni COSTANTE c'e' una varibile "idx"
        
        //costruttore
        private Azione (int idx) {
            this.idx = idx;
        }
        
        public int getIDX() { return this.idx; }
    }
        
    //su questa tavola ci sono tante forchette quanti posti a sedere
    boolean forchetta[]; //true: sulla tavola, false: NON sulla tavola (in mano ad un filosofo)
    int posti; // posti a sedere che corrisponde al numero di forchette
 
    //costruttore
    public Tavola(int N) {
        this.posti = N;
        this.forchetta = new boolean[N];
        for (int i=0; i<N; i++) { this.forchetta[i] = true; } //tutte le forchette iniziano sulla tavola
    }
    
    public synchronized boolean prendiFORCHETTE (int Sinistra) {
        int Destra = Sinistra + 1;
        
        if (Destra == posti) {
            Destra = 0;
        }
        
        if (forchetta[Sinistra] && forchetta[Destra]) {
            forchetta[Sinistra] = false; //prende la forchetta Sinistra
            forchetta[Destra] = false; //prende la forchetta Destra
            return true;
        } else {
            return false;  // almeno una delle forchette non ERA disponibile (saranno disponibili ora?)
        }
    }    

    public synchronized void aspettaFORCHETTE() {
            try {
                wait(); //si mette in attesa che qualcuno al tavolo finisca di usare le forchette
            } catch (InterruptedException ex) {
                Logger.getLogger(Tavola.class.getName()).log(Level.SEVERE, null, ex);
            }
            // qualcuno al tavolo ha smesso di usare le forchette      
    }
    
    public synchronized void posaFORCHETTE (int Sinistra) {
        int Destra = Sinistra + 1;
        
        if (Destra == posti) {
            Destra = 0;
        }
        
        forchetta[Sinistra] = true; //posa la forchetta Sinistra
        forchetta[Destra] = true; //posa la forchetta Destra
        
        // System.out.println("F[" + Sinistra + "]: HA FINITO DI MANGIARE");
        
        notifyAll();
        
    }
    
}

// Monitor per scrivere sullo schermo
class Schermo {
    
    // a questa tavola ci possono essere sudute delle persone che o pensano o mangiano... filosofi mangioni
    // questa variabile e' usata per tenere conto di cosa sta facendo ogni filosofo
    int filosofi[][] = new int[Tavola.Azione.values().length][FilosofiACena.Nfilosofi]; 
    // e questa per comporre il messaggio da scrivere sullo schermo
    String msg;
    
    public synchronized void aggiornaSITUAZIONE(int id, Tavola.Azione STAfacendo) {
            // for(Azione a: Azione.values()) { filosofi[a.getIDX()][id] = 0; } // azzera qualsiasi azione stava facendo prima filosofo "id"
            for(int i=0; i<Tavola.Azione.values().length; i++) { filosofi[i][id] = 0; }
            filosofi[STAfacendo.getIDX()][id] = 1; // metti e segnala quello che sta facendo in questo istante
            msg = "F[" + id + "]" + STAfacendo.name();
            for(Tavola.Azione a: Tavola.Azione.values()) {
                msg += " |---| " + a.name() + ": ";
                for(int i=0; i<FilosofiACena.Nfilosofi; i++) { if(filosofi[a.getIDX()][i] == 1) { msg+=i; } }
            } 
            System.out.println(msg);
    }
}

class Filosofo implements Runnable {
    
    // variabili di OGGETTO (una copia per ogni oggetto di questa classe)
    int id; //numero che identifica il filosofo
    Tavola.Azione STAfacendo;
    int mangiato = 0; //quante volte il filosofo ha mangiato
    Tavola tavolo;
    Schermo schermo;
    
    // costruttore
    public Filosofo(int i, Tavola tav, Schermo schermoPERscrivere) {
        this.id = i;
        this.tavolo = tav;
        this.STAfacendo = Tavola.Azione.PENSA;
        this.schermo = schermoPERscrivere;
    }
    
    // la classe Runnable DEVE implementare il metodo RUN
    @Override
    public void run () {
    
        while (true) {
        //synchronized(this) {
            schermo.aggiornaSITUAZIONE(id, STAfacendo);
            if(STAfacendo.name().equals("seNEandatoVIA")) { return; } // finalmente sazio se ne andato

            switch (STAfacendo){

                case PENSA:
                    //System.out.println("F[" + id + "] sta PENSANDO");
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {}
                    STAfacendo = Tavola.Azione.vuolePRENDEREforchette;
                    break;

                case vuolePRENDEREforchette:
                    //System.out.println("F[" + id + "] vuole PRENDERE le forchette");
                    if (tavolo.prendiFORCHETTE(id)) {
                        System.out.println("F[" + id + "] ha PRESO le forchette");
                        STAfacendo = Tavola.Azione.MANGIA;
                    } else {
                        System.out.println("F[" + id + "] NON ha trovato almeno una delle forchette");
                        STAfacendo = Tavola.Azione.ASPETTAforchette;
                    }
                    break;

                case ASPETTAforchette:
                    tavolo.aspettaFORCHETTE();
                    STAfacendo = Tavola.Azione.vuolePRENDEREforchette;
                    break;

                case MANGIA:
                    // sta mangiando
                    // System.out.println("F[" + id + "] sta MANGIANDO");
                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (InterruptedException e) {}
                    // ha finito di mangiare
                    mangiato++;
                    System.out.println("F[" + id + "] ha FINITO di MANGIARE la portata N. " + mangiato);
                    tavolo.posaFORCHETTE(id);
                    if(mangiato == 3) { 
                        STAfacendo = Tavola.Azione.seNEandatoVIA;
                        System.out.println("F[" + id + "] si ALZA da TAVOLA");
                    } // finalmente sazio!  Filosofo si alza...
                    else { STAfacendo = Tavola.Azione.PENSA; } // non ancora sazio...
                    break;

                default:
                    System.out.println("ERROR: non mi dovrei trovare qui MAI!!");
                    return;

            }
            
        }
    }
    //}
}

public class FilosofiACena {
   public final static int Nfilosofi = 5; // numero filosofi

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Tavola tavola = new Tavola(Nfilosofi);
        Schermo perSCRIVERE = new Schermo();
        Thread[] filosofo = new Thread[Nfilosofi];
        
        for (int i=0; i<Nfilosofi; i++) {

            filosofo[i] = new Thread( new Filosofo(i, tavola, perSCRIVERE) );
            filosofo[i].start();

        }
    }
    
}
