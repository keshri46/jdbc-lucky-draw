package org.example.database;

import org.example.user.Player;
import java.sql.*;
import java.util.ArrayList;

//This DataBase class is a singleton class for performing all the jdbc related operations
public class DataBase {
    static DataBase dataBase;
    private final Connection connection;

    private DataBase() throws SQLException {
        String path = "jdbc:mysql://localhost:3306/LUCKY";
        connection = DriverManager.getConnection(path, "root", "root");
    }

    public static synchronized DataBase getInstance() throws SQLException {
        if (dataBase == null) {
            dataBase = new DataBase();
        }
        return dataBase;
    }

    public static void createDatabase() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "root");
        String createDatabaseQuery = "CREATE DATABASE LUCKY";
        CallableStatement callableStatement = connection.prepareCall(createDatabaseQuery);
        boolean result = callableStatement.execute();
        System.out.println("Database created : not " + result);
    }

    public void createTables() throws SQLException {

        /* todo create 4 table
            1. User
            2. System Numbers
            3. User Numbers
            4. TRANSACTION
         */

        String createUserTableQuery = "CREATE TABLE USER ( NAME VARCHAR(20) NOT NULL, CONTACT BIGINT PRIMARY KEY, DEPOSIT DOUBLE, EARNED DOUBLE )";
        String createSystemNumberTableQuery = "CREATE TABLE SYSTEM_NUMBERS ( CONTACT BIGINT , NUM1 INTEGER, NUM2 INTEGER, NUM3 INTEGER, NUM4 INTEGER, NUM5 INTEGER, FOREIGN KEY (CONTACT) REFERENCES USER(CONTACT) )";
        String createUserNumberTableQuery = "CREATE TABLE USER_NUMBERS ( CONTACT BIGINT , NUM1 INTEGER, NUM2 INTEGER, NUM3 INTEGER, NUM4 INTEGER, NUM5 INTEGER, FOREIGN KEY (CONTACT) REFERENCES USER(CONTACT) )";
        String createTransactionTableQuery = "CREATE TABLE TRANSACTION ( CONTACT BIGINT , DEPOSIT DOUBLE, EACH_ROUND DOUBLE, EARNED DOUBLE, FOREIGN KEY (CONTACT) REFERENCES USER(CONTACT) )";

        CallableStatement callableStatement = connection.prepareCall(createUserTableQuery);
        boolean result = callableStatement.execute();
        System.out.println("USER Table created : not " + result);

        callableStatement = connection.prepareCall(createSystemNumberTableQuery);
        result = callableStatement.execute();
        System.out.println("SYSTEM_NUMBERS Table created : not " + result);

        callableStatement = connection.prepareCall(createUserNumberTableQuery);
        result = callableStatement.execute();
        System.out.println("USER_NUMBERS Table created : not " + result);

        callableStatement = connection.prepareCall(createTransactionTableQuery);
        result = callableStatement.execute();
        System.out.println("TRANSACTION Table created : not " + result);

    }

    public int insertData(Player u) throws SQLException {

        // NAME VARCHAR(20) NOT NULL, CONTACT BIGINT PRIMARY KEY, DEPOSIT INT, EARNED INT
        String insertUserQuery= "INSERT INTO USER VALUES ('"+u.getName()+"', "+u.getContactNo()+", "+u.getDepositAmount()+", "+u.getEarnedAmount()+")";
        CallableStatement callableStatement = connection.prepareCall(insertUserQuery);
        int result = callableStatement.executeUpdate();

        insertUserEnteredNumber(u.getContactNo(),u.getUserEnteredNumbers());
        insertSystemGeneratedNumber(u.getContactNo(),u.getSystemGeneratedNumbers());
        insertTransaction(u.getContactNo(),u.getDepositAmount(),u.getEarnedAmount(),u.getEachTurnAmount());

        return result;

    }

    public int insertUserEnteredNumber(long contact, int[] num) throws SQLException {
        // CONTACT BIGINT , NUM1 INTEGER, NUM2 INTEGER, NUM3 INTEGER, NUM4 INTEGER, NUM5 INTEGER, FOREIGN KEY (CONTACT) REFERENCES USER(CONTACT)
        String insertUserNumberQuery = "INSERT INTO USER_NUMBERS VALUES ("+contact+", "+num[0]+", "+num[1]+", "+num[2]+", "+num[3]+", "+num[4]+")";
        CallableStatement callableStatement = connection.prepareCall(insertUserNumberQuery);
        return callableStatement.executeUpdate();
    }

    public int insertSystemGeneratedNumber(long contact, int[] num) throws SQLException {
        // CONTACT BIGINT , NUM1 INTEGER, NUM2 INTEGER, NUM3 INTEGER, NUM4 INTEGER, NUM5 INTEGER, FOREIGN KEY (CONTACT) REFERENCES USER(CONTACT)
        String insertSystemNumberQuery = "INSERT INTO SYSTEM_NUMBERS VALUES ("+contact+", "+num[0]+", "+num[1]+", "+num[2]+", "+num[3]+", "+num[4]+")";
        CallableStatement callableStatement = connection.prepareCall(insertSystemNumberQuery);
        return callableStatement.executeUpdate();
    }

    public int insertTransaction(long contact, double deposit, double earned, double eachTurn) throws SQLException {
        // CONTACT BIGINT , DEPOSIT INTEGER, EACH_ROUND INTEGER, EARNED INTEGER, FOREIGN KEY (CONTACT) REFERENCES USER(CONTACT)
        String insertTransactionQuery= "INSERT INTO TRANSACTION VALUES ("+contact+", "+deposit+", "+eachTurn+", "+earned+")";

        CallableStatement callableStatement = connection.prepareCall(insertTransactionQuery);
        return callableStatement.executeUpdate();
    }

    public ArrayList<Player> fetchAllPlayers() throws SQLException {

        ArrayList<Player> players = new ArrayList<>();
        String selectAllPlayerQuery = "SELECT * FROM USER";

        CallableStatement callableStatement = connection.prepareCall(selectAllPlayerQuery);
        ResultSet result = callableStatement.executeQuery();

        while(result.next()){
            long contact = result.getLong("contact");
            Player player = new Player(result.getString("NAME"),contact,result.getInt("DEPOSIT"),result.getInt("EARNED"));
            player.setSystemGeneratedNumbersFromDatabase(fetchSystemGeneratedNumber(contact));
            player.setUserEnteredNumbersFromDatabase(fetchUserEnteredNumber(contact));
            players.add(player);
        }

        return players;
    }

    public ArrayList<Integer[]> fetchUserEnteredNumber(long contact) throws SQLException {
        ArrayList<Integer[]> userEnteredNumber = new ArrayList<>();
        String userEnteredNumberQuery = "SELECT * FROM USER_NUMBERS WHERE CONTACT = "+contact;
        CallableStatement callableStatement = connection.prepareCall(userEnteredNumberQuery);
        ResultSet result = callableStatement.executeQuery();

        while (result.next()){
            Integer[] num = new Integer[5];
            for (int i = 0; i < 5; i++) {
                String column = "NUM"+(i+1);
                num[i]=result.getInt(column);
            }
//            System.out.println(Arrays.toString(num));
            userEnteredNumber.add(num);
        }
        return userEnteredNumber;
    }

    public ArrayList<Integer[]> fetchSystemGeneratedNumber(long contact) throws SQLException {
        ArrayList<Integer[]> systemGeneratedNumber = new ArrayList<>();
        String userEnteredNumberQuery = "SELECT * FROM SYSTEM_NUMBERS WHERE CONTACT = "+contact;
        CallableStatement callableStatement = connection.prepareCall(userEnteredNumberQuery);
        ResultSet result = callableStatement.executeQuery();

        while (result.next()){
            Integer[] num = new Integer[5];
            for (int i = 0; i < 5; i++) {
                String column = "NUM"+(i+1);
                num[i]=result.getInt(column);
            }
//            System.out.println(Arrays.toString(num));
            systemGeneratedNumber.add(num);
        }
        return systemGeneratedNumber;
    }

    public double fetchTotalEarnedAmount(long contact) throws SQLException {

        String totalEarnedAmountQuery = "SELECT SUM(EARNED) AS TOTAL_EARNED FROM TRANSACTION WHERE CONTACT = "+contact;
        CallableStatement callableStatement = connection.prepareCall(totalEarnedAmountQuery);
        ResultSet result = callableStatement.executeQuery();
        double totalEarned = 0;

        while (result.next()){
            totalEarned = result.getDouble("TOTAL_EARNED");
        }
        return totalEarned;
    }

    public double fetchTotalDepositAmount(long contact) throws SQLException {

        String totalDepositAmountQuery = "SELECT SUM(DEPOSIT) AS TOTAL_DEPOSIT FROM TRANSACTION WHERE CONTACT = "+contact;
        CallableStatement callableStatement = connection.prepareCall(totalDepositAmountQuery);
        ResultSet result = callableStatement.executeQuery();
        double totalDeposit = 0;

        while (result.next()){
            totalDeposit = result.getDouble("TOTAL_DEPOSIT");
        }
        return totalDeposit;
    }

    public int updateDepositAndEarned(long contact, double deposit, double earned) throws SQLException {
        double totalEarned = fetchTotalEarnedAmount(contact)+earned;
        double totalDeposit = fetchTotalDepositAmount(contact)+deposit;

        String updateDepositAndEarnedQuery = "UPDATE USER SET DEPOSIT ="+totalDeposit+", EARNED = "+totalEarned+" WHERE CONTACT="+contact;
        CallableStatement callableStatement = connection.prepareCall(updateDepositAndEarnedQuery);
        return callableStatement.executeUpdate();
    }

    public int updateName(long contact,String name) throws SQLException {
        String updateDepositAndEarnedQuery = "UPDATE USER SET NAME ='"+name+"' WHERE CONTACT="+contact;
        CallableStatement callableStatement = connection.prepareCall(updateDepositAndEarnedQuery);
        return callableStatement.executeUpdate();
    }


}
