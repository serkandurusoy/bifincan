/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dna.bifincan.util;


import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runners.JUnit4;

/**
 *
 * @author hantsy
 */
@org.junit.runner.RunWith(JUnit4.class)
public class StringUtilsTest {
    
    public StringUtilsTest() {
    }


    /**
     * Test of trimRepeatedText method, of class StringUtils.
     */
    @org.junit.Test
    public void testTrimRepeatedText() {
        System.out.println("this is so funny he he he he he");
        String original = "this is so funny he he he he he";
        String expResult = "this is so funny he he";
        String result = StringUtils.trimRepeatedText(original);
        System.out.print("result @"+result+"@");
        assertEquals(expResult, result);       
    }
    
    @org.junit.Test
    public void testTrimRepeatedText2() {
        System.out.println(":)))))))))))))))");
        String original = ":)))))))))))))))";
        String expResult = ":))";
        String result = StringUtils.trimRepeatedText(original);
        System.out.print("result @"+result+"@");
        assertEquals(expResult, result);       
    }
    
    @org.junit.Test
    public void testTrimRepeatedText3() {
        System.out.println("soooo great");
        String original = "soooo great";
        String expResult = "soo great";
        String result = StringUtils.trimRepeatedText(original);
        System.out.print("result @"+result+"@");
        assertEquals(expResult, result);       
    }
    
    @org.junit.Test
    public void testTrimRepeatedText4() {
        System.out.println("helloooo :) :) :) :)");
        String original = "helloooo :) :) :) :)";
        String expResult = "helloo :) :)";
        String result = StringUtils.trimRepeatedText(original);
        System.out.print("result @"+result+"@");
        assertEquals(expResult, result);       
    }
    
    @org.junit.Test
    public void testTrimRepeatedText5() {
        System.out.println("this is a test");
        String original = "this is a test";
        String expResult = "this is a test";
        String result = StringUtils.trimRepeatedText(original);
        System.out.print("result @"+result+"@");
        assertEquals(expResult, result);       
    }
    
    
    @org.junit.Test
    public void testTrimRepeatedText6() {
        System.out.println("aAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAAAAAAAAaaaaaa");
        String original = "aAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAaAAAAAAAAaaaaaa";
        String expResult = "aA";
        String result = StringUtils.trimRepeatedText(original);
        System.out.print("result @"+result+"@");
        assertEquals(expResult, result);       
    }
    
    @org.junit.Test
    public void testTrimRepeatedText7() {
        System.out.println("aaaaa bbbbb aaa bbb aaa bbb aaa bbb aaaaaa bbbbbbb");
        String original = "aaaaa bbbbb aaa bbb aaa bbb aaa bbb aaaaaa bbbbbbb";
        String expResult = "aa bb aa bb";
        String result = StringUtils.trimRepeatedText(original);
        System.out.print("result @"+result+"@");
        assertEquals(expResult, result);       
    }
    
    @org.junit.Test
    public void testTrimRepeatedText8() {
        System.out.println("wordd1 worddd1 wordddd1 wordddd1 word2 word 3 word4 word4 word4 word4");
        String original = "wordd1 worddd1 wordddd1 wordddd1 word2 word 3 word4 word4 word4 word4";
        String expResult = "wordd1 wordd1 word2 word 3 word4 word4";
        String result = StringUtils.trimRepeatedText(original);
        System.out.print("result @"+result+"@");
        assertEquals(expResult, result);       
    }
    
    @org.junit.Test
    public void testTrimRepeatedText9() {
        System.out.println("................................................................................ :))))) :)) :))))))) :)) :))))) :)))))) :))");
        String original = "................................................................................ :))))) :)) :))))))) :)) :))))) :)))))) :))";
        String expResult = ".. :)) :))";
        String result = StringUtils.trimRepeatedText(original);
        System.out.print("result @"+result+"@");
        assertEquals(expResult, result);       
    }
    
    @org.junit.Test
    public void testTrimRepeatedText10() {
        System.out.println("helloooooo world hellooo helloo ooooooo ooo oooooooo ooo oo o oooo :))))))) :))))))) :)) :))))))) :))))))))))) :))) ..........................");
        String original = "helloooooo world hellooo helloo ooooooo ooo oooooooo ooo oo o oooo :))))))) :))))))) :)) :))))))) :))))))))))) :))) ..........................";
        String expResult = "helloo world helloo helloo oo oo o oo :)) :)) ..";
        String result = StringUtils.trimRepeatedText(original);
        System.out.print("result @"+result+"@");
        assertEquals(expResult, result);       
    }
    
    @org.junit.Test
    public void testTrimRepeatedText11() {
        System.out.println("heliiii iiii İİİİİ ıııııı IIIIIII ..........................");
        String original = "heliiii iiii İİİİİ ıııııı IIIIIII ..........................";
        String expResult = "helii ii İİ ıı II ..";
        String result = StringUtils.trimRepeatedText(original);
        System.out.print("result @"+result+"@");
        assertEquals(expResult, result);       
    }    

    @org.junit.Test
    public void testTrimRepeatedText12() {
        System.out.println("Heliiii iiii İİİİİ ıııııı IIIIIII .......................... Capital Fine");
        String original = "Heliiii iiii İİİİİ ıııııı IIIIIII .......................... Capital Fine";
        String expResult = "Helii ii İİ ıı II .. Capital Fine";
        String result = StringUtils.trimRepeatedText(original);
        System.out.print("result @"+result+"@");
        assertEquals(expResult, result);       
    }    

}
