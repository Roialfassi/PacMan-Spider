package View;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * @author Eagles
 * @author 76ers
 */
public class ScoreText extends Parent {


//  override var font = Font { size: 11 };
  private static final Font SCORE_FONT = new Font(11);
//  override var fill = Color.YELLOW;
  private static final Color SCORE_FILL = Color.YELLOW;
  private static final int DISPLAY_TIME = 2;
  private final Text text;

  private Timeline timeline;

  public ScoreText(String s, boolean isVisible) { //patweb
    text = new Text(s);
    text.setFont(SCORE_FONT);
    text.setFill(SCORE_FILL);
    createTimeline();
    getChildren().add(text);
    setVisible(isVisible);
  }

  private void createTimeline() {
//  var timeline= Timeline {
//    repeatCount: 1
//    keyFrames: [
//      KeyFrame {
//        time: 2s
//        action: function() {
//          visible = false;
//        }
//      }
//    ]
//  };
    timeline = new Timeline();
    timeline.setCycleCount(1);
    KeyFrame kf = new KeyFrame(Duration.seconds(DISPLAY_TIME), new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        setVisible(false);
      }
    });
    timeline.getKeyFrames().add(kf);
  }

  public void showText() {
    setVisible(true);
    timeline.playFromStart();
  }

  public void setX(int x) {
    text.setX(x);
  }

  public void setY(int y) {
    text.setY(y);
  }
  public void setText(String str){
    this.text.setText(str);
  }
}
