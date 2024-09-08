import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener{
    int boardWidth = 360;
    int boardHeight = 640;


    //image 
    Image backgroundImage;
    Image birdImage;
    Image topPipeImage;
    Image bottomPipeImage;


    //bird

    int birdx = boardWidth/8;
    int birdy = boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;


    //bird class
    class Bird{
        int x = birdx;
        int y = birdy;
        int width = birdWidth;
        int height = birdHeight;
        Image img;
        Bird(Image img){
            this.img = img;
        }
    }

    //pipe class
    int pipeX = boardWidth;
    int pipeY = 0;
    int  pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed  = false;

        Pipe(Image img){
            this.img = img;

        }
    }

    //game logic
    Bird bird;
    int velocityX = -4;
    int velocityY = -0;
    int gravity = 1;



    Timer gameLoop;
    Timer placePipesTimer;

    boolean gameOver = false;

    ArrayList<Pipe> pipes; 
    Random random = new Random();

    double score = 0;


    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        // setBackground(Color.BLUE);
        setFocusable(true); //key listener focused on bird only
        addKeyListener(this);
        //load images

            backgroundImage = new ImageIcon(getClass().getResource("/Images/flappybirdbg.png")).getImage();
            birdImage = new ImageIcon(getClass().getResource("/Images/flappybird.png")).getImage();
            topPipeImage = new ImageIcon(getClass().getResource("/Images/toppipe.png")).getImage();
            bottomPipeImage = new ImageIcon(getClass().getResource("/Images/bottompipe.png")).getImage();

            //bird
            bird = new Bird(birdImage);
            pipes = new ArrayList<Pipe>();

            //place pipe timer

            placePipesTimer = new Timer(1500,new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    placePipes();
                }
            });

            placePipesTimer.start();

            //game timer
            gameLoop = new Timer(1000/60, this); //make it 60fps
            gameLoop.start();
    }


    public void placePipes(){
        int randomPipeY = (int)(pipeY-pipeHeight/4 - Math.random()*(pipeHeight/2));
        int opening = boardHeight/4;

        Pipe topPipe = new Pipe(topPipeImage);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);  

        Pipe bottomPipe = new Pipe(bottomPipeImage);
        bottomPipe.y = topPipe.y + pipeHeight + opening;
        pipes.add(bottomPipe);
    }

    public void  paintComponent(Graphics g){
        super.paintComponent(g); //super  refers to the parent class that is JPanel
        draw(g);
    }

    public void draw(Graphics g){
        //background
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight,null);

        //bird
        g.drawImage(bird.img, bird.x,bird.y,bird.width, bird.height,null);

        //pipes
        for(int i = 0;i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.PLAIN,32));
        if(gameOver){
            g.drawString("Game Over: " + String.valueOf((int)(score)),10,35);
        }
        else{
            g.drawString( String.valueOf((int)(score)), 10 , 35);
        }
    }


    public void move(){
        //birds
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y,0);

        //pipes
        for(int i = 0;i<pipes.size();i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;
            
            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += 0.5;
            }

            if(collision(bird, pipe)) gameOver = true;
        }   

        if(bird.y > boardHeight) gameOver = true;
    }

    public boolean collision(Bird a, Pipe b){
        return  a.x < b.x + b.width &&       // bird's top left corner doesn't reach pipe's top right corner
                a.x + a.width > b.x &&       // bird's top right corner passes pipe's top left corner
                a.y < b.y + b.height &&      // bird's top left corner doesn't reach pipe's bottom left corner
                a.y + a.height > b.y;       // bird's bottom left corner passes pipe's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();

        if(gameOver){
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9; 
        }

        if(gameOver){
            bird.y = birdy;
            velocityY = 0;
            pipes.clear();
            score = 0;
            gameOver = false;
            gameLoop.start();
            placePipesTimer.start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
