/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.game;

import java.util.Date;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Luís
 */
public class WorldTest {
    
    public WorldTest() {
    }
    
    @Test
    public void testInitialization() {
        int test_Int = 3;
        String test_String = "testy";
        Date test_Date = new Date();
        Double test_Double = 32.4;
        
        JSONObject jo = new JSONObject();
        jo.put("id", test_Int);
        jo.put("name", test_String);
        jo.put("created", test_Date.getTime());
        jo.put("crimerate", test_Double);
        
        World test_World = new World(jo.toJSONString());
        assertEquals(test_World.getId(), test_Int);
        assertEquals(test_World.getName(), test_String);
        assertEquals(test_World.getCreated(), test_Date);
        assertEquals(test_World.getCrimerate(), test_Double);
    }
      
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}