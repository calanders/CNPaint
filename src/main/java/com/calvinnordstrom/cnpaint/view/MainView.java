package com.calvinnordstrom.cnpaint.view;

import com.calvinnordstrom.cnpaint.Main;
import com.calvinnordstrom.cnpaint.tool.ToolManager;
import com.calvinnordstrom.cnpaint.tool.ToolType;
import com.calvinnordstrom.cnpaint.tool.control.ToolControlFactoryRegistry;
import com.calvinnordstrom.cnpaint.util.ImageUtils;
import com.calvinnordstrom.cnpaint.util.ServiceLocator;
import com.calvinnordstrom.cnpaint.view.node.EditorTab;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainView extends BorderPane {
    private final ToolsPane toolsPane = new ToolsPane();
    private final TabPane tabPane = new TabPane();

    public MainView() {
        initTop();
        initLeft();
        initCenter();
        initRight();
        initBottom();
    }

    private void initTop() {
        Menu fileMenu = new Menu("File");
        MenuItem close = new MenuItem("Close");
        ServiceLocator.getInstance().register("close", close);
        fileMenu.getItems().add(close);

        Menu editMenu = new Menu("Edit");

        Menu viewMenu = new Menu("View");

        MenuBar menuBar = new MenuBar(fileMenu, editMenu, viewMenu);

        ToolManager tm = ToolManager.getInstance();
        ComboBox<ToolType> comboBox = new ComboBox<>();
        for (ToolType toolType : tm.getToolKeys()) {
            comboBox.getItems().add(toolType);
        }
        comboBox.getSelectionModel().selectFirst();
        comboBox.getSelectionModel().selectedItemProperty().addListener((_, _, newValue) -> tm.setTool(newValue));

        ToolControlFactoryRegistry tcfr = ToolControlFactoryRegistry.getInstance();
        Pane toolControls = new Pane(tcfr.getToolControls(tm.getTool()));
        tm.toolProperty().addListener((_, _, newValue) -> {
            comboBox.getSelectionModel().select(tm.getToolType(newValue));
            toolControls.getChildren().setAll(tcfr.getToolControls(newValue));
        });

        setTop(new VBox(menuBar, new HBox(comboBox, toolControls)));
    }

    private void initLeft() {
        setLeft(toolsPane);
    }

    private void initCenter() {
        Image image1 = new Image(String.valueOf(Main.class.getResource("IMG_2878.PNG")));
        Image image2 = new Image(String.valueOf(Main.class.getResource("IMG_3698.JPG")));

        EditorPane editor1 = new EditorPane(ImageUtils.toWritableImage(image1));
        EditorPane editor2 = new EditorPane(ImageUtils.toWritableImage(image2));

        EditorTab one = new EditorTab("IMG_2878.PNG", editor1);
        EditorTab two = new EditorTab("IMG_3698.JPG", editor2);
        tabPane.getTabs().addAll(one, two);

        tabPane.getSelectionModel().selectedItemProperty().addListener(((_, _, newValue) -> {
            EditorPane editorPane = (EditorPane) newValue.getContent();
            setBottom(ServiceLocator.getInstance().getEditorControlsPane(editorPane));
        }));

        setCenter(tabPane);
    }

    private void initRight() {
        ControlsPane controls = new ControlsPane();
        setRight(controls);
    }

    private void initBottom() {
        EditorPane editorPane = (EditorPane) tabPane.getSelectionModel().selectedItemProperty().get().getContent();
        setBottom(ServiceLocator.getInstance().getEditorControlsPane(editorPane));
    }
}
