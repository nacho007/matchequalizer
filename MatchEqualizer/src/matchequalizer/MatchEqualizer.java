/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matchequalizer;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

public class MatchEqualizer extends Application {

    private static final ListView<Player> playersListView = new ListView<Player>();

    private static final ObservableList<Player> playersList = FXCollections.observableArrayList();

    private static final ListView<Player> teamListView = new ListView<Player>();

    private static final ObservableList<Player> teamList = FXCollections.observableArrayList();
    private static final GridPane rootPane = new GridPane();
    private static final GridPane matchPane = new GridPane();

    MediaPlayer mediaPlayer;
    private static final String SOUND_OFF = "Sound Off";
    private static final String SOUND_ON = "Sound On";

    Label team1 = new Label("Equipo 1");
    Label player1 = new Label("1");
    Label player2 = new Label("2");
    Label player3 = new Label("3");
    Label player4 = new Label("4");
    Label player5 = new Label("5");

    Label team2 = new Label("Equipo 2");
    Label player6 = new Label("6");
    Label player7 = new Label("7");
    Label player8 = new Label("8");
    Label player9 = new Label("9");
    Label player10 = new Label("10");
//    Label player11 = new Label("11");
//    Label player12 = new Label("12");
//   Label player13 = new Label("13");
//    Label player14 = new Label("14");

    ImageView leftArrow;
    ImageView rightArrow;
    Label equalizedTeamsCount;
    ArrayList<ArrayList<Player[]>> teams;
    int index = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Match Equalizer");

        initializeSound();

        initializeComponents();

        initializeListeners();

        buildGUI();

        populateData();

        Scene scene = new Scene(rootPane, 900, 625);
        scene.getStylesheets().add(MatchEqualizer.class.getResource("mycss.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeSound() {
        URL resource = getClass().getResource("music.mp3");
        Media media = new Media(resource.toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });

        mediaPlayer.play();
    }

    private void initializeComponents() {
        initializeListView(playersListView);
        initializeListView(teamListView);
    }

    private void initializeListView(ListView<Player> listView) {
        listView.setPrefSize(450, 490);
        listView.setEditable(false);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.setCellFactory(new StringListCellFactory());
    }

    String listDragging = "";

    private void initializeListeners() {

        playersListView.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Dragboard dragBoard = playersListView.startDragAndDrop(TransferMode.MOVE);

                ClipboardContent content = new ClipboardContent();

                if (playersListView.getSelectionModel().getSelectedItem() != null) {
                    content.putString(playersListView.getSelectionModel().getSelectedItem().getName());
                }

                dragBoard.setContent(content);
                event.consume();
            }
        });

        playersListView.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {

                String player = dragEvent.getDragboard().getString();

                if (!playersListView.getItems().contains(new Player(player))) {
                    int magic = 0;

                    for (int i = 0; i < teamList.size(); i++) {
                        if (teamList.get(i).getName().equals(player)) {
                            magic = teamList.get(i).getLevel();
                        }
                    }

                    playersListView.getItems().addAll(new Player(player, magic));
                    teamListView.getItems().removeAll(new Player(player));
                }

                if (teamList.size() > 9) {
                    btn.setDisable(false);
                } else {
                    btn.setDisable(true);
                }

                dragEvent.setDropCompleted(true);
            }
        });

        teamListView.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {

                String player = dragEvent.getDragboard().getString();

                if (!teamListView.getItems().contains(new Player(player))) {

                    if (teamListView.getItems().size() < 10) {
                        int magic = 0;

                        for (int i = 0; i < playersList.size(); i++) {
                            if (playersList.get(i).getName().equals(player)) {
                                magic = playersList.get(i).getLevel();
                            }
                        }

                        teamListView.getItems().addAll(new Player(player, magic));
                        playersListView.getItems().removeAll(new Player(player));

                    }
                }

                if (teamList.size() > 9) {
                    btn.setDisable(false);
                } else {
                    btn.setDisable(true);
                }

                dragEvent.setDropCompleted(true);
            }
        });

        teamListView.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Dragboard dragBoard = teamListView.startDragAndDrop(TransferMode.MOVE);

                ClipboardContent content = new ClipboardContent();

                if (teamListView.getSelectionModel().getSelectedItem() != null) {
                    content.putString(teamListView.getSelectionModel().getSelectedItem().getName());
                }

                dragBoard.setContent(content);
                event.consume();
            }
        });

        playersListView.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                dragEvent.acceptTransferModes(TransferMode.MOVE);
            }
        });

        playersListView.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
            }
        });

        playersListView.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
            }
        });

        playersListView.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
            }
        });

        teamListView.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                dragEvent.acceptTransferModes(TransferMode.MOVE);
            }
        });

        teamListView.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
            }
        });

        teamListView.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
            }
        });

    }

    public void printTeamList() {
        for (int i = 0; i < teamList.size(); i++) {
            System.out.println(teamList.get(i).getName());
        }
    }

    Button btn;
    Button btnSound;

    private void buildGUI() {
        rootPane.setPadding(new Insets(10));
        rootPane.setPrefHeight(30);
        rootPane.setPrefWidth(100);
        rootPane.setVgap(10);
        rootPane.setHgap(20);
        rootPane.setAlignment(Pos.TOP_CENTER);

        Label playersLabel = new Label("Todos");
        Label teamLabel = new Label("En cancha");

        rootPane.add(playersLabel, 0, 0);
        rootPane.add(playersListView, 0, 1);
        rootPane.add(teamLabel, 1, 0);
        rootPane.add(teamListView, 1, 1);

        GridPane soundAndEqualize = new GridPane();
        soundAndEqualize.setVgap(30);
        soundAndEqualize.setAlignment(Pos.CENTER);
        soundAndEqualize.setPadding(new Insets(0, 0, 25, 0));

        btn = new Button("Equalizer");
        btn.setMaxWidth(120);
        btn.setDisable(true);
        btn.setAlignment(Pos.CENTER);

        btnSound = new Button(SOUND_OFF);
        btnSound.setMaxWidth(120);
        btnSound.setAlignment(Pos.CENTER);

        soundAndEqualize.add(btn, 0, 0);
        soundAndEqualize.add(btnSound, 0, 1);

        rootPane.add(soundAndEqualize, 0, 2);

        team1.setPadding(new Insets(0, 0, 0, 30));
        player1.setPadding(new Insets(0, 0, 0, 30));
        player1.setMinHeight(40);
        player2.setPadding(new Insets(0, 0, 0, 30));
        player3.setPadding(new Insets(0, 0, 0, 30));
        player3.setMinHeight(40);
        player4.setPadding(new Insets(0, 0, 0, 30));
        player4.setMinHeight(40);
        player5.setPadding(new Insets(0, 0, 0, 30));
        player5.setMinHeight(40);
       
        
        team2.setPadding(new Insets(0, 0, 0, 30));
        player6.setPadding(new Insets(0, 0, 0, 30));
        player6.setMinHeight(40);
        player7.setPadding(new Insets(0, 0, 0, 30));
        player7.setMinHeight(40);
 
      

       
        player8.setPadding(new Insets(0, 0, 0, 30));
        player8.setMinHeight(40);
        player9.setPadding(new Insets(0, 0, 0, 30));
        player9.setMinHeight(40);
        player10.setPadding(new Insets(0, 0, 0, 30));
        player10.setMinHeight(40);
      /*  player11.setPadding(new Insets(0, 0, 0, 30));
        player11.setMinHeight(40);
        player12.setPadding(new Insets(0, 0, 0, 30));
        player12.setMinHeight(40);
        player13.setPadding(new Insets(0, 0, 0, 30));
        player13.setMinHeight(40);
        player14.setPadding(new Insets(0, 0, 0, 30));
        player14.setMinHeight(40);*/

        File file = new File("/Users/ignaciodeandreisdenis/NetBeansProjects/MatchEqualizer/src/matchequalizer/left.png");
        Image image = new Image(file.toURI().toString());
        leftArrow = new ImageView();
        leftArrow.setVisible(false);
        leftArrow.setFitHeight(40);
        leftArrow.setFitWidth(40);
        leftArrow.setImage(image);
        leftArrow.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("Left pressed");

                if (index > 0) {
                    index--;
                }
                equalizedTeamsCount.setText("(" + (index + 1) + ")");
                drawEqualizedTeam(teams.get(index));

                event.consume();
            }
        });

        matchPane.add(leftArrow, 0, 3);

        matchPane.add(team1, 1, 1);
        matchPane.add(player1, 1, 2);
        matchPane.add(player2, 1, 3);
        matchPane.add(player3, 1, 4);
        matchPane.add(player4, 1, 5);
        matchPane.add(player5, 1, 6);
       

        equalizedTeamsCount = new Label("-");
        equalizedTeamsCount.setPadding(new Insets(0, 0, 0, 30));
        matchPane.add(equalizedTeamsCount, 2, 0);

        matchPane.add(team2, 3, 1);        
      
         matchPane.add(player6, 3, 2);
        matchPane.add(player7, 3, 3);
        matchPane.add(player8, 3, 4);
        matchPane.add(player9, 3, 5);
        matchPane.add(player10, 3, 6);
      /*  matchPane.add(player11, 3, 5);
        matchPane.add(player12, 3, 6);
        matchPane.add(player13, 3, 7);
        matchPane.add(player14, 3, 8);*/

        File file2 = new File("/Users/ignaciodeandreisdenis/NetBeansProjects/MatchEqualizer/src/matchequalizer/right.png");
        Image image2 = new Image(file2.toURI().toString());
        rightArrow = new ImageView();
        rightArrow.setVisible(false);
        rightArrow.setFitHeight(40);
        rightArrow.setFitWidth(40);
        rightArrow.setImage(image2);
        rightArrow.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("Right pressed");

                if (index < teams.size() - 1) {
                    index++;
                }

                equalizedTeamsCount.setText("(" + (index + 1) + ")");
                drawEqualizedTeam(teams.get(index));

                event.consume();
            }
        });

        matchPane.add(rightArrow, 4, 3);

        rootPane.add(matchPane, 1, 2);

        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                index = 0;
                Object[] players = teamList.toArray();

                Logic logic = new Logic(players);
                teams = logic.equalize();

                if (teams.size() > 1) {

                    leftArrow.setVisible(true);
                    rightArrow.setVisible(true);
                    equalizedTeamsCount.setText("(1)");
                    drawEqualizedTeam(teams.get(0));

                } else {

                    leftArrow.setVisible(false);
                    rightArrow.setVisible(false);
                    equalizedTeamsCount.setText("(1)");
                    drawEqualizedTeam(teams.get(0));

                }

            }
        });

        btnSound.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                if (btnSound.getText().toString().equals(SOUND_OFF)) {
                    btnSound.setText(SOUND_ON);
                    mediaPlayer.pause();
                } else {
                    btnSound.setText(SOUND_OFF);
                    mediaPlayer.play();
                }

            }
        });

    }

    private void drawEqualizedTeam(ArrayList<Player[]> teams) {

        Player[] team1 = teams.get(0);
        Player[] team2 = teams.get(1);

        player1.setText(team1[0].getName());
        player2.setText(team1[1].getName());
        player3.setText(team1[2].getName());
        player4.setText(team1[3].getName());
        player5.setText(team1[4].getName());
        player6.setText(team2[0].getName());
        player7.setText(team2[1].getName());
        
        player8.setText(team2[2].getName());
        player9.setText(team2[3].getName());
        player10.setText(team2[4].getName());
     /*   player11.setText(team2[3].getName());
        player12.setText(team2[4].getName());
        player13.setText(team2[5].getName());
        player14.setText(team2[6].getName());*/

    }

    private void populateData() {
        playersList.addAll(
                new Player("Nacho", 90), new Player("Dani", 82), new Player("Gonza", 70),
                new Player("Nico Castro", 88), new Player("Beto", 73), new Player("Ruben", 70),
                new Player("Alvaro", 76), new Player("Nico Fornaro", 79), new Player("Pablov", 73),
                new Player("Dorian", 75), new Player("Rafa", 85), new Player("Dante", 76),
                new Player("Rocky", 58), new Player("Matteo", 70), new Player("Marcelo", 80),
                new Player("Maxi", 77), new Player("Alex", 85), new Player("Gabriel", 77),
                new Player("Fede Orellano", 78), new Player("Santiago", 66), new Player("Micky", 80)
                , new Player("Bruno", 70), new Player("Joaquin", 0), new Player("Amigo Dorian", 0)
                , new Player("Ismael", 82), new Player("Lucio",58)
        );

        playersListView.setItems(playersList);
        teamListView.setItems(teamList);
    }

    class StringListCellFactory implements Callback<ListView<Player>, ListCell<Player>> {

        @Override
        public ListCell<Player> call(ListView<Player> playerListView) {
            return new StringListCell();
        }

        class StringListCell extends ListCell<Player> {

            @Override
            protected void updateItem(Player player, boolean b) {
                super.updateItem(player, b);

                if (b || player == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(player.getName());
                }

            }
        }

    }
}
