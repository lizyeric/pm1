package com.sg.pcrf.ossi;

import com.sg.pcrf.util.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunCommand {
    public static String runCommand(String cmd) throws Exception {
        StringBuilder cmdReturn = new StringBuilder();
        Process p = null;
        String s = null;
        BufferedReader stdInput = null;
        BufferedReader stdError = null;
        try {
            p = Runtime.getRuntime().exec(new String[]{"bash", "-c", cmd});

            stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream(), Constants.charset_utf_8));

            stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream(), Constants.charset_utf_8));

            while ((s = stdInput.readLine()) != null) {
//                read output from the command
//                System.out.println(s);
                cmdReturn.append(s).append(System.lineSeparator());
            }

            while ((s = stdError.readLine()) != null) {
//                System.out.println("---error---");
                System.out.println(s);
            }

        } catch (Exception e) {
            throw new Exception("error in runCommand");
        } finally {
            if(stdInput != null) stdInput.close();
            if(stdError != null) stdError.close();
            if(p != null) p.destroyForcibly();
        }
        return cmdReturn.toString();
    }

}