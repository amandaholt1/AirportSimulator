import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Game {

    public static void takeFlight() throws Exception {
        String personName="";
        //make it a 5 letter name
        Scanner scanner = new Scanner(System.in);  // Create a Scanner object

        while ( personName.length()!=5  ) {
            System.out.println("Please enter a name.");
            personName = scanner.nextLine();
        }

        //part a
        System.out.println("How much money do you have?");
        System.out.print("$");

        // Read user input
        String userInput = scanner.nextLine();
        double userMoney = 0.0;
        try {
            userMoney = Double.parseDouble(userInput);
        }
        catch(Exception e) {
            System.out.println("Please try a number!");
            System.out.println("How much money do you have?");
            System.out.print("$");
            // Read user input
            userInput = scanner.nextLine();
        }
        //part b
        System.out.println("What is your time constraint?");
        // Read user input
        String userTime = scanner.nextLine();
        System.out.println("Where would you like to fly to?\n" +
                "1 - LAX\n" +
                "2 - JFK\n" +
                "3 - ORD\n" +
                "4 - CLT\n");

        while (!scanner.hasNextInt()) {
            System.out.println("Where would you like to fly to?\n" +
                    "1 - LAX\n" +
                    "2 - JFK\n" +
                    "3 - ORD\n" +
                    "4 - CLT\n");
        }
        int destination = scanner.nextInt();
        String destinationString = "";
        switch (destination) {
            case 1:
                destinationString = "LAX";
                break;
            case 2:
                destinationString = "JFK";
                break;
            case 3:
                destinationString = "ORD";
                break;
            default:
            case 4:
                destinationString = "CLT";
                break;
        }

        int planeSize = 6;
        System.out.println("What size plane will you be flying on?\n1 - small\n2 - medium\n3 - large");
        while (!scanner.hasNextInt()) {
            System.out.println("What size plane will you be flying on?\n1 - small\n2 - medium\n3 - large");
        }
        int selectedSize = scanner.nextInt();
        if (selectedSize == 2) planeSize = 10;
        if (selectedSize == 3) planeSize = 14;


        int row = 1;
        int seat = 1;
        String letter = "A";

        //seat preference?
        System.out.println("Do you have any seat preferences?");
        System.out.println("0: NO.    1:YES?");
        while (!scanner.hasNextInt()) {
            System.out.println("Do you have any seat preferences?");
            System.out.println("0: NO.    1:YES?");
        }
        int seatPref = scanner.nextInt();
        if (seatPref == 0) {
            row = (int) Math.floor(Math.random() * 8);
            double r = Math.random();
            if (r <= 0.33) letter = "B";
            else if (r <= 0.67) letter = "C";
        } else {
            System.out.println("Select your row from 1 to " + planeSize);
            while (!scanner.hasNextInt()) {
                System.out.println("Select your row from 1 to " + planeSize);
            }
            row = scanner.nextInt();

            System.out.println("Seat position:");
            System.out.println("1 - Aisle\n2 - Window\n3 - Middle");
            while (!scanner.hasNextInt()) {
                System.out.println("Seat position:");
                System.out.println("1 - Aisle\n2 - Window\n3 - Middle");
            }
            seat = scanner.nextInt();
            switch (seat) {
                default:
                case 1:
                    letter = "C";
                    seat = 2;
                    break;
                case 3:
                    letter = "B";
                    seat = 1;
                    break;
                case 2:
                    letter = "A";
                    seat = 0;
                    break;
            }
        }


        Ticket customTicket = new Ticket(
                personName,
                "SEA-TAC",
                destinationString,
                userTime,
                userMoney,
                "First",
                row,
                letter);

        System.out.println("You're all set, heading to the airplane now.");
        Thread.sleep(2000);

        Queue<Ticket> bunchOfTickets = Ticket.generateRandomTickets((planeSize * 6) - 1, planeSize, 6, row, seat);
        for (Ticket t : bunchOfTickets) System.out.println(t.seatRow + ", " + t.seatLetter);
        bunchOfTickets.add(customTicket);
        Queue<Ticket> afterTSA = TSA.check(bunchOfTickets, 5, 400);
        Airplane ap = new Airplane(planeSize, 3);
        ap.board(afterTSA, customTicket.toDestination);

        Thread.sleep(1000);

        takeFlight();
    }



    public static class Ticket {
        public String ownerName;
        public String fromDestination;
        public String toDestination;
        public String departureTime;
        public double cost;
        public String seatClass;
        public int seatRow;
        public String seatLetter;


        public Ticket(String ownerName, String fromDestination, String toDestination, String departureTime,
                      double cost, String seatClass, int seatRow, String seatLetter) {
            this.ownerName = ownerName;
            this.departureTime = departureTime;
            this.cost = cost;
            this.seatClass = seatClass;
            this.seatRow = seatRow;
            this.seatLetter = seatLetter;
            this.fromDestination = fromDestination;
            this.toDestination = toDestination;
        }


        @Override
        public String toString() {
            return fromDestination + " to " + toDestination + ", " + seatClass + " class: $" + cost + " " + seatLetter + seatRow + " " + ownerName;
        }


        public static Queue<Ticket> generateRandomTickets(int n, int maxRow, int rowWidth, int playerRow, int playerSeat) throws Exception {
            if (maxRow * rowWidth < n) {
                System.out.println("error, not possible");
                throw new Exception();
            }
            Queue<Ticket> res = new LinkedList<Ticket>();
            int[][] seats = new int[maxRow][rowWidth];
            seats[playerRow - 1][playerSeat] = 1;
            for (int i = 0; i < n; i++) {
                Seat seat = generateSeat(seats);
                res.add(new Ticket(
                        names[(int) (Math.random() * names.length)],
                        "Seattle",
                        "Texas",
                        "0900",
                        100.00,
                        "First",
                        seat.row + 1,
                        convertIntToSeat(seat.letter)
                ));

            }
            return res;
        }


        private static String convertIntToSeat(int letter) {
            switch (letter) {
                default:
                case 0:
                    return "A";
                case 1:
                    return "B";
                case 2:
                    return "C";
                case 3:
                    return "D";
                case 4:
                    return "E";
                case 5:
                    return "F";
            }
        }


        private static class Seat {
            int row;
            int letter;
            Seat(int row, int letter) {
                this.row = row;
                this.letter = letter;
            }
        }


        public static Seat generateSeat(int[][] seats) {
            int seatRow = (int) (Math.random() * seats.length);
            int seatLetter = (int) (Math.random() * seats[0].length);
            Seat seat = new Seat(seatRow, seatLetter);
            if (seats[seatRow][seatLetter] == 1) { // seat is taken
                seat = generateSeat(seats);
            }
            seats[seatRow][seatLetter] = 1; // mark seat as taken
            return seat;
        }


        private static String[] names = new String[]{
                "Sally",
                "Steve",
                "Abdul",
                "Adele",
                "Aaron",
                "Clair",
                "  AJ ",
                "Aiden",
                " Joe ",
                " Sara",
                "Allie",
                "Amber",
                "Angel",
                "Brady",
                "Debra",
                "Denis",
                "Derek",
                "Erika",
                "Gemma",
                "Holly",
                "Isaac",
                "Kacey",
                "Kelly",
                " Bill",
                "Mandy",
                "Myles",
                "Nancy",
                "Paige",
                "Ricky",
                "Riley",
                "Rylan",
                "Raven",
                "Shane",
                "Sadie",
                "Trish",
                "Wendy",
        };
    }



    public static class TSA {
        Queue<Ticket> queue;
        TSA() {
            this.queue = new LinkedList<>();
        }

        public static Queue<Ticket> check(Queue<Ticket> input, int length, long speed) throws InterruptedException {
            Ticket[] arr = new Ticket[length];
            Queue<Ticket> res = new LinkedList<>();

            printLine(arr);
            while (!input.isEmpty()) {
                // shift elements in arr to the right
                for (int i = arr.length - 1; i > 0; i--) {
                    // add elements in the last position to output
                    if (i == arr.length - 1 && arr[i] != null) res.add(arr[i]);
                    arr[i] = arr[i-1];
                }
                // add a new element from input to arr
                arr[0] = input.remove();
                printLine(arr);
                Thread.sleep(speed);
            }


            // move elements to the right then make the first position empty
            if (arr[length - 1] != null) res.add(arr[length-1]);
            System.arraycopy(arr, 0, arr, 1, arr.length - 1);
            arr[0] = null;
            printLine(arr);
            Thread.sleep(speed);

            for (int i = 1; i < arr.length; i++) {
                if (arr[length - 1] != null) res.add(arr[length-1]);
                System.arraycopy(arr, 0, arr, 1, arr.length - 1);
                printLine(arr);
                Thread.sleep(speed);
            }

            return res;
        }

        private static void printLine(Ticket[] arr) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) System.out.println("\n"); // clear screen with empty lines
            sb.append("                  [TSA]\n\n");
            sb.append("|");
            for (Ticket t : arr) {
                sb.append(t != null ? " " + t.ownerName : "      "); // # empty spaces = ticket.name.length + 1
                sb.append(" |");
            }
            sb.append("\n");

            // stick figure:
            for (Ticket t : arr) sb.append(t != null ? "   o    " : "        ");
            sb.append("\n");
            for (Ticket t : arr) sb.append(t != null ? "  /|\\   " : "        ");
            sb.append("\n");
            for (Ticket t : arr) sb.append(t != null ? "  / \\   " : "        ");
            sb.append("\n");
            System.out.println(sb.toString());
        }

    }



    /*
    Tickets = passengers. Passengers enter the plane one at a time and find their seats.
     */
    public static class Airplane {
        Ticket[] aisle;
        Ticket[][] seats;
        long boardingSpeed = 100;

        public Airplane(int rows, int seatsPerRow) {
            aisle = new Ticket[rows + seatsPerRow + 1]; // extra spaces to allow passengers to move out of the way during boarding
            seats = new Ticket[rows * 2][seatsPerRow]; // each row has two arrays, one on each side of the aisle
            for (int i = 0; i < rows * 2; i += 2) {
                seats[i] = new Ticket[seatsPerRow];
                seats[i + 1] = new Ticket[seatsPerRow];
            }
        }


        public void board(Queue<Ticket> passengers, String destination) throws InterruptedException {
            while (!passengers.isEmpty()) {
                Ticket p = passengers.remove();
                int row = p.seatRow * 2 - 2; // * 2 - 2 is because there are two arrays for each row, for each side of the aisle
                if (convertSeatLetterToInt(p.seatLetter) >= seats[0].length) row++; // this determines which side of the aisle they sit on

                // move p to the space right before their seat row
                for (int i = 1; i <= p.seatRow ; i++) {
                    aisle[i] = p;
                    aisle[i - 1] = null;
                    System.out.println(planeDrawing());
                    Thread.sleep(boardingSpeed);
                }

                // if p's seat is an aisle seat (index 0) just move them there
                if (convertSeatLetterToPosition(p.seatLetter) == 0) {
                    movePUpOneAndIntoRow(row, p);
                }

                if (convertSeatLetterToPosition(p.seatLetter) == 1) { // index 1
                    System.out.println("seats " + Arrays.toString(seats));
                    if (seats[row][0] != null) {
                        // move the sitting passenger out of the way
                        aisle[p.seatRow + 1] = seats[row][0];
                        seats[row][0] = null;
                        System.out.println(planeDrawing());
                        Thread.sleep(boardingSpeed);
                        aisle[p.seatRow + 2] = aisle[p.seatRow + 1];
                        aisle[p.seatRow + 1] = null;
                        System.out.println(planeDrawing());
                        Thread.sleep(boardingSpeed);

                        // move the new passenger to their seat
                        movePUpOneAndIntoRow(row, p);
                        seats[row][1] = p;
                        seats[row][0] = null;
                        System.out.println(planeDrawing());
                        Thread.sleep(boardingSpeed);

                        // re-seat the other passenger
                        aisle[p.seatRow + 1] = aisle[p.seatRow + 2];
                        aisle[p.seatRow + 2] = null;
                        System.out.println(planeDrawing());
                        Thread.sleep(boardingSpeed);
                        seats[row][0] = aisle[p.seatRow + 1];
                        aisle[p.seatRow + 1] = null;
                    } else {
                        // move the new passenger to their seat
                        movePUpOneAndIntoRow(row, p);
                        seats[row][1] = p;
                        seats[row][0] = null;
                    }
                    System.out.println(planeDrawing());
                    Thread.sleep(boardingSpeed);
                }

                if (convertSeatLetterToPosition(p.seatLetter) == 2) { // window seats
                    if (seats[row][0] != null || seats[row][1] != null) {
                        // move the sitting passenger(s) out of the way
                        aisle[p.seatRow + 1] = seats[row][0];
                        seats[row][0] = seats[row][1];
                        seats[row][1] = null;
                        System.out.println(planeDrawing());
                        Thread.sleep(boardingSpeed);
                        aisle[p.seatRow + 2] = aisle[p.seatRow + 1];
                        aisle[p.seatRow + 1] = seats[row][0];
                        seats[row][0] = null;
                        System.out.println(planeDrawing());
                        Thread.sleep(boardingSpeed);
                        aisle[p.seatRow + 3] = aisle[p.seatRow + 2];
                        aisle[p.seatRow + 2] = aisle[p.seatRow + 1];
                        aisle[p.seatRow + 1] = null;
                        System.out.println(planeDrawing());
                        Thread.sleep(boardingSpeed);
                    }

                    // move the new passenger to their seat
                    movePUpOneAndIntoRow(row, p);
                    seats[row][1] = p;
                    seats[row][0] = null;
                    System.out.println(planeDrawing());
                    Thread.sleep(boardingSpeed);
                    seats[row][2] = p;
                    seats[row][1] = null;
                    System.out.println(planeDrawing());
                    Thread.sleep(boardingSpeed);

                    // re-seat the other passengers
                    if (aisle[p.seatRow + 2] != null || aisle[p.seatRow + 3] != null) {
                        aisle[p.seatRow + 1] = aisle[p.seatRow + 2];
                        aisle[p.seatRow + 2] = aisle[p.seatRow + 3];
                        aisle[p.seatRow + 3] = null;
                        System.out.println(planeDrawing());
                        Thread.sleep(boardingSpeed);
                        seats[row][0] = aisle[p.seatRow + 1];
                        aisle[p.seatRow + 1] = aisle[p.seatRow + 2];
                        aisle[p.seatRow + 2] = null;
                        System.out.println(planeDrawing());
                        Thread.sleep(boardingSpeed);
                        seats[row][1] = seats[row][0];
                        seats[row][0] = aisle[p.seatRow + 1];
                        aisle[p.seatRow + 1] = null;
                        System.out.println(planeDrawing());
                        Thread.sleep(boardingSpeed);
                    }
                }
            }

            takeOff(planeDrawing(), destination);
        }


        private void movePUpOneAndIntoRow(int row, Ticket p) throws InterruptedException {
            aisle[p.seatRow + 1] = aisle[p.seatRow];
            aisle[p.seatRow] = null;
            System.out.println(planeDrawing());
            Thread.sleep(boardingSpeed);
            seats[row][0] = aisle[p.seatRow + 1];
            aisle[p.seatRow + 1] = null;
            System.out.println(planeDrawing());
            Thread.sleep(boardingSpeed);
        }


        private int convertSeatLetterToPosition(String letter) {
            switch (letter) {
                case "A":
                case "F":
                    return 2;
                case "B":
                case "E":
                    return 1;
                case "C":
                case "D":
                default:
                    return 0;
            }
        }


        private int convertSeatLetterToInt(String letter) {
            switch (letter) {
                default:
                case "A": return 0;
                case "B": return 1;
                case "C": return 2;
                case "D": return 3;
                case "E": return 4;
                case "F": return 5;
            }
        }


        // this prints a drawing of the plane based on data in seats[][] and aisle[]
        // the empty spaces in this method are used to line everything up, it is set up for ownerName lengths of 5
        public String planeDrawing() {
            StringBuilder s = new StringBuilder();
            s.append("\n\n\n\n\n\n\n"); // this is just a buffer to make it look like the drawing refreshes

            // top wing/elevator
            for (int j = 0; j < seats.length/2; j++) s.append(" ");
            s.append("/------=-------\\\n");
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < seats.length/2; j++) s.append(" ");
                s.append("|              |\n");
            }
            for (int i = 0; i < seats.length/2; i++) s.append(" ");
            s.append("|              |");
            for (int i = 0; i < seats.length/2; i++) s.append("   ");
            s.append("   /-------|\n");
            for (int i = 0; i < seats.length/2; i++) s.append(" ");
            s.append("|              |");
            for (int i = 0; i < seats.length/2; i++) s.append("   ");
            s.append("  /        |\n");
            for (int i = 0; i < seats.length/2; i++) s.append(" ");
            s.append("|              |");
            for (int i = 0; i < seats.length/2; i++) s.append("   ");
            s.append(" /         |\n");
            for (int i = 0; i < seats.length/2; i++) s.append(" ");
            s.append("|              |");
            for (int i = 0; i < seats.length/2; i++) s.append("   ");
            s.append("|          |\n");

            // top wall
            for (int i = 0; i < seats.length/2+1; i++) {
                s.append("------");
            }
            s.append("-------\n");

            // top seats
            for (int i = seats[0].length - 1; i >= 0; i--) {
                s.append("            |");
                for (int j = 0; j < seats.length; j+=2) {
                    s.append(seats[j][i] != null ? seats[j][i].ownerName : "     ");
                    s.append("|");
                }
                s.append("\n");
            }

            // aisle
            for (Ticket t : aisle) {
                s.append(t != null ? " " + t.ownerName + " " : "      ");
            }
            s.append("\n");

            // bottom seats
            for (int i = 0; i < seats[0].length; i++) {
                s.append("            |");
                for (int j = 1; j < seats.length; j+=2) {
                    s.append(seats[j][i] != null ? seats[j][i].ownerName : "     ");
                    s.append("|");
                }
                s.append("\n");
            }

            // bottom wall
            s.append("-------");
            for (int i = 0; i < seats.length/2+1; i++) {
                s.append("------");
            }
            s.append("\n");

            // bottom wing and elevator
            for (int i = 0; i < seats.length/2; i++) s.append(" ");
            s.append("|              |");
            for (int i = 0; i < seats.length/2; i++) s.append("   ");
            s.append("|          |\n");
            for (int i = 0; i < seats.length/2; i++) s.append(" ");
            s.append("|              |");
            for (int i = 0; i < seats.length/2; i++) s.append("   ");
            s.append(" \\         |\n");
            for (int i = 0; i < seats.length/2; i++) s.append(" ");
            s.append("|              |");
            for (int i = 0; i < seats.length/2; i++) s.append("   ");
            s.append("  \\        |\n");
            for (int i = 0; i < seats.length/2; i++) s.append(" ");
            s.append("|              |");
            for (int i = 0; i < seats.length/2; i++) s.append("   ");
            s.append("   \\-------|\n");
            for (int i = 0; i < seats.length/2; i++) s.append(" ");
            s.append("|              |");

            return s.toString();
        }


        private void takeOff(String plane, String destination) throws InterruptedException {
            int aisleIndex = 18 + seats[0].length;
            String[] lines = plane.split("\n");

            // "preparing for takeoff..."
            StringBuilder newAisleString = new StringBuilder();
            for (int i = 0; i < seats.length/2-1; i++) newAisleString.append("   ");
            newAisleString.append("Preparing for takeoff");
            for (int i = 0; i < 5; i++) {
                StringBuilder p = new StringBuilder();
                p.append("\n\n\n\n\n\n\n");
                for (int j = 0; j < aisleIndex; j++) {
                    p.append(lines[j]);
                    p.append("\n");
                }
                p.append(newAisleString);
                p.append("\n");
                newAisleString.append(".");
                for (int k = aisleIndex + 1; k < lines.length; k++) {
                    p.append(lines[k]);
                    p.append("\n");
                }
                System.out.println(p.toString());
                Thread.sleep(700);
            }

            // taxi away
            long speed = 800;
            for (int i = 1; i < lines[aisleIndex].length() - 1; i++) {
                StringBuilder s = new StringBuilder();
                s.append("\n\n\n\n\n\n\n");

                for (String line : lines) {
                    if (line.length() > i) {
                        s.append(line.substring(i));
                    }
                    s.append("\n");
                }
                System.out.println(s.toString());
                Thread.sleep(speed);
                speed = Math.max(25, speed - 50);
            }

            System.out.println("You've arrived at " + destination + "!");
            Thread.sleep(1000);
            System.out.println("Want to buy another ticket?\n");
            Thread.sleep(1000);
        }
    }


    public static void main(String[] args) throws Exception {
        takeFlight();
    }

}
