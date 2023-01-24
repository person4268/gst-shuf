package gst.shuf;

import edu.wpi.first.shuffleboard.api.components.ResizableImageView;
import edu.wpi.first.shuffleboard.api.data.SimpleDataType;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.freedesktop.gstreamer.Bin;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Pipeline;
import org.freedesktop.gstreamer.fx.FXImageSink;

import java.nio.channels.Pipe;

@Description(name = "GstWidget", summary = "GStreamer Pipeline Widget", dataTypes = String.class)
@ParametrizedController("gstwidget.fxml")
public class GstWidget extends SimpleAnnotatedWidget<String> {

    @FXML
    private StackPane root;

    @FXML
    private ResizableImageView imageView;
    Pipeline pipeline;

    @FXML
    private void initialize() {
        FXImageSink imageSink = new FXImageSink();
        imageSink.requestFrameSize(640, 480);
        ChangeListener<String> t = (obs, oldVal, newVal)->{

        };
        dataOrDefault.addListener(t);

        Bin bin = Gst.parseBinFromDescription("videotestsrc ! videoconvert", true);
        pipeline = new Pipeline();
        pipeline.addMany(bin, imageSink.getSinkElement());
        Pipeline.linkMany(bin, imageSink.getSinkElement());

        imageView.imageProperty().bind(imageSink.imageProperty());
        pipeline.play();
        dataOrDefault.subscribe((s)->{

        });
    }

    @Override
    public Pane getView() {
        return root;
    }
}
