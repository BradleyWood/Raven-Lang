import javax.swing.JOptionPane;

fun main() {
    var input = JOptionPane.showInputDialog("What is your name?");
    JOptionPane.showMessageDialog(null, "Hello, " + input);
}
