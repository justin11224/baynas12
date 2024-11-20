package Baynas;

import java.util.Scanner;

public class AddPlayer {

    public void addPlayerMenu() {
        String response;
        AddPlayer playerApp = new AddPlayer();

        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("Choose an action:");
            System.out.println("1. Add Player");
            System.out.println("2. View Players");
            System.out.println("3. Edit Player");
            System.out.println("4. Delete Player");
            System.out.println("5. EXIT");

            System.out.print("Enter action number: ");
            int action = sc.nextInt();

            switch (action) {
                case 1:
                    playerApp.addPlayer();
                    break;
                case 2:
                    playerApp.viewPlayers();
                    break;
                case 3:
                    playerApp.viewPlayers();
                    playerApp.updatePlayer();
                    playerApp.viewPlayers();
                    break;
                case 4:
                    playerApp.viewPlayers();
                    playerApp.deletePlayer();
                    playerApp.viewPlayers();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return; // Exit the method
                default:
                    System.out.println("Invalid option. Please try again.");
            }

            System.out.print("Do you want to continue? (yes or no): ");
            response = sc.next();

        } while (response.equalsIgnoreCase("yes"));

        System.out.println("Thank you, see you!");
    }

    public void addPlayer() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        String teamId;

        // Get player details
        System.out.print("Enter First Name: ");
        String firstName = sc.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = sc.nextLine();
        System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
        String dateOfBirth = sc.nextLine();
        System.out.print("Enter Position: ");
        String position = sc.nextLine();
        String query = "SELECT * FROM tbl_team";
        String[] headers = {"Team ID", "Team Name", "Team Location", "Team Coach", "Team Contact"};
        String[] columns = {"t_id", "team_name", "t_location", "t_coach", "t_contact"};

        conf.viewRecords(query, headers, columns);
        while (true) {
            System.out.print("Enter Team ID: ");
            teamId = sc.nextLine();
            String checkTeamQuery = "SELECT 1 FROM tbl_team WHERE t_id = ?";
            if (conf.recordExists(checkTeamQuery, teamId)) {
                break; // Team ID exists
            } else {
                System.out.println("Team ID does not exist. Please try again.");
            }
        }

        // Insert player record
        String sql = "INSERT INTO Player (f_name, l_name, dob, p, t_id) VALUES (?, ?, ?, ?, ?)";
        conf.addRecord(sql, firstName, lastName, dateOfBirth, position, teamId);

        System.out.println("Player successfully added!");
    }

    private void viewPlayers() {
        config conf = new config();
        String query = "SELECT * FROM Player";
        String[] headers = {"Player ID", "First Name", "Last Name", "Date Of Birth", "Position", "Team ID"};
        String[] columns = {"p_id", "f_name", "l_name", "dob", "p", "t_id"};

        conf.viewRecords(query, headers, columns);
    }

    private void updatePlayer() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        // Validate Player ID
        String playerId;
        while (true) {
            System.out.print("Enter the Player ID to update: ");
            playerId = sc.next();
            sc.nextLine(); // Consume newline
            String checkPlayerQuery = "SELECT 1 FROM Player WHERE p_id = ?";
            if (conf.recordExists(checkPlayerQuery, playerId)) {
                break; // Player ID exists
            } else {
                System.out.println("Player ID does not exist. Please try again.");
            }
        }

        // Get updated player details
        System.out.print("New First Name: ");
        String firstName = sc.nextLine();
        System.out.print("New Last Name: ");
        String lastName = sc.nextLine();
        System.out.print("New Date of Birth (YYYY-MM-DD): ");
        String dateOfBirth = sc.nextLine();
        System.out.print("New Position: ");
        String position = sc.nextLine();
        String teamId;
        while (true) {
            System.out.print("Enter New Team ID: ");
            teamId = sc.nextLine();
            String checkTeamQuery = "SELECT 1 FROM tbl_team WHERE t_id = ?";
            if (conf.recordExists(checkTeamQuery, teamId)) {
                break; // Team ID exists
            } else {
                System.out.println("Team ID does not exist. Please try again.");
            }
        }

        // Update player record
        String sql = "UPDATE Player SET f_name = ?, l_name = ?, dob = ?, p = ?, t_id = ? WHERE p_id = ?";
        conf.updateRecord(sql, firstName, lastName, dateOfBirth, position, teamId, playerId);

        System.out.println("Player successfully updated!");
    }

    private void deletePlayer() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        // Validate Player ID
        String playerId;
        while (true) {
            System.out.print("Enter the Player ID to delete: ");
            playerId = sc.next();
            String checkPlayerQuery = "SELECT 1 FROM Player WHERE p_id = ?";
            if (conf.recordExists(checkPlayerQuery, playerId)) {
                break; // Player ID exists
            } else {
                System.out.println("Player ID does not exist. Please try again.");
            }
        }

        // Delete player record
        String sql = "DELETE FROM Player WHERE p_id = ?";
        conf.deleteRecord(sql, playerId);

        System.out.println("Player successfully deleted!");
    }

    public static void main(String[] args) {
        AddPlayer app = new AddPlayer();
        app.addPlayerMenu();
    }
}
