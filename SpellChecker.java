import java.util.*;
import java.io.*;

public class SpellChecker{
    HashMap<Double,String> dict;
    
    public SpellChecker() {
        dict = new HashMap<Double,String>(100);
    }

    public void createDictionary(String filename){
        File file = new File(filename);
        try{
            Scanner s = new Scanner(file);
            while(s.hasNext()){
                String word = s.next();
                double initialKey = 0,currentKey;
                currentKey = hash(word); //calculate hash value of the word
                initialKey = currentKey;
                while(dict.get(currentKey) != null && !dict.get(currentKey).equals(word)){    //resolve collision
                    currentKey++;
                    if(currentKey >= 100)
                        currentKey = currentKey - 100;
                    if(currentKey == initialKey){
                        System.out.println("hash table full");
                        return;
                    }
                }
                dict.put(currentKey,word); //insert it into the hash table
                //System.out.println(dict.get(currentKey));
            }
        }
        catch(FileNotFoundException e){
            System.out.println("Dictionary File not found");
        }
        
    }

    public void spellCheck(String filename){
        File check = new File(filename);
        try{
            Scanner s = new Scanner(check);
            while(s.hasNext()){
                String word = s.next();
                if(!contains(word)){
                    System.out.print("Misspelt: ");
                    System.out.println(word);
                    System.out.println("Word suggestions: ");
                    findCorrectSpelling(word);
                    System.out.println();
                }                

            }
        }catch (FileNotFoundException e) {
            System.out.println("Test file not found");
        }
    }

    public boolean contains(String word){
        double initialKey = hash(word);
        double currentKey = initialKey;
        while(dict.get(currentKey) != null && !dict.get(currentKey).equals(word)){
            currentKey++;
            if (currentKey >= 100) {
                currentKey -= 100;
            }
            if(currentKey == initialKey){
                return false;
            }
        }
        if(dict.get(currentKey) == null)
            return false;                
        return true;
    }

    public void findCorrectSpelling(String word){
        addLetter(word);
        removeLetter(word);
        exchangeLetter(word); 
    }

    public void addLetter(String word){
        char[] alphabets = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for(char c : alphabets){
            for(int i=0;i<=word.length();i++) {
                String newWord = word.substring(0,i) + c + word.substring(i,word.length());
                if(contains(newWord)){
                    System.out.println(newWord);
                    break;
                }
            }
        }
    }

    public void removeLetter(String word){
        for(int i=0;i<word.length();i++){
            StringBuilder s = new StringBuilder(word);
            s.deleteCharAt(i);
            if(contains(s.toString())){
                System.out.println(s);
            }

        }
    }

    public void exchangeLetter(String word){
        for(int i=0;i<word.length()-1;i++){
            StringBuilder s = new StringBuilder(word);
            char temp = s.charAt(i);
            s.setCharAt(i,s.charAt(i+1));
            s.setCharAt(i+1,temp);
            if(contains(s.toString())){
                System.out.println(s);
            }
        }
    }

    public double hash(String word){
        double val = 0;
        for(int i=0;i<word.length();i++)
            val += word.charAt(i);
        double k = (Math.sqrt(5)-1)/2;
        double num = k*val;
        double frac = num - (int)num;
        val = Math.floor(frac*100);
        return val;
    }

    public static void main(String[] args) throws FileNotFoundException{
        SpellChecker sc = new SpellChecker();
        sc.createDictionary("");
        sc.spellCheck("");
    }
}
