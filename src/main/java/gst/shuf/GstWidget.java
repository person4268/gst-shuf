package gst.shuf;

import edu.wpi.first.shuffleboard.api.components.ResizableImageView;
import edu.wpi.first.shuffleboard.api.data.SimpleDataType;
import edu.wpi.first.shuffleboard.api.widget.Description;
import edu.wpi.first.shuffleboard.api.widget.ParametrizedController;
import edu.wpi.first.shuffleboard.api.widget.SimpleAnnotatedWidget;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

@Description(name = "GstWidget", summary = "GStreamer Pipeline Widget", dataTypes = String.class)
@ParametrizedController("gstwidget.fxml")
public class GstWidget extends SimpleAnnotatedWidget<String> {

    @FXML
    private Pane root;

    @FXML
    private ResizableImageView imageView;

    public GstWidget() {

    }

    @Override
    public Pane getView() {
        return root;
    }
}
