package views;

import com.sun.xml.internal.ws.util.ASCIIUtility;
import cryptoutils.RSA;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Settings;
import static java.nio.file.StandardCopyOption.*;
import java.io.*;
import java.nio.file.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;


public class LokiController implements Serializable{
    private transient Stage stage;
    @FXML
    private transient Button cryptButton;
    @FXML
    private transient Button decryptButton;
    @FXML
    private transient Button settingsButton;
    @FXML
    private transient Button loadButton;
    @FXML
    private transient Button saveButton;
    @FXML
    private transient MenuItem importItem;
    @FXML
    private transient MenuItem exportItem;
    @FXML
    private transient TextArea contentsTextField;
    public void init(){}
    @FXML
    private void crypt() {
        try {

            // Check if the pair of keys are present else generate those.
            if (!RSA.areKeysPresent()) {
                // Method generates a pair of keys using the RSA algorithm and stores it
                // in their respective files
                RSA.generateKey();
            }

            final String originalText = contentsTextField.getText();
            ObjectInputStream inputStream = null;

            // Encrypt the string using the public key
            inputStream = new ObjectInputStream(new FileInputStream(Settings.PUBLIC_KEY_FILE));
            final PublicKey publicKey = (PublicKey) inputStream.readObject();
            final byte[] cipherText = RSA.encrypt(originalText, publicKey);
            String cipherTextString="";
            for(int b:cipherText){cipherTextString+=b+" ";}
            contentsTextField.setText(String.valueOf(cipherTextString));
            contentsTextField.setText(Arrays.toString(cipherText));
        } catch (Exception exc) {exc.printStackTrace();}
    }
    @FXML
    private void decrypt(){
        try{
            // Check if the pair of keys are present else generate those.
            if (!RSA.areKeysPresent()) {
                // Method generates a pair of keys using the RSA algorithm and stores it
                // in their respective files
                RSA.generateKey();
            }
            ObjectInputStream inputStream = null;
            // Decrypt the cipher text using the private key.
            inputStream = new ObjectInputStream(new FileInputStream(Settings.PRIVATE_KEY_FILE));
            final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
            final String plainText = RSA.decrypt(fromString(contentsTextField.getText()), privateKey);
            contentsTextField.setText(plainText);
        }catch (Exception exc) {exc.printStackTrace();}
    }

    @FXML
    private void save(){
        try {
            String m = contentsTextField.getText();
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("File LOKI", "*.loki");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(stage);
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(m);
            out.close();
            fileOut.close();
        }catch (Exception exc){}
    }

    @FXML
    private void load(){
        try {
            String m="";
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("File LOKI", "*.loki");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(stage);
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            m = (String) in.readObject();
            in.close();
            fileIn.close();
            contentsTextField.setText(m);
        }catch (Exception exc){}
    }

    @FXML
    private void importKey(){
        try {
        DirectoryChooser dirChooser = new DirectoryChooser();
        String sourceRoot= String.valueOf(dirChooser.showDialog(stage));
        File sourcePublic=new File(sourceRoot+"\\public.key");
        File sourcePrivate=new File(sourceRoot+"\\private.key");
        File targetPublic=new File(System.getProperty("user.dir")+"\\key\\public.key");
        File targetPrivate=new File(System.getProperty("user.dir")+"\\key\\private.key");
        Files.copy(sourcePrivate.toPath(), targetPrivate.toPath(),StandardCopyOption.REPLACE_EXISTING);
        Files.copy(sourcePublic.toPath(), targetPublic.toPath(),StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception exc){exc.printStackTrace();}
    }

    @FXML
    private void exportKey(){
        try {
            File sourcePublic=new File(System.getProperty("user.dir")+"\\key\\public.key");
            File sourcePrivate=new File(System.getProperty("user.dir")+"\\key\\private.key");
            DirectoryChooser dirChooser = new DirectoryChooser();
            String targetRoot= String.valueOf(dirChooser.showDialog(stage));
            File targetPublic=new File(targetRoot+"\\public.key");
            File targetPrivate=new File(targetRoot+"\\private.key");

            Files.copy(sourcePrivate.toPath(), targetPrivate.toPath(),StandardCopyOption.REPLACE_EXISTING);
            Files.copy(sourcePublic.toPath(), targetPublic.toPath(),StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception exc){exc.printStackTrace();}
    }

    private static byte[] fromString(String string) {
        String[] strings = string.replace("[", "").replace("]", "").split(", ");
        byte result[] = new byte[strings.length];
        for (int i = 0; i < strings.length; i++) {
            result[i] = Byte.parseByte(strings[i]);
        }
        return result;
    }

    /** Imposta lo {@code stage}.
     * @param stage  il nuovo {@code stage} */
    public void setStage(Stage stage) {this.stage = stage;}
}
