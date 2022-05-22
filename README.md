Progetto per il corso: Programmazione di sistemi embedded.

Realizzazione di un gioco, campo minato 3D.

Come si gioca: è presente una griglia di cubi in una superficie di un cubo,
il cu cubetto indicato è quello puntato dal simbolo "+" in mezzo allo schermo.

Si scopre o si piazza la bandiera in base alle impostazioni in uso, di default
un tocco piazza la bandiera, un tocco prolungato scopre il cubo indicato.
Se si "sposta il tocco" viene annullata la selezione del cubetto facendo ruotare
la griglia.

Il gioco è realizzato con un sistema ad eventi, cancellabili e ignorazione 
della cancellazione, le chiamate ad evento hanno una priorità creando un ordine
prefissato e non casuale.

Ci sono vari preset di gioco + la possibilità di custommizzare il game che si
vuole fare.

La grafica è realizzata con la OpenGL.

Per capire come funziona l'applicazione, bisogna prima capire come funzionano gli eventi,
dato che se si cerca che chiama cosa dovuto da un evento ci si imbatte in una funzione
apparentemente mai usata, non è vera la cosa, le funzioni dentro le classi "Listener", vengono
tutte chiamate, ma non in modo "diretto" ma attraverso la Reflection di Java/Kotlin.
Capire come funziona il contenuto del packege "it.elsalamander.mine3d.Game.Event.Manager" è
necessario se si vuole sapere come si evolvono alcuni eventi.
