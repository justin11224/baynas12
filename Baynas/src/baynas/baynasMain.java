package Baynas;

import java.util.Scanner;

public class baynasMain {
    public static void main(String[] args) {
        String response;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println("1. AddPlayer    ");
            System.out.println("2. AddTeam   ");
            System.out.println("3. AddPlayerTeam   ");
            System.out.println("4. Exit  ");

            String action;
            boolean validInput = false;

            do {
                System.out.print("Enter action Number: ");
                action = sc.nextLine().toUpperCase();

                if (action.equals("1") || action.equals("2") || action.equals("3")|| action.equals("4")) {
                    validInput = true;
                } else {
                    System.out.println("Invalid input! Try again!!");
                }
            } while (!validInput);

            AddPlayer Action1 = new AddPlayer();
            AddPlayerTeam Action2 = new AddPlayerTeam();
            AddTeam Action3 = new AddTeam();
            switch (action) {
                case "1":
                    Action1.addPlayerMenu();
                    break;
                case "3":
                    Action2.addplayerteam();
                    break;
                case "2":
                    Action3.addTeamMenu();
                    break;
                case "4":
                    System.out.println("Exiting the program...");
                    break;
                default:
                    System.out.println("Invalid input! Try Again!!");
            }

            if (!action.equals("C")) {
                System.out.print("Do you want to continue? (yes or no): ");
                response = sc.next();
                sc.nextLine();
            } else {
                response = "no";  // Ensure the loop exits after selecting "D"
            }
        } while (response.equalsIgnoreCase("yes"));

        System.out.println("Thank you, see you!");
        sc.close();
    }
}
