package Baynas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class config {

    // Establish connection to SQLite database
    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC"); // Load SQLite JDBC driver
            con = DriverManager.getConnection("jdbc:sqlite:baynas"); // Establish connection
            System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }

    // Check if a record exists
    public boolean recordExists(String query, String param) {
        try (Connection conn = connectDB(); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, param); // Set parameter
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Return true if record exists
            }
        } catch (SQLException e) {
            System.out.println("Error checking record existence: " + e.getMessage());
        }
        return false;
    }

    // Add record with parameterized query
     public void addRecord(String sql, Object... params) {
        try (Connection conn = connectDB(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    
}

    // View records with headers and columns
    public void viewRecords(String sqlQuery, String[] headers, String[] columnNames) {
        if (headers.length != columnNames.length) {
            System.out.println("Error: Mismatch between headers and columns.");
            return;
        }

        try (Connection conn = connectDB(); 
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = pstmt.executeQuery()) {

            printTableHeaders(headers);

            while (rs.next()) {
                printTableRow(rs, columnNames);
            }
            printTableFooter(headers.length);

        } catch (SQLException e) {
            System.out.println("Error retrieving records: " + e.getMessage());
        }
    }

    // View records with parameterized query
    public void viewRecordsWithParam(String query, String[] headers, String[] columns, String param) {
        try (Connection conn = connectDB(); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, param); // Set parameter value
            try (ResultSet rs = pstmt.executeQuery()) {
                printTableHeaders(headers);
                while (rs.next()) {
                    printTableRow(rs, columns);
                }
                printTableFooter(headers.length);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving records with param: " + e.getMessage());
        }
    }

    // Update record
    public void updateRecord(String sql, Object... values) {
        try (Connection conn = connectDB(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setPreparedStatementValues(pstmt, values);
            pstmt.executeUpdate();
            System.out.println("Record updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }

    // Delete record
    public void deleteRecord(String sql, Object... values) {
        try (Connection conn = connectDB(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setPreparedStatementValues(pstmt, values);
            pstmt.executeUpdate();
            System.out.println("Record deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }

    // Helper: Set values for PreparedStatement
    private void setPreparedStatementValues(PreparedStatement pstmt, Object... values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) values[i]);
            } else if (values[i] instanceof Double) {
                pstmt.setDouble(i + 1, (Double) values[i]);
            } else if (values[i] instanceof Float) {
                pstmt.setFloat(i + 1, (Float) values[i]);
            } else if (values[i] instanceof Long) {
                pstmt.setLong(i + 1, (Long) values[i]);
            } else if (values[i] instanceof Boolean) {
                pstmt.setBoolean(i + 1, (Boolean) values[i]);
            } else if (values[i] instanceof java.util.Date) {
                pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime()));
            } else if (values[i] instanceof java.sql.Date) {
                pstmt.setDate(i + 1, (java.sql.Date) values[i]);
            } else if (values[i] instanceof java.sql.Timestamp) {
                pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]);
            } else {
                pstmt.setString(i + 1, values[i].toString());
            }
        }
    }

    // Helper: Print table headers
    private void printTableHeaders(String[] headers) {
        System.out.println("--------------------------------------------------------------------------------");
        for (String header : headers) {
            System.out.printf("%-20s", header);
        }
        System.out.println("\n--------------------------------------------------------------------------------");
    }

    // Helper: Print a table row
    private void printTableRow(ResultSet rs, String[] columns) throws SQLException {
        for (String column : columns) {
            String value = rs.getString(column);
            System.out.printf("%-20s", value != null ? value : "");
        }
        System.out.println();
    }

    // Helper: Print table footer
    private void printTableFooter(int numColumns) {
        System.out.println("--------------------------------------------------------------------------------");
    }
}
