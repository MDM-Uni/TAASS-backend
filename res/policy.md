> Queste sono solo delle proposte, giusto per allinearsi sullo stesso piano di lavoro. Qualsiasi cosa può essere cambiata se ne discutiamo tra di noi
# Git Policy
1. Non lavorare mai sul branch _main_ se si vuole modificare il codice o file ad esso collegato che potrebbero portare problemi all'esecuzione.
2. Quando si vuole lavorare ad un task di Trello, creare un nuovo branch che un nome significativo della feature da implementare.
3. Fare una `git merge` sul branch _main_ solo quando il codice è stato testato. [Pro e contro del merge e rebase (con tutorial)](https://www.youtube.com/watch?v=Nftif2ynvdA&t=298s&ab_channel=JetBrainsTV) 
4. Mi sembra scontato ma, dare nomi significativi ai commit.

# Project configuration
Creare i seguenti progetti separati:
1. per il backend su Spring.
2. per il frontend con Angular.
3. per il frontend con Flutter.

In fase di configurazione di progetto su GitHub c'è un'opzione apposita per scegliere un template di _.gitignore_. Scegliamo quello corretto così da avere meno problemi.

## Versioni
E' meglio mettersi d'accordo sulle versioni che utilizzeremo delle varie tecnologie, anche perchè io con mac M1 potrei avere ulteriori problemi.  
La X sta per qualsiasi numero. 

### Java per il backend
> Versione 11  

Molto collaudata e so che mi funziona su mac M1

### Angular
> Angular CLI: 13.1.X  
> Node.js: 17.2.X  

La versione si può verificare con `ng --version`

### Flutter  
> Flutter: stable, 2.5.X  
> Dart SDK version: 2.14.X (stable)  
> Android SDK 31.0.0  

Non so bene se serva e dove specificare un Android SDK.  
Per scoprire la versione installata sul proprio dispositivo runnare:  
```
flutter doctor
dart --version
```

# Trello policy
1. Aggiornare lo stato di avanzamento di un task tramite le etichette impostate (doing, testing, ...) ed eventualmente, se si è in ritardo con la consegna, aggiornare la data presunta di terminazione del task

# Note
## Perchè non usare git merge
Perchè non mantiene su un'unica linea temporale i commit e se un branch viene eliminato dopo il 'git merge', vengono persi tutti i commit che hanno portato al completamento della feature (ovviamente rimane il commit di merge). E' una questione estetica e di analytics più che altro.

## Perchè non usare git rebase
Perchè può portare più conflitti del dovuto
