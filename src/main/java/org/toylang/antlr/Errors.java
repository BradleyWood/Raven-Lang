package org.toylang.antlr;

import java.util.ArrayList;

public class Errors {

    private static final ArrayList<String> ERRORS = new ArrayList<String>();

    public static void put(String error) {
        ERRORS.add(error);
    }
    public static int getErrorCount() {
        return ERRORS.size();
    }
    public static void printErrors() {
        if(getErrorCount() > 0) {
            System.out.println("--------------ERRORS--------------");
            for (String error : ERRORS) {
                System.err.println("Error: "+error);
            }
            System.out.println("--------------ERRORS--------------");
        }
    }
}
