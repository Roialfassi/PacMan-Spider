package Model.pacman;

import Controller.Game;
import Controller.Music;
import Controller.pacman.Maze;
import Model.Config;
import Model.Question;
import View.Dot;
import javafx.animation.Animation;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import static Controller.Game.getScoreUpdate;

/**
 * @see <a href="http://www.javafxgame.com">http://www.javafxgame.com</a>
 * @author NBA Live
 * @author NBA 2k
 */
public class PacMan extends MovingObject implements MovingInterface {
//public class PacMan extends CustomNode, MovingObject {

 /**
  * The number of dots eaten.
  */
  public int dotEatenCount;
//  public var dotEatenCount : Integer = 0;

 /**
  * Score of the game.
  */
  public SimpleIntegerProperty score;
//  public var scores: Integer = 0;

  public Question questionToUser = null; 
  public ImageView currentPacmanImage; 
 /**
  * Angles of rotating the images.
  */
  private static final int[] ROTATION_DEGREE = new int[] {0, 90, 180, 270};
//  def rotationDegree = [0, 90, 180, 270];

 /**
  * Buffer to keep the keyboard input.
  */
  private int keyboardBuffer;
//  var keyboardBuffer: Integer = -1;

 /**
  * Current direction of Pac-Man.
  */
  private final SimpleIntegerProperty currentDirection;
//  var currentDirection: Integer = MOVE_LEFT;

 /**
  * Constructor.
  *
  * @param maze
  * @param x
  * @param y
  */
  @SuppressWarnings({ "unchecked" })
public PacMan(Maze maze, int x, int y) {

    this.maze = maze;
    this.x = x;
    this.y = y;
    
    String color = Config.getPacman_color();
    Image defaultImage = new Image(getClass().getResourceAsStream("/Resources/pacman/images/Character/left1_"+color+".png"));
    images = new Image[] {defaultImage,
      new Image(getClass().getResourceAsStream("/Resources/pacman/images/Character/left2_"+color+".png")),
      defaultImage,
      new Image(getClass().getResourceAsStream("/Resources/pacman/images/Character/round_"+color+".png"))
    };

    dotEatenCount = 0;
    score = new SimpleIntegerProperty(0);
    currentDirection = new SimpleIntegerProperty(MOVE_LEFT);

//  postinit {
//    imageX = MazeData.calcGridX(x);
//    imageY = MazeData.calcGridX(y);
//
//    xDirection = -1;
//    yDirection = 0;
//
//  }
    imageX = new SimpleIntegerProperty(MazeData.calcGridX(x));
    imageY = new SimpleIntegerProperty(MazeData.calcGridX(y));

    xDirection = -1;
    yDirection = 0;

  // GUI image of the man
//  var PACMAN_IMAGE : ImageView = ImageView {
//    x: bind imageX - 13
//    y: bind imageY - 13
//    image: bind images[currentImage]
//  //  cache: true
//    transforms: Rotate {
//      angle: bind rotationDegree[currentDirection]
//      pivotX: bind imageX
//      pivotY: bind imageY
//      }
//   }
    ImageView pacmanImage = new ImageView(defaultImage);
    pacmanImage.xProperty().bind(imageX.add(-13));
    pacmanImage.yProperty().bind(imageY.add(-13));
    pacmanImage.imageProperty().bind(imageBinding);
//    pacmanImage.setCache(true);
    IntegerBinding rotationBinding = new IntegerBinding() {

      {
        super.bind(currentDirection);
      }

      @Override
      protected int computeValue() {
        return ROTATION_DEGREE[currentDirection.get()];
      }
    };
    pacmanImage.rotateProperty().bind(rotationBinding);

    keyboardBuffer = -1;

    getChildren().add(pacmanImage); // patweb
    this.currentPacmanImage = pacmanImage;
  }
  
  /**
   * set pacman images
   */
  @SuppressWarnings("unchecked")
public void setPacmanColor()
  {
	  String color = Config.getPacman_color();
	    Image defaultImage = new Image(getClass().getResourceAsStream("/Resources/pacman/images/Character/left1_"+color+".png"));
	    images = new Image[] {defaultImage,
	      new Image(getClass().getResourceAsStream("/Resources/pacman/images/Character/left2_"+color+".png")),
	      defaultImage,
	      new Image(getClass().getResourceAsStream("/Resources/pacman/images/Character/round_"+color+".png"))
	   };
	    ImageView pacmanImage = new ImageView(defaultImage);
	    pacmanImage.xProperty().bind(imageX.add(-13));
	    pacmanImage.yProperty().bind(imageY.add(-13));
	    pacmanImage.imageProperty().bind(imageBinding);
	    maze.dyingPacMan.setFill(Color.valueOf(Config.getPacman_color()));
//	    pacmanImage.setCache(true);
	    IntegerBinding rotationBinding = new IntegerBinding() {

	      {
	        super.bind(currentDirection);
	      }

	      @Override
	      protected int computeValue() {
	        return ROTATION_DEGREE[currentDirection.get()];
	      }
	    };
	    pacmanImage.rotateProperty().bind(rotationBinding);
	    getChildren().remove(currentPacmanImage);
	    getChildren().add(pacmanImage); // patweb
	    this.currentPacmanImage = pacmanImage;
  }

 /**
  * moving horizontally.
  */
  public void moveHorizontally() {

    moveCounter++;

    if (moveCounter < ANIMATION_STEP) {
      imageX.set(imageX.get() + (xDirection * MOVE_SPEED));
//      imageX += xDirection * MOVE_SPEED;
    }
    else {
      moveCounter = 0;
      x += xDirection;

      imageX.set(MazeData.calcGridX(x));
//      imageX = MazeData.calcGridX(x);

      // the X coordinate of the next point in the grid
      int nextX = xDirection + x;

      if ( (y == 14) && ( nextX <= 1 || nextX >= 28) ) {
        if ( (nextX < -1) && (xDirection < 0) ) {
          x = MazeData.GRID_SIZE_X;
          imageX.set(MazeData.calcGridX(x));
//          imageX = MazeData.calcGridX(x);
        }
        else {
          if ( (nextX > 35) && (xDirection > 0) ) {
            x = 0;
            imageX.set(MazeData.calcGridX(x));
//            imageX = MazeData.calcGridX(x);
          }
        }
      }
      else // check if the character hits a wall
      if (MazeData.getData(nextX, y) == MazeData.BLOCK) {
        state = STOPPED;
      }
    }
  }

 /**
  * moving vertically.
  */
  public void moveVertically() {

    moveCounter++;

    if (moveCounter < ANIMATION_STEP) {
      imageY.set(imageY.get() + (yDirection * MOVE_SPEED));
//      imageY += yDirection * MOVE_SPEED;
    }
    else {
      moveCounter = 0;
      y += yDirection;
      imageY.set(MazeData.calcGridX(y));
//      imageY = MazeData.calcGridX(y);

      // the Y coordinate of the next point in the grid
      int nextY = yDirection + y;

      // check if the character hits a wall
      if (MazeData.getData(x, nextY) == MazeData.BLOCK) {
        state = STOPPED;
      }
    }
  }

 /**
  * Turn Pac-Man to the right.
  */
  private void moveRight() {

    if (currentDirection.get() == MOVE_RIGHT) {
//    if ( currentDirection == MOVE_RIGHT )  return;
        return;
    }

    int nextX = x + 1;

    if (nextX >= MazeData.GRID_SIZE_X) {
      return;
    }

    if (MazeData.getData(nextX, y) == MazeData.BLOCK) {
      return;
    }

    xDirection = 1;
    yDirection = 0;

    keyboardBuffer = -1;
    currentDirection.set(MOVE_RIGHT);
//    currentDirection = MOVE_RIGHT;

    state = MOVING;
  }

 /**
  * Turn Pac-Man to the left.
  */
  private void moveLeft() {

    if (currentDirection.get() == MOVE_LEFT) {
//    if ( currentDirection == MOVE_LEFT )   return;
        return;
    }

    int nextX = x - 1;

    if (nextX <= 1) {
      return;
    }

    if (MazeData.getData(nextX, y) == MazeData.BLOCK) {
      return;
    }

    xDirection = -1;
    yDirection = 0;

    keyboardBuffer = -1;
    currentDirection.set(MOVE_LEFT);
//    currentDirection = MOVE_LEFT;

    state = MOVING;
  }

 /**
  * Turn Pac-Man up.
  */
  private void moveUp() {

    if (currentDirection.get() == MOVE_UP) {
//    if ( currentDirection == MOVE_UP )  return;
      return;
    }

    int nextY = y - 1;

    if (nextY <= 1) {
      return;
    }

    if (MazeData.getData(x,nextY) == MazeData.BLOCK) {
      return;
    }

    xDirection = 0;
    yDirection = -1;

    keyboardBuffer = -1;
    currentDirection.set(MOVE_UP);
//    currentDirection = MOVE_UP;

    state = MOVING;
  }

 /**
  * Turn Pac-Man down.
  */
  private void moveDown() {

    if (currentDirection.get() == MOVE_DOWN) {
//    if ( currentDirection == MOVE_DOWN ) return;
        return;
    }

    int nextY = y + 1;

    if (nextY >= MazeData.GRID_SIZE_Y) {
      return;
    }

    if (MazeData.getData(x, nextY) == MazeData.BLOCK) {
      return;
    }

    xDirection = 0;
    yDirection = 1;

    keyboardBuffer = -1;
    currentDirection.set(MOVE_DOWN);
//    currentDirection = MOVE_DOWN;

    state = MOVING;
  }

 /**
  * Handle keyboard input.
  */
  private void handleKeyboardInput() {

    if (keyboardBuffer < 0) {
      return;
    }

    if (keyboardBuffer == MOVE_LEFT) {
      moveLeft();
    } else if (keyboardBuffer == MOVE_RIGHT) {
      moveRight();
    } else if (keyboardBuffer == MOVE_UP) {
      moveUp();
    } else if (keyboardBuffer == MOVE_DOWN) {
      moveDown();
    }

  }


  public void setKeyboardBuffer(int k) {
    keyboardBuffer = k;
  }

 /**
  * Update score if a dot is eaten.
  */
  private void updateScore() {
      Music gameMusic = Music.getInstance();
      if ( y != 14 || ( x > 0 && x < MazeData.GRID_SIZE_X ) ) {
          Dot dot = (Dot) MazeData.getDot(x, y);

          if ( dot != null && dot.isVisible() ) {
              score.set(score.get() + 1);
//        score += 10;
              dot.setVisible(false);
              dotEatenCount++;

              if (score.get() >= 10000) {
//        if ( score >= 10000 ) {
                  maze.addLife();
              }
              //eating money
              if(dot.dotType == MazeData.silver_Dot){
                  //set score
            	  score.set(score.get()+Config.getSilver_dot_value());
            	  //music for silver dot
                  maze.moneyCandy.setText(String.valueOf(Config.getSilver_dot_value()));
                  View.ScoreText st =maze.moneyCandy;
                  st.setX(this.imageX.getValue() -10 );
                  st.setY((this.imageY.getValue()));
                  st.showText();
                  gameMusic.playsilverdot();
                  dot.setVisible(false);
                  dotEatenCount++;
              }
              //eating poison
              if (dot.dotType == MazeData.poison_Dot) {
                  maze.pacManEatsPoison();
                  //music for pacman death
                  gameMusic.playPoisonDot();
                  dot.setVisible(false);

              }
              //eating candy question
              if (dot.dotType == MazeData.question_Dot) {
                  //maze.ad
                  //music for question dot
                  gameMusic.questionCandy();
            	  questionToUser = Game.getRandomQuestion(maze.getNumoffield());
                  maze.startQuestionGhosts();
                  dot.setVisible(false);
              }


              // check if the player wins and should start a new level
              if (dotEatenCount >= MazeData.getDotTotal()) {
                  //maze.startNewLevel();
                  score.set(score.get()+100);
                  try {
                	  Game.stopAllGames(true);
                  }
                  catch(Exception ex){
                      maze.pauseGame();
                  }
              }
          }
      }
  }


  public void hide() {
    setVisible(false);
    timeline.stop();
  }

 /**
  * Handle animation of one tick.
  */
  @Override
  public void moveOneStep() {
//      System.out.println(keyboardBuffer);
//  public override function moveOneStep() {
    if (maze.gamePaused.get()) {

//      if ( timeline.paused == false ) {
      if (timeline.getStatus() != Animation.Status.PAUSED) {
        timeline.pause();
      }

      return;
    }

    // handle keyboard input only when Pac-Man is at a point on the grid
    if (currentImage.get() == 0) {
//    if ( currentImage == 0 ) {
      handleKeyboardInput();
    }

    if (state == MOVING) {

      if (xDirection != 0) {
        moveHorizontally();
      }

      if (yDirection != 0) {
        moveVertically();
      }

      // switch to the image of the next frame
      if (currentImage.get() < ANIMATION_STEP - 1) {
//      if ( currentImage < ANIMATION_STEP - 1  ) {
        currentImage.set(currentImage.get() + 1);
//        currentImage++;
      }
      else {
        currentImage.set(0);
//        currentImage=0;
        updateScore();
//          Game.setUScore( maze.numOfField,  score); for the controller
          getScoreUpdate(maze.numOfField  , score.get());
      }

    }

    maze.pacManMeetsGhosts();
  }

 /**
  * Place Pac-Man at the startup position for a new game.
  */
  public void resetStatus() {
    state = MOVING;
    currentDirection.set(MOVE_LEFT);
//    currentDirection = MOVE_LEFT;
    xDirection = -1;
    yDirection = 0;

    keyboardBuffer = -1;
    currentImage.set(0);
//    currentImage = 0;
    moveCounter = 0;

    x = 15;
    y = 24;

    imageX.set(MazeData.calcGridX(x));
    imageY.set(MazeData.calcGridY(y));
//    imageX = MazeData.calcGridX(x);
//    imageY = MazeData.calcGridY(y);

    setVisible(true); // patweb: Added because Pac-Man is invisible at start of new life.
    start();
  }

}
