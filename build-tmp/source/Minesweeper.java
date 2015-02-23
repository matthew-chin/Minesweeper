import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.bezier.guido.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Minesweeper extends PApplet {




//Declare and initialize NUM_ROWS and NUM_COLS = 20
private MSButton[][] buttons; //2d array of minesweeper buttons
private ArrayList <MSButton> bombs = new ArrayList <MSButton>(); //ArrayList of just the minesweeper buttons that are mined
public final static int NUM_ROWS = 20;
public final static int NUM_COLS = 20;
public final static int BOMB_NUM = 50;
private int markedBombs = 0;

public void setup ()
{
    size(400, 400);
    textAlign(CENTER,CENTER);
    
    // make the manager
    Interactive.make( this );
    buttons = new MSButton[NUM_COLS][NUM_ROWS];
    for(int y = 0; y < NUM_ROWS; y++)
    {
        for(int x = 0; x <NUM_COLS; x++)
        {
            buttons[y][x] = new MSButton(y,x);
        }
    }

    //declare and initialize buttons
    setBombs();
}
public void setBombs()
{
    for(int i = 0; i < BOMB_NUM; i++)
    {
        int y = (int)(Math.random()*20);
        int x = (int)(Math.random()*20);
        if(!bombs.contains(buttons[y][x]))
        {
            bombs.add(buttons[y][x]);
            print(y + ", ");
            println(x);
        }
        else{i--;}
    }
}

public void draw ()
{
    background( 0 );
    if(isWon())
        displayWinningMessage();
}
public boolean isWon()
{
    int count = 0;
    for(int i = 0; i < bombs.size(); i++)
    {
        if(bombs.get(i).isMarked())
        {
            count++;
            if(count == BOMB_NUM && markedBombs == BOMB_NUM)
            {
                return true;
            }
        }
    }
    return false;
}
public void displayLosingMessage()
{
    for(int i = 0; i < bombs.size(); i++)
    {
        bombs.get(i).mousePressed();
    }
    buttons[8][7].setLabel("L");
    buttons[8][8].setLabel("O");
    buttons[8][9].setLabel("S");
    buttons[8][10].setLabel("E");
    buttons[8][11].setLabel("R");
    buttons[8][12].setLabel("!");
}
public void displayWinningMessage()
{
    buttons[8][6].setLabel("W");
    buttons[8][7].setLabel("I");
    buttons[8][8].setLabel("N");
    buttons[8][9].setLabel("N");
    buttons[8][10].setLabel("E");
    buttons[8][11].setLabel("R");
    buttons[8][12].setLabel("!");
    buttons[8][13].setLabel("!");
}

public class MSButton
{
    private int r, c;
    private float x,y, width, height;
    private boolean clicked, marked;
    private String label;
    
    public MSButton ( int rr, int cc )
    {
        width = 400/NUM_COLS;
        height = 400/NUM_ROWS;
        r = rr;
        c = cc; 
        x = c*width;
        y = r*height;
        label = "";
        marked = clicked = false;
        Interactive.add( this ); // register it with the manager
    }
    public boolean isMarked()
    {
        return marked;
    }
    public boolean isClicked()
    {
        return clicked;
    }
    // called by manager
    public void setLabel(String newLabel)
    {
        label = newLabel;
    }
    public void mousePressed () 
    {

     if(clicked == false)
     {
        clicked = true;

        if(mouseButton == LEFT)
        {
            if(bombs.contains(this))
            {
                displayLosingMessage();
            }
            else if(countBombs(r,c) > 0)
            {
                if(label == "")
                {
                    label = label + (countBombs(r,c));
                    println("label: "+label);
                }
            }
            else 
            {
                for(int i = -1; i < 2; i++)
                {
                    for(int m = -1; m < 2; m++)
                    {
                        if(isValid(r+i,c+m))
                        {
                            if(buttons[r+i][c+m].isClicked() == false)
                            {
                                buttons[r+i][c+m].mousePressed();
                            }
                        }
                    }
                }


            }
        }
    }
    if(mouseButton == RIGHT)
    {
        marked = !marked;
        if(marked){markedBombs++;}
        else{markedBombs--; clicked = false;}
    }
}


public void draw () 
{    
    if (marked)
        fill(0);
    else if( clicked && bombs.contains(this) ) 
     fill(255,0,0);
 else if(clicked)
    fill( 200 );
else 
    fill( 100 );

rect(x, y, width, height);
fill(0);
text(label,x+width/2,y+height/2);
}

public boolean isValid(int r, int c)
{
    if((0 <= r && r < NUM_ROWS) && (0 <= c && c < NUM_COLS))
    {
        return true;
    }
    return false;
}
public int countBombs(int row, int col)
{
    int numBombs = 0;
    for(int i = -1; i < 2; i++)
    {
        for(int j = -1; j < 2; j++)
        {
            if(isValid(row+i,col+j))
            {
                if(bombs.contains(buttons[row+i][col+j]))
                {
                    numBombs++;
                }
            }
        }
    }
    return numBombs;
}
}



  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Minesweeper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
