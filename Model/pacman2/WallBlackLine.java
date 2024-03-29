package Model.pacman2;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * @author Noa Kirel
 * @author Lea Goldberg
 */
public class WallBlackLine extends Line {

//  public var x1: Number;
//  public var y1: Number;
//  public var x2: Number;
//  public var y2: Number;

  /**
   * constructor without color
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   */
  public WallBlackLine(float x1, float y1, float x2, float y2) {
    init(x1, y1, x2, y2);
    
//    strokeWidth = MazeData.GRID_STROKE + 1;
    setStrokeWidth(MazeData.GRID_STROKE + 1);
//    stroke = Color.BLACK;
    setStroke(Color.BLACK);
  }

  /**
   * constructor with color
   * @param lineColor
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   */
  public WallBlackLine(Color lineColor, float x1, float y1, float x2, float y2) {
    init(x1, y1, x2, y2);
    setStrokeWidth(MazeData.GRID_STROKE);
    setStroke(lineColor);
  }

  private void init(float x1, float y1, float x2, float y2) {
//  postinit {
//    cache = true;
    setCache(true);

//    if ( x1 == x2 ) { // vertically line
//      startX = MazeData.calcGridX(x1);
//      startY = MazeData.calcGridY(y1) + MazeData.GRID_STROKE;
//      endX = MazeData.calcGridX(x2);
//      endY = MazeData.calcGridY(y2) - MazeData.GRID_STROKE;
//    }
//    else  { // horizontal line
//      startX = MazeData.calcGridX(x1) + MazeData.GRID_STROKE;
//      startY = MazeData.calcGridY(y1);
//      endX = MazeData.calcGridX(x2) - MazeData.GRID_STROKE;
//      endY = MazeData.calcGridY(y2);
//    }
    if (x1 == x2) { // vertical line
      setStartX(MazeData.calcGridXFloat(x1));
      setStartY(MazeData.calcGridYFloat(y1) + MazeData.GRID_STROKE);
      setEndX(MazeData.calcGridXFloat(x2));
      setEndY(MazeData.calcGridYFloat(y2) - MazeData.GRID_STROKE);
    }
    else { // horizontal line
      setStartX(MazeData.calcGridXFloat(x1) + MazeData.GRID_STROKE);
      setStartY(MazeData.calcGridYFloat(y1));
      setEndX(MazeData.calcGridXFloat(x2) - MazeData.GRID_STROKE);
      setEndY(MazeData.calcGridYFloat(y2));
    }
//  } // end postinit
  }
  
}
