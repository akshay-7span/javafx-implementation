package com.javaFX.recorder;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
public class ScreenRecorder extends Application{
    private ScreenRecordingService recordingService;
    private Button startButton;
    private Button stopButton;
    private Label timerLabel;
    private Timeline timer;

    @Override
    public void start(Stage primaryStage) {
        recordingService = new ScreenRecordingService();

        startButton = new Button("Start Recording");
        startButton.setOnAction(e -> {
            recordingService.startScreenRecording();
            startButton.setDisable(true);
            stopButton.setDisable(false);
            timer.play(); // Start the timer when recording starts
        });

        stopButton = new Button("Stop Recording");
        stopButton.setOnAction(e -> {
            recordingService.stopRecording();
            stopButton.setDisable(true);
            startButton.setDisable(false);
            timer.stop(); // Stop the timer when recording stops
        });
        stopButton.setDisable(true);

        timerLabel = new Label("Recording time: 0 seconds");

        VBox root = new VBox(10, startButton, stopButton, timerLabel);
        root.setPrefSize(300, 200);
        root.setSpacing(10);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Screen Recording App");
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> {
            // Ensure recording is stopped when closing the application
            recordingService.stopRecording();
            Platform.exit();
            System.exit(0);
        });

        // Initialize the timer to update the recording duration
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            long elapsedTime = recordingService.getRecordingDuration() / 1000; // Convert to seconds
            timerLabel.setText("Recording time: " + elapsedTime + " seconds");
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class ScreenRecordingService {
        private FFmpegFrameRecorder recorder;
        private long startTime;
        private boolean recording;
        private Robot robot;

        public ScreenRecordingService() {
            try {
                // Load FFmpeg
                Loader.load(org.bytedeco.ffmpeg.ffmpeg.class);
                avutil.av_log_set_level(avutil.AV_LOG_ERROR);
                robot = new Robot();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void startScreenRecording() {
            try {
                String downloadFolderPath = System.getProperty("user.home") + "/Downloads/";
                String videoFilePath = downloadFolderPath + "recorded_video.mp4";

                recorder = new FFmpegFrameRecorder(videoFilePath, 1280, 720);
                recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
                recorder.setFrameRate(10); // Adjust frame rate as needed
                recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P); // Set pixel format for better compatibility
                recorder.setVideoBitrate(2000000); // Adjust video bitrate as needed
                recorder.setFormat("mp4");
                recorder.setVideoOption("preset", "ultrafast"); // Set video option for faster encoding


                // Start the recorder
                recorder.start();

                startTime = System.currentTimeMillis(); // Record start time
                recording = true;

                // Start a new thread for capturing and recording frames
                new Thread(() -> {
                    try {
                        while (recording) {
                            long frameStartTime = System.currentTimeMillis();
                            BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                            recordFrame(image); // Pass the captured frame to the recordFrame method
                            long frameEndTime = System.currentTimeMillis();

                            // Calculate sleep time to ensure frame rate consistency
                            long sleepTime = (long) (1000 / recorder.getFrameRate() - (frameEndTime - frameStartTime));
                            if (sleepTime > 0) {
                                Thread.sleep(sleepTime);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void stopRecording() {
            recording = false; // Set recording flag to false to stop the recording thread
            if (recorder != null) {
                try {
                    recorder.stop();
                    recorder.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public long getRecordingDuration() {
            return System.currentTimeMillis() - startTime;
        }

        public void recordFrame(BufferedImage image) {
            if (recorder != null) {
                try {
                    Frame frame = convertToDesiredFormat(image);
                    recorder.record(frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private Frame convertToDesiredFormat(BufferedImage image) {
            // Convert BufferedImage to Frame
            Java2DFrameConverter converter = new Java2DFrameConverter();
            return converter.getFrame(image, 1);
        }
    }
}
