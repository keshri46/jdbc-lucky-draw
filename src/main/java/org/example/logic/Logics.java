package org.example.logic;

import org.example.database.DataBase;
import org.example.user.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Logics {

    String name;
    Long contactNo;
    int[] systemGeneratedNumbers = new int[5];
    int[] userEnteredNumbers = new int[5];
    double depositAmount, earnedAmount, eachTurnAmount;
    Scanner sc;


    public Logics(Scanner sc) {
        this.sc = sc;
    }

    public void start() throws SQLException {

        boolean userExist = false;
        System.out.println("----------------------------------------------------");
        System.out.print("Enter your Name \t: ");
        name = sc.next();

        System.out.print("Enter your Contact Number \t: ");
        contactNo = sc.nextLong();

        ArrayList<Player> users = DataBase.getInstance().fetchAllPlayers();
        for (Player user : users) {
            if (user.getContactNo() == contactNo) {
                System.out.println("Contact already associated with the Name : "+user.getName()+"\nDo you want to continue or start fresh ? Yes/No");
                String option = sc.next();
                if(option.equalsIgnoreCase("yes")){
                    userExist = true;
                    System.out.println("Continue with 1.New name or 2. Last Name");
                    if(sc.nextInt()==1)
                        DataBase.getInstance().updateName(contactNo,name);
                }
                else {
                    return;
                }
            }
        }

        System.out.print("Enter your Deposit Amount \t: ");
        depositAmount = sc.nextDouble();

        eachTurnAmount = depositAmount / 5;

        generateRandoms();
        inputNumbersFromUsers();
        insertDataInDatabase(userExist);
    }


    private void insertDataInDatabase(boolean userExist) throws SQLException {
        DataBase db = DataBase.getInstance();
        Player player = new Player(name, contactNo, systemGeneratedNumbers, userEnteredNumbers, depositAmount, earnedAmount, eachTurnAmount);
        if(!userExist)
            db.insertData(player);
        else {
            db.insertSystemGeneratedNumber(player.getContactNo(),player.getSystemGeneratedNumbers());
            db.insertUserEnteredNumber(player.getContactNo(), player.getUserEnteredNumbers());
            db.insertTransaction(player.getContactNo(),player.getDepositAmount(),player.getEarnedAmount(),player.getEachTurnAmount());
            db.updateDepositAndEarned(player.getContactNo(),player.getDepositAmount(), player.getEarnedAmount());
        }
    }

    private void inputNumbersFromUsers() {
        for (int i = 0; i < 5; i++) {
            System.out.print("Enter number  \t " + (i + 1) + " : ");
            userEnteredNumbers[i] = sc.nextInt();
            if (userEnteredNumbers[i] == systemGeneratedNumbers[i]) {
                System.out.println("Congrats ! you won -> " + eachTurnAmount * 2);
                earnedAmount += 2 * eachTurnAmount;
            } else {
                System.out.println("Oh No ! you lost -> " + eachTurnAmount);
            }
        }
        System.out.println("Total Amount Earned : " + earnedAmount);
        System.out.println("----------------------------------------------------");
    }

    private void generateRandoms() {
        Random r = new Random();
        for (int i = 0; i < 5; i++) {
            systemGeneratedNumbers[i] = r.nextInt(10);
        }

    }

    public void searchUserDetails() throws SQLException {
        System.out.println("----------------------------------------------------");
        System.out.println("Enter User Contact Number: ");
        long number = sc.nextLong();
        ArrayList<Player> users = DataBase.getInstance().fetchAllPlayers();
        for (Player user : users) {
            if (user.getContactNo() == number) {
                System.out.println(user);
                user.displayNumbers();
            }
        }
        System.out.println("----------------------------------------------------");
    }

    public void searchAllUserDetails() throws SQLException {
        System.out.println("----------------------------------------------------");
        int count = 1;
        ArrayList<Player> players= DataBase.getInstance().fetchAllPlayers();
        for (Player user : players) {
            System.out.println(count + ". " + user);
            user.displayNumbers();
            count++;
        }
        System.out.println("----------------------------------------------------");
    }

    public void printNumbers(long contactNo){

    }
}

