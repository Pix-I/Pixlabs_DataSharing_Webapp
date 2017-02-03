package com.pixlabs.test;

import com.pixlabs.util.GravatarUtil;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.fail;

/**
 * Created by pix-i on 06/02/2017.
 * ${Copyright}
 */


public class UtilsTest {


    /**
     * Tests if the gravatar util fetches an image.
     */
    @Test
    public void gravatarTest(){
        String randomEmail = UUID.randomUUID().toString()+"@te-st.com";
        if(GravatarUtil.getGravatar(randomEmail)==null){
            fail();
        }
        System.out.println("Test for gravatar was successfull.");


    }
}
