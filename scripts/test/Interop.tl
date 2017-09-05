package scripts.test;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.lang.Math;

fun main() {
    var frame = JFrame("Toy lang JFrame");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(400,400);
    frame.setVisible(true);


    var button = JButton("Click me");
    frame.add(button);


    println("Math.Pi = "+Math.PI);
    println("Enter a number");
    var num = real(readLine());
    println("Sin("+num+") = "+Math.sin(num));
    println(""+num +" in degress is "+Math.toDegrees(num));
}