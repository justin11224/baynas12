package Baynas;

import java.util.Scanner;

public class AddTeam {

    public void addTeamMenu() {
        String response;
        AddTeam league = new AddTeam();

        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("Choose an action:");
            System.out.println("1. Add Teams");
            System.out.println("2. View Teams");
            System.out.println("3. Edit Teams");
            System.out.println("4. Delete Teams");
            System.out.println("5. EXIT");

            System.out.print("Enter action number: ");
            int action = sc.nextInt();

            switch (action) {
                case 1:
                    league.addTeam();
                    break;
                case 2:
                    league.viewTeams();
                    break;
                case 3:
                    league.viewTeams();
                    league.updateTeam();
                    league.viewTeams();
                    break;
                case 4:
                    league.viewTeams();
                    league.deleteTeam();
                    league.viewTeams();
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

    public void addTeam() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Team Name: ");
        String tname = sc.nextLine();
        System.out.print("City/Location/Purok: ");
        String tloc = sc.nextLine();
        System.out.print("Coach Name: ");
        String cname = sc.nextLine();
        System.out.print("Contact Information (Email or phone number): ");
        String con = sc.nextLine();

        String sql = "INSERT INTO tbl_team (team_name, t_location, t_coach, t_contact) VALUES (?, ?, ?, ?)";
        config conf = new config();
        conf.addRecord(sql, tname, tloc, cname, con);
    }

    private void viewTeams() {
    config conf = new config();
    String query = "SELECT * FROM tbl_team";
    String[] headers = {"Team ID", "Team Name", "Team Location", "Team Coach", "Team Contact"};
    String[] columns = {"t_id", "team_name", "t_location", "t_coach", "t_contact"};
        conf.viewRecords(query, headers, columns);
    
}


    private void updateTeam() {
        Scanner sc = new Scanner(System.in);
config conf = new config();
        String playerId;
        while (true) {
            System.out.print("Enter the Team ID to update: ");
            playerId = sc.next();
            sc.nextLine(); // Consume newline
            String checkPlayerQuery = "SELECT 1 FROM tbl_team WHERE t_id = ?";
            if (conf.recordExists(checkPlayerQuery, playerId)) {
                break; // Player ID exists
            } else {
                System.out.println("Player ID does not exist. Please try again.");
            }
        }

        System.out.print("New Team Name: ");
        String tname = sc.nextLine();
        System.out.print("New Location: ");
        String tloc = sc.nextLine();
        System.out.print("New Coach Name: ");
        String cname = sc.nextLine();
        System.out.print("New Contact Information: ");
        String con = sc.nextLine();

        String qry = "UPDATE tbl_team SET team_name = ?, t_location = ?, t_coach = ?, t_contact = ? WHERE t_id = ?";
        
        conf.updateRecord(qry, tname, tloc, cname, con, playerId);
    }

    private void deleteTeam() {
        Scanner sc = new Scanner(System.in);

        config conf = new config();
        String playerId;
        while (true) {
            System.out.print("Enter the Team ID to update: ");
            playerId = sc.next();
            sc.nextLine(); // Consume newline
            String checkPlayerQuery = "SELECT 1 FROM tbl_team WHERE t_id = ?";
            if (conf.recordExists(checkPlayerQuery, playerId)) {
                break; // Player ID exists
            } else {
                System.out.println("Player ID does not exist. Please try again.");
            }
        String qry = "DELETE FROM tbl_team WHERE t_id = ?";
        
        conf.deleteRecord(qry, playerId);
    }

    }
    public static void main(String[] args) {
        AddTeam app = new AddTeam();
        app.addTeamMenu();
    }
}
