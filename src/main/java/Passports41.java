import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Passports41 {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner s = new Scanner(new File("/home/osteenbu/otto/adventOfCode/src/main/resources/4passports.txt"));
        ArrayList<Passport> passports = new ArrayList<>();
        Passport passportBuilder = new Passport();
        while (s.hasNext()) {
            final String nextLine = s.nextLine();
            if (nextLine.length() == 0) {
                passports.add(passportBuilder);
                passportBuilder = new Passport();
            } else {
                passportBuilder.addLine(nextLine);
            }
        }
        passports.add(passportBuilder);

        int validPassports = 0;
        for (Passport passport : passports) {
            if (passport.isValid()) {
                validPassports++;
            }
        }
        System.out.println(validPassports);

    }


    public enum FieldType {
        BIRTHYEAR("byr", true, s -> {
            int birthYear = Integer.parseInt(s);
            return birthYear >= 1920 && birthYear <= 2002;
        }),
        ISSUEYEAR("iyr", true, s -> {
            int issueYear = Integer.parseInt(s);
            return (issueYear >= 2010 && issueYear <= 2020);
        }),
        EXPIRATIONYEAR("eyr", true, s -> {
            int expirationYear = Integer.parseInt(s);
            return expirationYear >= 2020 && expirationYear <= 2030;
        }),
        HEIGHT("hgt", true,s -> {
            if(!s.trim().matches("\\d{2,3}((cm)|(in))"))
                return false;
            String unit = s.trim().substring(s.length()-2, s.length()).toLowerCase();
            int height = Integer.parseInt(s.trim().substring(0, s.length()-2));
            return (unit.equals("cm") && height >= 150 && height <= 193 ) || (unit.equals("in") && height >= 59 && height <= 76 );
        }),
        HAIRCOLOR("hcl", true, s -> s.matches("#[\\da-f]{6}")),
        EYECOLOR("ecl", true, s -> Set.of("amb","blu","brn","gry","grn","hzl","oth").contains(s)),
        PASSPORTID("pid", true, s -> s.matches("[\\d]{9}")),
        COUNTRYID("cid", false, s -> true);

        private final String key;
        private final boolean mandatory;
        private final Function<String, Boolean> validator;

        FieldType(String key, boolean mandatory, Function<String, Boolean> validator) {
            this.key = key;
            this.mandatory = mandatory;
            this.validator = validator;
        }

        public static FieldType byKey(String key) {
            for (FieldType e : values()) {
                if (e.key.equals(key)) {
                    return e;
                }
            }
            throw new RuntimeException("Unknown Field in Passport, call security");
        }

    }

    private static class Passport {
        public static final String FIELD_DELIMETER = " ";
        public static final String KEY_VALUE_DELIMETER = ":";
        Map<FieldType, String> fields = new HashMap<>();


        public void addLine(String line) {
            Arrays.stream(line.trim().split(FIELD_DELIMETER)).forEach(
                    field -> {
                        final String[] split = field.split(KEY_VALUE_DELIMETER);
                        final FieldType fieldType = FieldType.byKey(split[0]);
                        if (fields.containsKey(fieldType)) {
                            throw new RuntimeException("Double entry in passport");
                        }
                        fields.put(fieldType, split[1]);
                    }
            );

        }

        public boolean isValid() {
            final int numberOfFields = fields.size();
            for(Map.Entry<FieldType, String> entry:fields.entrySet()){
                final Boolean isValidField = entry.getKey().validator.apply(entry.getValue());
                if(!isValidField){
                    System.out.println("Field is not valid: " + entry.toString());
                    return false;
                }
            }

            if (numberOfFields == 8) {
                //Passport has all required fields
                return true;
            } else if (numberOfFields == 7 && !fields.containsKey(FieldType.COUNTRYID)) {
                //exception for North Poles Credentials (Passports withoud CID)
                return true;
            } else {
                //passport is invalid
                return false;
            }
        }
    }
}
