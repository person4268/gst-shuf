package gst.shuf;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
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
import org.freedesktop.gstreamer.State;
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
    private Image emptyImage;

    @FXML
    private void initialize() {
        ChangeListener<String> t = (obs, oldVal, newVal)->{
            System.out.println("[GstShuf] Change listener, old " + oldVal + " new, " + newVal + ", reconfiguring");
            parseJson(newVal);
        };
        dataOrDefault.addListener(t);
        System.out.println("[GstShuf] Initialized, current value is " + dataOrDefault.getValue());
        String pStr = dataOrDefault.getValue();
        parseJson(pStr);
    }

    private void parseJson(String str) {
        if(str == "" || str == null) { configurePipeline("", 0, 0); return; }
        var j = new JsonParser().parse(str).getAsJsonObject();
        var pipelineStr = j.get("pipeline").getAsString();
        if(pipelineStr == "") {
            pipelineStr = "videotestsrc ! videoconvert";
        }

        var x = j.get("x").getAsInt();
        if(x <= 0) {
            x = 640;
        }
        var y = j.get("y").getAsInt();
        if(y <= 0) {
            y = 480;
        }
        System.out.println("[GstShuf] Configuring with pipeline of `" + pipelineStr + "`, x = " + x + ", y = " + y);
        configurePipeline(pipelineStr, x, y);
    }

    private void configurePipeline(String pipeStr, int x, int y) {
        /* If pipeline is playing, stop it */
        if(pipeline != null && pipeline.getState(50) == State.PLAYING) {
            pipeline.stop();
        }

        if(pipeStr == null || pipeStr == "") {
            System.out.println("[GstShuf] Empty pipeline, using empty image");
            imageView.imageProperty().set(emptyImage);
            return;
        }

        /* Create our image sink */
        FXImageSink imageSink = new FXImageSink();
        imageSink.requestFrameSize(x, y);

        /* Parse and create our pipeline */
        Bin bin = Gst.parseBinFromDescription(pipeStr, true);

        pipeline = new Pipeline();
        pipeline.addMany(bin, imageSink.getSinkElement());
        Pipeline.linkMany(bin, imageSink.getSinkElement());

        /* Bind our image sink to the image */
        imageView.imageProperty().bind(imageSink.imageProperty());

        pipeline.play();
    }

    @Override
    public Pane getView() {
        return root;
    }
}
