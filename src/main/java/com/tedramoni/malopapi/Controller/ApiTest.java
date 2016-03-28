package com.tedramoni.malopapi.Controller;


import com.tedramoni.malopapi.Model.Opening;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ApiTest {

    @Test
    public void testDecoupage() {
        Opening opening = new Opening(1);
        opening.setOpening("#1: \"staple stable\" by Chiwa Saito (TV Broadcast: eps 2, 6-7, 11-12; BD/DVD: 1-2, 12)");
    }
}