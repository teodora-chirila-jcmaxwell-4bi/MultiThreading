# TicTacToe
TicTacToe è un programma/progetto con lo scopo di prendere più confidenza con la multiprogrammazione e il linguaggio java.

# Istruzioni
Ci è stato richiesto di modificare il programma creato dal professore ed aggiungere delle funzionalità diverse:
1. Modificare il nome della classe da TicTac a TicTacToe.
2. Creare e avviare un terzo thread "TOE".
3. Modificare il tempo di sleep di ciascun thread sostituendolo con uno generato casualmente.
4. Contare quante volte il thread di nome "TOE" viene immediatamente dopo al thread di nome "TAC" con un'apposita variabile da incrementare.

Una volta eseguite le modifiche si può dedurre lo scopo del programma. E cioè:
Far partire tutti e 3 i thread contemporaneamente con un tempo di riposo casuale in modo da ottenere una sequenza dei thread diversa ad ogni compilazione del codice, ogni volta che il thread TOE capita subito dopo il thread TAC deve essere incrementata una variabile che conti il numero di volte che la condizione precedentemente detta è vera. Stamapare a video il contenuto della variabile.
Per verificare se TOE capita dopo TAC bisogna fare un confronto tra 2 varibili:
- quella contenente il thread attuale.
- quella contenente il thread subito precedente.
Quindi se in nome contenente nella variabile attuale è "TAC" e il nome contenuto nella variabile precedente è "TOE" incrementare la variabile per il punteggio.
## IL RISULTATO SARA' IL SEGUENTE SE IL CODICE E' CORRETTO: 
(per vedere il corretto funzionamento basta contare quante volte toe capita dopo tac e vedere se combacia con il risultato della variabile del punteggio, come nelle immagini seguenti //parti evidenziate in giallo)


NB:Ogni volta che si complila il codice il risultato cambia quindi ci sono infinite soluzioni appunto perchè il tempo di sleep è casuale. Quele sopra sono 2 prove del codice da me fatto.
