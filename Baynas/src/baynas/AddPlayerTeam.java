package Baynas;

import java.util.Scanner;

public class AddPlayerTeam {

    public void addplayerteam() {
        String response;
        Scanner sc = new Scanner(System.in);

        do {
            String action;

            do {
                System.out.println("1. ADD PlayerTeam");
                System.out.println("2. VIEW PlayerTeam");
                System.out.println("3. UPDATE PlayerTeam");
                System.out.println("4. DELETE PlayerTeam");
                System.out.println("5. EXIT");
                boolean validInput = false;

                do {
                    System.out.print("Enter action Number: ");
                    action = sc.nextLine().toUpperCase();

                    if (action.equals("1") || action.equals("2") || action.equals("3") || action.equals("4") || action.equals("5")) {
                        validInput = true;
                    } else {
                        System.out.println("Invalid input! Try Again!!");
                    }
                } while (!validInput);

                if (action.equals("1")) {
                    addPlayerTeam();
                    break;
                } else if (action.equals("2")) {
                    viewPlayerTeam();
                    break;
                } else if (action.equals("3")) {
                    updatePlayerTeam();
                    break;
                } else if (action.equals("4")) {
                    deletePlayerTeam();
                    break;
                } else if (action.equals("5")) {
                    System.out.println(" Exiting...");
                    return;
                }

            } while (true);

            System.out.print(" Do you want to continue? (yes or no): ");
            response = sc.next();

        } while (response.equalsIgnoreCase("yes"));

        System.out.println(" Thank you, see you!");
    }

    public void addPlayerTeam() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        String playerId, teamId;

        String query = "SELECT * FROM Player";
        String[] headers = {"Player ID", "First Name", "Last Name", "Date Of Birth", "Position", "Team ID"};
        String[] columns = {"p_id", "f_name", "l_name", "dob", "p", "t_id"};

        conf.viewRecords(query, headers, columns);
        while (true) {
            System.out.print("Enter PlayerID: ");
            playerId = sc.next();
            String checkPlayerQuery = "SELECT 1 FROM Player WHERE p_id = ?";
            if (conf.recordExists(checkPlayerQuery, playerId)) {
                break;
            } else {
                System.out.println("PlayerID does not exist. Please try again.");
            }
        }

        // Display teams
        String query1 = "SELECT * FROM tbl_team";
        String[] headers1 = {"Team ID", "Team Name", "Team Location", "Team Coach", "Team Contact"};
        String[] columns1 = {"t_id", "team_name", "t_location", "t_coach", "t_contact"};
        conf.viewRecords(query1, headers1, columns1);

        // Team ID Validation
        while (true) {
            System.out.print("Enter TeamID: ");
            teamId = sc.next();
            String checkTeamQuery = "SELECT 1 FROM tbl_team WHERE t_id = ?";
            if (conf.recordExists(checkTeamQuery, teamId)) {
                break;
            } else {
                System.out.println("TeamID does not exist. Please try again.");
            }
        }

        // Collect additional data
        System.out.print("Enter JoinDate (YYYY-MM-DD): ");
        String joinDate = sc.next();
        System.out.print("Enter Role: ");
        String role = sc.next();
        System.out.print("Enter Status(Approved/declined)Only!: ");
        String status = sc.next();

        // Insert the new Player-Team relation
        String sql = "INSERT INTO AddPlayerTeam (p_id, t_id, jd, r, s) VALUES (?,?,?,?,?)";
        conf.addRecord(sql, playerId, teamId, joinDate, role, status);

        System.out.println("Player-Team association successfully added!");
    }

    public void viewPlayerTeam() {
        Scanner sc = new Scanner(System.in);
        config conf = new config();

        while (true) {
    System.out.println("\nChoose an action:");
    System.out.println("1. View all player-team assignments");
    System.out.println("2. View players of a specific team");
    System.out.println("3. View all Approved players");
    System.out.println("4. View all declined players");
    System.out.println("5. Exit");

    String action = sc.nextLine().toUpperCase();

    switch (action) {
        case "1":
            String query1 = "SELECT apt.pt_id, Player.f_name, Player.l_name, Player.p, t.team_name, apt.jd, apt.r, apt.s " +
                            "FROM AddPlayerTeam apt " +
                            "INNER JOIN Player ON apt.p_id = Player.p_id " +
                            "INNER JOIN tbl_team t ON apt.t_id = t.t_id";

            String[] headers1 = {"PlayerTeamID", "First Name", "Last Name", "Position", "Team Name", "Join Date", "Role", "Status"};
            String[] columns1 = {"pt_id", "f_name", "l_name", "p", "team_name", "jd", "r", "s"};
            conf.viewRecords(query1, headers1, columns1);
            break;

        case "2":
            // Display all teams
            String query = "SELECT * FROM tbl_team";
            String[] headers = {"Team ID", "Team Name", "Team Location", "Team Coach", "Team Contact"};
            String[] columns = {"t_id", "team_name", "t_location", "t_coach", "t_contact"};
            conf.viewRecords(query, headers, columns);

            String teamId;
            while (true) {
                System.out.print("Enter the Team ID to view players: ");
                teamId = sc.next();
                sc.nextLine(); // Consume newline

                // Validate if the Team ID exists in the database
                String checkTeamQuery = "SELECT 1 FROM tbl_team WHERE t_id = ?";
                if (conf.recordExists(checkTeamQuery, teamId)) {
                    break; // Team ID exists
                } else {
                    System.out.println("Team ID does not exist. Please try again.");
                }
            }

            // Query to display players in the selected team
            String query3 = "SELECT t.team_name, p.f_name, p.l_name, p.p " +
                            "FROM tbl_team t " +
                            "INNER JOIN AddPlayerTeam apt ON t.t_id = apt.t_id " +
                            "INNER JOIN Player p ON apt.p_id = p.p_id " +
                            "WHERE t.t_id = ? " + // Use Team ID as the filter
                            "ORDER BY p.l_name ASC";

            String[] headers3 = {"Team Name", "First Name", "Last Name", "Position"};
            String[] columns3 = {"team_name", "f_name", "l_name", "p"};

            // Display the players of the selected team
            conf.viewRecordsWithParam(query3, headers3, columns3, teamId);
            break;

        case "3":
            // Query to show all approved players
            String query4 = "SELECT apt.pt_id, p.f_name, p.l_name, p.p, t.team_name, apt.jd, apt.r " +
                            "FROM AddPlayerTeam apt " +
                            "INNER JOIN Player p ON apt.p_id = p.p_id " +
                            "INNER JOIN tbl_team t ON apt.t_id = t.t_id " +
                            "WHERE apt.s = 'Approved' " +
                            "ORDER BY p.l_name ASC";

            String[] headers4 = {"PlayerTeamID", "First Name", "Last Name", "Position", "Team Name", "Join Date", "Role"};
            String[] columns4 = {"pt_id", "f_name", "l_name", "p", "team_name", "jd", "r"};

            conf.viewRecords(query4, headers4, columns4);
            break;

       case "4":
    // Query to show all declined players
    String query5 = "SELECT apt.pt_id, p.f_name, p.l_name, p.p, t.team_name, apt.jd, apt.r " +
                    "FROM AddPlayerTeam apt " +
                    "INNER JOIN Player p ON apt.p_id = p.p_id " +
                    "INNER JOIN tbl_team t ON apt.t_id = t.t_id " +
                    "WHERE apt.s = 'declined' " +
                    "ORDER BY p.l_name ASC";

    String[] headers5 = {"PlayerTeamID", "First Name", "Last Name", "Position", "Team Name", "Join Date", "Role"};
    String[] columns5 = {"pt_id", "f_name", "l_name", "p", "team_name", "jd", "r"};

    // Display all declined players
    conf.viewRecords(query5, headers5, columns5);
    break;

        case "5":
            System.out.println("Exiting...");
            return;

        default:
            System.out.println("Invalid choice. Please try again.");
    }
}


    }

    public void updatePlayerTeam() {
        config conf = new config();
        Scanner sc = new Scanner(System.in);
        String query1 = "SELECT apt.pt_id, Player.f_name, Player.l_name, Player.p, t.team_name, apt.jd, apt.r, apt.s " +
                            "FROM AddPlayerTeam apt " +
                            "INNER JOIN Player ON apt.p_id = Player.p_id " +
                            "INNER JOIN tbl_team t ON apt.t_id = t.t_id";

            String[] headers1 = {"PlayerTeamID", "First Name", "Last Name", "Position", "Team Name", "Join Date", "Role", "Status"};
            String[] columns1 = {"pt_id", "f_name", "l_name", "p", "team_name", "jd", "r", "s"};
            conf.viewRecords(query1, headers1, columns1);
        String playerId;
        while (true) {
            System.out.print("Enter Player ID: ");
            playerId = sc.next();
            String checkPlayerQuery = "SELECT 1 FROM AddPlayerTeam WHERE pt_id = ?";
            if (conf.recordExists(checkPlayerQuery, playerId)) {
                break;
            } else {
                System.out.println("PlayerID does not exist. Please try again.");
            }
        }

        System.out.print("EDIT TeamID: ");
        String newTeamId = sc.next();
        System.out.print("EDIT JoinDate: ");
        String newJoinDate = sc.next();
        System.out.print("EDIT Role: ");
        String newRole = sc.next();
        System.out.print("EDIT Status: ");
        String newStatus = sc.next();

        String sql = "UPDATE AddPlayerTeam SET t_id = ?, jd = ?, r = ?, s = ? WHERE p_id = ?";
        conf.updateRecord(sql, newTeamId, newJoinDate, newRole, newStatus, playerId);

        System.out.println("Player-Team association successfully updated!");
    }

    public void deletePlayerTeam() {
        config conf = new config();
        Scanner sc = new Scanner(System.in);
        String playerId;

        while (true) {
            System.out.print("Enter Player ID to delete: ");
            playerId = sc.next();

            String checkPlayerQuery = "SELECT 1 FROM AddPlayerTeam WHERE p_id = ?";
            if (conf.recordExists(checkPlayerQuery, playerId)) {
                break;
            } else {
                System.out.println("PlayerID does not exist. Please try again.");
            }
        }

        String sql = "DELETE FROM AddPlayerTeam WHERE p_id = ?";
        conf.deleteRecord(sql, playerId);

        System.out.println("Player-Team association successfully deleted!");
    }
}
