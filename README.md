# Mitme kasutajaga vestlusrakendus
## 1. Projekti kirjeldus
Käesolev projekt on klient-server rakendus reaalajas tekstisõnumite vahetamiseks. Süsteem toetab kasutajate registreerimist ja autentimist, ühist avalikku jututuba, privaatsõnumite saatmist ning võrgus olevate kasutajate nimekirja kuvamist.

Funktsionaalsed nõuded
###Autentimine:### Kasutajal on võimalik registreerida unikaalne kasutajanimi ja logida sisse parooliga.

Avalik vestlus: Registreeritud kasutajad saavad saata sõnumeid üldisesse kanalisse, mis edastatakse koheselt kõigile osalejatele.

Privaatsõnumid: Kasutajad saavad saata privaatseid sõnumeid konkreetsele adressaadile, kasutades käsku /msg [kasutajanimi] [tekst].

Oleku teavitused: Süsteem teavitab automaatselt kasutaja liitumisest (JOIN) või lahkumisest (LEAVE).

Kasutajate nimekiri: Rakenduse liideses kuvatakse ja uuendatakse reaalajas aktiivsete kasutajate nimekirja.

Mittefunktsionaalsed nõuded
Turvalisus: Kasutajate paroole ei salvestata andmebaasi avatud tekstina; turvalisuse tagamiseks kasutatakse jBCrypt krüpteerimisteeki.

Andmebaasi jõudlus: SQLite andmebaasis on sisse lülitatud WAL (Write-Ahead Logging) režiim ja määratud lukustuse ooteaeg (busy_timeout = 5000), mis hoiab ära tõrked sagedaste päringute korral.

Lõimede haldus (Concurrency): Server töötleb iga klienti eraldi sõltumatus lõimes (ClientHandler). Kliendiprogramm uuendab Swing-liidest rangelt sündmuste töötlemise lõimes (EDT), kasutades SwingUtilities.invokeLater meetodit.

Kasutajanõuded
Graafilise kasutajaliidese (GUI) olemasolu, mis ei nõua kasutajalt käsurea tundmist.

Intuitiivsed ja selged aknad registreerumiseks, sisselogimiseks ja vestluse pidamiseks.

## 2. Süsteemi kirjeldus
Kasutatud tehnoloogiad
Programmeerimiskeel: Java (JDK 17 või uuem).

Kasutajaliides (GUI): Java Swing / AWT.

Andmebaas: SQLite (lokaalne fail chat.db).

Andmebaasi draiver: JDBC (SQLite-JDBC).

Krüpteerimine: jBCrypt (paroolide räsiks).

Süsteemi üldine ülesehitus
Projekt on jagatud kaheks põhiliseks ja iseseisvaks osaks:

Server (chat.server, chat.service): Kuulab porti 5000, võtab vastu sissetulevaid TCP-ühendusi (Socket), haldab aktiivsete sessioonide nimekirja ja salvestab sõnumite ajaloo andmebaasi.

Klient (chat.client): Haldab graafilise kasutajaliidese aknaid, ühendub serveriga ning edastatab/võtab vastu tekstiandmeid.

Protsessid ja API (Andmevahetusprotokoll)
Kliendi ja serveri vaheline suhtlus toimub tekstipõhise protokolli alusel läbi TCP-pesade (BufferedReader / PrintWriter).

Peamised protokollikäsud (API):

ENTER_USERNAME — Serveri süsteemne päring kliendile esmasel ühendumisel.

JOIN|[kasutajanimi] — Teavitus uue kasutaja sisselogimise kohta.

LEAVE|[kasutajanimi] — Teavitus kasutaja väljumise kohta.

MSG|[kasutajanimi]|[tekst] — Tavalise tekstisõnumi edastamise formaat.

PRIV|[saatja]|[tekst] — Suunatud privaatsõnum ühelt kasutajalt teisele.

USERS|[kasutaja1,kasutaja2,...] — Võrgus olevate kasutajate nimekirja uuendamine.

Andmemudelid
Süsteem kasutab kahte peamist andmemudelit (pakett chat.model):

User: id (INT), username (TEXT), password (TEXT — parooli räsi).

Message: id (INT), sender (TEXT), message (TEXT), created_at (TIMESTAMP).

## 3. Disain ja arhitektuur
Moodulite kirjeldused
Projekt on jaotatud loogilisteks pakettideks:

chat.client — Kasutajaliidese komponendid (LoginFrame, RegisterFrame, ChatFrame) ja kliendi võrgumoodul (ClientConnection).

chat.server — Serveri võrguloogika (ChatServer, ClientHandler).

chat.database — Otsene ühendus SQLite andmebaasiga (Database, DatabaseInitializer) ja andmepääsu objektid (UserDAO, MessageDAO).

chat.model — POJO andmeklassid (User, Message).

chat.service — Ärilogika andmetega töötamiseks serveri poolel (ChatService).

Liidesed ja andmevahetus
Rakenduses kasutatakse tagasikutsumise liidest (Callback Interface), et eraldada võrguloogika graafilisest liidest. ClientConnection võtab vastu MessageListener kuulaja, võimaldades ChatFrame aknal dünaamiliselt reageerida võrgust tulevatele sõnumitele ilma otsese siduvuseta soketite loogikaga.

## 4. Testimine
Testiplaan ja testjuhtumid
Süsteemi testimine toimub integratsioonitestide raames käsitsi (Manual Integration Testing) järgmiste põhiliste juhtumite alusel:

Uue kasutaja registreerimine: Andmed krüpteeritakse edukalt, luuakse kirje failis chat.db ja aken sulgub.

Sisselogimine vale parooliga: Süsteem tuvastab vale parooli ja kuvatakse asjakohane veateade.

Kahe kliendi paralleelne ühendus: Mõlemad kliendid tuvastatakse serveri poolt ja nad näevad üksteist aktiivsete kasutajate nimekirjas.

Privaatsõnumi saatmine: Sõnum käsuga /msg edastatakse serveri kaudu ja see jõuab ainult valitud sihtkasutajani.

## 5. Kasutus- ja hooldusjuhend
Paigaldamine ja käivitamine
Keskkonna ettevalmistus: Veenduge, et arvutisse on paigaldatud Java JDK versioon 17 või uuem.

Projekti ehitamine: Lisage projekti sõltuvustesse raamatukogud sqlite-jdbc ja jbcrypt (läbi Maveni/Gradle või JAR-failidena).

Serveri käivitamine: Käivitage klass chat.server.ChatServer. Konsooli ilmub teade, et server on käivitunud pordil 5000. Esmakordsel käivitamisel luuakse automaatselt andmebaasi fail chat.db koos vajalike tabelitega.

Kliendi käivitamine: Käivitage klass chat.client.LoginFrame. Avaneb sisselogimisaken.

Süsteemi kasutamine
Kui teil puudub konto, vajutage nuppu Register, sisestage kasutajanimi ja parool ning vajutage Create.

Sisselogimise aknas sisestage oma konto andmed ja sisenege süsteemi.

Avaneb peaaken. Sõnumite saatmiseks kirjutage tekst alumisele väljale ja vajutage Enter või nuppu Send.

Privaatsõnumi saatmiseks kasutage järgmist formaati: /msg [Kasutajanimi] [Teie privaatne tekst].

Hooldus ja uuendamine
Varundamine: Kogu andmebaas, kasutajad ja sõnumite ajalugu salvestatakse ühte faili nimega chat.db, mis asub projekti juurkataloogis. Varukoopia tegemiseks piisab selle faili kopeerimisest ja turvalisest hoiustamisest.

## 6. Lisad
Andmebaasi tabelite struktuur
Andmebaas koosneb kahest põhitabelist:

users: Sisaldab välju id (automaatselt suurenev peavõti), username (unikaalne tekstiväli) ja password (krüpteeritud parooli tekstiväli).

messages: Sisaldab välju id (automaatselt suurenev peavõti), sender (sõnumi saatja tekst), message (sõnumi sisu tekst) ja created_at (sõnumi loomise aeg, mis määratakse automaatselt).
