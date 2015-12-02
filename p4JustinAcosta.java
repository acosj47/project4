// by Justin Acosta
// CST 112 EVE
// Project 4 

//Globals
float top, bottom, left, right, middle;  // edges and middle of felt
int score;                               // integer for the games score  
int count = 0;                           // count for animation
boolean wall, rat, bird;                 // on and off for wall, rat and bird. Defined in reset()
boolean cue;                             // controlls cue ball movement. Defined in reset()
boolean showListXY, showClosest;
Cloud[] nimbus;                          //Array of clouds called nimbus
Ball[] acos;                             // array of ball objects
Button[] just;                           // array of button objects
float [] ballX, ballY, distance;         // array of ball coordinates and distance from cue
int [] ballNumber;                       // array of ball numbers
// Objects
Pool table;     // pool table object
Rodent mouse;  // Rodent object
Grass g;       // Grass object
Cloud c;       // Cloud object
Bird eagle;    // Bird object

void setup() {
  size(1200, 900); 
  reset();
  fillArray();
  // Button array
  just = new Button[7];
  float buttonX1 = width*4/100, buttonY1 = height*6/100; 
  float buttonX2 = width*15/100, buttonY2 = height*15/100;
  for (int i = 0; i < just.length; i++) {
    just[i] = new Button(buttonX1, buttonY1, buttonX2, buttonY2, color(255), " ");
    buttonX1 += 150;
    buttonX2 += 150;
  }
  //Cloud array
  nimbus = new Cloud[7];  
  float cloudX = 0;       // starting postion of cloud
  for (int i=0; i < nimbus.length; i++) {  // loop to fill array nimbus
    nimbus[i] = new Cloud( cloudX, height/4, 30, 30); // nimbus is filled with cloud objects
    cloudX += 100;  // distance between clouds
  }
}
// Starting positions.  Includes ball array
void reset() {
  score = 0;
  showClosest = false;
  showListXY = false;
  wall = true;              // wall exists on start and reset
  rat = false;              // rat does not start on screen
  bird = false;             // bird does not start on screen
  cue = false;              // cue does not move at start, cue moves when true 
  top= height*34/100;       // top edge of felt     
  bottom= height*88/100;    // bottom edge of felt
  left= width*8/100;        // left edge of felt
  right=width*89/100;       // right edge of felt
  middle= left + (right-left)/2;  //center of felt
  // Rodent and Bird objects
  mouse = new Rodent(0, height*82/100, color(150));               
  eagle = new Bird(100, height*25/100, 80);
  // Ball  Object Array
  acos= new Ball[16];
  acos[0] = new Ball(0);  // cue ball
  for (int i = 1; i < acos.length; i++) {  // non-cue balls 1-15
    acos[i] = new Ball(i);
  }
}
// fill arrays for lists - ball x and y, distance from cue
void fillArray() {
  // arrays for ball x & y coordinates, and ball number array
  ballX = new float [15];
  ballY = new float [15];
  ballNumber = new int [15];
  for (int i = 1; i < acos.length; i++) {
    for (int j = i - 1; j < ballX.length; j++) {
      for (int k = i - 1; k < ballNumber.length; k++) {
        ballX[j] = acos[i].x;
        ballY[j] = acos[i].y;
        ballNumber[k] = acos[i].number;
      }
    }
  }
  // array filled with distance from cue
  distance= new float [15];
  for (int i = 1; i < acos.length; i++) {
    for (int j = i -1; j < distance.length; j++) {
      distance[j]= dist(acos[0].cueX, acos[0].cueY, acos[i].x, acos[i].y);
    }
  }
}

void draw() {
  background(0, 100, 150);
  count += 1;
  scene();
  balls();
  action();
  info();
  showButtons();
  displayList();
  displayClosest();
}
// Display the ball closest to cue when button is clicked & draw a white ring around it
void displayClosest() {
  if (showClosest == true) {  // becomes true when button 'just[4]' is clicked
    fill(0);
    rect(width/2-450, top - 60, width/2-90, top);
    // display the # of ball with shortest distance, apply its color to text  
    int ballNumber;
    ballNumber = closeCue(distance, distance.length);
    fill(acos[ballNumber].r, acos[ballNumber].g, acos[ballNumber].b );
    textSize(20);
    text("The ball closest to the Cue is " +ballNumber, width/2 - 400, top - 20);
    textSize(12);
    acos[ballNumber].ring();  // draw ring around cloest ball
  }
}

// Find the shortest distance in the distance array and its and ball #
int  closeCue(float a[], int m) {
  float close; // the shorest value
  int ballNumber = 0;   // ball # cloeset to cue
  close = a[0];
  for (int i = 0; i < m; i++) {
    if (a[i] <= close) {    // Note!!- if you only put <, you wont get ball # 1
      close= a[i];
      ballNumber = i + 1; // the # of the ball with the shortest distance
    }
  }
  return ballNumber;
}

// show values of a float array in a list
void showList(float a[], int m, float x, float y) {
  for (int i = 0; i < m; i++) {
    fill(0);
    text(a[i], x, y);
    y += 30;
  }
}
// show values of an integer array in a list
void showNumbers(int a[], int m, float x, float y) {
  for (int i = 0; i < m; i++) {
    fill(0);
    text(a[i], x, y);
    y += 30;
  }
}
// list the x,y of each non-cue ball, and their distance from cue
void displayList() {
  if (showListXY == true) {
    float w = width/2 + 150;
    float h = top + 50;
    float cx = acos[0].cueX;  // the x of cue ball
    float cy = acos[0].cueY;  // y of cue ball
    // column lables
    fill(255);
    text("#", w-100, h-60);
    text("X", w - 50, h-60);
    text("Y", w + 50, h-60);
    text("Distance from Cue", w + 150, h-60);

    // text that displays cue x and y in real time
    fill(0);
    text(0, w-100, h -30);
    text(cx, w-50, h-30);
    text(cy, w + 50, h-30);
    // show lists of ball #, x ,y coordinates & distance from cue
    showNumbers(ballNumber, ballNumber.length, width/2 +50, top + 50);  // Ball #
    showList(ballX, ballX.length, width/2 + 100, top + 50);           // x coordinates
    showList(ballY, ballY.length, width/2 + 200, top + 50);           // y coordinates
    showList(distance, distance.length, width/2 + 300, top + 50);     // distance from cue
  }
}

// find index of largest value in array
int whereBig( float a[], int m ) {
  int k=0;

  for (int i=0; i<m; i++) {
    if ( a[i] > a[k] ) {
      k=  i;
    }
  }

  return k;
}

// sort an array from low to high
void srt( float a[], int many ) {
  int m = many;
  int j;
  int k;
  for (m=many; m > 1; m = m-1) {
    k = whereBig(a, m );
    j = m-1;
    swap(a, j, k);
  }
}

// swap values in array
void swap( float a[], int j, int k) {
  float tmp;
  tmp = a[k];
  a[k] = a[j];
  a[j] = tmp;
}

// Shows the scene, table, grass, clouds, rat and bird
void scene() {
  table = new Pool();  // table is a new Pool object
  g = new Grass();     // g is a Grass object
  table.show();        // show table
  g.show();            // show grass
  mouse.show();        // show rat
  mouse.move();        // move rat
  // loop that goes through the nimbus array
  for (int i=0; i < 7; i++) { nimbus[i].show();}    // show clouds
  eagle.show();        // show bird
  eagle.move();        // move bird
}

// show, move, and bounce balls
void balls() {
  acos[0].showCue(); 
  acos[0].moveCue();
  acos[0].bounceCue();
  for (int i = 1; i < acos.length; i++) {
    acos[i].show();
    acos[i].move();
    acos[i].bounce();
  }
}

// contains all collison checks for cueball, balls and rat
void action() {
  // if cue is hit it will move
  for (int i = 1; i < acos.length; i++) {
    if (dist(acos[0].cueX, acos[0].cueY, acos[i].x, acos[i].y) < 30) {
      cue = true;
    }
  }
  // collisions between cue and balls
  for (int i = 1; i < acos.length; i++) {
    cueHit(acos[0], acos[i]);
  }

  // collisions between non cue balls
  for (int i = 1; i < acos.length; i++) {
    for (int j = i + 1; j < acos.length; j++) {
      collisions(acos[i], acos[j]);
    }
  }
  // collisions with rat and balls
  for (int i = 0; i < acos.length; i++) {
    ratHit(mouse, acos[i]);
  }
}

//make ball objects collide
void collisions(Ball p, Ball q) {
  float tmp;
  if (dist(p.x, p.y, q.x, q.y) < 30) {
    tmp = p.dx;    p.dx = q.dx;    q.dx = tmp;
    tmp = p.dy;    p.dy = q.dy;    q.dy = tmp;
    {p.x += p.dx;}  // stop them from sticking together
    {p.y += p.dy;}
    score += 1;
  }
}
// make cueball object collide
void cueHit(Ball p, Ball q) {
  float tmp;
  if (dist(acos[0].cueX, acos[0].cueY, q.x, q.y) < 30) {
    tmp = p.cueDX;    p.cueDX = q.dx;    q.dx = tmp;
    tmp = p.cueDY;    p.cueDY = q.dy;    q.dy = tmp;
    {p.cueX += p.cueDX;}  // stop them from sticking together
    {p.cueY += p.cueDY;}
    score += 1;
  }
}
// rat hits balls, stoping them, rat bounces off balls randomly
void ratHit(Rodent p, Ball q) {
  if (dist(mouse.x, mouse.y, q.x, q.y) < 30) {
    p.dx = random(-6, 6);   // Rat goes in random directions when it hits a ball
    p.dy = random(-6, 6);
    q.dx = 0;              // Ball stops moving when hit by rat
    q.dy = 0;
    score -= 10;           // deduct 10 points when rat hits ball
  }
}

// shows buttons and their text
void showButtons() {
  //Locals for Button text   
  String clear= "Reset               (r)";  // buttonReset text
  String toggle= "Wall               (w)";  // buttonWall text
  String rodent= "Rat                (m)";  // buttonRat text
  String bombs= "Bird (b)      Bomb ( j )";  // bird and bomb button
  String closest = "Closest to the Cue";
  String list = "List Coordinates & Distance";
  String srt = "Sort   Y Coordinates";

  // highlight buttons
  for (int i = 0; i < just.length; i++) { 
    just[i].highlight();
  }

  // button colors, and text   
  just[0].c = color(100, 200, 100);    just[0].z = clear;
  just[1].c = color(150, 0, 255);      just[1].z = toggle;  
  just[2].c = color(150, 150, 150);    just[2].z = rodent;
  just[3].c = color(255, 0, 0);        just[3].z = bombs;
  just[4].c = color(255, 255, 0);      just[4].z = closest;
  just[5].c = color(255, 200, 100);    just[5].z = list;
  just[6].c = color(255, 200, 200);    just[6].z = srt;
  // diplay buttons
  for (int i = 0; i < just.length; i++) {just[i].show();}
}

// Displays name, info, and score
void info() {
  String name = "Justin Acosta";                                      // My name
  String title = "Project 4  CST 112 EVE";                           // Title 
  String click= "Click any ball to reset its position  -10 points!";              // click ball text

  if (score < 0) {score = 0;}    // score cannot be negative

  fill(0);
  textSize(20);
  text(name, width*4/100, height*2/100);       //name 
  text(title, width*4/100, height*5/100);       //title
  text("Score", middle + 150, height*5/100);          //score text
  text(score, middle+ 220, height*5/100);         //score display
  text(click, middle + 150, height*2/100);       // click ball text
  textSize(12);  // reset text size to default
}

// Handles key presses
void keyPressed() {
  if (key == 'w') {wall= !wall;}     // w removes wall
  if (key == 'r') {reset();}         // r resets table
  if (key == 'm') {rat = !rat;}      // m calls rat
  if (key == 'b') {bird = !bird;}    // b calls bird
  if (key == 'j') {eagle.bombDrop();}// j drops bomb from bird
}

void mousePressed() {
  // reset button
  if (mouseButton == LEFT &&          
    mouseX > just[0].x1 && mouseX < just[0].x2 &&
    mouseY > just[0].y1 && mouseY < just[0].y2) { reset();}
  // wall button  
  if (mouseButton == LEFT &&
    mouseX > just[1].x1 && mouseX < just[1].x2 &&
    mouseY > just[1].y1 && mouseY < just[1].y2) { wall = !wall;}
  // rat button  
  if (mouseButton == LEFT &&
    mouseX > just[2].x1 && mouseX < just[2].x2 &&
    mouseY > just[2].y1 && mouseY < just[2].y2) { rat = !rat;}
  // bird button
  if (mouseButton == LEFT &&
    mouseX > just[3].x1 && mouseX < just[3].x2 &&
    mouseY > just[3].y1 && mouseY < just[3].y2) { 
    bird = true; 
    if (bird = true) {
      eagle.bombDrop();
    }
  }
  // closest to cue button  
  if (mouseButton == LEFT &&
    mouseX > just[4].x1 && mouseX < just[4].x2 &&
    mouseY > just[4].y1 && mouseY < just[4].y2) { 
    fillArray();
    showClosest = true;
  }

  // list X, Y and Distance button   
  if (mouseButton == LEFT &&  // button to show listXY
    mouseX > just[5].x1 && mouseX < just[5].x2 &&
    mouseY > just[5].y1 && mouseY < just[5].y2) {
    fillArray();
    showListXY = true;
  }
  // Sort Y button
  if (mouseButton == LEFT &&
    mouseX > just[6].x1 && mouseX < just[6].x2 &&
    mouseY > just[6].y1 && mouseY < just[6].y2) {srt(ballY, ballY.length);}
  
  // click rat
  if (mouseButton == LEFT &&
    dist(mouse.x, mouse.y, mouseX, mouseY) < 50) {
    mouse.x = 0;
    mouse.dx = 6;
    mouse.y = random(top, bottom);
    score += 50;  // add 50 points
  }
  // click ball to reset its position
  for (int i = 1; i < acos.length; i++) {
    if (mouseButton == LEFT &&
      dist(acos[i].x, acos[i].y, mouseX, mouseY) < 40) {
      acos[i].x = random(middle+30, right-30);
      acos[i].y = random(top+30, bottom-30);
      acos[i].dx = random(-6,6);
      acos[i].dy = random(-6,6);
      score -= 10; // deduct 10 points
    }
  }
  // click cue to reset it
  if (mouseButton == LEFT &&
    dist(acos[0].cueX, acos[0].cueY, mouseX, mouseY) < 50) {
    acos[0].cueX =  width/4;
    acos[0].cueY = height*60/100;
    cue = false;  // cue resets at rest
  }
}

// Ball class
class Ball {
  // locals for ball class
  float x, y, dx, dy;   // used for position and speed
  float cueX, cueY, cueDX, cueDY;   // speed of cue ball
  float r, g, b;
  color c;              // used for ball color
  int number;    // used for the number of the ball
  // Ball Constructor
  Ball( int tempNumber ) { 
    // initialize the constructor
    number = tempNumber;
    dx = random(-6, 6);     // balls will all have random speed
    dy = random(-6, 6);
    cueX = width/4;
    cueY = height*60/100;
    cueDX = random(-6, 6);
    cueDY = random(-6, 6);
    randomize();
  }
  // gives random values to position and color
  void randomize() {
    r = random(255);
    g = random(255);
    b = random(255);
    x = random(middle+30, right-30);
    y = random(top+30, bottom-30);
  }

  // Method that shows a ball 
  void show() {
    fill(r, g, b);
    ellipse(x, y, 30, 30);
    fill(0);                  // text for balls is black
    textSize(15);
    text(number, x-7, y+7);   
    textSize(12);
  }
  // show cue ball
  void showCue() {
    fill(255);
    ellipse(cueX, cueY, 30, 30);
  }
  // shows ring around ball
  void ring() {
    noFill();
    strokeWeight(6);
    stroke(255);
    ellipse(x, y, 60, 60);
    stroke(0);
    strokeWeight(0);
  }

  // Move balls
  void move() {
    x += dx;  
    y += dy;
  }
  // bouce balls of walls
  void bounce() {
    if (wall) {  // bounce of wall         
      if (x < middle + 35 || x > right - 15)   dx *= -1; 
      if (y < top + 15 || y > bottom - 15)     dy *= -1;
    } else {  //bounce off left if wall is gone 
      if (x < left + 15  || x > right - 15)   dx *= -1; 
      if (y < top + 15 || y > bottom - 15)    dy *= -1;
    }
    // push balls harder off walls to keep them inside border
    if (wall) { 
      if (x < middle + 35 || x > right - 15) {move(); move();}
      if (y < top + 15 || y > bottom - 15)   {move(); move();}
    } else {
      if (x < left + 15  || x > right - 15) {move();  move();}
      if (y < top + 15 || y > bottom - 15)  {move();  move();}
    }
    // force back to table if ball breaks border
    if (x < left - 15  || x > right + 15) {x = random(middle+30, right-30);}
    if (y < top - 15 || y > bottom + 15) {y = random(top+30, bottom-30);}
  }

  //  Moves cueball
  void moveCue() {
    if (cue == true) {  // cue is at rest at start. if cue is hit by other balls it moves.
      cueX += cueDX;
      cueY += cueDY;
    }
  }
  // bounce cue off walls
  void bounceCue() {
    if (cueX < left + 15  || cueX > right - 15) {cueDX *= -1;}  // bounce cue ball off walls
    if (cueY < top + 15 || cueY > bottom - 15) {cueDY *= -1;}   // cue ignores wall
    // bounce back harder
    if (cueX < left + 15  || cueX > right - 15) {moveCue(); moveCue();}
    if (cueY < top + 15 || cueY > bottom - 15) {moveCue();  moveCue();}
    // force back to table
    if (cueX < left - 15  || cueX > right + 15) {cueX = random(middle+30, right-30);}
    if (cueY < top - 15 || cueY > bottom + 15)  {cueY = random(top+30, bottom-30);}
  }
}

// button class
class Button {
  //locals for button class
  float x1, y1, x2, y2;  // used for position
  color c;               // used for color, each button can have a different color
  float hi; // highlight for buttons
  String z;              // used for button text, each button can have different text
  //Button constructor
  Button(float tempX1, float tempY1, float tempX2, float tempY2, color tempC, String tempZ) {
    x1 = tempX1;     y1 = tempY1;
    x2 = tempX2;     y2 = tempY2;
    c = tempC;       z = tempZ;
  }
  // shows button
  void show() {
    rectMode(CORNERS);
    fill(c, hi);
    rect(x1, y1, x2, y2);
    fill(0);                      // button text is black
    textSize(15);
    text(z, x1 + 35, y1, x2, y2);
    textSize(12);
  }
  // highlight a button when the mouse pointer is over it
  void highlight() {
    for (int i = 0; i < just.length; i++) {
      if (mouseX > just[i].x1 && mouseX < just[i].x2 &&
          mouseY > just[i].y1 && mouseY < just[i].y2) {
         just[i].hi = 255;
      } else {
         just[i].hi = 175;
      }
    }
  }
}

// pool table class
class Pool {
  // shows table
  void show() {
    rectMode(CORNERS);  // rect mode is corners
    fill(#432805);
    rect( width*4/100, height*29/100, width*93/100, height*93/100);     //boarder
    fill(100, 200, 100);
    rect(left, top, right, bottom);            //felt

    strokeWeight(20);                          //stroke weight of wall
    stroke(150, 0, 255);                       // wall color
    if (wall) {                                  //wall
      line(middle, top + 6, middle, bottom - 6);
      fill(0);
      textSize(20);
      text("W", middle - 7, height/2);         // wall text
      text("A", middle - 7, height/2 + 30);
      text("L", middle - 7, height/2 + 60);
      text("L", middle - 7, height/2 + 90);
      textSize(12);
    }
    strokeWeight(1);                           //reset stroke weight and color
    stroke(0);
  }
}

// rodent class
class Rodent {
  float x, y, w, h;
  float dx = 6, dy = 0;
  color c;
  Rodent(float tempX, float tempY, color tempC) {
    x = tempX;    
    y = tempY;       
    c = tempC;
  }
  // shows rat
  void show() {
    if (rat == true) {
      fill(c);
      ellipse(x, y, 50, 30);     //body
      fill(255, 200, 200);
      ellipse(x + 30, y - 15, 30, 10);  //nose
      fill(c);
      ellipse(x + 20, y - 20, 10, 30);  // ear
      ellipse(x + 30, y - 20, 10, 30);  //ear
      fill(255, 200, 200);
      ellipse(x + 20, y - 20, 5, 20);  // inner ear
      ellipse(x + 30, y - 20, 5, 20);  // inner ear
      fill(c);
      ellipse(x + 25, y - 15, 30, 20);  // head
      stroke(0);
      strokeWeight(4);
      point(x + 23, y - 18);   //eye
      point(x + 33, y - 18);   //eye
      stroke(255, 200, 200);
      line(x - 20, y - 10, x - 30, y - 30);   // tail
      stroke(0); // leg color
      if (count/30 % 2 == 0) {           //leg animation
        line(x - 15, y + 10, x - 10, y + 25);    //legs
        line(x + 15, y + 10, x + 10, y + 25);
      } else {
        line(x - 15, y + 10, x - 25, y + 25);    //legs
        line(x + 15, y + 10, x + 25, y + 25);
      }
    }
    strokeWeight(1);
    stroke(0);
  }

  void move() {
    if (rat == true) { // rat only moves when on screen
      x += dx; 
      y += dy;
    } else {
      x = 0; // if rat is off screen its x position returns to 0
    }
    if (x > width || x < 0) {dx *=  -1;}    // bounce rat off  screen
    if (y > bottom  || y < top) { dy *= -1;}  // bounce rat off top and bottom of pool table
  }
}

// grass class
class Grass {
  float y1 = height*90/100, y2 = height;  // grass y position
  void show() {
    for ( float x = 0; x < width; x += 30) {  //loop that makes grass until it hits the width
      strokeWeight(5);
      stroke(0, 100, 0);
      line(x, y1, x, y2);
    }
    strokeWeight(1); // reset stroke and stroke weight
    stroke(0);
  }
}

// cloud class
class Cloud {
  float x, y, w, h; 
  // cloud constructor
  Cloud(float tempX, float tempY, float tempW, float tempH) {
    x = tempX;    
    y = tempY;
    w = tempW;    
    h = tempH;
  }
  // shape of cloud
  void show() {
    noStroke();
    fill(255);
    ellipse(x, y, w, h);
    ellipse(x, y - 25, w, h);
    ellipse(x + 25, y, w, h);
    ellipse(x + 25, y -25, w, h);
    ellipse(x + 12, y - 15, w+50, h);
    stroke(0);
    x = ( x + random( 3 ) ) % width;   // move clouds randomly
  }
}
//Bird class.  Birds can drop bombs
class Bird {
  float x, y, dx = 3, dy = random(-.5, .5); // position and speed of bird
  float w; // width of bird
  float bombY = 0, bombDY = 0;  // position and speed of bombs
  float drop = 9.81 / 60;       // rate of bomb drop
  // bird constructor
  Bird(float tempX, float tempY, float tempW) {
    x = tempX;    
    y = tempY;    
    w = tempW;
  }
  void show() {
    if (bird == true) {
      fill(255, 0, 0);
      triangle(x, y, x - w, y - 20, x - w, y +10);  // bird body
      if (count /30 % 2 == 0) {   // wing animation
        fill(255, 0, 0);
        triangle(x - 40, y - 5, x - 60, y + 40, x - 60, y - 5);   // wing down
      } else {
        fill(255);
        triangle(x - 40, y - 5, x - 60, y - 50, x - 60, y - 5);   // wing up
      }
    }
    // show bomb
    if (bombDY > 0) {  // bomb shows only when it moves
      if (count / 15 % 2 == 0) { // bomb flash
        fill(0);         //  flash red
      } else {
        fill(#FFB905);           //  flash orange
      }
      ellipse(x - 30, bombY, 30, 50); // bomb shape
    }
  }
  // moves bird
  void move() {
    if (bird == true) {  // bird only moves when on screen
      x += dx; 
      y += dy;
    } else {
      x = 0;        // bird x position resets to 0 when off screen
    }
    if (x > width + 200) { x = 0;}  //  bird comes back if it goes horizontaly off screen
    if (y < height*20/100 || y > top - 30) { dy *= -1;} // bird bounces up and down

    if (bombDY > 0 ) {   
      bombDY += drop;  // increase bomb speed
      bombY += bombDY; // makes the bomb move down
      if (bombY > height) {  // if the bomb goes off screen, it stops moving, which makes it disapear
        bombY = 0;
        bombDY = 0;
      }
    }
  }

  // makes the bomb drop
  void bombDrop() {
    bombY = y + 10;  //  bomb postion is just below bird
    bombDY = drop;   //  gives the bombDY a value, which makes the bomb appear.
  }
}
