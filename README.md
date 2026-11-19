# Totem Pop Chams (Minecraft 26.2, Fabric)

Clientseitige Mod: Löst ein Spieler (auch du) ein Totem der Unsterblichkeit aus,
bleibt an dieser Stelle ca. 4 Sekunden eine Kopie des Spielers stehen, die eine
leuchtende Umrandung hat (auch durch Wände sichtbar).

## Jar über GitHub bauen

1. Neues Repository auf GitHub anlegen.
2. Diesen Ordner hochladen bzw. pushen (der Ordner `.github` muss dabei sein).
3. Im Repository auf **Actions** gehen, den Lauf **Build** öffnen.
4. Unten bei **Artifacts** `totem-pop-chams` herunterladen, die `.jar` liegt in der ZIP.
5. Die `.jar` und das Fabric API (0.156.0+26.2 oder neuer) in den `mods`-Ordner legen.

## Lokal bauen

JDK 25 und Gradle 9.5.1 installieren, dann `gradle build`.

## Anpassen

`GhostManager.java`: `LIFETIME_TICKS` (Dauer), `MAX_GHOSTS`.
