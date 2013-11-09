/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package StockTradingServer;

import StockTradingCommon.Enumeration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @date    :   Nov 6, 2013
 * @author  :   Hirosh Wickramasuriya
 */
class  Leet
{
    private String symbol;
    private String letter;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
    
    public Leet(String symbol, String letter)
    {
        this.symbol = symbol;
        this.letter = letter;
    }
}

public class PasswordClassifier 
{
    private ArrayList<Leet> leetArray = new ArrayList();
    private ArrayList<String> commonPasswords = new ArrayList();
    private ArrayList<String> multiwaySymbols =  new ArrayList();

    public PasswordClassifier()
    {
        PopulateLeetArray();
        PopulateCommonPasswords();
        multiwaySymbols = GetMultiwaySymbols();
    }
    public  int GradePassword(String password)
    {
        //String grade = "";
        int grade = 0;
        
        // 1. check the length
        
        // 2. check wheather a common password
        
        // 3. check wheather a leetspeak password 
        
        // 4. check whether the repetition of common words
        
        // 5. check whether the 4 digit years included
        
        // 6. compute the entrophy
            // determine lower case letters
            // determine upper case letters
            // determine digits
            // determin special characters
        
        
        // 1. check the length 
        if (password.length() >= 8)
        {
                                    
            // 4. checks whether the four digit year between 1900 - 2099 included
            //boolean yearFound = false;
            Pattern patternYear = Pattern.compile(".*[1][9]\\d{2}.*|.*[2][0]\\d{2}.*");
            Matcher matcherYear = patternYear.matcher(password);
            if (matcherYear.matches())
            {
                // yearFound = true;
                if (password.length() < 12)
                {
                    //grade = "weak";
                    grade = Enumeration.PasswordGrade.PASSWORD_STRENGTH_WEAK;
                }                
            }
            
            //if (!grade.equals("weak"))
            if (grade != Enumeration.PasswordGrade.PASSWORD_STRENGTH_WEAK )
            {
                // 3. check wheather a leetspeak password 
                // Eg. p@$$vv0rd => password
                
                boolean hasLeet = IsLeetPassword(password);                

                boolean isCommonWord = false;
                if (hasLeet)
                {
                    // convert the leet to alpha and check against the common passwords      
                    
                    for (String passwordText : GetPossibleTexts(password))
                    {
                        //System.out.println(passwordText);

                        // Check whether the given password is a common password written as a leet
                        if (commonPasswords.contains(passwordText.toLowerCase()))
                        {
                            isCommonWord = true;
                            break;
                        }
                        
                        // Check whether the common password is a part of the given password 
                        // Eg. A43dsrPASSWORD.ew
                        for(String commonPassword : commonPasswords)
                        {
                            if (commonPassword.length() > 8)
                            {
                                if (passwordText.toLowerCase().indexOf(commonPassword) > -1)
                                {
                                    isCommonWord = true;
                                    break;
                                }
                            }
                        }
                    }                
                }
                else
                {
                    // check for a common password
                    if (commonPasswords.contains(password.toLowerCase()))
                    {
                        isCommonWord = true;
                    }

                    // Check whether the common password is a part of the given password
                    // Eg. A43dsrPASSWORD.ew
                    // Eg. HelloHelloHello
                    for(String commonPassword : commonPasswords)
                    {
                        if (commonPassword.length() > 8)
                        {
                            if (password.toLowerCase().indexOf(commonPassword) > -1)
                            {
                                isCommonWord = true;
                                break;
                            }
                        }
                    }                
                }           
            
                if (isCommonWord)
                {
                    //grade = "Weak - contains common word";
                    grade = Enumeration.PasswordGrade.PASSWORD_STRENGTH_WEAK;
                }
                else
                {
                    // In order to classify the passowrd for further entropy level, determine the character space used. 
                    // Let N be the number of characters in the pasword space , and L be the length of the password
                    // check whether lower case letters exist           N += 26
                    // Check whether upper case letters exist           N += 26 
                    // check whether digits are used                    N += 10                    
                    // check whether both the special characters used   N += 34
                    
                    // Engtropy H = log_2 ( N^L - 1) / (N - 1) =~ L * Log_2 N
                    
                    int N = 0;
                    
                    // check for lower case letters
                    Pattern patternLower = Pattern.compile(".*[a-z].*");
                    Matcher matcherLower = patternLower.matcher(password);
                    if (matcherLower.matches())
                    {
                        N += 26;
                    }
                    
                    // check for upper case letters
                    Pattern patternUpper = Pattern.compile(".*[A-Z].*");
                    Matcher matcherUpper = patternUpper.matcher(password);
                    if (matcherUpper.matches())
                    {
                        N += 26;
                    }
                    
                    // check for digits
                    Pattern patternDigits = Pattern.compile(".*[0-9].*");
                    Matcher matcherDigits = patternDigits.matcher(password);
                    if (matcherDigits.matches())
                    {
                        N += 10;
                    }
                    
                    // check for special characters
                    Pattern patternSpecial = Pattern.compile(".*[^a-zA-Z0-9]+.*");
                    
                    Matcher matcherSpecial= patternSpecial.matcher(password);
                    if (matcherSpecial.matches())
                    {
                        N += 34;
                    }
                    //      log(N x (N^L - 1)/(N - 1)      log N^L
                    //  H = -------------------------  ~= -------------
                    //          log 2                       log 2
                    //
                    double entrophy = password.length() * (Math.log((double)N)/ Math.log(2));
                    //System.out.println(password + " Entrophy   is :"  + entrophy);
                    
                    if (entrophy > 50.0)
                    {
                        //grade = "Strong";
                        grade = Enumeration.PasswordGrade.PASSWORD_STRENGTH_STRONG;
                    }
                    else if (entrophy > 35.0)
                    {
                        //grade = "Good";
                        grade = Enumeration.PasswordGrade.PASSWORD_STRENGTH_GOOD;
                    }
                    else if (entrophy > 20.0)
                    {
                        //grade = "Weak";
                        grade = Enumeration.PasswordGrade.PASSWORD_STRENGTH_WEAK;
                    }
                    else
                    {
                        grade = Enumeration.PasswordGrade.PASSWORD_STRENGTH_VERYWEAK;
                    }
                }
            }
        }
        else
        {
            //grade = "Very Weak";
            grade = Enumeration.PasswordGrade.PASSWORD_STRENGTH_VERYWEAK;
        }
        
        return grade;
    }
    
    private  void PopulateLeetArray()
    {
        leetArray.clear();
        
        leetArray.add(new Leet("@", "A"));
        leetArray.add(new Leet("/\\", "A"));
        leetArray.add(new Leet("4", "A"));
        leetArray.add(new Leet("^", "A"));
        
        leetArray.add(new Leet("13", "B"));
        leetArray.add(new Leet("/3", "B"));
        leetArray.add(new Leet("|3", "B"));
        leetArray.add(new Leet("l3", "B"));
        leetArray.add(new Leet("!3", "B"));
        leetArray.add(new Leet("i3", "B"));
        
        leetArray.add(new Leet("<", "C"));
        leetArray.add(new Leet("[", "C"));
        leetArray.add(new Leet("(", "C"));
        
        leetArray.add(new Leet("1>", "D"));
        leetArray.add(new Leet("|>", "D"));
        leetArray.add(new Leet("/>", "D"));
        leetArray.add(new Leet("!>", "D"));
        leetArray.add(new Leet("l>", "D"));
        leetArray.add(new Leet("i>", "D"));
        leetArray.add(new Leet("|)", "D"));
        leetArray.add(new Leet("i)", "D"));
        leetArray.add(new Leet("l)", "D"));
        leetArray.add(new Leet("1)", "D"));
        leetArray.add(new Leet("|]", "D"));
        leetArray.add(new Leet("i]", "D"));
        leetArray.add(new Leet("l]", "D"));
        leetArray.add(new Leet("1]", "D"));
                
        leetArray.add(new Leet("3", "E"));
        
        leetArray.add(new Leet("4", "F"));
        
        leetArray.add(new Leet("9", "G"));
        leetArray.add(new Leet("6", "G"));
        leetArray.add(new Leet("8", "G"));
        
        leetArray.add(new Leet("/-/", "H"));
        leetArray.add(new Leet("[-]", "H"));
        leetArray.add(new Leet("]-[", "H"));
        
        leetArray.add(new Leet("!", "I"));
        leetArray.add(new Leet("1", "I"));
        
        leetArray.add(new Leet("_/", "J"));
        
        leetArray.add(new Leet("l<", "K"));
        leetArray.add(new Leet("!<", "K"));        
        leetArray.add(new Leet("|<", "K"));
        leetArray.add(new Leet("1<", "K"));
        
        leetArray.add(new Leet("!_", "L"));
        leetArray.add(new Leet("1_", "L"));
        leetArray.add(new Leet("|_", "L"));
        leetArray.add(new Leet("[", "L"));
        leetArray.add(new Leet("[_", "L"));
        leetArray.add(new Leet("1", "L"));
        leetArray.add(new Leet("|", "L"));
        
        leetArray.add(new Leet("/\\/\\", "M"));        
        leetArray.add(new Leet("XX", "M"));           
        leetArray.add(new Leet("^^", "M"));
        
        leetArray.add(new Leet("l\\l", "N"));
        leetArray.add(new Leet("|\\|", "N"));        
        leetArray.add(new Leet("|V", "N"));
        
        leetArray.add(new Leet("0", "O"));        
        
        leetArray.add(new Leet("12", "R"));
        leetArray.add(new Leet("|2", "R"));
        leetArray.add(new Leet("l2", "R"));
        
        leetArray.add(new Leet("5", "S"));
        leetArray.add(new Leet("$", "S"));
        
        leetArray.add(new Leet("7", "T"));
        
        leetArray.add(new Leet("\\/", "V"));
        leetArray.add(new Leet("\\/\\/", "W"));
        leetArray.add(new Leet("VV", "W"));
        leetArray.add(new Leet("vv", "W"));
        
        leetArray.add(new Leet("><", "X"));
        
        leetArray.add(new Leet("\\|/", "Y"));
        
        leetArray.add(new Leet("2", "Z"));
        
    }
    
    private  void PopulateCommonPasswords() 
    {
        
        commonPasswords.clear();       

        String FileName = System.getProperty("user.dir") 
                        + System.getProperty("file.separator") + "resources"
                        + System.getProperty("file.separator") + "common.lst";
        try
        {
            FileReader commonPassworFile = new FileReader(FileName);

            BufferedReader brCommonPassword = new BufferedReader(commonPassworFile);        
            String line = brCommonPassword.readLine();
            while (line != null)
            {
                commonPasswords.add(line);
                line = brCommonPassword.readLine();
            }
            commonPassworFile.close();
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("File " + FileName + " is not available.");            
        }
        catch (Exception ex)
        {
            System.out.println("Error occurred.");            
        }             
    }
    
    private  ArrayList<String> GetPossibleTexts(String leetPassword)
    {
        ArrayList<String> partiallyConvertedPasswords = ReplaceLeet(leetPassword);
        ArrayList<String> textPasswords = new ArrayList();
        
        for (String password : partiallyConvertedPasswords)
        {
            String passwordText = GetPossibleText(password);
            if (!textPasswords.contains(passwordText))
            {
                textPasswords.add(passwordText);
            }            
        }
        
        return textPasswords;
    }
    
    private  ArrayList<String> ReplaceLeet(String leetPassword)
    {
        ArrayList<String> partiallyConvertedPasswords = new ArrayList();
        ArrayList<String> partiallyConvertedPasswords_temp = new ArrayList();
        
        for (String password : GetPossibleTextsForDifferentPatternInterpretations(leetPassword))
        {
            partiallyConvertedPasswords.add(password);
        }
        
        for (String password : GetPossibleTextsForDifferentSymbolInterpretations(leetPassword))
        {
            partiallyConvertedPasswords.add(password);
        }
       
        for (String password : partiallyConvertedPasswords)
        {
            if (IsLeetPassword(password))
            {
                for (String password2 : GetPossibleTextsForDifferentPatternInterpretations(password))
                {
                    partiallyConvertedPasswords_temp.add(password2);
                }

                for (String password2 : GetPossibleTextsForDifferentSymbolInterpretations(password))
                {
                    partiallyConvertedPasswords_temp.add(password2);
                }
            }
        }
        
        partiallyConvertedPasswords.clear();
        for (String password : partiallyConvertedPasswords_temp)
        {
                if (!partiallyConvertedPasswords.contains(password))
                {
                    partiallyConvertedPasswords.add(password);
                }
                               
         }        
        return partiallyConvertedPasswords;
    }
    
    private  ArrayList<String> GetPossibleTextsForDifferentPatternInterpretations(String leetPassword)
    {
        // There can be many ways to interpret same pattern of leet
        // Eg.
        // |><||<
        //  |
        //  +------ DCLLC  x 2
        //  +------ LXLLC  x 2
        //  +------ LXLK
        //  +------ L>CLLC x 2
        //  +------ DCLK   x 2 
        //  +------ L>CLK

        ArrayList<String> alphaNumericPasswords = new ArrayList();      
        
        String[] conflictSymbolArray = {"|>", "><", "|", "|<", "<"};
        String[] conflictSymbolReverseArray = conflictSymbolArray.clone();        

        for (String conflictSymbol : conflictSymbolArray)
        {
            if (leetPassword.indexOf(conflictSymbol) > -1)
            {
                String password1 = leetPassword.replace(conflictSymbol, GetLeetLetter(conflictSymbol));                   

                //password1 = GetPossibleText(password1);
                if (!alphaNumericPasswords.contains(password1))
                {
                    alphaNumericPasswords.add(password1);
                }
            }                
        }
        
        return alphaNumericPasswords;
    }
    
    private  ArrayList<String> GetPossibleTextsForDifferentSymbolInterpretations(String leetPassword)
    {
        // There can be many ways to interpret the same symbol
        // Eg.
        //  4
        //  |
        //  +------ A
        //  +------ F
        
        ArrayList<String> alphaNumericPasswords = new ArrayList();
                
        for (String multiwayConflictSymbol : multiwaySymbols)
        {
            for (String letter : GetLettersForSymbol(multiwayConflictSymbol))
            {
                String password1 = leetPassword.replace(multiwayConflictSymbol, letter);
                
                if (!alphaNumericPasswords.contains(password1))
                {
                    alphaNumericPasswords.add(password1);
                }
             }                   
        }
        
        return alphaNumericPasswords;
    }
    
    private  String GetPossibleText(String leetPassword)
    {      
        for(Leet leet : leetArray)
        {
            if (leetPassword.indexOf(leet.getSymbol()) > -1)
            {
                leetPassword = leetPassword.replace(leet.getSymbol(), leet.getLetter());
            }
        }
        
        return leetPassword;        
    }
    
    private  String GetLeetLetter(String symbol)
    {
        String letter="";
        for(Leet leet: leetArray)
        {
            if (leet.getSymbol().equals(symbol))
            {
                letter = leet.getLetter();
                break;
            }
        }
        return letter;
    }
    
    private  ArrayList<String> GetMultiwaySymbols()
    {
        ArrayList<String> multiWaySymbols = new ArrayList();
        
        for(Leet leet1: leetArray)
        {
            for (Leet leet2: leetArray)
            {
                if (leet2.getSymbol().equals(leet1.getSymbol()))
                {                       
                    if (!multiWaySymbols.equals(leet1.getSymbol()))
                    {
                        // add the symbol to array, only if it is new to the array
                        multiWaySymbols.add(leet1.getSymbol());
                    }
                }
            }
        }        
        return multiWaySymbols;        
    }
    
    private  ArrayList<String> GetLettersForSymbol(String symbol)
    {
        ArrayList<String> letters = new ArrayList();
        
        for(Leet leet: leetArray)
        {
            if (leet.getSymbol().equals(symbol))
            {
                letters.add(leet.getLetter());
            }
        } 
        
        return letters;
    }
    
    private  boolean IsLeetPassword(String password)
    {
        boolean hasLeet = false;
        for(Leet leet : leetArray)
        {
            if (password.indexOf(leet.getSymbol()) > -1)
            {
                // leet symbol found
                hasLeet = true;
                break;
            }
        }
        return hasLeet;
    }
    
}
