package com.sg.pcrf.ossi;

import com.sg.pcrf.exceptions.OssiException;
import com.sg.pcrf.util.Constants;

import java.io.*;

/**
 * Created by jyx on 2017/8/11.
 */
public class Utils {
    public static String readFile(String fileName) throws OssiException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
//                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            throw new OssiException("error in readFile");
        } catch (IOException e) {
            throw new OssiException("error in readFile");
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static void writeFile(String text ,String fileName) {
        try {
            PrintWriter out = new PrintWriter(fileName, Constants.charset_utf_8);
            out.println(text);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
