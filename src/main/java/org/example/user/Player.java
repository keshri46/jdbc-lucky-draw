package org.example.user;

import java.util.ArrayList;
import java.util.Arrays;

public class Player {
    private String name;
    private Long contactNo;
    private int[] systemGeneratedNumbers = new int[5];
    private ArrayList<Integer[]> systemGeneratedNumbersFromDatabase = new ArrayList<>();
    private int[] userEnteredNumbers = new int[5];
    private ArrayList<Integer[]> userEnteredNumbersFromDatabase = new ArrayList<>();
    private double depositAmount, earnedAmount, eachTurnAmount;

    public Player(String name, Long contactNo, int[] systemGeneratedNumbers, int[] userEnteredNumbers, double depositAmount, double earnedAmount, double eachTurnAmount) {
        this.name = name;
        this.contactNo = contactNo;
        this.systemGeneratedNumbers = systemGeneratedNumbers;
        this.userEnteredNumbers = userEnteredNumbers;
        this.depositAmount = depositAmount;
        this.earnedAmount = earnedAmount;
        this.eachTurnAmount = eachTurnAmount;
    }

    public Player(String name, Long contactNo, double depositAmount, double earnedAmount) {
        this.name = name;
        this.contactNo = contactNo;
        this.depositAmount = depositAmount;
        this.earnedAmount = earnedAmount;
        this.eachTurnAmount = depositAmount/5;
    }

    @Override
    public String toString() {
        return "PLAYER -->  " +
                "Name = " + name +
                ", ContactNo = " + contactNo +
                ", Deposit Amount = " + depositAmount +
                ", Earned Amount = " + earnedAmount ;
    }

    public String getName() {
        return name;
    }

    public int[] getSystemGeneratedNumbers() {
        return systemGeneratedNumbers;
    }

    public int[] getUserEnteredNumbers() {
        return userEnteredNumbers;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public double getEarnedAmount() {
        return earnedAmount;
    }

    public double getEachTurnAmount() {
        return eachTurnAmount;
    }

    public void setSystemGeneratedNumbersFromDatabase(ArrayList<Integer[]> systemGeneratedNumbersFromDatabase) {
        this.systemGeneratedNumbersFromDatabase = systemGeneratedNumbersFromDatabase;
    }

    public void setUserEnteredNumbersFromDatabase(ArrayList<Integer[]> userEnteredNumbersFromDatabase) {
        this.userEnteredNumbersFromDatabase = userEnteredNumbersFromDatabase;
    }

    public ArrayList<Integer[]> getSystemGeneratedNumbersFromDatabase() {
        return systemGeneratedNumbersFromDatabase;
    }

    public ArrayList<Integer[]> getUserEnteredNumbersFromDatabase() {
        return userEnteredNumbersFromDatabase;
    }

    public Long getContactNo() {
        return contactNo;
    }

    public void displayNumbers(){

        for (int i = 0;i<userEnteredNumbersFromDatabase.size();i++){
            Integer[] user = userEnteredNumbersFromDatabase.get(i);
            Integer[] system = systemGeneratedNumbersFromDatabase.get(i);
            System.out.print("System Generated \t");
            for (int j = 0; j < 5; j++) {
                System.out.print(system[j]+" ");
            }
            System.out.println();
            System.out.print("User Entered     \t");
            for (int j = 0; j < 5; j++) {
                System.out.print(user[j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

}

