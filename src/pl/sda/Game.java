package pl.sda;

import java.util.Scanner;

public class Game {

    // Stan gracza 1
    private int lifepointsOne;
    private char[][] shipsOne;
    private char[][] shotsOne;
    private boolean playerOneStillAlive = true;

    // Stan gracza 2
    private int lifepointsTwo;
    private char[][] shipsTwo;
    private char[][] shotsTwo;
    private boolean playerTwoStillAlive = true;

    // Znaczki
    private final char ship = 's';
    private final char water = 'o';
    private final char hit = 'x';
    private final char unknown = '?';

    // Rozmiar planszy, liczba żyć każdego gracza i czyj teraz ruch
    // do testów możesz ustawić jakieś małe wartości jak ja, zadziała dla 10x10, 100x100
    private final int width = 10;
    private final int height = 10;
    private final int maxHitpoints = 20;

    private boolean movePlayerOne = true; // zaczyna gracz 1, nasza przekładnia do ruchów

    // Wejście dla konsolowej wersji gry, zmienne wpisywane z klawiatury
    private final Scanner scanner = new Scanner(System.in);

    public Game() {
        // Gra ma 3  -
        // FIXME : Faza 1 - techniczne przygotowanie zmiennych

        lifepointsOne = 0; // zdrowie zaczniemy dodawać jak ustawi sobie statki
        shipsOne = new char[width][height];
        shotsOne = new char[width][height];

        lifepointsTwo = 0; // j. w.
        shipsTwo = new char[width][height];
        shotsTwo = new char[width][height];
        // wpisywanie znaków do każdej z pustych plansz
        // za jednym zamachem do każdej, a co
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                shipsOne[i][j] = this.water; // na początku woda, potem gracze ustawiają statki strzały
                shipsTwo[i][j] = this.water;
                shotsOne[i][j] = this.unknown; // na początku nieznane, jeszcze nie padły
                shotsTwo[i][j] = this.unknown;
            }
        }

        // FIXME : Faza 2 - ustawianie statków - dla androida tu


        // Dla androida zamiast scannera, trzeba planszę ze znaczkami wyświetlić użytkownikowi
        // Dotknięcie pola na planszy przełącza między statkiem, a wodą

        // i dodaje lub odejmuje punkt zdrowia

        // a tymczasem, dopóki gracz pierwszy nie ma wszystkich punktów zdrowia..
        System.out.println("Witaj w grze Sink IT!. Planszą do gry jest macierz 10x10, gdzie 0 oznacza pierwszą, a 9 ostatnią pozycję.");
        System.out.println("Aby ustawić statek lub oddać strzał, podaj współrzędne x od 0-9. Zatwierdź klawiszem Enter.");
        System.out.println("Podaj współrzędne dla osi y. Zatwierdź klawiszem Enter.");

        System.out.println(" ");
        System.out.println("Gracz 1 ustawia statki.");
        System.out.println(" ");

        while (lifepointsOne < maxHitpoints) {

            // tutaj jest drukowana użytkownikowi macierz, żeby widział co ustawia

            System.out.println("Gracz 1");
            print(shipsOne);

            // pobierz pozycje w macierzy od użytkownika (od 0 do 9 dla width/height == 10)
            // UWAGA : jeśli gracz wpisze coś spoza zasięgu macierzy (np. -1) gra się wykrzaczy

            int x = scanner.nextInt();
            int y = scanner.nextInt();

            // na wskazanym polu już może być statek, jeśli nie ma statku już na polu..
            if (shipsOne[x][y] != ship) {
                // to ustawimy statek..
                shipsOne[x][y] =  ship;
                // i dodamy punkt zdrowia
                lifepointsOne += 1;
            }
        }

        System.out.println("Gracz 1 zakończył ustawianie statków");
        System.out.println("Gracz 2 ustawia statki.");

        // czynność powtarzamy dla drugiego gracza
        while (lifepointsTwo < maxHitpoints) {
            System.out.println("Gracz 2");
            print(shipsTwo);
            int x = scanner.nextInt();
            int y = scanner.nextInt();

            if (shipsTwo[x][y] != ship) {
                shipsTwo[x][y] = ship;
                lifepointsTwo += 1;
            }

        }

        System.out.println("Gracz 2 zakończył ustawianie statków");

        // FIXME : Faza 3 - rozgrywka, wzajemna wymiana strzałów

        // to jest właściwa "pętla gry"

        System.out.println("Rozpoczynam rozgrywkę...");

        do {
            // jeśli gracz pierwszy ma teraz ruch...
            if (movePlayerOne) {
                // najpierw wyświetlam, żeby widział co się dzieje
                System.out.println("Teraz ruch ma gracz 1");
                System.out.println("Gracz 1");
                print(shipsOne); // a druknijmy na konsoli co tam się dzieje na planszy gracza 1
                print(shotsOne);

                // pobieram współrzędne do strzału

                int x = scanner.nextInt();
                int y = scanner.nextInt();

                // tu są dwie opcje: pudło lub trafienie. Sprawdźmy w co trafił gracz 1
                // zaglądamy na wskazaną pozycję do gracza 2..

                if (shipsTwo[x][y] == water) {

                    shotsOne[x][y] = water;

                    System.out.println("Pudło!");
                } else if (shipsTwo[x][y] == ship) {
                    shotsOne[x][y] = hit;
                    shipsTwo[x][y] = hit;

                    // odejmujemy punkt zdrowia graczowi 2
                    lifepointsTwo -= 1;

                    System.out.println("Trafiony!");
                    if (lifepointsTwo < 1) playerTwoStillAlive = false;
                } else if (shipsTwo[x][y] == hit) {
                    System.out.println("Ten statek był już trafiony! Tracisz ruch...");
                }

                // gracz 1 zakończył ruch - musimy ustawić "przekładnię" na gracza 2
                movePlayerOne = false;

            } else { // gracz drugi, analogicznie
                System.out.println("Teraz ruch ma gracz 2");
                System.out.println("Gracz 2");
                print(shipsTwo);
                print(shotsTwo);

                int x = scanner.nextInt();
                int y = scanner.nextInt();

                if (shipsOne[x][y] == water) {
                    shotsTwo[x][y] = water;
                    System.out.println("Pudło!");
                } else if (shipsOne[x][y] == ship) {
                    shotsTwo[x][y] = hit;
                    shipsOne[x][y] = hit;
                    lifepointsOne -= 1;
                    System.out.println("Trafiony!");
                    if (lifepointsOne < 1) playerOneStillAlive = false;
                } else if (shipsTwo[x][y] == hit) {
                    System.out.println("Ten statek był już trafiony! Tracisz ruch...");
                }


                movePlayerOne = true;
            }
        } while (playerOneStillAlive && playerTwoStillAlive); // gra trwa dopóki obaj gracze "żyją"

                if (playerOneStillAlive) { // kto żyje ten wygrywa
            System.out.println("Gracz 1 jest zwycięzcą!");
        } else if (playerTwoStillAlive) { // analogicznie
            System.out.println("Gracz 2 jest zwycięzcą!");
        }
    }

    private static void print(char[][] matrix) { // taka pomocnicza metodka do wydrukowania macierzy w konsoli
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                builder.append(matrix[i][j]);
            }
            builder.append("\n");
        }
        System.out.println(builder.toString());
    }
}
