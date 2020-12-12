import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Passwords21 {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner s = new Scanner(new File("/home/osteenbu/otto/adventOfCode/src/main/resources/2passwords.txt")).useDelimiter("\\n");
        ArrayList<String> lines = new ArrayList<>();
        while (s.hasNext()) {
            lines.add(s.next());
        }
        s.close();
        int legalPasswords = 0;
        for (String line : lines) {
            final String[] parts = line.split(":");
            Policy policy = new Policy(parts[0]);
            Password password = new Password(parts[1]);
            if(policy.matches(password)){
                legalPasswords++;
            }

        }
        System.out.println(legalPasswords);
    }

    private static class Policy {
        final String policedChar;
        final int min;
        final int max;

        Policy(String policy) {
            final String[] split = policy.split(" ");
            policedChar = split[1];
            min = Integer.valueOf(split[0].split("-")[0]);
            max = Integer.valueOf(split[0].split("-")[1]);
        }

        boolean matches(Password password) {
            final int occurences = StringUtils.countMatches(password.password, policedChar);
            if (occurences <= max && occurences >= min)
                return true;
            else
                return false;
        }

    }

    private static class Password {
        final String password;

        Password(String password) {
            this.password = password;
        }
    }

}
