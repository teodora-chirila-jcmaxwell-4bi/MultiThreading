# MultiThreading
JAVA Mutithreading per le classi 4te

Con questo progetto vorrei introdurre gli allievi delle 4te ai concetti di programmazione concorrente

## Cos'e' la con-correnza?
e' il verificarsi di eventi allo stesso tempo (contemporaneamente)

pensiamo alla vita delle persone:
* piu' persone vivono nello stesso giorno
* ogni persona puo' fare piu' cose allo stesso tempo (es. studiare, ascoltare musica e giocare a videogioco...)
* in un bar affollato ci sono diversi baristi per smaltire la folla che vuole il caffe'
* mentre un ufficio svolge una pratica un altro ufficio ne svolge un altra (non in Italia... dove nessun ufficio svolge qualcosa)

I sistemi informatici che sono uno strumento umano, e che si interfacciano con l'umanita' sono progettati per poter fare piu' di una cosa contemporaneamente.

In questo modo lo stesso utente puo' fare piu' cose al computer contemporaneamente (es. leggere email, ascoltare musica online, stampare documento) e piu' persone possono accedere allo stesso programma (facebook, mail, registro elettronico...)

Supponiamo piu' utenti debbano usare il registro elettronico, se il programma fosse progettatto senza usare la programmazioine concorrenziale (multithreading), solo un utente alla volta sarebbe in grado di accedervi.

Molto tempo e' usato dall'utente per inserire i dati o leggerli, e in quel tempo il programma non esegue nessun codice, non utilizzando (sprecando) la potenza di calcolo del computer in uso.

Se piu' utenti potessero accedere al registro di classe contemporaneamente, il computer ,se e' in attesa di un utente a compiere una certa azione, potrebbe eseguire il codice per soddisfare la richiesta di un altro utente.

In generale la programmazione concorrente porta ad un utilizzo piu' intelligente ed efficiente dello strumento informatico.

## processo
e' un programma (flusso di istruzioni e dati) indipendente da un altro programma e che non condividono nulla ...tranne la CPU

esempio: un programma di calcolo e un videogioco

uno strumento informatico che permette a piu' processi di essere in esecuzione contemoporaneamete e' detto multi-processing

## thread
e' una porzione di un programma che viene eseguito contemporaneamente ad altre porzioni dello stesso programma. Spesso THREADs comunicano, condividono risorse, collaborano e hanno parte di codice in comune.

esempio: il registro elettronico che permette a piu' utenti di accedervi contemporaneamente

uno strumento informatico che permette a piu' processi di essere in esecuzione contemoporaneamete e' detto multi-htreading


## Al fine di presentare i concetti di mutithreading in JAVA usero' due esempi

1. Multithread.java che vuole illustrare i seguenti concetti base:
 * 1. MAIN e' un thread come gli altri e quindi puo' terminare prima che gli altri
 * 2. THREADs vengono eseguiti allo stesso tempo
 * 3. THREADs possono essere interrotti e hanno la possibilita' di interrompersi in modo pulito
 * 4. THREADs possono essere definiti mediante una CLASSE che implementa un INTERFACCIA Runnable
 * 5. THREADs possono essere avviati in modo indipendente da quando sono stati definiti
 * 6. posso passare parametri al THREADs tramite il costruttore della classe Runnable

2. FilosofiACena (Filosofi a cena) che vuole illustrare i seguenti concetti di comunicazione tra THREADs e condivisione di risorse:
 * 0. THREADs comunicano condividendo variabili
 * 1. MONITOR gestiscono attività di coordinamento e di accesso alle variabili tra più THREADs. 
 * 2. Le variabili dell'OGGETTO MONITOR sono le risorse da condividere tra THREADs
 * 3. Le attivita' CRITICHE che vengono fatte sulle variabili del MONITOR sono i metodi SINCRONIZZATI della classe MONITOR
 * 4. i THREADs ricevono il puntatore all'OGGETTO MONITOR e ne usano i metodi per accedere alle risorse
 * 5. la parola SYNCRONIZED chiude l'accesso agli altri THREADs che vogliono usare la stessa risorsa (competizione)
 * 6. WAIT e' usato per mettere in attesa i THREADs che vogliono usare una risorsa OCCUPATA (collaborazione)
 * 7. NOTIFY[ALL] e' usato per comunicare ai THREADs quando la risorsa e' di nuovo disponibile (collaborazioine)
