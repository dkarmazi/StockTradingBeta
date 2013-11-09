/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package StockTradingServer;

import StockTradingCommon.Enumeration;
/**
 * @date    :   Nov 2, 2013
 * @author  :   Hirosh Wickramasuriya
 */
public class Security {

    private String alphabet = "abcdefghijklmnopqrstuvwxyz";
    private String special = "!$%^&*{}[]@#~:;<>?*-+";
    
    public String CreateRandomPassword()
    {
        String password ="";
        char[] alphabetArray = alphabet.toCharArray();
        char[] specialArray = special.toCharArray();
        
        java.security.SecureRandom srg = new  java.security.SecureRandom();
        //byte[] bytes = new byte[20];
        int lowerChar = 0;
        int upperChar = 0;
        int specialChar = 0;
        int digits = 0;
        
        int minLower = Enumeration.Security.PASSWORD_MIN_LOWER;
        int minUpper = Enumeration.Security.PASSWORD_MIN_UPPER;
        int minDigit = Enumeration.Security.PASSWORD_MIN_DIGIT;
        int minSpecl = Enumeration.Security.PASSWORD_MIN_SPECL;
        
        int i;
        while (password.length() < Enumeration.Security.PASSWORD_DEFAULT_LENGTH)
        {
            switch (srg.nextInt(4))
            {
                
                case 0:                     // include lower case letters
                    if ((minLower > 0 || ( (minUpper == 0) && (minDigit == 0) && ( minSpecl ==0 )) )) 
                    {
                        i = srg.nextInt(26);
                        password += alphabetArray[i] ;
                        lowerChar++;
                        minLower--;
                    }
                    
                    break;
                    
                case 1:                     // include upper case letters
                    if ((minUpper > 0 || ( (minLower <= 0) && (minDigit <= 0) && ( minSpecl <=0 )) )) 
                    {
                        i = srg.nextInt(26);
                        password +=  Character.toString(alphabetArray[i]).toUpperCase()  ;
                        upperChar++;
                        minUpper--;
                    }
                    break;
                    
                case 2:                     // include digits
                    if ((minDigit > 0 || ( (minLower <= 0) && (minUpper <= 0) && ( minSpecl <=0 )) )) 
                    {
                        i = srg.nextInt(10);
                        password += Integer.toString(i) ;
                        digits++;
                        minDigit--;
                    }                        
                    break;
                    
                case 3:                     // include special characters
                    if ((minSpecl > 0 || ( (minLower <= 0) && (minUpper <= 0) && ( minDigit <=0 )) )) 
                    {
                        i = srg.nextInt(20);
                        password += specialArray[i] ;
                        specialChar++;
                        minSpecl--;
                    }                        
                    break;
            }
            
        }
        
        return password;
    }
}
