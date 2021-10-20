import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class TextLib {

    public static ArrayList<String> splitIntoSentences(String text) {
        text = text.replaceAll("[\\s]+", " "); // collapse whitespace

        ArrayList<String> output = new ArrayList<String>();

        Locale locale = Locale.US;
        BreakIterator breakIterator = BreakIterator.getSentenceInstance(locale);
        breakIterator.setText(text);

        int prevIndex = 0;
        int boundaryIndex = breakIterator.first();
        while(boundaryIndex != BreakIterator.DONE) {
            String sentence = text.substring(prevIndex, boundaryIndex).trim();
            if (sentence.length()>0)
                output.add(sentence);
            prevIndex = boundaryIndex;
            boundaryIndex = breakIterator.next();
        }

        String sentence = text.substring(prevIndex).trim();
        if (sentence.length()>0)
            output.add(sentence);

        return output;
    }

    public static ArrayList<String> splitIntoSentences2(String text) {
        text = text.replaceAll("[\\s]+", " "); // collapse whitespace

        // remove punctuation
        String noPunctuationText = text.replaceAll("[^a-zA-Z -]", "");

        // splits when it sees a . ! or ? followed by whitespace
        String[] sentences = text.split("[.!?]+\\s+");

        // convert array into ArrayList
        return new ArrayList<String>( Arrays.asList(sentences) );
    }

    public static String readFile(String filePath) {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath));) {

            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.getProperty("line.separator"));
                line = br.readLine();
            }

        } catch (Exception errorObj) {
            System.err.println("There was a problem reading the " + filePath);
        }

        return sb.toString();
    }

    public static void writeToFile(String filePath, String data) {
        try (FileWriter f = new FileWriter(filePath);
             BufferedWriter b = new BufferedWriter(f);
             PrintWriter writer = new PrintWriter(b);) {

            writer.print(data);

        } catch (Exception errorObj) {
            System.out.println("There was an error with the file");
            errorObj.printStackTrace();
        }
    }

    public static ArrayList<String> extractWords(String text){
        text = text.toLowerCase();
        String acceptable = "abcdefghijklmnopqrstuvwxyz'";
        ArrayList<String> words = new ArrayList<String>();
        ArrayList<String> sentences = splitIntoSentences(text);

        String word = "";

        for(String sentence : sentences){
            for(int a = 0; a < sentence.length(); a++){
                if(acceptable.contains( sentence.charAt(a)+"" ) == false){
                    if(word.length() > 0)words.add(word);
                    word = "";
                }
                else{
                    word += sentence.charAt(a);
                }
            }
        }


        return words;

    }

    public static double readabilityScore(String text){
        ArrayList<String> words = extractWords(text);

        int totalWords = words.size();
        int totalSyllables = 0;
        int totalSentences = splitIntoSentences(text).size();

        for(String word : words){
            totalSyllables += getSyllables(word);
        }

        return 206.835 - 1.015*(totalWords / totalSentences)-84.6*(totalSyllables / totalWords);
    }

    public static double getSMOGScore(String text){
        ArrayList<String> words = extractWords(text);

        int totalWords = words.size();
        int totalSyllables = 0;
        int totalSentences = splitIntoSentences(text).size();

        if(totalSentences < 30) return -1;

        for(String word : words){
            if(getSyllables(word) >= 3){
                totalSyllables++;
            }

        }

        return 1.043 * Math.sqrt(totalSyllables * totalSentences / 30) + 3.1291;
    }

    public static int getSyllables(String word){


        if(word.charAt(word.length()-1) == 'e') word = word.substring(0, word.length()-1);

        word = word.replace("tion", "ton");

        if(word.endsWith("th")) word = word.substring(0, word.length()-2) + "t";

        String syllableEnders = "a ?" +
                ",b acdefgijklmnopstuvwxyz" +
                ",c abedfgijlmnopqstuvwxyz" +
                ",d abcefghijklmnopqtuvwxyz" +
                ",e ?oi" +
                ",f abcdeghijklmnopqrsuvwxyz" +
                ",g abcdefhijklmnopqrtuvwxyz" +
                ",h abcdefghijlmnopqrstuvwxz" +
                ",i o?eau" +
                ",j abcdefghiklmnopqrstuvwxyz" +
                ",k abcdefghijlmnopqrstuvwxyz" +
                ",l abcgehijmnopqrstuvwxyz" +
                ",m acdefghijklnoqrtuvwxyz" +
                ",n aefhijlmopqruvwxyz" +
                ",o ?" +
                ",p abcdefgijklmnoqstuvwxyzl" +
                ",q abcdefghijklmnoprstuvwxyz" +
                ",r abcefghijopquvwxyz" +
                ",s abcdefgjlmnopqruvwxyz" +
                ",t abcdefghijklmnopqrsuvwxyz" +
                ",u ?iao" +
                ",v abcdefghijklmnopqrstuwxyz" +
                ",w abcdefghijkmoqrtuvxyz" +
                ",x abcdefghijklmnopqrstuvwxyz" +
                ",y abcdfghijklmnopqrsuvwxz" +
                ",z abcdefghijklmnopqrstuvwxyz";

        HashMap<String, String> enders = new HashMap<String, String>();

        String[] arr = syllableEnders.split(",");

        if(word.length() <= 2) return 1;

        String result = word.substring(0, 2);

        for(int a = 0; a < arr.length; a++){
            //System.out.println(arr[a]);
            String[] p = arr[a].split(" ");

            enders.put(p[0], p[1]);
        }

        int syllables = 1;
        boolean canEnd = false;
        String vowels = "aeoiuy";


        for(int a = 1; a < word.length()-1; a++){



            String char1 = word.substring(a, a+1);
            String char2 = word.substring(a+1, a+2);

            if( vowels.contains(char1) ) canEnd = true;
            if(enders.containsKey(char1) == false){
                syllables++;
                result += "*";
                canEnd = false;
            }
            else if(enders.get(char1).contains(char2) && canEnd){
                syllables++;
                result += "*";
                canEnd = false;
            }


            result += char2;
        }

        System.out.println(result);


        return syllables;



    }
}
