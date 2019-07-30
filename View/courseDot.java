package View;

import View.Dot;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


public class courseDot extends Dot {

    public int x;
    public int y;
    //ת�?ונה ש�? הסוכריה
    public static Image candyImage;
    //קורדינטה �?ת�?ונה
    public SimpleIntegerProperty imageX;
    //קורדינטה �?ת�?ונה
    public SimpleIntegerProperty imageY;
    //protected ObjectBinding imageBinding;
    public ImageView candyNode;
    //יצירת טיי�? �?יי�? �?נקודה
    public Timeline timeline = new Timeline();

    @SuppressWarnings("static-access")
	public courseDot(int x, int y, int dotType, Image image) {
        this.x = x;
        this.y = y;
        this.dotType = dotType;
        this.candyImage = image;
        this.shouldStopAnimation = new SimpleBooleanProperty(false);
        //הצגת הת�?ונה ש�? הסוכריה ב�?ס�?
        imageX = new SimpleIntegerProperty(x);
        imageY = new SimpleIntegerProperty(y);
        candyNode = new ImageView(candyImage);
        candyNode.xProperty().bind(imageX.add(-13));
        candyNode.yProperty().bind(imageY.add(-13));
        //candyNode.imageProperty().bind(imageBinding);
        getChildren().add(candyNode);

    }

    public void createTimeline() {
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.25), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                doOneTick();
            }
        });

        timeline.getKeyFrames().add(kf);
    }

    //  public function playTimeline() {
    public void playTimeline() {
        if (timeline == null) {
//    timeline = createTimeline();
            createTimeline();
        }

        timeline.play();
    }
    public courseDot(int x, int y, int dotType) {
        super(x, y, dotType);
    }


}
