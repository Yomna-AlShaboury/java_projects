/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Dialog;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 *
 * @author yomna
 */
public class NotePad extends Application{
    
    MenuBar bar;
    
    Menu file;
    Menu edit;
    Menu view;
    Menu help;
    
    MenuItem f1New;
    MenuItem f2Open;
    MenuItem f3Save;
    MenuItem f4Exit;
    MenuItem e1Undo;
    MenuItem e2Redo;
    MenuItem e3Cut;
    MenuItem e4Copy;
    MenuItem e5Paste;
    MenuItem e6Delete;
    MenuItem e7SelectAll;
    MenuItem h1About;
    MenuItem h2Compile;
    
    CheckMenuItem terminal;
    
    TextArea txt;
    TextArea output;
    Label copyRight;
    SeparatorMenuItem sep1, sep2, sep3;
    BorderPane pane;
    Scene s;
    String myClipBoard;
    FileChooser fileChooser;
    Dialog<ButtonType> dialog;
    
    ButtonType save;
    ButtonType dontSave;
    ButtonType cancel;  
    FileReader fr;
    BufferedReader br;
    FileWriter fw;
    BufferedWriter bw;
    boolean saved;
    String fileName = "";
    String path = "";

    @Override
    public void init() {
        saved = true;
        myClipBoard = "";
        bar = new MenuBar();    // menu bar
        // menus to be set in menu bar
        file = new Menu("File");
        edit = new Menu("Edit");
        view = new Menu("View");
        help = new Menu("Help");
        // items in menu file
        f1New = new MenuItem("New");
        f2Open = new MenuItem("Open");
        f3Save = new MenuItem("Save");
        f4Exit = new MenuItem("Exit");
        // items in menu edit
        e1Undo = new MenuItem("Undo");
        e2Redo = new MenuItem("Redo");
        e3Cut = new MenuItem("Cut");
        e4Copy = new MenuItem("Copy");
        e5Paste = new MenuItem("Paste");
        e6Delete = new MenuItem("Delete");
        e7SelectAll = new MenuItem("Select All");
        // items in menu help
        h1About = new MenuItem("About");
        h2Compile = new MenuItem("Compile + Run");
        // view
        terminal = new CheckMenuItem("Show Terminal");
        // serparator menu item
        sep1 = new SeparatorMenuItem();
        sep2 = new SeparatorMenuItem();
        sep3 = new SeparatorMenuItem();
        // text area
        txt = new TextArea();
        // copyright label
        copyRight = new Label("Release Date: 14 DEC 2021          ©YSSH");
        // pane type --> border
        pane = new BorderPane();
        // adding menus items
        file.getItems().addAll(f1New, f2Open, f3Save, sep1, f4Exit);
        edit.getItems().addAll(e1Undo, e2Redo, sep2, e3Cut, e4Copy, e5Paste, e6Delete, sep3, e7SelectAll);
        view.getItems().add(terminal);
        help.getItems().addAll(h1About, h2Compile);

        // adding menus to menu bar
        bar.getMenus().addAll(file, edit, view, help);
        // adding bar, text area, label to borderpane
        pane.setTop(bar);
        pane.setCenter(txt);
        output = new TextArea();
        output.setEditable(false);
        output.setVisible(false);
//        output.ba
        output.setId("out");
        pane.setBottom(new VBox(output, copyRight));
        
        // shortcut for new
        f1New.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        f2Open.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        f3Save.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        f4Exit.setAccelerator(KeyCombination.keyCombination("Alt+Shift+F4"));

        e1Undo.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+Z"));
        e2Redo.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+Y"));
        e3Cut.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+X"));
        e4Copy.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+C"));
        e5Paste.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+V"));
        e6Delete.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+D"));
        e7SelectAll.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+A"));

        h1About.setAccelerator(KeyCombination.keyCombination("Ctrl+F1"));
        h2Compile.setAccelerator(KeyCombination.keyCombination("Ctrl+F9"));

        terminal.setAccelerator(KeyCombination.keyCombination("Ctrl+t"));
        
        // file chooser
        fileChooser = new FileChooser();
        save = new ButtonType("Save");
        dontSave = new ButtonType("Don't Save");
        cancel = new ButtonType("Cancel"); 
    }
    @Override
    public void start(Stage primaryStage) { 
        s = new Scene(pane, 300, 400);
        e1Undo.setDisable(true);
        e2Redo.setDisable(true);
        e3Cut.setDisable(true);
        e4Copy.setDisable(true);
        e5Paste.setDisable(true);
        e6Delete.setDisable(true);
        e7SelectAll.setDisable(true);        
//        txt.

        if(output.isVisible())
            output.setPrefHeight(s.getHeight()/5);
        else
            output.setPrefHeight(0);

        File f = new File("src/Style.css");
        s.getStylesheets().clear();
        s.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

        s.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(output.isVisible())
                    output.setPrefHeight(s.getHeight()/5);
                else
                    output.setPrefHeight(0);                
            }
        });
        output.visibleProperty().addListener( new InvalidationListener(){

            @Override
            public void invalidated(Observable observable) {
                if(output.isVisible())
                    output.setPrefHeight(s.getHeight()/5);
                else
                    output.setPrefHeight(0);
            }
        });

        primaryStage.setOnCloseRequest(evt -> {
            // prevent window from closing
            evt.consume();
            f4Exit.fire();
        });
        // dialoge
        dialog = new Dialog<ButtonType>();
        dialog.setTitle("Save");
        dialog.setContentText("Do you want to save last changes?");
        dialog.getDialogPane().getButtonTypes().addAll(save, dontSave, cancel);

        txt.setOnMouseMoved(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(!txt.isUndoable())
                    e1Undo.setDisable(true);
                else
                    e1Undo.setDisable(false);

                if(!txt.isRedoable())
                    e2Redo.setDisable(true);
                else
                    e2Redo.setDisable(false);
                if(myClipBoard.length() == 0)
                    e5Paste.setDisable(true);
                else
                    e5Paste.setDisable(false);
                    
                if(txt.getSelectedText().length() == 0){
                    e3Cut.setDisable(true);
                    e4Copy.setDisable(true);
                    e6Delete.setDisable(true);
                }
                else{
                    e3Cut.setDisable(false);
                    e4Copy.setDisable(false);
                    e6Delete.setDisable(false);
                }
                
                if(txt.getText().length() == 0)
                    e7SelectAll.setDisable(true);
                else
                    e7SelectAll.setDisable(false);
            }
            
        });
        txt.setOnKeyTyped(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                
                saved = false;
                primaryStage.setTitle("FX NotePad "+fileName + "*");                
                
                if(!txt.isUndoable())
                    e1Undo.setDisable(true);
                else
                    e1Undo.setDisable(false);

                if(!txt.isRedoable())
                    e2Redo.setDisable(true);
                else
                    e2Redo.setDisable(false);
                if(myClipBoard.length() == 0)
                    e5Paste.setDisable(true);
                else
                    e5Paste.setDisable(false);
                    
                if(txt.getSelectedText().length() == 0){
                    e3Cut.setDisable(true);
                    e4Copy.setDisable(true);
                    e6Delete.setDisable(true);
                }
                else{
                    e3Cut.setDisable(false);
                    e4Copy.setDisable(false);
                    e6Delete.setDisable(false);
                }
                
                if(txt.getText().length() == 0)
                    e7SelectAll.setDisable(true);
                else
                    e7SelectAll.setDisable(false);
            }
        });

        
        terminal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(terminal.isSelected())
                    output.setVisible(true);
                else
                    output.setVisible(false);                    
            }
        });
        
        f1New.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

//                if(txt.getText().length() == 0 && !txt.isUndoable()){                    
//                    System.out.println("E");
//                }
                if(!saved){
                    Optional<ButtonType> result = dialog.showAndWait();
//                    System.out.println(result.get().getButtonData());
                    if(!result.isPresent()){
                    }

                    else if(result.get() == save){
                        f3Save.fire();
                        if(saved){
                            txt.clear();              
                            fileName = "";
                            path = "";
                        }

                    }
                    else if(result.get() == dontSave){
                        txt.clear();
                        saved = true;
                        fileName = "";
                        path = "";
                    }
                    else if(result.get() == cancel){
                        dialog.close();
                    }                
                }
                primaryStage.setTitle("FX NotePad "+fileName);
            }
        });

        f2Open.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                fileChooser.showOpenDialog(primaryStage);
                if(!saved)
                    f1New.fire();
                if(saved){
                    txt.clear();
                    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
                    try{
                        File f = fileChooser.showOpenDialog(primaryStage);
                        fileName = f.getName();
                        path = f.getParent();
                        fr = new FileReader(f);
                        br = new BufferedReader(fr);
                        String line;
                        do{
                            line = br.readLine();
                            if(line != null)
                                txt.appendText(line+"\n");

                        }while(line != null);
                        br.close();
                        fr.close();
                        saved = true;
                        primaryStage.setTitle("FX NotePad "+fileName);                    
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }
                }
            }
        });
        
        f3Save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                fileChooser.showSaveDialog(primaryStage);
                try{
                    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
                    File f = fileChooser.showSaveDialog(primaryStage);
                    fileName = f.getName();
                    path = f.getParent();
                    fw = new FileWriter(f);
                    bw = new BufferedWriter(fw);
                    bw.write(txt.getText());
                    bw.close();
                    fw.close();
                    saved = true;
                    primaryStage.setTitle("FX NotePad "+fileName);
                }
                catch(Exception e){
                    System.out.println(e);
                }                
            }
        });
        
        f4Exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                Platform.exit();
//                if(txt.getText().length() == 0 && !txt.isUndoable()){                    
//                    primaryStage.close();
//                }
                if(!saved){
                    Optional<ButtonType> result = dialog.showAndWait();
                    if(!result.isPresent()){
                    }
                    else if(result.get() == save){
                        f3Save.fire();
                        if(saved)
                            Platform.exit();
                    }
                    else if(result.get() == dontSave){
//                        primaryStage.close();
                        Platform.exit();

                    }
                    else if(result.get() == cancel){
                        dialog.close();
                    }                
                }
                else
                    Platform.exit();
            }
        });
        
        e1Undo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(txt.isUndoable()){
                    txt.undo();
                }
            }
        });
        
        e2Redo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(txt.isRedoable()){
                    txt.redo();
                }
            }
        });

        e3Cut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                IndexRange r = txt.getSelection();
                myClipBoard = txt.getSelectedText();
                txt.deleteText(r);
                
//                txt.cut();
            }
        });
        
        e4Copy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myClipBoard = txt.getSelectedText();  
                
//                txt.copy();
            }
        });
        
        e5Paste.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                txt.insertText(txt.getCaretPosition(), myClipBoard);

//                txt.paste();
            }
        });
        
        e6Delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                IndexRange r = txt.getSelection();
                txt.deleteText(r);
            }
        });
        
        e7SelectAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                txt.selectAll();
            }
        });
        
        h1About.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                System.out.println("about");
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("About");
                alert.setHeaderText("FX NotePad");
                alert.setContentText("Developed by YSSH.");
                alert.showAndWait();
//                if (result.get() == ButtonType.OK){
//                if (alert.showAndWait().get() == ButtonType.OK){
//                    System.out.println("Save fistly");
//                }
//                else {
//                        /*No changes had done*/                       
//                    System.out.println("cancel");
//                }

            }
        });
        
        h2Compile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String code = txt.getText();
                output.clear();
                terminal.setSelected(true);
                output.setVisible(true);
                fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
//                String fileName = "Simple.java";
                if(!saved){
                    f3Save.fire();
                }
                if(saved){
                    try{
                        fw = new FileWriter(fileName);
                        bw = new BufferedWriter(fw);
                        bw.write(txt.getText());
                        bw.close();
                        fw.close();
                        saved = true;
                    }
                    catch(Exception e){
                        System.out.println(e);
                    }        

                    try {
//                        String g  = System.getProperty("user.dir").replace("\\", "//");
//                        Process p = Runtime.getRuntime().exec("javac "+ g + "/"+fileName);
//                        p = Runtime.getRuntime().exec("java Simple");

                        Process p = Runtime.getRuntime().exec("javac "+ path.replace("\\", "//") + "/"+fileName);
                        // java -cp "Desktop" Simple
                        p.waitFor();
                        p = Runtime.getRuntime().exec("java -cp \"" + path.replace("\\", "//") +"\" " + fileName.substring(0, fileName.lastIndexOf('.')));
//                        p = Runtime.getRuntime().exec("java " + path.replace("\\", "//") +"/" + "Simple");
//                        System.out.println("java " + path.replace("\\", "//") +"/Simple");
//                        System.out.println("java -cp \"" + path.replace("\\", "//") +"\" " + "Simple");

                        final BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        String line;
                        while ((line = is.readLine()) != null) {
                            System.out.println(line);
                            output.appendText(line+"\n");
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(NotePad.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(NotePad.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
//        s.seton
        primaryStage.setTitle("FX NotePad");
        primaryStage.setScene(s);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
