import javax.swing.*;

public class App{
    public static void main(String[] args) {

        //dimension of the window
        int boardWidth = 360;
        int boardHeight = 640;

        //making JFrame object and "Flappy Bird" as the title  of the window
        JFrame frame = new JFrame("Flappy Bird");
       
        frame.setSize(boardWidth,boardHeight); //size of the screen
        frame.setLocationRelativeTo(null); // this will make the window in the center of the screen
        frame.setResizable(false); //window can't be resizable
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close the complete window as soon as we exit

        FlappyBird flappyBird = new FlappyBird(); //make an object of flappy bird
        frame.add(flappyBird); //adding flappy bird to the frame
        frame.pack(); // this is required so that flappybird will leave the title bar untouched other it will make it blue as well.
        flappyBird.requestFocus(); //bird will be in the focus of key listener
        frame.setVisible(true);   //this will make it visible for everyone
    } 
}