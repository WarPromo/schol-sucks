import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class hello {

    public static HashMap<String, Integer> syllableMap = new HashMap<String, Integer>();

    public static void main(String[] args) {

        System.out.println(TextLib.getSyllables("italy"));


        // ============= test of extracting words ===========
        /*
        String testSentence = "Martha said \"I think pome means apple or something??\"";
        ArrayList<String> words = TextLib.extractWords(testSentence);

        System.out.println(testSentence);
        System.out.println(words);



        // ================= test of syllables ==============
        for (String word : words) {
            System.out.println(word + " : " + TextLib.getSyllables(word));
        }
        */


    }





}





