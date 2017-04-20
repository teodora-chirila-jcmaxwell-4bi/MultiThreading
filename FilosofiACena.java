/*
 * Con questo programma voglio illustrare i seguenti concetti:
 * 0. THREADs comunicano condividendo variabili
 * 1. MONITOR gestiscono attività di coordinamento e di accesso alle variabili tra più THREADs. 
 * 2. Le variabili dell'OGGETTO MONITOR sono le risorse da condividere tra THREADs
 * 3. Le attivita' CRITICHE che vengono fatte sulle variabili del MONITOR sono i metodi SINCRONIZZATI della classe MONITOR
 * 4. i THREADs ricevono il puntatore all'OGGETTO MONITOR e ne usano i metodi per accedere alle risorse
 * 5. la parola SYNCRONIZED chiude l'accesso agli altri THREADs che vogliono usare la stessa risorsa (competizione)
 * 6. WAIT e' usato per mettere in attesa i THREADs che vogliono usare una risorsa OCCUPATA (collaborazione)
 * 7. NOTIFY[ALL] e' usato per comunicare ai THREADs quando la risorsa e' di nuovo disponibile (collaborazioine)
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
        // ognuna di queste dichiarazioni invoca il costruttore (che in questo caso assegna il valore passato come argomento
        // e' come se ognuno di queste COSTANTI fossero degli oggetti della classe enum...
        PENSA                   (0),  // dopo tutto per questo che e' chiamato filosofo
        vuolePRENDEREforchette  (1),  // questi filosofi mangiano solo se hanno due forchette in mano (sembrano mio figlio di 21 mesi...)
        ASPETTAforchette        (2),  // se tutte e due le forchette non sono disponibili aspettano che qualcuno finisca di mangiare
        MANGIA                  (3),  // dopo tutto e' un filosofo mangione
        seNEandatoVIA           (4);  // una volta sazio e' ora di andare a farsi una pennichella dopo tutto quel pensare....

        private final int idx;      // per ogni COSTANTE c'e' una varibile "idx"
        
        //costruttore
        private Azione (int idx) {
            this.idx = idx;
        }

        //questo metodo ci permette di usare variabili di tipo ENUM in ARRAY (i cui indici possono essere solo degli interi)
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
        
        //ogni filosofo puo' prendere solo le forchette alla sua SINISTRA e alla sua DESTRA
        int Destra = Sinistra + 1;
        
        // il tavolo e' rotondo, la forchetta di DESTRA dell'ultimo filosofo e' quella di SINISTRA del primo
        if (Destra == posti) {
            Destra = 0;
        }
        
        if (forchetta[Sinistra] && forchetta[Destra]) {
            // entranbi le forchette sono sul tavolo e il filosofo le agguanta per mangiare
            forchetta[Sinistra] = false; //prende la forchetta Sinistra
            forchetta[Destra] = false; //prende la forchetta Destra
            return true;
        } else {
            return false;  // almeno una delle forchette non e' disponibile... non prendo nemmeno quella disponibile (nel caso ce ne fosse una)
        }
    }    

    public synchronized void aspettaFORCHETTE() {
            try {
                wait(); //si mette in attesa che qualcuno al tavolo finisca di usare le forchette
            } catch (InterruptedException ex) {
                Logger.getLogger(Tavola.class.getName()).log(Level.SEVERE, null, ex);
            }
            // qualcuno al tavolo ha smesso di usare le forchette e il filosofo esce dall'attesa    
    }
    
    public synchronized void posaFORCHETTE (int Sinistra) {
        int Destra = Sinistra + 1;
        
        if (Destra == posti) {
            Destra = 0;
        }
        
        // filosofo ripone le forchette sul tavolo
        forchetta[Sinistra] = true; //posa la forchetta Sinistra
        forchetta[Destra] = true; //posa la forchetta Destra
        
        // System.out.println("F[" + Sinistra + "]: HA FINITO DI MANGIARE");
        // notifica a tutti i filosofi in attesa che lui ha riposto le forchette sul tavolo e quindi sono nuovamente disponibili (collaborazione)
        notifyAll();
        
    }
    
}

// Monitor per scrivere sullo schermo
// anche lo schermo e' una risorsa comune ai vari filosofi che lo usano per dire cosa stanno facendo
class Schermo {
    
    // questa variabile e' usata per tenere conto di cosa sta facendo ogni filosofo
    int filosofi[][] = new int[Tavola.Azione.values().length][FilosofiACena.Nfilosofi]; 
    
    // e questa per comporre il messaggio da scrivere sullo schermo
    String msg;
    
    // i filosofi possono dire quello che stanno facendo scrivendolo sullo schermo ma solo UNO ALLA VOLTA!
    public synchronized void aggiornaSITUAZIONE(int id, Tavola.Azione STAfacendo) {
            // il filosofo scrive quello che sta facendo
            msg = "F[" + id + "]" + STAfacendo.name();

            // e aggiorna la situazione di chi sta facendo che cosa:
            
            // azzera qualsiasi azione stava facendo prima
            // for(Azione a: Azione.values()) { filosofi[a.getIDX()][id] = 0; } 
            for(int i=0; i<Tavola.Azione.values().length; i++) { filosofi[i][id] = 0; }
            // e aggiorna la situazione segnalando quello che sta facendo in questo istante
            filosofi[STAfacendo.getIDX()][id] = 1; 

            // scrive la situazione aggiornata
            for(Tavola.Azione a: Tavola.Azione.values()) {
                msg += " |---| " + a.name() + ": ";
                for(int i=0; i<FilosofiACena.Nfilosofi; i++) { if(filosofi[a.getIDX()][i] == 1) { msg+=i; } }
            } 
            System.out.println(msg);
    }
}

// la classe Filosofo e' la classe Runnable che significa avremo tante THREADs quanti sono i filosofi
class Filosofo implements Runnable {
    
    // variabili di OGGETTO (una copia per ogni oggetto di questa classe)
    int id;                    // numero che identifica il filosofo
    Tavola.Azione STAfacendo;  // che cosa sta facendo
    int mangiato = 0;          // quante portate il filosofo ha mangiato
    Tavola tavolo;             // in quale tavolo sta seduto
    Schermo schermo;           // quale schermo ha per scrivere
    
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

            schermo.aggiornaSITUAZIONE(id, STAfacendo);

            if(STAfacendo.name().equals("seNEandatoVIA")) { return; } // finalmente sazio se ne andato

            //quello che il filosofo fara' dipende anche da quello che sta facendo
            
            switch (STAfacendo){

                case PENSA:
                    
                    // filosofo ci pensa un po' su... ma non troppo 100ms
                    try { TimeUnit.MILLISECONDS.sleep(100); } catch (InterruptedException e) {}
                    
                    // adesso e' ora di mangiare!
                    STAfacendo = Tavola.Azione.vuolePRENDEREforchette;
                    break;

                case vuolePRENDEREforchette:

                    // il filosofo vuole prendere sia la forchetta alla sua sinistra che alla sua destra
                    if (tavolo.prendiFORCHETTE(id)) {
                    // se disponibili prende entrambi
                        System.out.println("F[" + id + "] ha PRESO le forchette");
                        STAfacendo = Tavola.Azione.MANGIA;
                    } else {
                        // se entrambe forchette non sono disponibili
                        // non ne prende nessuna e si mette ad aspettare
                        System.out.println("F[" + id + "] NON ha trovato almeno una delle forchette");
                        STAfacendo = Tavola.Azione.ASPETTAforchette;
                    }
                    break;

                case ASPETTAforchette:
                    // si mette ad aspettare che quelli che stanno mangiando finiscano e lo avvertano
                    tavolo.aspettaFORCHETTE();
                    STAfacendo = Tavola.Azione.vuolePRENDEREforchette;
                    break;

                case MANGIA:

                    // ci mettono piu' a mangiare che a pensare.... che strani filosofi...
                    try { TimeUnit.MILLISECONDS.sleep(200); } catch (InterruptedException e) {}

                    // ha finito di mangiare
                    mangiato++;
                    System.out.println("F[" + id + "] ha FINITO di MANGIARE la portata N. " + mangiato);
                    tavolo.posaFORCHETTE(id);

                    // siamo in italia... almeno 3 portate...
                    if(mangiato == 3) { 
                        //anche il dolce era buono... ora di andare a fare la pennichella
                        STAfacendo = Tavola.Azione.seNEandatoVIA;
                        System.out.println("F[" + id + "] si ALZA da TAVOLA");
                    } // finalmente sazio!  Filosofo si alza...
                    else { STAfacendo = Tavola.Azione.PENSA; } // non ancora sazio... rimane a tavola
                    break;

                default:
                    System.out.println("ERROR: non mi dovrei trovare qui MAI!!");
                    return;

            }
        }
    }
}

public class FilosofiACena {
   public final static int Nfilosofi = 5; // numero filosofi

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // i 2 OGGETTI MONITOR
        Tavola tavola = new Tavola(Nfilosofi);
        Schermo perSCRIVERE = new Schermo();
        
        // i filosofi
        Thread[] filosofo = new Thread[Nfilosofi];
        
        // i filosofi si siedono a tavola e gli viene detto quale schermo usare per scrivere
        for (int i=0; i<Nfilosofi; i++) {

            filosofo[i] = new Thread( new Filosofo(i, tavola, perSCRIVERE) );
            filosofo[i].start();

        }
    }
    
}
