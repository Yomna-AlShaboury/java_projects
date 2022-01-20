/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */


import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
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
    Menu javaM;
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
    MenuItem j1Compile;
    MenuItem j2Run;
    
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
    boolean isSaved;
    String fileName = "Untitled";
    String path = "";
    Thread clip;
    String oldTxt = "";
    String newTxt = "";
    
    @Override
    public void stop() throws Exception {
        clip.stop();    // closes the thread
        super.stop(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void init() {
        // thread to get the string clipboard
        clip = new Thread(new Runnable(){
            @Override
            public void run() {
                while(true){
                    Toolkit toolkit = Toolkit.getDefaultToolkit();
                    Clipboard clipboard = toolkit.getSystemClipboard();
                    String result = "";
                    try {
                        result = (String) clipboard.getData(DataFlavor.stringFlavor);
                    } catch (Exception ex) {}
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {}
                    myClipBoard = result;
                }
            }
        });
        clip.start();
                
        isSaved = true;
        bar = new MenuBar();    // menu bar
        // menus to be set in menu bar
        file = new Menu("File");
        edit = new Menu("Edit");
        view = new Menu("View");
        javaM = new Menu("Java");
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
        // items in menu java
        j1Compile = new MenuItem("Compile");
        j2Run = new MenuItem("Run");
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
        javaM.getItems().addAll(j1Compile,j2Run);
        help.getItems().addAll(h1About);

        // adding menus to menu bar
        bar.getMenus().addAll(file, edit, view, javaM, help);
        // adding bar, text area, label to borderpane
        pane.setTop(bar);
        pane.setCenter(txt);
        output = new TextArea();
        output.setEditable(false);
        output.setVisible(false);
        output.setId("out");
        pane.setBottom(new VBox(output, copyRight));
        
        // shortcut for menu items
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
        j1Compile.setAccelerator(KeyCombination.keyCombination("Ctrl+F9"));
        j2Run.setAccelerator(KeyCombination.keyCombination("Ctrl+F10"));

        terminal.setAccelerator(KeyCombination.keyCombination("Ctrl+t"));
        
        // file chooser
        fileChooser = new FileChooser();
        save = new ButtonType("Save");
        dontSave = new ButtonType("Don't Save");
        cancel = new ButtonType("Cancel"); 
    }
    
    @Override
    public void start(Stage primaryStage) { 
        s = new Scene(pane, 600, 400);
        e1Undo.setDisable(true);
        e2Redo.setDisable(true);
        e3Cut.setDisable(true);
        e4Copy.setDisable(true);
        e5Paste.setDisable(true);
        e6Delete.setDisable(true);
        e7SelectAll.setDisable(true);        
        
        // set the terminal height as a ratio of the scene height
        if(output.isVisible())
            output.setPrefHeight(s.getHeight()/5);
        else
            output.setPrefHeight(0);

        // add css file
        File f = new File("src/Style.css");
        s.getStylesheets().clear();
        s.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));

        // reset the terminal height ratio whenever the scene hight is changed
        s.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(output.isVisible())
                    output.setPrefHeight(s.getHeight()/5);
                else
                    output.setPrefHeight(0);                
            }
        });

        // reset the terminal height ratio whenever the terminal visibility is changed
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
            // prevent window from closing before executing the exit routine
            evt.consume();
            f4Exit.fire();
        });
        // save dialoge
        dialog = new Dialog<ButtonType>();
        dialog.setTitle("Save");
        dialog.setContentText("Do you want to save last changes?");
        dialog.getDialogPane().getButtonTypes().addAll(save, dontSave, cancel);

        txt.setOnMouseMoved(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                setEn();                // in edit menu --> enable and disable menu items
            }
        });
        txt.setOnKeyTyped(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                // check if change happened in the text area --> not saved
                oldTxt = newTxt;
                newTxt = txt.getText();
                if(!newTxt.equals(oldTxt)){
                    isSaved = false;
                    primaryStage.setTitle("*" + fileName + " - FX NotePad"); // add "*" before file name if not saved
                }
                setEn();                // in edit menu --> enable and disable menu items
            }
        });
        
        terminal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // show and hide terminal
                if(terminal.isSelected())
                    output.setVisible(true);
                else
                    output.setVisible(false);                    
            }
        });
        
        f1New.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(!isSaved){
                    // if not saved --> show ask to save dialog
                    Optional<ButtonType> result = dialog.showAndWait();
                    if(!result.isPresent()){
                        // do nothing
                    }
                    else if(result.get() == save){
                        // if save button save is clicked --> save then initialize to a new document
                        f3Save.fire();
                        if(isSaved){
                            txt.clear();              
                            fileName = "Untitled";
                            path = "";
                        }
                    }
                    else if(result.get() == dontSave){
                        // if don't save is clicked --> re initialize to a new document without saving
                        txt.clear();
                        isSaved = true;
                        oldTxt = "";
                        newTxt = "";
                        fileName = "Untitled";
                        path = "";
                    }
                    else if(result.get() == cancel){
                        // if cancel is clicked --> just close the dialog and return
                        dialog.close();
                        return;
                    }                
                }
                else{
                    // if saved --> reinitialize to a new document
                    fileName = "Untitled";
                    path = "";
                    oldTxt= newTxt = "";
                    txt.clear();
                }
                primaryStage.setTitle(fileName + " - FX NotePad ");         // set the title
            }
        });

        f2Open.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // save the file is not saved
                if(!isSaved)
                    f1New.fire();
                // if saved show open dialog
                if(isSaved){
                    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
                    try{
                        File f = fileChooser.showOpenDialog(primaryStage);      // choose the file to open
                        fileName = f.getName();                                 // get file name
                        txt.clear();                                            // clear the text area
                        path = f.getParent();                                   // get directory path
                        fr = new FileReader(f);                                 // file reader
                        br = new BufferedReader(fr);                            // buffer reader to read the file
                        String line;
                        do{
                            line = br.readLine();                               // read lines
                            if(line != null)
                                txt.appendText(line+"\n");                      // set on text area

                        }while(line != null);
                        br.close();                                             // close the buffer reader
                        fr.close();                                             // close the file reader
                        isSaved = true;                                         // issaved flag is true
                        // initialize old and new txt
                        oldTxt = txt.getText();
                        newTxt = txt.getText();
                        primaryStage.setTitle(fileName + " - FX NotePad ");     // set the title
                    }
                    catch(Exception e){}
                }
            }
        });
        
        f3Save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
                    File f = fileChooser.showSaveDialog(primaryStage);          // choose the file name to be saved
                    fileName = f.getName();                                     // get file name
                    path = f.getParent();                                       // get directory path
                    fw = new FileWriter(f);                                     // file writer
                    bw = new BufferedWriter(fw);                                // buffer writer for the file writer
                    bw.write(txt.getText());                                    // write to buffer writer
                    bw.close();                                                 // close buffer writer
                    fw.close();                                                 // close file writer
                    isSaved = true;                                             // set the issaved flag to true
                    // initialize old and new txt
                    oldTxt = txt.getText();
                    newTxt = txt.getText();
                    primaryStage.setTitle(fileName + " - FX NotePad ");         // set the title
                }
                catch(Exception e){}                
            }
        });
        
        f4Exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!isSaved){
                    // if not saved --> show ask to save dialog
                    Optional<ButtonType> result = dialog.showAndWait();
                    if(!result.isPresent()){
                    }
                    else if(result.get() == save){
                        // if saved is clicked --> fire save menu item
                        f3Save.fire();
                        if(isSaved)
                            Platform.exit();        // exit if saved
                    }
                    else if(result.get() == dontSave){
                        Platform.exit();            // if don't save is clicked --> just exit!
                    }
                    else if(result.get() == cancel){
                        dialog.close();             // cancel is clicked --> just lcose the dialog!
                    }                
                }
                else
                    Platform.exit();                // if saved --> exit
            }
        });
        
        e1Undo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(txt.isUndoable()){
                    txt.undo();         // undo
                    isSaved = false;
                    primaryStage.setTitle("*" + fileName + " - FX NotePad"); 
                }
            }
        });
        
        e2Redo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(txt.isRedoable()){
                    txt.redo();         // redo
                    isSaved = false;
                    primaryStage.setTitle("*" + fileName + " - FX NotePad"); 
                }
            }
        });

        e3Cut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                txt.cut();              // cut
                isSaved = false;
                primaryStage.setTitle("*" + fileName + " - FX NotePad"); 
            }
        });
        
        e4Copy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                txt.copy();             // copy
            }
        });
        
        e5Paste.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                txt.paste();            // paste
                isSaved = false;
                primaryStage.setTitle("*" + fileName + " - FX NotePad"); 
            }
        });
        
        e6Delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                IndexRange r = txt.getSelection();
                txt.deleteText(r);      // delete
                isSaved = false;
                primaryStage.setTitle("*" + fileName + " - FX NotePad"); 
            }
        });
        
        e7SelectAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                txt.selectAll();        // select all
            }
        });
        
        h1About.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // alert to show about information
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("About");
                alert.setHeaderText("FX NotePad");
                alert.setContentText("Developed by YSSH.");
                alert.showAndWait();
            }
        });
        
        j1Compile.setOnAction(new EventHandler<ActionEvent>() {
            // compiles only
            @Override
            public void handle(ActionEvent event) {
                String code = txt.getText();
                if(code.isEmpty())
                    return;
                output.clear();                 // clear the terminal
                terminal.setSelected(true);     // set terminal as selected
                output.setVisible(true);        // show terminal if not shown
                fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
                if(!isSaved){
                    f3Save.fire();              // save if not saved
                }
                if(isSaved){
//                 
                    try {
                        // execute the process
                        Process p = Runtime.getRuntime().exec("javac "+ path.replace("\\", "//") + "/"+fileName);
                        // get error msgs
                        BufferedReader errinput = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        String line;
                        while ((line = errinput.readLine()) != null) {
                            output.appendText(line+"\n");
                        }
                        p.waitFor();    // wait for the process until finished
                    } catch (IOException ex) {}
                    catch (InterruptedException ex) {}
                }
            }
        });
        
        
        j2Run.setOnAction(new EventHandler<ActionEvent>() {
            // compiles again and runs
            @Override
            public void handle(ActionEvent event) {
                String code = txt.getText();
                if(code.isEmpty())
                    return;
                output.clear();                 // clear the terminal
                terminal.setSelected(true);     // set terminal as selected
                output.setVisible(true);        // show terminal if not shown
                fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
                if(!isSaved){
                    f3Save.fire();              // save if not saved
                }
                if(isSaved){

                    try {
                        // execute the process for compilation
                        Process p = Runtime.getRuntime().exec("javac "+ path.replace("\\", "//") + "/"+fileName);
                        // get error msgs
                        BufferedReader errinput = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        String line;
                        while ((line = errinput.readLine()) != null) {
                            output.appendText(line+"\n");
                        }
                        p.waitFor();            // wait for the process until finished
                        
                        // execute the process to run .class files
                        p = Runtime.getRuntime().exec("java -cp \"" + path.replace("\\", "//") +"\" " + fileName.substring(0, fileName.lastIndexOf('.')));
                        // get output lines
                        final BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        while ((line = is.readLine()) != null) {
                            output.appendText(line+"\n");
                        }
                    } catch (IOException ex) {}
                    catch (InterruptedException ex) {}
                }
            }
        });

        primaryStage.setTitle(fileName + " - FX NotePad");              // set title
        primaryStage.setScene(s);                                       // set scene
        primaryStage.show();                                            // show stage
    }
    void setEn(){
        // set edit menu items as enabled or disabled
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
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
