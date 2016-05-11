/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotDoom;

import javax.swing.*;

public class Frame
{
    
    private JFrame frame;
    private int width, height;
    
    public Frame()
    {
        width = 300;
        height = 400;
        frame = new JFrame();
        frame.setSize(width, height);
        frame.setTitle("DOOM");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}