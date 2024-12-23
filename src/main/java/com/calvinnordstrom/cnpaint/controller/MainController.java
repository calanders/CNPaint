package com.calvinnordstrom.cnpaint.controller;

import com.calvinnordstrom.cnpaint.adjustment.AutoLevel;
import com.calvinnordstrom.cnpaint.adjustment.Grayscale;
import com.calvinnordstrom.cnpaint.adjustment.InvertAlpha;
import com.calvinnordstrom.cnpaint.adjustment.InvertColors;
import com.calvinnordstrom.cnpaint.model.MainModel;
import com.calvinnordstrom.cnpaint.tool.ToolManager;
import com.calvinnordstrom.cnpaint.tool.ToolType;
import com.calvinnordstrom.cnpaint.util.ServiceLocator;
import com.calvinnordstrom.cnpaint.view.MainView;
import com.calvinnordstrom.cnpaint.view.adjustment.HueSaturation;
import com.calvinnordstrom.cnpaint.view.stage.DefaultStage;
import com.calvinnordstrom.cnpaint.view.stage.ProgressStage;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {
    private final MainModel model;
    private final MainView view;

    public MainController(MainModel model, MainView view) {
        this.model = model;
        this.view = view;

        initEventHandlers();
    }

    private void initEventHandlers() {
        ServiceLocator serviceLocator = ServiceLocator.getInstance();

        serviceLocator.getMenuItem("exit").setOnAction(_ -> exit());
        serviceLocator.getMenuItem("grayscale").setOnAction(_ -> {
            String text = serviceLocator.getMenuItem("grayscale").getText();
            Task<Void> task = Grayscale.apply(view.getCurrentImage());
            initTask(text, task);
        });
        serviceLocator.getMenuItem("auto-level").setOnAction(_ -> {
            String text = serviceLocator.getMenuItem("auto-level").getText();
            Task<Void> task = AutoLevel.apply(view.getCurrentImage());
            initTask(text, task);
        });
        serviceLocator.getMenuItem("invert-colors").setOnAction(_ -> {
            String text = serviceLocator.getMenuItem("invert-colors").getText();
            Task<Void> task = InvertColors.apply(view.getCurrentImage());
            initTask(text, task);
        });
        serviceLocator.getMenuItem("invert-alpha").setOnAction(_ -> {
            String text = serviceLocator.getMenuItem("invert-alpha").getText();
            Task<Void> task = InvertAlpha.apply(view.getCurrentImage());
            initTask(text, task);
        });
        serviceLocator.getMenuItem("hue-saturation").setOnAction(_ -> {
            String text = serviceLocator.getMenuItem("hue-saturation").getText();
            initStage(text, new Scene(new HueSaturation()));
        });

        serviceLocator.getNode("pencil-tool").setOnMouseClicked(_ -> setTool(ToolType.PENCIL));
        serviceLocator.getNode("paintbrush-tool").setOnMouseClicked(_ -> setTool(ToolType.PAINTBRUSH));
        serviceLocator.getNode("eraser-tool").setOnMouseClicked(_ -> setTool(ToolType.ERASER));
    }

    private void initTask(String text, Task<Void> task) {
        ProgressStage stage = new ProgressStage(getStage(), text);
        stage.show();
        task.progressProperty().addListener((_, _, newValue) -> {
            stage.setProgress((double) newValue);
        });
        task.setOnSucceeded(_ -> {
            view.drawImage();
            stage.dispose();
        });
        task.setOnFailed(_ -> {
            System.out.println("Failed?");
        });
    }

    private void initStage(String title, Scene scene) {
        DefaultStage stage = new DefaultStage(getStage(), title);
        stage.setScene(scene);
        stage.show();
    }

    private Stage getStage() {
        return (Stage) view.getScene().getWindow();
    }

    public void setTool(ToolType toolType) {
        ToolManager tm = ToolManager.getInstance();
        tm.setTool(toolType);
    }

    public void exit() {
        Platform.exit();
    }

    public Parent getView() {
        return view;
    }
}
