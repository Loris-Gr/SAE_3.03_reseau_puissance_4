package test.java.com.cdal;

import main.java.com.cdal.AppliJeu;
import main.java.com.cdal.Change;
import main.java.com.cdal.ControleurClavier;
import main.java.com.cdal.ModeleJeu;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class ControleurClavierTest {
    private ControleurClavier controleurClavier;
    private AppliJeu mockVueJeu;
    private ModeleJeu mockModeleJeu;

    @BeforeEach
    public void setUp() {
        mockVueJeu = Mockito.mock(AppliJeu.class);
        mockModeleJeu = Mockito.mock(ModeleJeu.class);
        controleurClavier = new ControleurClavier(mockVueJeu, mockModeleJeu);
    }

    @Test
    public void testHandleLeftKey() {
        KeyEvent leftKeyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.LEFT, false, false, false, false);
        
        when(mockModeleJeu.estGagnee()).thenReturn(false);
        AppliJeu.WAITING = false;
        
        controleurClavier.handle(leftKeyEvent);
        
        verify(mockModeleJeu).selectionGauche();
        verify(mockVueJeu).maj(Change.LEFT);
    }

    @Test
    public void testHandleRightKey() {
        KeyEvent rightKeyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.RIGHT, false, false, false, false);
        
        when(mockModeleJeu.estGagnee()).thenReturn(false);
        AppliJeu.WAITING = false;
        
        controleurClavier.handle(rightKeyEvent);
        
        verify(mockModeleJeu).selectionDroite();
        verify(mockVueJeu).maj(Change.RIGHT);
    }

    @Test
    public void testHandleEnterKey() {
        KeyEvent enterKeyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ENTER, false, false, false, false);
        
        when(mockModeleJeu.estGagnee()).thenReturn(false);
        AppliJeu.WAITING = false;
        
        controleurClavier.handle(enterKeyEvent);
        
        verify(mockModeleJeu).drop();
        verify(mockVueJeu).maj(Change.DROP);
    }

    @Test
    public void testHandleSpaceKey() {
        KeyEvent spaceKeyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.SPACE, false, false, false, false);
        
        when(mockModeleJeu.estGagnee()).thenReturn(false);
        AppliJeu.WAITING = false;
        
        controleurClavier.handle(spaceKeyEvent);
        
        verify(mockModeleJeu).drop();
        verify(mockVueJeu).maj(Change.DROP);
    }

    @Test
    public void testHandleKeyWhenGameWon() {
        KeyEvent anyKeyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ANY, false, false, false, false);
        
        when(mockModeleJeu.estGagnee()).thenReturn(true);
        
        controleurClavier.handle(anyKeyEvent);
        
        verify(mockModeleJeu, never()).selectionGauche();
        verify(mockModeleJeu, never()).selectionDroite();
        verify(mockModeleJeu, never()).drop();
        verify(mockVueJeu, never()).maj(any(Change.class));
    }

    @Test
    public void testHandleKeyWhenWaiting() {
        KeyEvent anyKeyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ANY, false, false, false, false);
        
        when(mockModeleJeu.estGagnee()).thenReturn(false);
        AppliJeu.WAITING = true;
        
        controleurClavier.handle(anyKeyEvent);
        
        verify(mockModeleJeu, never()).selectionGauche();
        verify(mockModeleJeu, never()).selectionDroite();
        verify(mockModeleJeu, never()).drop();
        verify(mockVueJeu, never()).maj(any(Change.class));
    }
}
