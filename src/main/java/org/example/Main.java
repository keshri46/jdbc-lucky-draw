package org.example;

import org.example.database.DataBase;
import org.example.logic.Logics;

import java.sql.SQLException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws SQLException {
        startLuckyDraw();
//        System.out.println(DataBase.getInstance().updateDepositAndEarned(1,0,0));
    }

    private static void startLuckyDraw() throws SQLException {
        Scanner sc = new Scanner(System.in);
        boolean ex = false;
        Logics l = new Logics(sc);
        while (!ex) {
            System.out.println("\n\n--------------------PLAY AGAIN----------------------");
            System.out.println("\n1. Play Game\n2. See User Details\n3. See all Users' Details\n4. Exit");
            int choice = sc.nextInt();
            switch (choice) {
                case 1: {
                    l.start();
                    break;
                }
                case 2: {
                    l.searchUserDetails();
                    break;
                }
                case 3: {
                    l.searchAllUserDetails();
                    break;
                }
                case 4: {
                    System.out.println("Thank you");
                    ex = true;
                    break;
                }
                default: {
                    System.out.println("Invalid Input. Try Again !");
                }
            }

        }

    }
}