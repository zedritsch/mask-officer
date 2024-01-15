import de.zedritsch.wrapper.*;

public class Main {

	private static Window window = new Window("Der Masken Cop");
	private static int[][] matchfield = new int[15][16];
	private static int[][] objectfield = new int[15][16];
	private static int money = 100;
	private static int most_money = 100;

	public static void main(String[] args) throws InterruptedException {
		// Löschen die Konsolenangaben(Falls vorhanden)
		if (args != null) {
			args = null;
		}
		// Ausgabe der Quellen
		System.out.println(">> Quellen <<\n" +
				"Musik(Hintergrund): Skipping in the No Standing Zone(by Peter Gresser)\n" +
				"Geraeusche(Pfeife): Plastic Whistle(by Joseph Sardin)\n" +
				"Game-Design & Code: Zedritsch");
		// Konfiguration des Fensters
		window.resizable = false;
		window.icon = "assets/images/0.png";
		// Konfiguration des Musikspielers
		AudioStreamPlayer music_player = new AudioStreamPlayer();
		music_player.open("assets/audio/soundtrack.wav");
		music_player.loop = true;
		music_player.setVolume(-25.0f);
		// "Gameloop"
		boolean retry;
		boolean read_instructions = false;
		do {
			retry = false;
			// Spielfeld generieren
			generateMatchfield();
			// Fenster mit Spielfeld als Inhalt anzeigen und Musikspieler starten
			if (!window.visible) {
				window.show(matchfield, Integer.toString(money), Integer.toString(most_money));
				music_player.start();
			} else {
				window.update(matchfield, Integer.toString(money), Integer.toString(most_money));
			}
			// Zeigt ein Popup mit der Anleitung, vor dem ersten Spiel
			if (!read_instructions) {
				window.popup("Anleitung", "Willkommen bei 'Der Masken Cop'!\n" +
						"In diesem Spiel geht es darum Maskenverweigerer\n" +
						"auf ihre Kosten kommen zu lassen.\n" +
						"Bewege dich dazu mit 'W,A,S,D' und Pfeife mit 'Leer' oder 'Enter'.\n" +
						"Erwischt du einen Maskenverweigerer bekommst du 100 Euro!\n" +
						"Pfeifst du aber einen Maskenträger an oder entkommt\n" +
						"ein Maskenverweigerer, verlierst du 100 Euro.\n" +
						"Also pass auf was du tust!");
				read_instructions = true;
			}
			// Spielverlauf bis das Geld auf -100 sinkt
			while (money != -100) {
				Thread.sleep(10);
				moveOfficer();
			}
			// Anfrage zum Neustart
			if (window.popup("Spiel vorbei!", "Du hast dich verschuldet!\nMöchtest du neustarten?", 1) == 0) {
				retry = true;
				money = 100;
			}
		} while (retry);
		// Beenden des Programs
		matchfield = null;
		music_player.stop();
		music_player.close();
		System.exit(0);
	}

	private static void generateMatchfield() {
		// Erstellen der Anzeigen(Geld und höchste Geldsumme)
		matchfield[0][0] = 1;
		matchfield[1][0] = 2;
		for (int i = 2; i < Math.round((matchfield[0].length - 2)); i++) {
			matchfield[i][0] = 5;
		}
		matchfield[13][0] = 3;
		matchfield[14][0] = 4;
		// Erstellen des eigenlichen Spielfelds(Mit mindestens 64 Häusern)
		int houses = 0;
		while (houses < 64) {
			houses = 0;
			for (int y = 1; y < matchfield[0].length; y++) {
				for (int x = 0; x < matchfield.length; x++) {
					if (x == 0 & y == 1) {// Erstes Feld(Erste Reiche)
						matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 11)) + 6);
					} else if (x != matchfield.length - 1 & y == 1) {// Mittlere Felder(Erste Reiche)
						if (matchfield[x - 1][y] == 6 || matchfield[x - 1][y] == 7 || matchfield[x - 1][y] == 8 ||
								matchfield[x - 1][y] == 11 || matchfield[x - 1][y] == 12 ||
								matchfield[x - 1][y] == 14 || matchfield[x - 1][y] == 16) {
							matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 7)) + 7);
						} else {
							if (x == 1 || matchfield[x - 2][y] < 17) {
								matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 6)) + 21);
								houses += 1;
							} else {
								int number = Math.toIntExact(Math.round(Math.floor(Math.random() * 4)) + 13);
								if (number == 13) {
									number = 6;
								}
								matchfield[x][y] = number;
							}
						}
					} else if (y == 1) {// Letztes Feld(Erste Reiche)
						if (matchfield[x - 1][y] == 6 || matchfield[x - 1][y] == 7 || matchfield[x - 1][y] == 8 ||
								matchfield[x - 1][y] == 11 || matchfield[x - 1][y] == 12 ||
								matchfield[x - 1][y] == 14 || matchfield[x - 1][y] == 16) {
							if (matchfield[0][y] == 7 || matchfield[0][y] == 8 || matchfield[0][y] == 9 ||
									matchfield[0][y] == 10 || matchfield[0][y] == 11 ||
									matchfield[0][y] == 12 || matchfield[0][y] == 13) {
								int number = Math.toIntExact(Math.round(Math.floor(Math.random() * 4)) + 9);
								if (number == 9) {
									number = 7;
								} else if (number == 10) {
									number = 8;
								}
								matchfield[x][y] = number;
							} else {
								matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 7)) + 7);
							}
						} else if (matchfield[0][y] == 7 || matchfield[0][y] == 8 || matchfield[0][y] == 9 ||
								matchfield[0][y] == 10 || matchfield[0][y] == 11 ||
								matchfield[0][y] == 12 || matchfield[0][y] == 13) {
							int number = Math.toIntExact(Math.round(Math.floor(Math.random() * 2)) + 15);
							if (number == 15) {
								number = 14;
							}
							matchfield[x][y] = number;
						} else {
							if ((matchfield[x - 2][y] < 17 & matchfield[0][y] < 17)
									|| (matchfield[x - 1][y] < 17 & matchfield[1][y] < 17)) {
								matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 6)) + 21);
								houses += 1;
							} else {
								matchfield[x][y] = 15;
							}
						}
					} else if (x == 0 & y != matchfield[0].length - 1) {// Erstes Feld(Mittlere Reichen)
						if (matchfield[x][y - 1] == 6 || matchfield[x][y - 1] == 8 || matchfield[x][y - 1] == 9 ||
								matchfield[x][y - 1] == 12 || matchfield[x][y - 1] == 13 ||
								matchfield[x][y - 1] == 14 || matchfield[x][y - 1] == 15) {
							matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 7)) + 10);
						} else {
							if (y == 2 || matchfield[x][y - 2] < 17) {
								matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 6)) + 21);
								houses += 1;
							} else {
								matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 4)) + 6);
							}
						}
					} else if (x != matchfield.length - 1 & y != matchfield[0].length - 1) {// Mittlere Felder(Mittlere Reihen)
						if (matchfield[x - 1][y] == 6 || matchfield[x - 1][y] == 7 || matchfield[x - 1][y] == 8 ||
								matchfield[x - 1][y] == 11 || matchfield[x - 1][y] == 12 ||
								matchfield[x - 1][y] == 14 || matchfield[x - 1][y] == 16) {
							if (matchfield[x][y - 1] == 6 || matchfield[x][y - 1] == 8 || matchfield[x][y - 1] == 9 ||
									matchfield[x][y - 1] == 12 || matchfield[x][y - 1] == 13 ||
									matchfield[x][y - 1] == 14 || matchfield[x][y - 1] == 15) {
								matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 4)) + 10);
							} else {
								matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 3)) + 7);
							}
						} else if (matchfield[x][y - 1] == 6 || matchfield[x][y - 1] == 8 || matchfield[x][y - 1] == 9 ||
								matchfield[x][y - 1] == 12 || matchfield[x][y - 1] == 13 ||
								matchfield[x][y - 1] == 14 || matchfield[x][y - 1] == 15) {
							matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 3)) + 14);
						} else {
							if (x == 1 || y == 2) {
								if (x == 1 & y == 2) {
									matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 6)) + 21);
									houses += 1;
								} else if (x == 1) {
									if (matchfield[x][1] < 17) {
										matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 6)) + 21);
										houses += 1;
									} else {
										matchfield[x][y] = 6;
									}
								} else {
									if (matchfield[0][y] < 17) {
										matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 6)) + 21);
										houses += 1;
									} else {
										matchfield[x][y] = 6;
									}
								}
							} else if (matchfield[x - 2][y] < 17 & matchfield[x][y - 2] < 17) {
								matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 6)) + 21);
								houses += 1;
							} else {
								matchfield[x][y] = 6;
							}
						}
					} else if (y != matchfield[0].length - 1) {// Letztes Feld(Mittlere Reihen)
						if (matchfield[x - 1][y] == 6 || matchfield[x - 1][y] == 7 || matchfield[x - 1][y] == 8 ||
								matchfield[x - 1][y] == 11 || matchfield[x - 1][y] == 12 ||
								matchfield[x - 1][y] == 14 || matchfield[x - 1][y] == 16) {
							if (matchfield[x][y - 1] == 6 || matchfield[x][y - 1] == 8 || matchfield[x][y - 1] == 9 ||
									matchfield[x][y - 1] == 12 || matchfield[x][y - 1] == 13 ||
									matchfield[x][y - 1] == 14 || matchfield[x][y - 1] == 15) {
								if (matchfield[0][y] == 7 || matchfield[0][y] == 8 || matchfield[0][y] == 9 ||
										matchfield[0][y] == 10 || matchfield[0][y] == 11 ||
										matchfield[0][y] == 12 || matchfield[0][y] == 13) {
									matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 2)) + 11);
								} else {
									int number = Math.toIntExact(Math.round(Math.floor(Math.random() * 2)) + 12);
									if (number == 12) {
										number = 10;
									}
									matchfield[x][y] = number;
								}
							} else if (matchfield[0][y] == 7 || matchfield[0][y] == 8 || matchfield[0][y] == 9 ||
									matchfield[0][y] == 10 || matchfield[0][y] == 11 ||
									matchfield[0][y] == 12 || matchfield[0][y] == 13) {
								matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 2)) + 7);
							} else {
								matchfield[x][y] = 9;
							}
						} else if (matchfield[x][y - 1] == 6 || matchfield[x][y - 1] == 8 || matchfield[x][y - 1] == 9 ||
								matchfield[x][y - 1] == 12 || matchfield[x][y - 1] == 13 ||
								matchfield[x][y - 1] == 14 || matchfield[x][y - 1] == 15) {
							if (matchfield[0][y] == 7 || matchfield[0][y] == 8 || matchfield[0][y] == 9 ||
									matchfield[0][y] == 10 || matchfield[0][y] == 11 ||
									matchfield[0][y] == 12 || matchfield[0][y] == 13) {
								matchfield[x][y] = 14;
							} else {
								matchfield[x][y] = 15;
							}
						} else if (matchfield[0][y] == 7 || matchfield[0][y] == 8 || matchfield[0][y] == 9 ||
								matchfield[0][y] == 10 || matchfield[0][y] == 11 ||
								matchfield[0][y] == 12 || matchfield[0][y] == 13) {
							matchfield[x][y] = 6;
						} else {
							matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 6)) + 21);
							houses += 1;
						}
					} else if (x == 0 & y == matchfield[0].length - 1) {// Erstes Feld(Letzte Reihe)
						if (matchfield[x][1] == 10 || matchfield[x][1] == 11 || matchfield[x][1] == 12 ||
								matchfield[x][1] == 13 || matchfield[x][1] == 14 ||
								matchfield[x][1] == 15 || matchfield[x][1] == 16) {
							if (matchfield[x][y - 1] == 6 || matchfield[x][y - 1] == 8 || matchfield[x][y - 1] == 9 ||
									matchfield[x][y - 1] == 12 || matchfield[x][y - 1] == 13 ||
									matchfield[x][y - 1] == 14 || matchfield[x][y - 1] == 15) {
								matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 4)) + 12);
							} else {
								int number = Math.toIntExact(Math.round(Math.floor(Math.random() * 3)) + 7);
								if (number == 7) {
									number = 6;
								}
								matchfield[x][y] = number;
							}
						} else if (matchfield[x][y - 1] == 6 || matchfield[x][y - 1] == 8 || matchfield[x][y - 1] == 9 ||
								matchfield[x][y - 1] == 12 || matchfield[x][y - 1] == 13 ||
								matchfield[x][y - 1] == 14 || matchfield[x][y - 1] == 15) {
							int number = Math.toIntExact(Math.round(Math.floor(Math.random() * 3)) + 10);
							if (number == 12) {
								number = 16;
							}
							matchfield[x][y] = number;
						} else {
							if ((matchfield[x][y - 2] < 17 & matchfield[x][1] < 17)
									|| (matchfield[x][y - 1] < 17 & matchfield[x][2] < 17)) {
								matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 6)) + 21);
								houses += 1;
							} else {
								matchfield[x][y] = 7;
							}
						}
					} else if (x != matchfield.length - 1) {// Mittlere Felder(Letzte Reihe)
						if (matchfield[x - 1][y] == 6 || matchfield[x - 1][y] == 7 || matchfield[x - 1][y] == 8 ||
								matchfield[x - 1][y] == 11 || matchfield[x - 1][y] == 12 ||
								matchfield[x - 1][y] == 14 || matchfield[x - 1][y] == 16) {
							if (matchfield[x][y - 1] == 6 || matchfield[x][y - 1] == 8 || matchfield[x][y - 1] == 9 ||
									matchfield[x][y - 1] == 12 || matchfield[x][y - 1] == 13 ||
									matchfield[x][y - 1] == 14 || matchfield[x][y - 1] == 15) {
								if (matchfield[x][1] == 10 || matchfield[x][1] == 11 || matchfield[x][1] == 12 ||
										matchfield[x][1] == 13 || matchfield[x][1] == 14 ||
										matchfield[x][1] == 15 || matchfield[x][1] == 16) {
									matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 2)) + 12);
								} else {
									matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 2)) + 10);
								}
							} else if (matchfield[x][1] == 10 || matchfield[x][1] == 11 || matchfield[x][1] == 12 ||
									matchfield[x][1] == 13 || matchfield[x][1] == 14 ||
									matchfield[x][1] == 15 || matchfield[x][1] == 16) {
								matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 2)) + 8);
							} else {
								matchfield[x][y] = 7;
							}
						} else if (matchfield[x][y - 1] == 6 || matchfield[x][y - 1] == 8 || matchfield[x][y - 1] == 9 ||
								matchfield[x][y - 1] == 12 || matchfield[x][y - 1] == 13 ||
								matchfield[x][y - 1] == 14 || matchfield[x][y - 1] == 15) {
							if (matchfield[x][1] == 10 || matchfield[x][1] == 11 || matchfield[x][1] == 12 ||
									matchfield[x][1] == 13 || matchfield[x][1] == 14 ||
									matchfield[x][1] == 15 || matchfield[x][1] == 16) {
								matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 2)) + 14);
							} else {
								matchfield[x][y] = 16;
							}
						} else if (matchfield[x][1] == 10 || matchfield[x][1] == 11 || matchfield[x][1] == 12 ||
								matchfield[x][1] == 13 || matchfield[x][1] == 14 ||
								matchfield[x][1] == 15 || matchfield[x][1] == 16) {
							matchfield[x][y] = 6;
						} else {
							matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 6)) + 21);
							houses += 1;
						}
					} else {// Letztes Feld(Letzte Reihe)
						if (matchfield[x - 1][y] == 6 || matchfield[x - 1][y] == 7 || matchfield[x - 1][y] == 8 ||
								matchfield[x - 1][y] == 11 || matchfield[x - 1][y] == 12 ||
								matchfield[x - 1][y] == 14 || matchfield[x - 1][y] == 16) {
							if (matchfield[x][y - 1] == 6 || matchfield[x][y - 1] == 8 || matchfield[x][y - 1] == 9 ||
									matchfield[x][y - 1] == 12 || matchfield[x][y - 1] == 13 ||
									matchfield[x][y - 1] == 14 || matchfield[x][y - 1] == 15) {
								if (matchfield[x][1] == 10 | matchfield[x][1] == 11 || matchfield[x][1] == 12 ||
										matchfield[x][1] == 13 || matchfield[x][1] == 14 ||
										matchfield[x][1] == 15 || matchfield[x][1] == 16) {
									if (matchfield[0][y] == 7 || matchfield[0][y] == 8 || matchfield[0][y] == 9 ||
											matchfield[0][y] == 10 || matchfield[0][y] == 11 ||
											matchfield[0][y] == 12 || matchfield[0][y] == 13) {
										matchfield[x][y] = 12;
									} else {
										matchfield[x][y] = 13;
									}
								} else if (matchfield[0][y] == 7 || matchfield[0][y] == 8 || matchfield[0][y] == 9 ||
										matchfield[0][y] == 10 || matchfield[0][y] == 11 ||
										matchfield[0][y] == 12 || matchfield[0][y] == 13) {
									matchfield[x][y] = 11;
								} else {
									matchfield[x][y] = 10;
								}
							} else if (matchfield[x][1] == 10 | matchfield[x][1] == 11 || matchfield[x][1] == 12 ||
									matchfield[x][1] == 13 || matchfield[x][1] == 14 ||
									matchfield[x][1] == 15 || matchfield[x][1] == 16) {
								if (matchfield[0][y] == 7 || matchfield[0][y] == 8 || matchfield[0][y] == 9 ||
										matchfield[0][y] == 10 || matchfield[0][y] == 11 ||
										matchfield[0][y] == 12 || matchfield[0][y] == 13) {
									matchfield[x][y] = 8;
								} else {
									matchfield[x][y] = 9;
								}
							} else if (matchfield[0][y] == 7 || matchfield[0][y] == 8 || matchfield[0][y] == 9 ||
									matchfield[0][y] == 10 || matchfield[0][y] == 11 ||
									matchfield[0][y] == 12 || matchfield[0][y] == 13) {
								matchfield[x][y] = 7;
							} else {
								matchfield[x][y] = 17;
							}
						} else if (matchfield[x][y - 1] == 6 || matchfield[x][y - 1] == 8 || matchfield[x][y - 1] == 9 ||
								matchfield[x][y - 1] == 12 || matchfield[x][y - 1] == 13 ||
								matchfield[x][y - 1] == 14 || matchfield[x][y - 1] == 15) {
							if (matchfield[x][1] == 10 | matchfield[x][1] == 11 || matchfield[x][1] == 12 ||
									matchfield[x][1] == 13 || matchfield[x][1] == 14 ||
									matchfield[x][1] == 15 || matchfield[x][1] == 16) {
								if (matchfield[0][y] == 7 || matchfield[0][y] == 8 || matchfield[0][y] == 9 ||
										matchfield[0][y] == 10 || matchfield[0][y] == 11 ||
										matchfield[0][y] == 12 || matchfield[0][y] == 13) {
									matchfield[x][y] = 14;
								} else {
									matchfield[x][y] = 15;
								}
							} else if (matchfield[0][y] == 7 || matchfield[0][y] == 8 || matchfield[0][y] == 9 ||
									matchfield[0][y] == 10 || matchfield[0][y] == 11 ||
									matchfield[0][y] == 12 || matchfield[0][y] == 13) {
								matchfield[x][y] = 16;
							} else {
								matchfield[x][y] = 18;
							}
						} else if (matchfield[x][1] == 10 | matchfield[x][1] == 11 || matchfield[x][1] == 12 ||
								matchfield[x][1] == 13 || matchfield[x][1] == 14 ||
								matchfield[x][1] == 15 || matchfield[x][1] == 16) {
							if (matchfield[0][y] == 7 || matchfield[0][y] == 8 || matchfield[0][y] == 9 ||
									matchfield[0][y] == 10 || matchfield[0][y] == 11 ||
									matchfield[0][y] == 12 || matchfield[0][y] == 13) {
								matchfield[x][y] = 6;
							} else {
								matchfield[x][y] = 20;
							}
						} else if (matchfield[0][y] == 7 || matchfield[0][y] == 8 || matchfield[0][y] == 9 ||
								matchfield[0][y] == 10 || matchfield[0][y] == 11 ||
								matchfield[0][y] == 12 || matchfield[0][y] == 13) {
							matchfield[x][y] = 19;
						} else {
							matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 6)) + 21);
							houses += 1;
						}
					}
				}
			}
		}
		// Hinzufügen der Objekte
		int x = Math.toIntExact(Math.round(Math.floor(matchfield.length / 2)));
		int y = Math.toIntExact(Math.round(Math.floor((matchfield[0].length) / 2)));
		// Polizist
		objectfield[x][y] = matchfield[x][y];
		matchfield[x][y] = 28;
		// NPCs
		for (y = 1; y < matchfield[0].length; y++) {
			for (x = 0; x < matchfield.length; x++) {
				if (matchfield[x][y] < 21 & Math.toIntExact(Math.round(Math.floor(Math.random() * 20))) == 0) {
					objectfield[x][y] = matchfield[x][y];
					matchfield[x][y] = Math.toIntExact(Math.round(Math.floor(Math.random() * 2)) + 29);
				}
			}
		}
	}

	private static void moveOfficer() throws InterruptedException {
		char input = 'w';
		for (int y = 1; y < matchfield[0].length; y++) {
			for (int x = 0; x < matchfield.length; x++) {
				if (matchfield[x][y] == 28) {
					boolean retry;
					do {
						retry = false;
						input = window.listen();
						if ((input == 'w' || input == 'W') & matchfield[x][y - 1] < 21) {
							// Bewegt den Spieler nach oben
							matchfield[x][y] = objectfield[x][y];
							/*
							 * if(matchfield[x][y] < 21) {
							 * matchfield[x][y] = objectfield[x][y];
							 * }
							 * else {
							 * matchfield[x][y] = 27;
							 * }
							 */
							objectfield[x][y - 1] = matchfield[x][y - 1];
							matchfield[x][y - 1] = 28;
							// Bewegt das Spielfeld nach unten
							int[][] new_field = new int[matchfield.length][matchfield[0].length];
							for (int y_2 = 0; y_2 < matchfield[0].length; y_2++) {
								for (int x_2 = 0; x_2 < matchfield.length; x_2++) {
									if (y_2 == 0) {
										new_field[x_2][y_2] = matchfield[x_2][y_2];
									} else if (y_2 == 1) {
										new_field[x_2][y_2] = matchfield[x_2][matchfield[0].length - 1];
									} else {
										new_field[x_2][y_2] = matchfield[x_2][y_2 - 1];
									}
								}
							}
							matchfield = new_field;
							new_field = new int[15][16];
							for (int y_2 = 1; y_2 < objectfield[0].length; y_2++) {
								for (int x_2 = 0; x_2 < objectfield.length; x_2++) {
									if (y_2 == 1) {
										new_field[x_2][y_2] = objectfield[x_2][objectfield[0].length - 1];
									} else {
										new_field[x_2][y_2] = objectfield[x_2][y_2 - 1];
									}
								}
							}
							objectfield = new_field;
						} else if ((input == 'a' || input == 'A') & matchfield[x - 1][y] < 21) {
							// Bewegt den Spieler nach links
							matchfield[x][y] = objectfield[x][y];
							/*
							 * if(matchfield[x][y] < 21) {
							 * matchfield[x][y] = objectfield[x][y];
							 * }
							 * else {
							 * matchfield[x][y] = 27;
							 * }
							 */
							objectfield[x - 1][y] = matchfield[x - 1][y];
							matchfield[x - 1][y] = 28;
							// Bewegt das Spielfeld nach rechts
							int[][] new_field = new int[matchfield.length][matchfield[0].length];
							for (int y_2 = 0; y_2 < matchfield[0].length; y_2++) {
								for (int x_2 = 0; x_2 < matchfield.length; x_2++) {
									if (y_2 == 0) {
										new_field[x_2][y_2] = matchfield[x_2][y_2];
									} else if (x_2 == 0) {
										new_field[x_2][y_2] = matchfield[matchfield.length - 1][y_2];
									} else {
										new_field[x_2][y_2] = matchfield[x_2 - 1][y_2];
									}
								}
							}
							matchfield = new_field;
							new_field = new int[15][16];
							for (int y_2 = 1; y_2 < objectfield[0].length; y_2++) {
								for (int x_2 = 0; x_2 < objectfield.length; x_2++) {
									if (x_2 == 0) {
										new_field[x_2][y_2] = objectfield[objectfield.length - 1][y_2];
									} else {
										new_field[x_2][y_2] = objectfield[x_2 - 1][y_2];
									}
								}
							}
							objectfield = new_field;
						} else if ((input == 's' || input == 'S') & matchfield[x][y + 1] < 21) {
							// Bewegt den Spieler nach unten
							matchfield[x][y] = objectfield[x][y];
							/*
							 * if(matchfield[x][y] < 21) {
							 * matchfield[x][y] = objectfield[x][y];
							 * }
							 * else {
							 * matchfield[x][y] = 27;
							 * }
							 */
							objectfield[x][y + 1] = matchfield[x][y + 1];
							matchfield[x][y + 1] = 28;
							// Bewegt das Spielfeld nach oben
							int[][] new_field = new int[matchfield.length][matchfield[0].length];
							for (int y_2 = 0; y_2 < matchfield[0].length; y_2++) {
								for (int x_2 = 0; x_2 < matchfield.length; x_2++) {
									if (y_2 == 0) {
										new_field[x_2][y_2] = matchfield[x_2][y_2];
									} else if (y_2 == matchfield[0].length - 1) {
										new_field[x_2][y_2] = matchfield[x_2][1];
									} else {
										new_field[x_2][y_2] = matchfield[x_2][y_2 + 1];
									}
								}
							}
							matchfield = new_field;
							new_field = new int[15][16];
							for (int y_2 = 1; y_2 < objectfield[0].length; y_2++) {
								for (int x_2 = 0; x_2 < objectfield.length; x_2++) {
									if (y_2 == objectfield[0].length - 1) {
										new_field[x_2][y_2] = objectfield[x_2][1];
									} else {
										new_field[x_2][y_2] = objectfield[x_2][y_2 + 1];
									}
								}
							}
							objectfield = new_field;
						} else if ((input == 'd' || input == 'D') & matchfield[x + 1][y] < 21) {
							// Bewegt den Spieler nach rechts
							matchfield[x][y] = objectfield[x][y];
							/*
							 * if(matchfield[x][y] < 21) {
							 * matchfield[x][y] = objectfield[x][y];
							 * }
							 * else {
							 * matchfield[x][y] = 27;
							 * }
							 */
							objectfield[x + 1][y] = matchfield[x + 1][y];
							matchfield[x + 1][y] = 28;
							// Bewegt das Spielfeld nach links
							int[][] new_field = new int[matchfield.length][matchfield[0].length];
							for (int y_2 = 0; y_2 < matchfield[0].length; y_2++) {
								for (int x_2 = 0; x_2 < matchfield.length; x_2++) {
									if (y_2 == 0) {
										new_field[x_2][y_2] = matchfield[x_2][y_2];
									} else if (x_2 == matchfield.length - 1) {
										new_field[x_2][y_2] = matchfield[0][y_2];
									} else {
										new_field[x_2][y_2] = matchfield[x_2 + 1][y_2];
									}
								}
							}
							matchfield = new_field;
							new_field = new int[15][16];
							for (int y_2 = 1; y_2 < objectfield[0].length; y_2++) {
								for (int x_2 = 0; x_2 < objectfield.length; x_2++) {
									if (x_2 == objectfield.length - 1) {
										new_field[x_2][y_2] = objectfield[1][y_2];
									} else {
										new_field[x_2][y_2] = objectfield[x_2 + 1][y_2];
									}
								}
							}
							objectfield = new_field;
						} else if (input == ' ' || input == '\n') {
							/*
							 * AudioPlayer sound_player = new AudioPlayer("../../assets/sound_" +
							 * Math.toIntExact(Math.round(Math.floor(Math.random() * 10))) + ".wav");
							 * sound_player.play();
							 */
							// Check ob falsch gepfiffen wurde
							if (matchfield[x - 1][y] == 30) {
								matchfield[x - 1][y] = objectfield[x - 1][y];
								money += 100;
							} else if (matchfield[x][y - 1] == 30) {
								matchfield[x][y - 1] = objectfield[x][y - 1];
								money += 100;
							} else if (matchfield[x + 1][y] == 30) {
								matchfield[x + 1][y] = objectfield[x + 1][y];
								money += 100;
							} else if (matchfield[x][y + 1] == 30) {
								matchfield[x][y + 1] = objectfield[x][y + 1];
								money += 100;
							} else {
								money -= 100;
							}
							if (money > most_money) {
								most_money = money;
							}
							System.out.println("\nGeld:\n" + money + "\nHighscore:\n" + most_money);
						} else {
							retry = true;
						}
					} while (retry);
					window.update(matchfield, Integer.toString(money), Integer.toString(most_money));
					break;
				}
			}
		}
		if (input == ' ' || input == '\n') {
			input = 's';
		}
		// moveNPCs(input);
	}

	private static void moveNPCs(char input) {
		for (int y = 1; y < matchfield[0].length; y++) {
			for (int x = 0; x < matchfield.length; x++) {
				if (matchfield[x][y] > 28) {
					int npc = matchfield[x][y];
					if ((input == 'w' || input == 'W') & matchfield[x][y - 1] < 21) {
						// Bewegt den Spieler nach oben
						objectfield[x][y - 1] = matchfield[x][y - 1];
						matchfield[x][y - 1] = npc;
					} else if ((input == 'a' || input == 'A') & matchfield[x - 1][y] < 21) {
						// Bewegt den Spieler nach links
						objectfield[x - 1][y] = matchfield[x - 1][y];
						matchfield[x - 1][y] = npc;
					} else if ((input == 's' || input == 'S') & matchfield[x][y + 1] < 21) {
						// Bewegt den Spieler nach unten
						objectfield[x][y + 1] = matchfield[x][y + 1];
						matchfield[x][y + 1] = npc;
					} else if ((input == 'd' || input == 'D') & matchfield[x + 1][y] < 21) {
						// Bewegt den Spieler nach rechts
						objectfield[x + 1][y] = matchfield[x + 1][y];
						matchfield[x + 1][y] = npc;
					}
					matchfield[x][y] = objectfield[x][y];
				}
			}
		}
	}

}