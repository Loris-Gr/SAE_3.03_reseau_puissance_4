package main.java.com.cdal;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/**
 * Classe principale de l'application du jeu Puissance 4.
 * Cette classe étend la classe Application de JavaFX et représente l'interface graphique du jeu.
 */
public class AppliJeu extends Application{
    private ModeleJeu modele;
    private Grille grille;
    private Label tourL;
    private Label scoreJ;
    private Label scoreR;
    public static boolean WAITING;

    /**
     * Constructeur de la classe AppliJeu
     */
    @Override
    public void init() throws Exception {
        this.modele = new ModeleJeu(Equipe.JAUNE);
        WAITING = false;
    }

    /**
     * Méthode start de la classe Application
     * @param stage La fenêtre de l'application
     */
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        BorderPane top = new BorderPane();
        top.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        Label titre = new Label("Puissance 4");
        Button parametres = new Button("Paramètres");
        parametres.setFocusTraversable(false);
        titre.setFont(new Font(40));
        top.setLeft(titre);
        top.setRight(parametres);
        root.setTop(top);
        root.setCenter(fenetreJeu());
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(new ControleurClavier(this, this.modele));
        stage.setTitle("Puissance 4");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Créer la fenetre du jeu
     * @return Pane de la fenetre de jeu
     */
    public Pane fenetreJeu(){
        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(10));
        this.tourL = new Label("Au tour des "+this.modele.getJoueur().getNom());
        this.tourL.setFont(new Font(20));
        bp.setTop(tourL);

        Label s = new Label("Score");
        s.setFont(new Font(20));
        
        this.scoreJ = new Label("0");
        this.scoreR = new Label("0");
        this.scoreJ.setFont(new Font(20));
        this.scoreR.setFont(new Font(20));
        this.scoreJ.setTextFill(Equipe.JAUNE.getColor());
        this.scoreR.setTextFill(Equipe.ROUGE.getColor());
        
        VBox vb = new VBox();
        vb.getChildren().addAll(s, this.scoreJ, this.scoreR);
        bp.setRight(vb);
        
        this.grille = new Grille();
        bp.setCenter(this.grille);
        return bp;
    }
    /**
     * Créer une fenetre de paramètres
     * @return La fenetre de paramètres
     */
    public Dialog<List<String>> dialogParametre(){
        return null;
    }
    /**
     * Met à jour la grille et le tour de jeu
     * @param change Le changement effectué
     */
    public void maj(Change change){
        this.grille.maj(this.modele, change);
        this.tourL.setText("Au tour des " + this.modele.getJoueur().getNom());
        if(this.modele.estPerdu()){
            Alert popup = popUpMatchNul();
            popup.setOnCloseRequest((e) -> {
                this.reset();
                this.grille.maj(this.modele, Change.RIEN);
                this.tourL.setText("Au tour des " + this.modele.getJoueur().getNom());
            });
            popup.showAndWait();
        }else if(this.modele.estGagnee()){
            Alert popup = popUpGagnee(this.modele.getGagnant());
            popup.setOnCloseRequest((e) -> {
                this.reset();
                this.grille.maj(this.modele, Change.RIEN);
                this.tourL.setText("Au tour des " + this.modele.getJoueur().getNom());
            });
            if(this.modele.getGagnant() == Equipe.JAUNE){
                this.scoreJ.setText(Integer.toString(Integer.parseInt(this.scoreJ.getText())+1));
            }else if(this.modele.getGagnant() == Equipe.ROUGE){
                this.scoreR.setText(Integer.toString(Integer.parseInt(this.scoreR.getText())+1));
            }
            popup.showAndWait();
        }
    }
    /**
     * Affiche une popup pour indiquer que l'équipe gagnante a gagnée
     * @param gagnant L'équipe gagnante
     * @return La popup
     */
    public Alert popUpGagnee(Equipe gagnant){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Les "+gagnant.getNom()+" ont gagnés !");
        alert.setHeaderText("L'équipe des "+gagnant.getNom()+" a gagné !");
        return alert;
    }

    /**
     * Affiche une popup pour indiquer que le match est nul
     * @return La popup
     */
    public Alert popUpMatchNul(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Match nul");
        alert.setHeaderText("Match nul, les deux équipes ont remplies le plateau");
        return alert;
    }

    /**
     * Réinitialise le jeu
     */
    public void reset(){
        this.modele.reset();
        this.grille.reset();
    }

    /**
     * Retourne le modèle du jeu
     * @return Le modèle du jeu
     */
    public Grille getGrille() {
        return grille;
    }

    /**
     * Retourne le modèle du jeu
     * @return Le modèle du jeu
     */
    public static void main(String[] args) {
        launch(AppliJeu.class);
    }
}