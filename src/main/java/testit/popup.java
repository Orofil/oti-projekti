package testit;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.concurrent.ThreadLocalRandom;

public class popup extends Application {
    final Rectangle2D BOUNDS = Screen.getPrimary().getVisualBounds();
    final double SCRWIDTH = BOUNDS.getWidth();
    final double SCRHEIGHT = BOUNDS.getHeight();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Pop-up ikkuna
        Text text1 = new Text("Tämä on iso ikkuna!");
        Button button1 = new Button("Avaa uusi ikkuna");
        VBox vb1 = new VBox(text1, button1);
        vb1.setSpacing(15);
        vb1.setAlignment(Pos.CENTER);

        button1.setOnAction(e -> {
            // Luodaan uusi stage
            Stage stage2 = new Stage();
            /*
            WINDOW_MODAL tarkoittaa, että takana olevalla ikkunalla ei voi
            tehdä mitään kun ylempi ikkuna on auki. initOwner määrittelee (ehkä)
            mikä se takana oleva ikkuna on.
             */
            stage2.initModality(Modality.WINDOW_MODAL);
            stage2.initOwner(stage);

            Text text2 = new Text("Tämä on pienempi ikkuna!");
            Button button2 = new Button("Sulje ikkuna");
            VBox vb2 = new VBox(text2, button2);
            vb2.setSpacing(15);
            vb2.setAlignment(Pos.CENTER);

            // Muokataan alempaa ikkunaa
            text1.setText("Pienempi ikkuna on nyt auki");

            // Suljetaan ikkuna
            button2.setOnAction(e1 -> {
                stage2.close();
                text1.setText("Tämä on iso ikkuna!");
            });

            // Luodaan uusi scene
            Scene scene2 = new Scene(vb2, 200, 200);
            stage2.setScene(scene2);
            stage2.setTitle("Pieni ikkuna");
            stage2.show();

            // Voidaan myös muuttaa ikkunan sijaintia, tässä nyt ihan huvikseen satunnainen sijainti
            stage2.setX(ThreadLocalRandom.current().nextDouble(0, SCRWIDTH - 200));
            stage2.setY(ThreadLocalRandom.current().nextDouble(0, SCRHEIGHT - 200));

            // Testaa myös tätä
//            button2.setFocusTraversable(false);
//            button2.setOnMouseEntered(e1 -> {
//                stage2.setX(ThreadLocalRandom.current().nextDouble(0, SCRWIDTH - 200));
//                stage2.setY(ThreadLocalRandom.current().nextDouble(0, SCRHEIGHT - 200));
//            });
        });

        Scene scene1 = new Scene(vb1, 500, 500);
        stage.setScene(scene1);
        stage.setTitle("Iso ikkuna");
        stage.show();
    }
}
