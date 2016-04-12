import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Amitai Sopher on 12/04/2016.
 */
public class ReadFile {

    private final String fileName;

    ReafFile (String fileName) {
        // The name of file to open.
        this.fileName = fileName;
    }
    public String readNextLine () {
        // This will reference one line at a time.
        String line = null;

        BufferedReader bufferedReader = null;

        try {
            // FileReader reads text files in the default encoding...
            FileReader fileReader = new FileReader(this.fileName);

            // Always wrap FileReader in BufferReader.
            bufferedReader = new BufferedReader(fileReader);

            if ((line = bufferedReader.readLine()) != null) {
                return line;
                break;
            } else if (bufferedReader != null) {
                bufferedReader.close();
                return null;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + this.fileName + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + this.fileName + "'");
        }
    }
}
