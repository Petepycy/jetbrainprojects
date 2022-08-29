import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;

class Main {
    public static String action = "enc";
    public static String input = "";
    public static int number = 0;
    public static String inputTxt = "";
    public static String outputTxt = "";
    public static String algorithm = "shift";
    public static boolean isIn = false;
    public static boolean isOut = false;
    public static boolean isData = false;
    public static ArrayList<Character> msg = new ArrayList<>();

    public static void unicodeDec(ArrayList<Character> msg, int number) {
        int charNumber;
        for (int i = 0; i < msg.size(); i++) {
            charNumber = msg.get(i) - number;
            msg.set(i, (char) charNumber);
        }
    }
    public static void unicodeEnc(ArrayList<Character> msg, int number) {
        int charNumber;
        for (int i = 0; i < msg.size(); i++) {
            charNumber = msg.get(i) + number;
            msg.set(i, (char) charNumber);
        }
    }

    public static void codingDec(String algorithm) {
        if (algorithm.equalsIgnoreCase("unicode")) {
            unicodeDec(msg,number);
        } else if (algorithm.equalsIgnoreCase("shift")) {
            shiftDec(msg, number);
        }
    }

    public static void codingEnc(String algorithm) {
        if (algorithm.equalsIgnoreCase("unicode")) {
            unicodeEnc(msg,number);
        } else if (algorithm.equalsIgnoreCase("shift")) {
            shiftEnc(msg, number);
        }
    }

    public static void shiftDec(ArrayList<Character> msg, int number) {
        int charNumber;
        char space = ' ';
        for (int i = 0; i < msg.size(); i++) {
            if (msg.get(i) == space){
                continue;
            }
            charNumber = (msg.get(i) - number);
            if ((msg.get(i) >= 65 && msg.get(i) <= 90) || (msg.get(i) >= 97 && msg.get(i) <= 122)) {
                if (msg.get(i) >= 65 && msg.get(i) <= 90) {
                    if (charNumber >= 90) {
                        charNumber = charNumber - 90 + 64;
                    } else if (charNumber <= 65) {
                        charNumber = charNumber - 64 + 90;
                    }
                } else if (msg.get(i) >= 97 && msg.get(i) <= 122) {
                    if (charNumber >= 122) {
                        charNumber = charNumber - 122 + 96;
                    } else if (charNumber <= 96) {
                        charNumber = charNumber - 96 + 122;
                    }
                }
            } else { charNumber = msg.get(i); }
            msg.set(i, (char) charNumber);
        }
    }

    public static void shiftEnc(ArrayList<Character> msg, int number) {
        int charNumber;
        char space = ' ';
        for (int i = 0; i < msg.size(); i++) {
            if (msg.get(i) == space){
                continue;
            }
            charNumber = ((msg.get(i) + number));
            if ((msg.get(i) >= 65 && msg.get(i) <= 90) || (msg.get(i) >= 97 && msg.get(i) <= 122)) {
                if (msg.get(i) >= 65 && msg.get(i) <= 90) {
                    if (charNumber >= 90) {
                        charNumber = charNumber - 90 + 64;
                    } else if (charNumber <= 65) {
                        charNumber = charNumber - 64 + 90;
                    }
                } else if (msg.get(i) >= 97 && msg.get(i) <= 122) {
                    if (charNumber >= 122) {
                        charNumber = charNumber - 122 + 96;
                    } else if (charNumber <= 96) {
                        charNumber = charNumber - 96 + 122;
                    }
                }
            } else { charNumber = msg.get(i); }
            msg.set(i, (char) charNumber);
        }
    }

    public static StringBuilder choose(String action, ArrayList<Character> msg) {
        StringBuilder ciph = new StringBuilder();
        if (action.equals("dec")) {
            codingDec(algorithm);
            for (char c : msg) {
                ciph.append(c);
            }

        } else if (action.equals("enc")){
            codingEnc(algorithm);
            for (char c : msg) {
                ciph.append(c);
            }
        }
        return ciph;
    }

    public static void add(String input, ArrayList<Character> msg) {
        for (int i = 0; i < input.length(); i++) {
            msg.add(input.charAt(i));
        }
    }

    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));

    }

    public static String openFile(String inputTxt) {
        String pathToFile = "./" + inputTxt;
        String fileCon = "";
        try {
            fileCon = readFileAsString(pathToFile);
        } catch (IOException e) {
            System.out.println("Cannot read file: " + e.getMessage());
        }
        return fileCon;
    }
    public static void writeFile(String outputTxt, String message) {
        File file = new File("./" + outputTxt);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(message);
        } catch (IOException e) {
            System.out.printf("An exception occurred %s", e.getMessage());
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("-mode")) {
                action = args[i+1];
            }
            if (args[i].equalsIgnoreCase("-key")) {
                number = Integer.parseInt(args[i+1]);
            }
            if (args[i].equalsIgnoreCase("-data")) {
                input = (args[i+1]);
                isData = true;
            }
            if (args[i].equalsIgnoreCase("-in")) {
                inputTxt = (args[i+1]);
                isIn = true;
            }
            if (args[i].equalsIgnoreCase("-out")) {
                outputTxt = (args[i+1]);
                isOut = true;
            }
            if (args[i].equalsIgnoreCase("-alg")) {
                algorithm = (args[i+1]);
            }
        }
        if(isData && isIn) {
            add(input, msg);
            choose(action, msg);
        } else if (isData) {
            add(input, msg);
            choose(action, msg);
        } else if (isIn) {
            add(openFile(inputTxt),msg);
            String message = String.valueOf(choose(action, msg));
            writeFile(outputTxt, message);
        }
        //-key 5 -data "\jqhtrj%yt%m~ujwxpnqq&" -mode dec
        //-mode enc -in dataset_91007.txt -out protected.txt -key 5
    }
}