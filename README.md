# TicTacToe
TicTacToe è un programma/progetto con lo scopo di prendere più confidenza con la multiprogrammazione e il linguaggio java.

# Istruzioni
Ci è stato richiesto di modificare il programma creato dal professore ed aggiungere delle funzionalità diverse:
1. Modificare il nome della classe da TicTac a TicTacToe.
2. Creare e avviare un terzo thread "TOE".
3. Modificare il tempo di sleep di ciascun thread sostituendolo con uno generato casualmente.
4. Contare quante volte il thread di nome "TOE" viene immediatamente dopo al thread di nome "TAC" con un'apposita variabile da incrementare.

Una volta eseguite le modifiche si può dedurre lo scopo del programma. E cioè:
Far partire tutti e 3 i thread contemporaneamente con un tempo di riposo casuale in modo da ottenere una sequenza dei thread diversa ad ogni compilazione del codice, ogni volta che il thread TOE capita subito dopo il thread TAC deve essere incrementata una variabile che conti il numero di volte che la condizione precedentemente detta è vera. Stamapare a video il contenuto della variabile. (I thread fanno un countdown da 10 a 1. Arrivati a 1 si fermano)
Per verificare se TOE capita dopo TAC bisogna fare un confronto tra 2 varibili:
- quella contenente il thread attuale.
- quella contenente il thread subito precedente.
Quindi se in nome contenente nella variabile attuale è "TAC" e il nome contenuto nella variabile precedente è "TOE" incrementare la variabile per il punteggio.
## IL RISULTATO SARA' IL SEGUENTE SE IL CODICE E' CORRETTO: 
(per vedere il corretto funzionamento basta contare quante volte toe capita dopo tac e vedere se combacia con il risultato della variabile del punteggio, come nelle righe seguenti //parti inndicate dalla freccia)

Main Thread iniziata...    

<TAC> TAC: 10

--><TOE> TOE: 10

<TIC> TIC: 10

<TAC> TAC: 9

--><TOE> TOE: 9

<TAC> TAC: 8

--><TOE> TOE: 8

<TIC> TIC: 9

<TAC> TAC: 7

--><TOE> TOE: 7

<TAC> TAC: 6

--><TOE> TOE: 6

<TIC> TIC: 8

<TAC> TAC: 5

--><TOE> TOE: 5

<TAC> TAC: 4

--><TOE> TOE: 4

<TIC> TIC: 7

<TAC> TAC: 3

<TAC> TAC: 2

--><TOE> TOE: 3

<TIC> TIC: 6

<TAC> TAC: 1

--><TOE> TOE: 2

<TOE> TOE: 1

<TIC> TIC: 5

<TIC> TIC: 4

<TIC> TIC: 3

<TIC> TIC: 2

<TIC> TIC: 1

Main Thread completata! tempo di esecuzione: 3754ms //questa è una funzione in più che ci mostra il tempo di esecuzione, può essere tolta modificando il programma.

Toe viene dopo Tac: 9 volte


NB:Ogni volta che si complila il codice il risultato cambia quindi ci sono infinite soluzioni appunto perchè il tempo di sleep è casuale. Quele sopra sono 2 prove del codice da me fatto.
