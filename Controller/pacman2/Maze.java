package Controller.pacman2;

import Controller.Music;
import Model.Config;
//import javafx.scene.CustomNode;
import Model.pacman2.*;
import View.Dot;
import View.ScoreText;
import View.courseDot;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import Controller.Game;


/**
 * @author Tony Yayo
 * @author 50 Cent
 */

public class Maze extends Parent implements NewGame {
//public class Maze extends CustomNode {

  public static final boolean DEBUG = true;
  public int numOfField=1;
  public boolean isquestionon = false;
  private int nmofquestionchildrenadded =0;
  //  override var onMouseClicked = function ( e:MouseEvent) {
//    requestFocus();
//  }
//interval for the timer
  int interval = 40;

  // counter for ghosts eaten
  @SuppressWarnings("unused")
private int ghostEatenCount;
//  var ghostEatenCount : Integer;

  public BooleanProperty gamePaused;
//  public var gamePaused: Boolean = false;

  public ScoreText moneyCandy =  new ScoreText(String.valueOf(Config.getSilver_dot_value()), false);


  private static ScoreText[] questionRight = {
          new ScoreText("100", false) //{ level 1
          ,
          new ScoreText("200", false) //{ level 2
          ,
          new ScoreText("500", false) //level 3
  };

  // Pac-Man Character
  public PacMan pacMan;
  //  public var pacMan : PacMan = PacMan{ maze:this x:15 y:18 } ;
//יצירת �?ער�? ש�? רוחות רפ�?י�? ש�? ש�?�?ות
  public final Ghost[] ghosts;
//  public var ghosts = [ghostBlinky, ghostPinky, ghostInky, ghostClyde];

  //יצירת �?ער�? ש�? רוחות רפ�?י�? ש�? ש�?�?ות
  public final Ghost[] questionGhosts;

  public DyingPacMan dyingPacMan;
//  public var dyingPacMan =
//    DyingPacMan {
//      maze: this
//      centerX: 0
//      centerY: 0
//      radiusX: 13
//      radiusY: 13
//      startAngle: 90
//      length: 360
//      type: ArcType.ROUND
//      fill: Color.YELLOW
//      visible: false
//   } ;

  //יצירת ת�?ונות ש�? נקודות
  //ת�?ונה ש�? רע�?
  public static final Image poison_dot_image = new Image(Maze.class.getResourceAsStream("/Resources/pacman/poisonDot.png"));
    //ת�?ונה כסף
  public static final Image silver_dot_image = new Image(Maze.class.getResourceAsStream("/Resources/pacman/silverDot.png"));
  //ת�?ונה ש�?�?ה
  public static final Image question_dot_image = new Image(Maze.class.getResourceAsStream("/Resources/pacman/questionDot.png"));
  // the Pac-Man image
  private static final String color = Config.getPacman_color();
  private static final Image PACMAN_IMAGE = new Image(Maze.class.getResourceAsStream("/Resources/pacman/Character/left1_"+color+".png"));

  //  var PACMAN_IMAGE = Image {
//    url: "{__DIR__}/Resources/pacman/images/left1.png"
//  }

  // level of the game
  private final SimpleIntegerProperty level;
//  public var level : Integer = 1;

  // flag to add a life to the player the first time score exceeds 10,000
  private boolean addLifeFlag;
//  var addLifeFlag: Boolean = true;

  // current lives of the player
  private final SimpleIntegerProperty livesCount;
//  var livesCount = 2;

  // message to start a game
  public BooleanProperty waitForStart;
//  public var waitForStart: Boolean = true;

  private final Group messageBox;
//  var messageBox = Group {
//    content: [
//      Rectangle {
//        x: Model.pacman2.MazeData.calcGridX(5)
//        width: Model.pacman2.MazeData.GRID_GAP * 19
//        y: Model.pacman2.MazeData.calcGridY(21)
//        height: Model.pacman2.MazeData.GRID_GAP *5
//        stroke: Color.RED
//        strokeWidth: 5
//
//        fill: Color.CYAN
//        opacity: 0.75
//        arcWidth: 25
//        arcHeight: 25
//      },
//      Text {
//        font: Font { size: 18 }
//        x: Model.pacman2.MazeData.calcGridX(6)
//        y: Model.pacman2.MazeData.calcGridY(24)
//
//        content: bind if ( gamePaused ) "PRESS 'P' BUTTON TO RESUME"
//                      else "   PRESS ANY KEY TO START!"
//
//        fill: Color.RED
//      }
//    ]
//  };

  // whether the last finished game is won by the player
  public final BooleanProperty lastGameResult;
// var lastGameResult: Boolean = false;

  // text of game winning
  private final Text gameResultText;
// var gameResultText =
//   Text {
////     cache: true
//     font: Font { size: 20
//                 }
//     x: Model.pacman2.MazeData.calcGridX(11)
//     y: Model.pacman2.MazeData.calcGridY(11)+8
//
//     content: bind if ( lastGameResult )
//                     " YOU WIN "
//                   else
//                     "GAME OVER";
//     fill: Color.RED
//     visible: false;
//   } ;

  public int flashingCount;
// var flashingCount: Integer = 0;

  public final Timeline flashingTimeline;
// var flashingTimeline: Timeline =
//   Timeline {
//     repeatCount: 5
//     keyFrames : [
//       KeyFrame {
//         time : 500ms
//         action: function() {
//           gameResultText.visible = not gameResultText.visible;
//           if ( ++flashingCount == 5) {
//             messageBox.visible = true;
//             waitForStart = true;
//           }
//         }
//       }
//     ]
//   };

  private final Group group;
//  var group : Group =
//  Group {
//    content: [
//      Rectangle {
//        x:0
//        y:0
//        width: Model.pacman2.MazeData.calcGridX(Model.pacman2.MazeData.GRID_SIZE + 2)
//        height: Model.pacman2.MazeData.calcGridY(Model.pacman2.MazeData.GRID_SIZE + 3)
//        fill: Color.BLACK
//        cache: true
//      },
//
//      Model.pacman2.WallRectangle{ x1:0 y1:0 x2:Model.pacman2.MazeData.GRID_SIZE y2:Model.pacman2.MazeData.GRID_SIZE },
//
//      Model.pacman2.WallRectangle { x1:14 y1:-0.5 x2:15 y2:4 },
//      Model.pacman2.WallBlackRectangle { x1:13.8 y1:-1 x2:15.3 y2:0 },
//
//      Model.pacman2.WallRectangle { x1:2 y1:2 x2:5 y2:4 },
//      Model.pacman2.WallRectangle { x1:7 y1:2 x2:12 y2:4 },
//      Model.pacman2.WallRectangle { x1:17 y1:2 x2:22 y2:4 },
//      Model.pacman2.WallRectangle { x1:24 y1:2 x2:27 y2:4 },
//      Model.pacman2.WallRectangle { x1:2 y1:6 x2:5 y2:7 },
//
//      Model.pacman2.WallRectangle { x1:14 y1:6.2 x2:15 y2:10 },
//      Model.pacman2.WallRectangle { x1:10 y1:6 x2:19 y2:7 },
//      Model.pacman2.WallBlackLine { x1:14 y1:7 x2:15 y2:7 },
//
//      Model.pacman2.WallRectangle { x1:7.5 y1:9 x2:12 y2:10 },
//      Model.pacman2.WallRectangle { x1:7 y1:6 x2:8 y2:13 },
//      Model.pacman2.WallBlackLine { x1:8 y1:9 x2:8 y2:10 },
//
//      Model.pacman2.WallRectangle { x1:17 y1:9 x2:21.5 y2:10 },
//      Model.pacman2.WallRectangle { x1:21 y1:6 x2:22 y2:13 },
//      Model.pacman2.WallBlackLine { x1:21 y1:9 x2:21 y2:10 },
//
//      Model.pacman2.WallRectangle { x1:24 y1:6 x2:27 y2:7 },
//
//      Model.pacman2.WallRectangle { x1:-1 y1:9 x2:5 y2:13 },
//      Model.pacman2.WallRectangle { x1:24 y1:9 x2:Model.pacman2.MazeData.GRID_SIZE + 1 y2:13 },
//      Model.pacman2.WallBlackLine { x1:0 y1:13 x2:0 y2:15  },
//      Model.pacman2.WallBlackLine { x1:Model.pacman2.MazeData.GRID_SIZE y1:13 x2:Model.pacman2.MazeData.GRID_SIZE y2:15},
//
//      //cage and the gate
//      Model.pacman2.WallRectangle { x1:10 y1:12 x2:19 y2:17 },
//      Model.pacman2.WallRectangle { x1:10.5 y1:12.5 x2:18.5 y2:16.5 },
//      Rectangle {
//        x: Model.pacman2.MazeData.calcGridX(13)
//        width: Model.pacman2.MazeData.GRID_GAP * 3
//        y: Model.pacman2.MazeData.calcGridY(12)
//        height: Model.pacman2.MazeData.GRID_GAP / 2
//        stroke: Color.GREY
//        fill: Color.GREY
//        cache: true
//      },
//
//      Model.pacman2.WallRectangle { x1:7.5 y1:19 x2:12 y2:20 },
//      Model.pacman2.WallRectangle { x1:7 y1:15 x2:8 y2:23 },
//      Model.pacman2.WallBlackLine { x1:8 y1:19 x2:8 y2:20 },
//
//      Model.pacman2.WallRectangle { x1:17 y1:19 x2:21.5 y2:20 },
//      Model.pacman2.WallRectangle { x1:21 y1:15 x2:22 y2:23 },
//      Model.pacman2.WallBlackLine { x1:21 y1:19 x2:21 y2:20 },
//
//      Model.pacman2.WallRectangle { x1:14 y1:19 x2:15 y2:27 },
//      Model.pacman2.WallRectangle { x1:10 y1:22 x2:19 y2:23 },
//      Model.pacman2.WallBlackLine { x1:14 y1:22 x2:15 y2:22 },
//      Model.pacman2.WallBlackLine { x1:14 y1:23 x2:15 y2:23 },
//
//      Model.pacman2.WallRectangle { x1:2 y1:25 x2:5 y2:27 },
//      Model.pacman2.WallRectangle { x1:17 y1:25 x2:22 y2:27 },
//
//      Model.pacman2.WallRectangle { x1:7 y1:25 x2:12 y2:27 },
//      Model.pacman2.WallRectangle { x1:24 y1:25 x2:27 y2:27 },
//
//      Model.pacman2.WallRectangle { x1:-1 y1:15 x2:5 y2:17 },
//      Model.pacman2.WallRectangle { x1:4 y1:19 x2:5 y2:23 },
//      Model.pacman2.WallRectangle { x1:2 y1:19 x2:4.5 y2:20 },
//      Model.pacman2.WallBlackRectangle { x1:4 y1:19.05 x2:5 y2:20.2 },
//      Model.pacman2.WallRectangle { x1:-1 y1:22 x2:2 y2:23 },
//
//      Model.pacman2.WallRectangle { x1:24 y1:15 x2:Model.pacman2.MazeData.GRID_SIZE + 1 y2:17 },
//      Model.pacman2.WallRectangle { x1:24 y1:19 x2:25 y2:23 },
//      Model.pacman2.WallRectangle { x1:24.5 y1:19 x2:27 y2:20 },
//      Model.pacman2.WallBlackRectangle { x1:24 y1:19.05 x2:25 y2:20.2 },
//      Model.pacman2.WallRectangle { x1:27 y1:22 x2:Model.pacman2.MazeData.GRID_SIZE + 1 y2:23 },
//
//      Model.pacman2.WallBlackRectangle { x1:-2 y1:8 x2:0 y2:Model.pacman2.MazeData.GRID_SIZE },
//      Model.pacman2.WallBlackRectangle {
//         x1:Model.pacman2.MazeData.GRID_SIZE
//         y1:8
//         x2:Model.pacman2.MazeData.GRID_SIZE + 2
//         y2:Model.pacman2.MazeData.GRID_SIZE
//      },
//
//      Rectangle {
//        x: Model.pacman2.MazeData.calcGridX(-0.5)
//        y: Model.pacman2.MazeData.calcGridY(-0.5)
//        width: (Model.pacman2.MazeData.GRID_SIZE + 1) * Model.pacman2.MazeData.GRID_GAP
//        height: (Model.pacman2.MazeData.GRID_SIZE + 1) * Model.pacman2.MazeData.GRID_GAP
//        strokeWidth: Model.pacman2.MazeData.GRID_STROKE
//        stroke: Color.BLUE
//        fill: null
//        arcWidth: 12
//        arcHeight: 12
//        cache: true
//      },
//      Line {
//        startX: Model.pacman2.MazeData.calcGridX(-0.5)
//        endX: Model.pacman2.MazeData.calcGridX(-0.5)
//        startY: Model.pacman2.MazeData.calcGridY(13)
//        endY: Model.pacman2.MazeData.calcGridY(15)
//        stroke: Color.BLACK
//        strokeWidth: Model.pacman2.MazeData.GRID_STROKE + 1
//        cache: true
//      },
//      Line {
//        startX: Model.pacman2.MazeData.calcGridX(Model.pacman2.MazeData.GRID_SIZE + 0.5)
//        endX: Model.pacman2.MazeData.calcGridX(Model.pacman2.MazeData.GRID_SIZE + 0.5)
//        startY: Model.pacman2.MazeData.calcGridY(13)
//        endY: Model.pacman2.MazeData.calcGridY(15)
//        stroke: Color.BLACK
//        strokeWidth: Model.pacman2.MazeData.GRID_STROKE + 1
//        cache: true
//      },
//      Line {
//        startX: Model.pacman2.MazeData.calcGridX(-0.5)
//        endX: Model.pacman2.MazeData.calcGridX(0)
//        startY: Model.pacman2.MazeData.calcGridY(13)
//        endY: Model.pacman2.MazeData.calcGridY(13)
//        stroke: Color.BLUE
//        strokeWidth: Model.pacman2.MazeData.GRID_STROKE
//        cache: true
//      },
//      Line {
//        startX: Model.pacman2.MazeData.calcGridX(-0.5)
//        endX: Model.pacman2.MazeData.calcGridX(0)
//        startY: Model.pacman2.MazeData.calcGridY(15)
//        endY: Model.pacman2.MazeData.calcGridY(15)
//        stroke: Color.BLUE
//        strokeWidth: Model.pacman2.MazeData.GRID_STROKE
//        cache: true
//      },
//      Line {
//        startX: Model.pacman2.MazeData.calcGridX(Model.pacman2.MazeData.GRID_SIZE + 0.5)
//        endX: Model.pacman2.MazeData.calcGridX(Model.pacman2.MazeData.GRID_SIZE)
//        startY: Model.pacman2.MazeData.calcGridY(13)
//        endY: Model.pacman2.MazeData.calcGridY(13)
//        stroke: Color.BLUE
//        strokeWidth: Model.pacman2.MazeData.GRID_STROKE
//        cache: true
//      },
//      Line {
//        startX: Model.pacman2.MazeData.calcGridX(Model.pacman2.MazeData.GRID_SIZE + 0.5)
//        endX: Model.pacman2.MazeData.calcGridX(Model.pacman2.MazeData.GRID_SIZE)
//        startY: Model.pacman2.MazeData.calcGridY(15)
//        endY: Model.pacman2.MazeData.calcGridY(15)
//        stroke: Color.BLUE
//        strokeWidth: Model.pacman2.MazeData.GRID_STROKE
//        cache: true
//      },
//      Text {
//        font: Font {
//              size: 20
//              }
//        x: Model.pacman2.MazeData.calcGridX(0),
//        y: Model.pacman2.MazeData.calcGridY(Model.pacman2.MazeData.GRID_SIZE + 2)
//        content: bind "SCORES: {pacMan.score} "
//        fill: Color.YELLOW
//        cache: true
//      },
//      scoreText,
//      dyingPacMan,
//      livesImage,
//      gameResultText,
//      Text {
//        font: Font { size: 20
//                    }
//        x: Model.pacman2.MazeData.calcGridX(22)
//        y: Model.pacman2.MazeData.calcGridY(Model.pacman2.MazeData.GRID_SIZE + 2)
//        content: bind "LEVEL: {level}"
//        fill: Color.YELLOW
//        cache: true
//      },
//
//    ]
//  }; // end Group

  // put dots into the maze
//  postinit {
//    putDotHorizontally(2,13,1);
//    putDotHorizontally(16,27,1);
//    putDotHorizontally(2,27,5);
//    putDotHorizontally(2,27,28);
//
//    putDotHorizontally(2,13,24);
//    putDotHorizontally(16,27,24);
//
//    putDotHorizontally(2,5,8);
//    putDotHorizontally(9,13,8);
//    putDotHorizontally(16,20,8);
//    putDotHorizontally(24,27,8);
//
//    putDotHorizontally(2,5,18);
//    putDotHorizontally(9,13,21);
//    putDotHorizontally(16,20,21);
//    putDotHorizontally(24,27,18);
//
//    putDotHorizontally(2,3,21);
//    putDotHorizontally(26,27,21);
//
//    putDotVertically(1,1,8);
//    putDotVertically(1,18,21);
//    putDotVertically(1,24,28);
//
//    putDotVertically(28,1,8);
//    putDotVertically(28,18,21);
//    putDotVertically(28,24,28);
//
//    putDotVertically(6,2,27);
//    putDotVertically(23,2,27);
//
//    putDotVertically(3,22,23);
//    putDotVertically(9,22,23);
//    putDotVertically(20,22,23);
//    putDotVertically(26,22,23);
//
//    putDotVertically(13,25,27);
//    putDotVertically(16,25,27);
//
//    putDotVertically(9,6,7);
//    putDotVertically(20,6,7);
//
//    putDotVertically(13,2,4);
//    putDotVertically(16,2,4);
//
//    insert pacMan into group.content;
//
//    insert ghosts into group.content;
//
//    insert Model.pacman2.WallBlackRectangle{ x1:-3, y1:13, x2:0, y2:15} into group.content;
//    insert Model.pacman2.WallBlackRectangle{ x1:29, y1:13, x2:31, y2:15} into group.content;
//
//    insert messageBox into group.content;
//  }


  public Maze() {

    setFocused(true);

    gamePaused = new SimpleBooleanProperty(false);

    pacMan = new PacMan(this, 15, 24);

    final Ghost ghostBlinky = new Ghost(
            new Image(getClass().getResourceAsStream("/Resources/pacman/images/ghostred1.png")),
            new Image(getClass().getResourceAsStream("/Resources/pacman/images/ghostred2.png")),
            this,
            pacMan,
            15, // x
            14, // y
            0,  // x Direction
            -1, // y Direction
            1); // trap time
//    public var ghostBlinky = Ghost {
//    defaultImage1: Image {
//      url: "{__DIR__}/Resources/pacman/images/ghostred1.png"
//    }
//
//    defaultImage2: Image {
//      url: "{__DIR__}/Resources/pacman/images/ghostred2.png"
//    }
//
//     maze: this
//     pacMan: pacMan
//     x: 17
//     y: 15
//     xDirection: 0
//     yDirection: -1
//     trapTime: 1
//    };

    final Ghost ghostPinky = new Ghost(
            new Image(getClass().getResourceAsStream("/Resources/pacman/images/ghostpink1.png")),
            new Image(getClass().getResourceAsStream("/Resources/pacman/images/ghostpink2.png")),
            this,
            pacMan,
            14,
            15,
            1,  // x Direction
            0,  // y Direction
            5); // trap time
//   public var ghostPinky = Ghost {
//     defaultImage1:Image {
//        url: "{__DIR__}/Resources/pacman/images/ghostpink1.png"
//     }
//
//     defaultImage2:Image {
//       url: "{__DIR__}/Resources/pacman/images/ghostpink2.png"
//     }
//
//     maze: this
//     pacMan: pacMan
//     x: 12
//     y: 14
//     xDirection: 0
//     yDirection: 1
//     trapTime: 10
//   };

    final Ghost ghostInky = new Ghost(
            new Image(getClass().getResourceAsStream("/Resources/pacman/images/ghostcyan1.png")),
            new Image(getClass().getResourceAsStream("/Resources/pacman/images/ghostcyan2.png")),
            this,
            pacMan,
            12,
            15,
            1,   // x Direction
            0,   // y Direction
            20); // trap time
//   public var ghostInky = Ghost {
//     defaultImage1:Image {
//       url: "{__DIR__}/Resources/pacman/images/ghostcyan1.png"
//     }
//     defaultImage2:Image {
//       url: "{__DIR__}/Resources/pacman/images/ghostcyan2.png"
//     }
//
//     maze: this
//     pacMan: pacMan
//     x: 13
//     y: 15
//     xDirection: 1
//     yDirection: 0
//     trapTime: 20
//   };

    final Ghost ghostClyde = new Ghost(
            new Image(getClass().getResourceAsStream("/Resources/pacman/images/ghostorange1.png")),
            new Image(getClass().getResourceAsStream("/Resources/pacman/images/ghostorange2.png")),
            this,
            pacMan,
            16,
            15,
            1,   // x Direction
            0,   // y Direction
            30); // trap time
//   public var ghostClyde = Ghost {
//     defaultImage1:Image {
//        url: "{__DIR__}/Resources/pacman/images/ghostorange1.png"
//     }
//     defaultImage2:Image {
//       url: "{__DIR__}/Resources/pacman/images/ghostorange2.png"
//     }
//
//     maze: this
//     pacMan: pacMan
//     x: 15
//     y: 14
//     xDirection: -1
//     yDirection: 0
//     trapTime: 30
//   };

    //יצירת רוחות �?ש�?�?ות
    //רוח ש�?�?ה �?ס'1
    final Ghost question1Ghost = new Ghost(
            new Image(getClass().getResourceAsStream("/Resources/pacman/images/questionDot1.png")),
            new Image(getClass().getResourceAsStream("/Resources/pacman/images/questionDot1.png")),
            this,
            pacMan,
            15, // x
            14, // y
            0,  // x Direction
            -1, // y Direction
            0); // trap time


    // רוח ש�?�?ה �?ס'2
    final Ghost question2Ghost = new Ghost(
            new Image(getClass().getResourceAsStream("/Resources/pacman/images/questionDot2.png")),
            new Image(getClass().getResourceAsStream("/Resources/pacman/images/questionDot2.png")),
            this,
            pacMan,
            15, // x
            14, // y
            0,  // x Direction
            -1, // y Direction
            0); // trap time

    // רוח ש�?�?ה �?ס'3
    final Ghost question3Ghost = new Ghost(
            new Image(getClass().getResourceAsStream("/Resources/pacman/images/questionDot3.png")),
            new Image(getClass().getResourceAsStream("/Resources/pacman/images/questionDot3.png")),
            this,
            pacMan,
            15, // x
            14, // y
            0,  // x Direction
            -1, // y Direction
            0); // trap time

    // רוח ש�?�?ה �?ס'4
    final Ghost question4Ghost = new Ghost(
            new Image(getClass().getResourceAsStream("/Resources/pacman/images/questionDot4.png")),
            new Image(getClass().getResourceAsStream("/Resources/pacman/images/questionDot4.png")),
            this,
            pacMan,
            15, // x
            14, // y
            0,  // x Direction
            -1, // y Direction
            0); // trap time

    //הש�?ת �?ער�? ש�? רוחות רפ�?י�? רגי�?ות
    ghosts = new Ghost[] {ghostBlinky, ghostPinky, ghostInky, ghostClyde};
    //הש�?ת �?ער�? ש�? רוחות רפ�?י�? ש�? ש�?�?ות
    questionGhosts =  new Ghost[] {question1Ghost, question2Ghost, question3Ghost, question4Ghost};

    dyingPacMan = new DyingPacMan(this);
//    dyingPacMan.maze = this;
    dyingPacMan.setCenterX(0);
    dyingPacMan.setCenterY(0);
    dyingPacMan.setRadiusX(13);
    dyingPacMan.setRadiusY(13);
    dyingPacMan.setStartAngle(90);
    dyingPacMan.setLength(360);
    dyingPacMan.setType(ArcType.ROUND);
    dyingPacMan.setFill(Color.valueOf(Config.getPacman_color()));

    dyingPacMan.setVisible(false);
    
    livesCount = new SimpleIntegerProperty(2);

    // images showing how many lives remaining
    final ImageView livesImage1 = new ImageView(PACMAN_IMAGE);
    livesImage1.setX(MazeData.calcGridX(18));
    livesImage1.setY(MazeData.calcGridYFloat(MazeData.GRID_SIZE_Y + 0.8f));
    livesImage1.visibleProperty().bind(livesCount.greaterThan(0));
    livesImage1.setCache(true);
    final ImageView livesImage2 = new ImageView(PACMAN_IMAGE);
    livesImage2.setX(MazeData.calcGridX(16));
    livesImage2.setY(MazeData.calcGridYFloat(MazeData.GRID_SIZE_Y + 0.8f));
    livesImage2.visibleProperty().bind(livesCount.greaterThan(1));
    livesImage2.setCache(true);
    final ImageView livesImage3 = new ImageView(PACMAN_IMAGE);
    livesImage3.setX(MazeData.calcGridX(14));
    livesImage3.setY(MazeData.calcGridYFloat(MazeData.GRID_SIZE_Y + 0.8f));
    livesImage3.visibleProperty().bind(livesCount.greaterThan(2));
    livesImage3.setCache(true);
//  var livesImage = [
//    ImageView {
//      image: PACMAN_IMAGE
//      x: Model.pacman2.MazeData.calcGridX(18)
//      y: Model.pacman2.MazeData.calcGridY(30)
//      visible: bind livesCount > 0
//      cache: true
//    },
//    ImageView {
//      image: PACMAN_IMAGE
//      x: Model.pacman2.MazeData.calcGridX(16)
//      y: Model.pacman2.MazeData.calcGridY(30)
//      visible: bind livesCount > 1
//      cache: true
//    },
//    ImageView {
//      image: PACMAN_IMAGE
//      x: Model.pacman2.MazeData.calcGridX(14)
//      y: Model.pacman2.MazeData.calcGridY(30)
//      visible: bind livesCount > 2
//      cache: true
//    }
//  ];
    @SuppressWarnings("unused")
	final ImageView[] livesImage = new ImageView[] {livesImage1, livesImage2, livesImage3};

    level = new SimpleIntegerProperty(1);
    addLifeFlag = true;
    waitForStart = new SimpleBooleanProperty(true);

    messageBox = new Group();
    final Rectangle rectMessage = new Rectangle(MazeData.calcGridX(5),
            MazeData.calcGridYFloat(17.5f),
            MazeData.GRID_GAP * 19,
            MazeData.GRID_GAP * 5);
    rectMessage.setStroke(Color.RED);
    rectMessage.setStrokeWidth(5);
    rectMessage.setFill(Color.CYAN);
    rectMessage.setOpacity(0.75);
    rectMessage.setArcWidth(25);
    rectMessage.setArcHeight(25);

    final StringBinding messageBinding = new StringBinding() {

      {
        super.bind(gamePaused);
      }

      @Override
      protected String computeValue() {
        if (gamePaused.get()) {
          return " PRESS 'P' BUTTON TO RESUME";
        } else {
          return "   PRESS ANY KEY TO START!";
        }
      }
    };

    final Text textMessage = new Text(MazeData.calcGridX(6),
            MazeData.calcGridYFloat(20.5f),
            "   PRESS ANY KEY TO START!");
    textMessage.textProperty().bind(messageBinding);
    textMessage.setFont(new Font(18));
    textMessage.setFill(Color.RED);
    messageBox.getChildren().add(rectMessage);
    messageBox.getChildren().add(textMessage);

    lastGameResult = new SimpleBooleanProperty(false);

    final StringBinding lastGameResultBinding = new StringBinding() {

      {
        super.bind(lastGameResult);
      }

      @Override
      protected String computeValue() {
        if (lastGameResult.get()) {
          return "  YOU WIN ";
        } else {
          return "GAME OVER   ";
        }
      }
    };

    gameResultText = new Text(MazeData.calcGridX(11),
            MazeData.calcGridY(11) + 8,
            " YOU WIN ");
    gameResultText.textProperty().bind(lastGameResultBinding);
    gameResultText.setFont(new Font(20));
    gameResultText.setFill(Color.RED);
    gameResultText.setVisible(false);

    flashingCount = 0;

    flashingTimeline = new Timeline();
    flashingTimeline.setCycleCount(5);
    final KeyFrame kf = new KeyFrame(Duration.seconds(0.5), new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        gameResultText.setVisible(!gameResultText.isVisible());
        if (++flashingCount == 5) {
          messageBox.setVisible(true);
          waitForStart.set(true);
        }
      }

    });
    flashingTimeline.getKeyFrames().add(kf);

    group = new Group();

    // Make big black rectangle to cover entire background
    final Rectangle blackBackground = new Rectangle(0, 0,
            MazeData.calcGridX(MazeData.GRID_SIZE_X + 2),
            MazeData.calcGridY(MazeData.GRID_SIZE_Y + 3));
    blackBackground.setFill(Color.BLACK);
    blackBackground.setCache(true);
    group.getChildren().add(blackBackground);

    // Inner border of outside wall
    group.getChildren().add(new WallRectangle(0, 0, MazeData.GRID_SIZE_X, MazeData.GRID_SIZE_Y));

    // Top middle vertical wall

    group.getChildren().add(new WallRectangle(14, -0.5f, 15, 4)); // upper-left rectangle
    group.getChildren().add(new WallBlackRectangle(13.8f, -1, 15.3f, 0));

    group.getChildren().add(new WallRectangle(2, 2, 5, 4)); // upper-left rectangle
    group.getChildren().add(new WallRectangle(7, 2, 12, 4)); // upper 2nd-from-left rectangle
    group.getChildren().add(new WallRectangle(17, 2, 22, 4)); // upper 2nd-from-right rectangle
    group.getChildren().add(new WallRectangle(24, 2, 27, 4)); // upper-right rectangle
    group.getChildren().add(new WallRectangle(2, 6, 5, 7)); // left-side 2nd from top rectangle

//     middle top T
    group.getChildren().add(new WallRectangle(14, 6, 15, 10));
    group.getChildren().add(new WallRectangle(10, 6, 19, 7));
    group.getChildren().add(new WallBlackLine(14.05f, 7, 14.95f, 7));

    // Upper-left sideways T
    group.getChildren().add(new WallRectangle(7.5f, 9, 12, 10));
    group.getChildren().add(new WallRectangle(7, 6, 8, 13));
    group.getChildren().add(new WallBlackLine(8, 9, 8, 10));

    // Upper-right sideways T
    group.getChildren().add(new WallRectangle(17, 9, 21.5f, 10));
    group.getChildren().add(new WallRectangle(21, 6, 22, 13));
    group.getChildren().add(new WallBlackLine(21, 9, 21, 10));

    group.getChildren().add(new WallRectangle(24, 6, 27, 7));

//    full bot side
    group.getChildren().add(new WallRectangle(24, 9, 27, 10));
    group.getChildren().add(new WallRectangle(24, 12, 27, 13));
    group.getChildren().add(new WallRectangle(24, 15, 27, 20));
    group.getChildren().add(new WallRectangle(24, 31, 27, 32));

//    right side winner
    group.getChildren().add(new WallRectangle(29, 2, 32, 3));
    group.getChildren().add(new WallRectangle(29, 5, 32, 7));
    group.getChildren().add(new WallRectangle(29, 21.9f, 32, 23));
    group.getChildren().add(new WallRectangle(29, 28, 32, 29));
    group.getChildren().add(new WallRectangle(29, 31, 32, 32));
    group.getChildren().add(new WallRectangle(29, 34, 32, 35));

    //middle cage Roi
//    group.getChildren().add(new Model.pacman2.WallRectangle(10, 12, 15, 14));
    group.getChildren().add(new WallRectangle(10, 12, 12, 17));
    group.getChildren().add(new WallRectangle(17, 12, 19, 17));




    //cage and the gate
//    group.getChildren().add(new Model.pacman2.WallRectangle(10, 12, 19, 17));
//    group.getChildren().add(new Model.pacman2.WallRectangle(10.5f, 12.5f, 18.5f, 16.5f));
//    final Rectangle cageRect = new Rectangle(Model.pacman2.MazeData.calcGridX(13),
//            Model.pacman2.MazeData.calcGridY(12),
//            Model.pacman2.MazeData.GRID_GAP * 3,
//            Model.pacman2.MazeData.GRID_GAP / 2);
//    cageRect.setStroke(Color.GREY);
//    cageRect.setFill(Color.GREY);
//    cageRect.setCache(true);
//    group.getChildren().add(cageRect);

    // Vertical rectangle below left side door
    group.getChildren().add(new WallRectangle(7, 15, 8, 20));

    // Vertical rectangle below right side door
    group.getChildren().add(new WallRectangle(21, 15, 22, 20));

    // middle middle T
    group.getChildren().add(new WallRectangle(14, 19, 15, 23));
    group.getChildren().add(new WallRectangle(10, 19, 19, 20));
    group.getChildren().add(new WallBlackLine(14.05f, 20, 14.95f, 20));

    // left L
    group.getChildren().add(new WallRectangle(4, 22, 5, 26));
    group.getChildren().add(new WallRectangle(2, 22, 5, 23));
    group.getChildren().add(new WallBlackRectangle(4, 22.05f, 5, 23.2f));

    group.getChildren().add(new WallRectangle(7, 22, 12, 23)); // left lower horizontal rectangle

    // right L
    group.getChildren().add(new WallRectangle(24, 22, 25, 26));
    group.getChildren().add(new WallRectangle(24, 22, 27, 23));
    group.getChildren().add(new WallBlackRectangle(24, 22.05f, 25, 23.2f));

    group.getChildren().add(new WallRectangle(17, 22, 22, 23)); // right lower horizontal rectangle

    group.getChildren().add(new WallRectangle(-1, 25, 2, 26)); // left horizontal wall
    group.getChildren().add(new WallRectangle(27, 25, MazeData.GRID_SIZE_X + 1, 26)); // right horizontal wall

    // left bottom upside-down T
    group.getChildren().add(new WallRectangle(7, 25, 8, 29));
    group.getChildren().add(new WallRectangle(2, 28, 12, 29));
    group.getChildren().add(new WallBlackLine(7.05f, 28, 7.95f, 28));

    // lower middle T
    group.getChildren().add(new WallRectangle(14, 25, 15, 29));
    group.getChildren().add(new WallRectangle(10, 25, 19, 26));
    group.getChildren().add(new WallBlackLine(14.05f, 26, 14.95f, 26));

    // right bottom upside-down T
    group.getChildren().add(new WallRectangle(21, 25, 22, 29));
    group.getChildren().add(new WallRectangle(17, 28, 27, 29));
    group.getChildren().add(new WallBlackLine(21.05f, 28, 21.95f, 28));

    // right bottom upside-down T morre
    group.getChildren().add(new WallRectangle(21, 31, 22, 35));
    group.getChildren().add(new WallRectangle(17, 34, 27, 35));
    group.getChildren().add(new WallBlackLine(21.05f, 34, 21.95f, 34));

    // left bottom upside-down T morre
    group.getChildren().add(new WallRectangle(7, 31, 8, 35));
    group.getChildren().add(new WallRectangle(2, 34, 12, 35));
    group.getChildren().add(new WallBlackLine(7.05f, 34, 7.95f, 34));

    // lower middle T morre
    group.getChildren().add(new WallRectangle(14, 31, 15, 35));
    group.getChildren().add(new WallRectangle(10, 31, 19, 32));
    group.getChildren().add(new WallBlackLine(14.05f, 32, 14.95f, 32));

//    small block left
    group.getChildren().add(new WallRectangle(2, 31, 5, 32));


    // Outer border of outside wall
    final Rectangle outerWall = new Rectangle(MazeData.calcGridXFloat(-0.5f),
            MazeData.calcGridYFloat(-0.5f),
            (MazeData.GRID_SIZE_X + 1) * MazeData.GRID_GAP,
            (MazeData.GRID_SIZE_Y + 1) * MazeData.GRID_GAP);
    outerWall.setStrokeWidth(MazeData.GRID_STROKE);
    outerWall.setStroke(Color.BLUE);
    outerWall.setFill(null);
    outerWall.setArcWidth(12);
    outerWall.setArcHeight(12);
    outerWall.setCache(true);
    group.getChildren().add(outerWall);

    group.getChildren().add(new WallRectangle(-1, 9, 5, 13)); // outer wall above left side door
    group.getChildren().add(new WallRectangle(-1, 9.5f, 4.5f, 12.5f)); // inner wall above left side door
    group.getChildren().add(new WallRectangle(-1, 15, 5, 20)); // outer wall below left side wall
    group.getChildren().add(new WallRectangle(-1, 15.5f, 4.5f, 19.5f)); // inner wall below left side door wall
//קיר בריחה י�?י�?
    group.getChildren().add(new WallRectangle(MazeData.GRID_SIZE_X - 5, 9, MazeData.GRID_SIZE_X + 1, 13)); // outer wall above right side door
    group.getChildren().add(new WallRectangle(MazeData.GRID_SIZE_X - 4.5f, 9.5f, MazeData.GRID_SIZE_X + 1, 12.5f)); // inner wall above right side door
    group.getChildren().add(new WallRectangle(MazeData.GRID_SIZE_X - 5, 15, MazeData.GRID_SIZE_X + 1, 20)); // outer wall below right side wall
    group.getChildren().add(new WallRectangle(MazeData.GRID_SIZE_X - 4.5f, 15.5f, MazeData.GRID_SIZE_X + 1, 19.5f)); // inner wall below right side door wall

    group.getChildren().add(new WallBlackRectangle(-2, 8, -0.5f, MazeData.GRID_SIZE_Y)); // black-out left garbage outside the wall
    group.getChildren().add(new WallBlackRectangle(-0.5f, 8, 0, 9.5f)); // black-out horizontal line inside outer-left wall above side door
    group.getChildren().add(new WallBlackRectangle(-0.5f, 19.5f, 0, MazeData.GRID_SIZE_Y)); // black-out horizontal lines inside outer-left wall below side door

    group.getChildren().add(new WallBlackRectangle(MazeData.GRID_SIZE_X + 0.5f, 8, MazeData.GRID_SIZE_X + 2, MazeData.GRID_SIZE_Y)); // black-out garbage on right side of outside wall
    group.getChildren().add(new WallBlackRectangle(MazeData.GRID_SIZE_X, 8, MazeData.GRID_SIZE_X + 0.5f, 9.5f)); // black-out horizontal line inside outer-right wall above side door
    group.getChildren().add(new WallBlackRectangle(MazeData.GRID_SIZE_X, 19.5f, MazeData.GRID_SIZE_X + 0.5f, MazeData.GRID_SIZE_Y)); // black-out horizontal lines inside outer-right wall below side door

    // black-out outer walls inside both side doors
    group.getChildren().add(new WallBlackRectangle(-1, 13, 1, 15)); // left
    group.getChildren().add(new WallBlackRectangle(MazeData.GRID_SIZE_X - 1, 13, MazeData.GRID_SIZE_X + 1, 15)); // right

    // Add back 4 blue wall segments that were deleted
    group.getChildren().add(new WallBlackLine(Color.BLUE, -0.5f, 9, -0.5f, 9.5f));
    group.getChildren().add(new WallBlackLine(Color.BLUE, -0.5f, 19.5f, -0.5f, 20));
    group.getChildren().add(new WallBlackLine(Color.BLUE, MazeData.GRID_SIZE_X + 0.5f, 9, MazeData.GRID_SIZE_X + 0.5f, 9.5f));
    group.getChildren().add(new WallBlackLine(Color.BLUE, MazeData.GRID_SIZE_X + 0.5f, 19.5f, MazeData.GRID_SIZE_X + 0.5f, 20));

    final Text textScore = new Text(MazeData.calcGridX(0),
            MazeData.calcGridY(MazeData.GRID_SIZE_Y + 2),
            "SCORE: " + pacMan.score);
    textScore.textProperty().bind(pacMan.score.asString("SCORE: %1d  "));
    textScore.setFont(new Font(20));
    textScore.setFill(Color.YELLOW);
    textScore.setCache(true);
    //group.getChildren().add(textScore);

    //�?תודת טיי�?ר
    SimpleIntegerProperty secondPasses;
    secondPasses = new SimpleIntegerProperty(40);
    final Text textTimer = new Text(MazeData.calcGridX(10),
            MazeData.calcGridY(MazeData.GRID_SIZE_Y + 2),
            "Time: " + secondPasses);



    @SuppressWarnings("unused")
	Timer myTimer = new Timer();

    TimerTask timerTask = new TimerTask() {
      public void run() {

        if(secondPasses.get() > 0){
          secondPasses.set(secondPasses.get()-1);
          System.out.println(secondPasses);
        }
        else{
          secondPasses.set(0);
        }
      }
    };
    timerTask.run();
    textTimer.textProperty().bind(secondPasses.asString("Time: %1d "));
    textTimer.setFont(new Font(20));
    textTimer.setFill(Color.YELLOW);
    textTimer.setCache(true);
    //group.getChildren().add(textTimer); //asaf timer
    //group.getChildren().addAll(SCORE_TEXT); //score text
    group.getChildren().add(dyingPacMan);
    group.getChildren().addAll(questionRight);// after eating a good answer
    group.getChildren().add(moneyCandy);

    //group.getChildren().addAll(livesImage);// lives of pacman

    final Text textLevel = new Text(MazeData.calcGridX(22),
            MazeData.calcGridY(MazeData.GRID_SIZE_Y + 2),
            "LEVEL: " + level);
    textLevel.textProperty().bind(level.asString("LEVEL: %1d "));
    textLevel.setFont(new Font(20));
    textLevel.setFill(Color.YELLOW);
    textLevel.setCache(true);
    //group.getChildren().add(textLevel); //text timer 
    group.setFocusTraversable(true); // patweb
    group.setOnKeyPressed(new EventHandler<KeyEvent>() {

      @Override public void handle(KeyEvent ke) {
        onKeyPressed(ke);
      }
    });


    // postinit
    putDotHorizontally(2,12,1);
    putDotHorizontally(17,27,1);

    putDotHorizontally(2,27,5);

    putDotHorizontally(2,5,8);
    putDotHorizontally(24,27,8);

    putDotHorizontally(10,13,8);
    putDotHorizontally(16,19,8);

    putDotHorizontally(2,12,21);
    putDotHorizontally(17,27,21);

    putDotHorizontally(2,2,24);
    putDotHorizontally(27,27,24);

    putDotHorizontally(7,12,24);
    putDotHorizontally(17,22,24);

    putDotHorizontally(2,5,27);
    putDotHorizontally(24,27,27);

    putDotHorizontally(10,12,27);
    putDotHorizontally(17,19,27);

    putDotHorizontally(2,27,30); // bottom row


    putDotVertically(1,1,4);
    putDotVertically(1,6,8);
    putDotVertically(28,3,8);
    putDotVertically(28,1,1);

    putDotVertically(1,21,24);
    putDotVertically(28,21,24);

    putDotVertically(1,27,30);
    putDotVertically(28,27,30);

    putDotVertically(3,24,26);
    putDotVertically(26,24,26);

    putDotVertically(6,2,4);
    putDotVertically(6,6,20);
    putDotVertically(6,22,27);
    putDotVertically(23,2,4);
    putDotVertically(23,6,20);
    putDotVertically(23,22,27);


    putDotVertically(9,6,8);
    putDotVertically(20,6,8);

    putDotVertically(9,25,27);
    putDotVertically(20,25,27);

    putDotVertically(13,1,4);
    putDotVertically(16,1,4);

    putDotVertically(13,21,22);
    putDotVertically(13,24,24);
    putDotVertically(16,21,24);

    putDotVertically(13,27,29);
    putDotVertically(16,27,29);


    // put dot team sbider
    putDotHorizontally(29,32,1);
    putDotHorizontally(29,32,4);
    putDotHorizontally(29,32,8);
    putDotHorizontally(24,28,11);
    putDotHorizontally(24,28,14);
    putDotHorizontally(29,32,21);
    putDotHorizontally(29,32,24);
    putDotHorizontally(29,32,27);
    putDotHorizontally(29,32,30);
    putDotHorizontally(29,32,33);
    putDotHorizontally(23,27,33);
    putDotHorizontally(2,32,36);
    putDotHorizontally(2,6,33);
    putDotHorizontally(9,12,33);
    putDotHorizontally(16,19,33);
    putDotHorizontally(9,19,11);
    putDotHorizontally(9,19,18);
    putDotHorizontally(7,8,14);//2911
    putDotHorizontally(21,22,14);//2911
    putDotVertically(20,31,33);
    putDotVertically(13,33,35);
    putDotVertically(16,34,35);
    putDotVertically(6,31,32);
    putDotVertically(9,31,32);
    putDotVertically(33,1,8);
    putDotVertically(28,9,10);
    putDotVertically(28,12,13);
    putDotVertically(28,15,20);
    putDotVertically(28,31,35);
    putDotVertically(33,27,36);
    putDotVertically(23,31,32);
    putDotVertically(1,31,36);
    putDotVertically(9,12,17);
    putDotVertically(9,19,20);
    putDotVertically(20,11,20);
    putDotVertically(15,12,17);
    putDotVertically(14,12,17);
    putDotVertically(13,12,17);
    putDotVertically(16,12,17);
    putDotVertically(33,21,24);



    // insert pacMan into group.content;
    group.getChildren().add(pacMan);

    // insert ghosts into group.content;
    group.getChildren().addAll(ghosts);

    // Black-out side door exit points so moving objects disappear at maze borders
//    insert Model.pacman2.WallBlackRectangle{ x1:-3, y1:13, x2:0, y2:15} into group.content;
//    insert Model.pacman2.WallBlackRectangle{ x1:29, y1:13, x2:31, y2:15} into group.content;
    group.getChildren().add(new WallBlackRectangle(-2, 13, -0.5f, 15));
    group.getChildren().add(new WallBlackRectangle(34, 13, 35, 15));

    // insert messageBox into group.content;
    group.getChildren().add(messageBox);

    // end postinit

    getChildren().add(group);

    //if (DEBUG) {
      //Model.pacman2.MazeData.printData();
      //Model.pacman2.MazeData.printDots();
    //}
    pacMan.setPacmanColor();
  }


  public void onKeyPressed(KeyEvent e) {
//  public override var onKeyPressed = function ( e: KeyEvent ) : Void {
//  @Override

    // wait for the player's keyboard input to start the game
    if (waitForStart.get()) {
      waitForStart.set(false);
      startNewGame();
      return;
    }

    if (e.getCode() == KeyCode.P) {
//    if ( e.code == KeyCode.VK_P ) {
      if (gamePaused.get()) {
        resumeGame();
      } else {
        pauseGame();
      }

      return;
    }

    if (gamePaused.get()) {
      return;
    }

    if (Config.getCommands().equals("Arrows"))
    {
    	if (e.getCode() == KeyCode.DOWN) {
//    	    if ( e.getCode() == KeyCode.VK_DOWN ) {
    	      pacMan.setKeyboardBuffer(MovingObject.MOVE_DOWN);
    	    } else if (e.getCode() == KeyCode.UP) {
    	      pacMan.setKeyboardBuffer(MovingObject.MOVE_UP);
    	    } else if (e.getCode() == KeyCode.RIGHT) {
    	      pacMan.setKeyboardBuffer(MovingObject.MOVE_RIGHT);
    	    } else if (e.getCode() == KeyCode.LEFT) {
    	      pacMan.setKeyboardBuffer(MovingObject.MOVE_LEFT);
    	    }
    }
    else
    {
    	if (e.getCode() == KeyCode.S) {
//    	    if ( e.getCode() == KeyCode.VK_DOWN ) {
    	      pacMan.setKeyboardBuffer(MovingObject.MOVE_DOWN);
    	    } else if (e.getCode() == KeyCode.W) {
    	      pacMan.setKeyboardBuffer(MovingObject.MOVE_UP);
    	    } else if (e.getCode() == KeyCode.D) {
    	      pacMan.setKeyboardBuffer(MovingObject.MOVE_RIGHT);
    	    } else if (e.getCode() == KeyCode.A) {
    	      pacMan.setKeyboardBuffer(MovingObject.MOVE_LEFT);
    	    }
    }

  }


  // create a View.Dot GUI object
  public final Dot createDot(int x1, int y1, int type) {
//  public function createDot( x1: Number,  y1:Number, type:Integer ): View.Dot {
    Dot d = new Dot(MazeData.calcGridX(x1), MazeData.calcGridY(y1), type);

    if (type == MazeData.NORMAL_DOT) {
//      d = View.Dot {
//        x: Model.pacman2.MazeData.calcGridX(x1)
//        y: Model.pacman2.MazeData.calcGridY(y1)
//        dotType: type
//        visible: true
//        shouldStopAnimation: bind gamePaused or waitForStart
//      }
      d.playTimeline();

      d.shouldStopAnimation.bind(gamePaused.or(waitForStart)); // patweb
    }
//    else {
//       d = View.Dot {
//        x: Model.pacman2.MazeData.calcGridX(x1)
//        y: Model.pacman2.MazeData.calcGridY(y1)
//        dotType: type
//        visible: true
//       }
//    }

    // set the dot type in data model
    MazeData.setData(x1, y1, type);

    // set dot reference
    MazeData.setDot(x1, y1, d);

    return d;
  }

  // יצירה ש�? סוכריות
  public final courseDot createCandy(int x1, int y1, int type) {
//  public function createDot( x1: Number,  y1:Number, type:Integer ): View.Dot {
    courseDot d = null;
    if(type == MazeData.poison_Dot) {
      d = new courseDot(MazeData.calcGridX(x1), MazeData.calcGridY(y1), type, poison_dot_image);
      d.shouldStopAnimation.bind(gamePaused.or(waitForStart)); // patweb
      MazeData.setData(x1, y1, type);
      MazeData.setDot(x1, y1, d);
    }

    if(type == MazeData.silver_Dot){
      d = new courseDot(MazeData.calcGridX(x1), MazeData.calcGridY(y1), type, silver_dot_image);
      d.shouldStopAnimation.bind(gamePaused.or(waitForStart)); // patweb
      MazeData.setData(x1, y1, type);
      MazeData.setDot(x1, y1, d);
    }

    if(type == MazeData.question_Dot){
      d = new courseDot(MazeData.calcGridX(x1), MazeData.calcGridY(y1), type, question_dot_image);
      d.shouldStopAnimation.bind(gamePaused.or(waitForStart)); // patweb
      MazeData.setData(x1, y1, type);
      MazeData.setDot(x1, y1, d);
    }
    return d;
  }


  // put dots into the maze as a horizontal line
  public final void putDotHorizontally(int x1, int x2, int y) {
    Dot dot;
    courseDot dot1;
    for (int x = x1; x <= x2; x++) {
      if (MazeData.getData(x, y) == MazeData.EMPTY) {
        int dotType;
//      var dotType: Integer;

        if ((x == 28 || x == 1) && (y == 3 || y == 24)) {
//                    dotType = Model.pacman2.MazeData.poison_Dot;
//                    dot1 = createCandy(x,y,dotType);
//                    // insert dots into group
//                    group.getChildren().add(dot1);
//                    System.out.println(x2);
        }
        //silver dot placement
        if ((x==24 && y == 8)){
            dot1 = createCandy( 27, 8, MazeData.silver_Dot);
            // insert dots into group
            group.getChildren().add(dot1);
        }
        //silver dot postion
        if ((x==2 && y == 30 )){
            dot1 = createCandy(2, 36, MazeData.silver_Dot);
            // insert dots into group
            group.getChildren().add(dot1);
        }

        else {
          dotType = MazeData.NORMAL_DOT;
          dot = createDot(x, y, dotType); // insert dots into group
          group.getChildren().add(dot);
          System.out.println();

        }
      }
      else {
        if (DEBUG) {
          System.out.println("!! WARNING: Trying to place horizontal dots at occupied position (" + x + ", " + y + ")");
        }
      }
    }
  }

  // put dots into the maze as a vertical line
  public final void putDotVertically(int x, int y1, int y2) {
    Dot dot;
    courseDot dot1;
    for (int y = y1; y <= y2; y++) {
      if (MazeData.getData(x, y) == MazeData.EMPTY) {
        @SuppressWarnings("unused")
		int dotType;

        //question dot placement
        if( ( x == 13) && (y == 21) ){
          dot1 = createCandy(x, 23, MazeData.question_Dot);
          // insert dots into group
          group.getChildren().add(dot1);
        }

        //silver dot placement
        if( ( x == 1) && (y == 1) ){
          dot1 = createCandy(x, 5, MazeData.silver_Dot);
          // insert dots into group
          group.getChildren().add(dot1);
        }

        //silver dot placement
        if( ( x == 33) && (y == 21) ){
            dot1 = createCandy(x, 24, MazeData.silver_Dot);
            // insert dots into group
            group.getChildren().add(dot1);
        }
        //silver dot placement
        if( ( x == 28) && (y == 27) ){
            dot1 = createCandy(x, 33, MazeData.silver_Dot);
            // insert dots into group
            group.getChildren().add(dot1);
          }

          //poison dot placment
        if ( (x == 28) && (y == 3) ) {

          dot1 = createCandy(x, y, MazeData.poison_Dot);
          // insert dots into group
          group.getChildren().add(dot1);
        }

        else {
          dotType = MazeData.NORMAL_DOT;
          dot = createDot(x, y, MazeData.NORMAL_DOT);
          group.getChildren().add(dot);
          if ( (x == 1) && (y == 5) ){
            System.out.println(x);
            System.out.println(y);
          }

        }

      }
      else {
        if (DEBUG) {
          System.out.println("!! WARNING: Trying to place vertical   dots at occupied position (" + x + ", " + y + ")");
        }
      }
    }
  }


  //makes the ghosts hollow
  public void makeGhostsHollow() {

    ghostEatenCount = 0;

    for (Ghost g : ghosts) {
      g.changeToHollowGhost();
    }
  }


  // determine if pacman meets a ghost
  public boolean hasMet(Ghost g) {

    int distanceThreshold = 22;

    int x1 = g.imageX.get();
    int x2 = pacMan.imageX.get();

    int diffX = Math.abs(x1 - x2);

    if (diffX >= distanceThreshold) {
      return false;
    }

    int y1 = g.imageY.get();
    int y2 = pacMan.imageY.get();
    int diffY = Math.abs(y1 - y2);

    if (diffY >= distanceThreshold) {
      return false;
    }

    // calculate the distance to see if pacman touches the ghost
    if (diffY * diffY + diffX * diffX <= distanceThreshold * distanceThreshold) {
      return true;
    }

    return false;
  }

  public void pacManMeetsGhosts() {
    pacManMeetsQuestionGhosts();
    for (Ghost g : ghosts) {
      if (hasMet(g)) {
        for (Ghost ghost : ghosts) {
          Music.getInstance().musicPacmanMeetsRegularGhost();
          ghost.stop();
        }
        //needs to stop question ghost if we ate a question
        if (isquestionon) {
          for (Ghost qg : questionGhosts) {

            qg.setPlace(2,11 );
            qg.stop();
            qg.setVisible(false);
            isquestionon = false;
          }

        }
        pacMan.stop();

        dyingPacMan.startAnimation(pacMan.imageX.get(), pacMan.imageY.get());
        break;
      }
    }
  }

  public void homeButton()
  {
    for (Ghost ghost : ghosts) {
      ghost.stop();
    }
    //needs to stop question ghost if we ate a question
    if (isquestionon) {
      for (Ghost qg : questionGhosts) {

        qg.setPlace(2,11 );
        qg.stop();
        qg.setVisible(false);
        isquestionon = false;
      }

    }
    pacMan.stop();
    lastGameResult.set(false);
    flashingCount = 0;
    flashingTimeline.playFromStart();
    //pacMan.setPacmanColor();//unneeded code
    this.resumeGame();
  }

  public void pacManMeetsQuestionGhosts() {

    for (Ghost gq : questionGhosts) {
      if (hasMet(gq)) {
        if (gq.isHollow) {
          Music.getInstance().musicPacmanMeetsQuestionGhost(1);
          ScoreText st = questionRight[getquestiondifficulty() - 1];
          st.setX(gq.imageX.get() - 10);
          st.setY(gq.imageY.get());
          pacManEatsGhost(gq);
          st.showText();
          getPointsforQuestionRight(getquestiondifficulty());
        }
        else {
          getPointsforQuestionWrong(getquestiondifficulty());
          for (Ghost ghost : ghosts) {
            ghost.stop();
          }
          //needs to stop question ghost if we ate a question
          Music.getInstance().musicPacmanMeetsQuestionGhost(2);
          for (Ghost qg : questionGhosts) {
            qg.stop();
            qg.setPlace(2,11 );
            qg.setVisible(false);
            isquestionon = false;
          }
          pacMan.stop();

          dyingPacMan.startAnimation(pacMan.imageX.get(), pacMan.imageY.get());
          break;
        }
      }
    }
  }

  //�?תודה כ�?שר פק�?�? �?וכ�? רע�?
  public void pacManEatsPoison() {

    for (@SuppressWarnings("unused") Ghost g : ghosts) {
      for (Ghost ghost : ghosts) {
        ghost.stop();
      }
      pacMan.stop();

      //needs to stop question ghost if we ate a question
      if (isquestionon) {
        for (Ghost qg : questionGhosts) {
          qg.stop();
          qg.setPlace(2,11 );
          qg.setVisible(false);
          isquestionon = false;
        }
      }

      dyingPacMan.startAnimation(pacMan.imageX.get(), pacMan.imageY.get());
      break;
    }
  }


  public void pacManEatsGhost(Ghost g) {
//  public function pacManEatsGhost(g: Ghost) {

    ghostEatenCount++;
    //answered question right
    for (Ghost qg:questionGhosts) {
      qg.stop();
      qg.setPlace(2,11 );
      qg.setVisible(false);
      isquestionon = false;
    }
    pacMan.score.set(pacMan.score.get() + 100);

  }

  public void resumeGame() {

    if (!gamePaused.get()) {
      return;
    }

    messageBox.setVisible(false);

    for (Ghost g : ghosts) {
      if (g.isPaused()) {
        g.start();
      }
    }

    for (Ghost qg : questionGhosts) {
      if (qg.isPaused()) {
        qg.start();
      }
    }

    if (pacMan.isPaused()) {
      pacMan.start();
    }

    if (dyingPacMan.isPaused()) {
      dyingPacMan.start();
    }

    if (flashingTimeline.getStatus() == Animation.Status.PAUSED) {
//    if ( flashingTimeline.paused ) {
      flashingTimeline.play();
    }

    gamePaused.set(false);

  }

  public void pauseGame() {

    if ( waitForStart.get() || gamePaused.get() ) {
      return;
    }

    messageBox.setVisible(true);

    for (Ghost g : ghosts) {
      if (g.isRunning()) {
        g.pause();
      }
    }

    for (Ghost qg : questionGhosts) {
      if (qg.isRunning()) {
        qg.pause();
      }
    }

    if (pacMan.isRunning()) {
      pacMan.pause();
    }

    if (dyingPacMan.isRunning()) {
      dyingPacMan.pause();
    }

    if (flashingTimeline.getStatus() == Animation.Status.RUNNING) {
//    if ( flashingTimepacmanline.running ) {
      flashingTimeline.pause();
    }
    gamePaused.set(true);
  }


  // reset status and start a new game
  public void startNewGame() {

    messageBox.setVisible(false);
    pacMan.resetStatus();

    gameResultText.setVisible(false);

    if (!lastGameResult.get()) {
//    if ( lastGameResult == false ) {
      level.set(1);
      addLifeFlag = true;
      pacMan.score.set(0);
      pacMan.dotEatenCount = 0;

    //livesCount.set(2);
      livesCount.set(Config.getPacman_life()-1);
      Game.getLifeUpdate(this.numOfField , this.livesCount.get());
    }
    else {
      lastGameResult.set(false);
      level.set(level.get() + 1);
//      level ++;
    }

    for (int x = 1; x <= MazeData.GRID_SIZE_X; x++) {
//    for ( x in [1..Model.pacman2.MazeData.GRID_SIZE] )
      for (int y = 1; y <= MazeData.GRID_SIZE_Y; y++) {
//      for ( y in [1..Model.pacman2.MazeData.GRID_SIZE] ) {
        Dot dot = (Dot) MazeData.getDot(x, y);
//        var dot : View.Dot = Model.pacman2.MazeData.getDot( x, y ) as View.Dot ;

        if ( (dot != null) && !dot.isVisible() ) {
          dot.setVisible(true);
        }
      }
    }
    for (Ghost g : ghosts) {
      g.resetStatus();
    }

  }

  // reset status and start a new level
  public void startNewLevel() {

    lastGameResult.set(true);

    pacMan.hide();
    pacMan.dotEatenCount = 0;

    for (Ghost g : ghosts) {
      g.hide();
    }

    flashingCount = 0;
    flashingTimeline.playFromStart();
  }

  // reset status and start a new life
  public void startNewLife() throws Exception {

    // reduce a life of Pac-Man
    if (livesCount.get() > 0) {
//    if ( livesCount > 0 ) {
      livesCount.set(livesCount.get() - 1);
      Game.getLifeUpdate(this.numOfField , this.livesCount.get());
//      livesCount--;
    }
    else {
      lastGameResult.set(false);
      flashingCount = 0;
      flashingTimeline.playFromStart();
      Game.endGame();
      return;
    }

    pacMan.resetStatus();

    for (Ghost g : ghosts) {
      g.resetStatus();
    }
  }

  public void addLife() {

    if (addLifeFlag) {
      livesCount.set(livesCount.get() + 1);
      Game.getLifeUpdate(this.numOfField , this.livesCount.get());
//      livesCount ++;
      addLifeFlag = false;
    }
  }
  //�?תודת טיי�?ר
  public void setTimer() {
    Timer timer;
    timer = new Timer();
    timer.scheduleAtFixedRate(new TimerTask() {
      public void run() {

        if(interval > 0)
        {
          final Label timeElapsed = new Label();

          Platform.runLater(() -> timeElapsed.setText("Time to read the question: "+ interval));
          System.out.println(interval);
          interval--;
        }
        else
          timer.cancel();
      }
    }, 1000,1000);
  }

  //when you eat question candy spawns questionghost
  public void startQuestionGhosts()
  {
    int correctAnswer = getCorrectAnswer();
    if(nmofquestionchildrenadded == 0)
      group.getChildren().addAll(questionGhosts);

    nmofquestionchildrenadded++;
    isquestionon = true;
    for (Ghost qg:questionGhosts)
      qg.resetStatus();

    makeQuestionGhostHollow(correctAnswer);
  }

  public void makeQuestionGhostHollow (int correctanswer)
  {
    if(correctanswer > 0 && correctanswer<=4)
      questionGhosts[correctanswer-1].isHollow= true;
  }

  public int getCorrectAnswer()
  {
    Integer correctAns = Integer.parseInt(pacMan.questionToUser.getRightAnswer());
//    Random rand = new Random();
//    return rand.nextInt(4)+1;
    return correctAns;
  }

  public int getquestiondifficulty()
  {
    Integer difficulty = Integer.parseInt(pacMan.questionToUser.getLevel());
//    int difficulty = 1;
    return difficulty;
  }

  public void getPointsforQuestionRight (int difficulty)
  {
    switch (difficulty)
    {
      case 0:
        break;
      case 1:
        pacMan.score.set(pacMan.score.get() + 100);
        break;
      case 2:
        pacMan.score.set(pacMan.score.get() + 200);
        break;
      case 3:
        pacMan.score.set(pacMan.score.get() + 500);
        break;
    }
  }

  public void getPointsforQuestionWrong (int difficulty)
  {
    switch (difficulty)
    {
      case 0:
      break;
      case 1:
        pacMan.score.set(pacMan.score.get() - 250);
        break;
      case 2:
        pacMan.score.set(pacMan.score.get() - 100);
        break;
      case 3:
        pacMan.score.set(pacMan.score.get() - 50);
        break;
    }
  }

  public void setNumoffield(int x)
  {
    this.numOfField = x;
  }

  public int getNumoffield()
  {
    return this.numOfField;
  }


}
