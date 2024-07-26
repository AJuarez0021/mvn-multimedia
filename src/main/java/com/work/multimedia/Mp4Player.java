package com.work.multimedia;

import java.io.File;
import java.util.Optional;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author linux
 */
public class Mp4Player extends Application {

    private MediaPlayer mediaPlayer;

    @Override
    public void start(Stage stage) throws Exception {

        Button openButton = new Button("Open File");
        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button stopButton = new Button("Stop");

        enableButton(true, playButton, pauseButton, stopButton);

        HBox controlBox = createHBox(openButton, playButton, pauseButton, stopButton);

        openButton.setOnAction(e -> {
            Optional<Media> media = openFile(stage);
            if (media.isPresent()) {
                mediaPlayer = new MediaPlayer(media.get());
                enableButton(false, playButton, pauseButton, stopButton);
                Scene scene = getScene(new MediaView(mediaPlayer), controlBox);
                stage.setScene(scene);
            }
        });
        playButton.setOnAction(e -> playVideo());
        pauseButton.setOnAction(e -> pauseVideo());
        stopButton.setOnAction(e -> {
            stopVideo();
            enableButton(true, playButton, pauseButton, stopButton);
        });

        Scene scene = getScene(new MediaView(), controlBox);
        stage.setTitle("MP4 Player");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    private void playVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    private void pauseVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    private void stopVideo() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.seek(Duration.ZERO);
        }
    }

    private HBox createHBox(Node... nodes) {
        HBox hBox = new HBox(10, nodes);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private VBox createVBox(Node... nodes) {
        VBox vBox = new VBox(10, nodes);
        vBox.setAlignment(Pos.CENTER);
        vBox.setBackground(Background.fill(Color.BLACK));
        return vBox;
    }

    private void enableButton(boolean enable, Node... nodes) {
        for (Node node : nodes) {
            node.setDisable(enable);
        }
    }

    private Scene getScene(MediaView mediaView, HBox controlBox) {
        VBox parent = createVBox(mediaView, controlBox);
        return new Scene(parent, 800, 600);
    }

    private Optional<Media> openFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Media To Play");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4")
        );
        File file = fileChooser.showOpenDialog(stage);
        Optional<Media> optMedia = Optional.empty();
        if (file == null) {
            return optMedia;
        }
        String mp4Path = file.toURI().toString();
        return Optional.of(new Media(mp4Path));
    }

    @Override
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
