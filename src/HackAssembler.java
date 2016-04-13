import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.List;

/**
 * Created by Amitai Sopher on 12/04/2016.
 */
public class HackAssembler {
    String inputFile;
    String[][] jumpTable;
    String[][] destTable;
    String[][] computationTableForA;
    String[][] computationTableForM;
    List<String> symbolTable[];
    public HackAssembler (String inputFileName) {
        //this.inputFile = "shit.txt";
        this.inputFile = inputFileName;
        this.jumpTable = new String [][] {{"null","JGT","JEQ","JGE","JLT","JNE","JLE","JMP"},
                                          {"000","001","010","011","100","101","110","111",}};
        this.destTable = new String [][] {{"null","M","D","MD","A","AM","AD","AMD"},
                                          {"000","001","010","011","100","101","110","111",}};
        this.computationTableForA = new String [][] {{"0","1","-1","D","A","!D","!A","-D","-A","D+1","A+1","D-1","A-1","D+A","D-A","A-D","D&A","D|A"},
                                                     {"101010","111111","111010","001100","110000","001101","110001","001111","110011","011111","110111","001110","110010","000010","010011","000111","000000","010101"}};
        this.computationTableForM = new String [][] {{"0","1","-1","D","M","!D","!M","-D","-M","D+1","M+1","D-1","M-1","D+M","D-M","M-D","D&M","D|M"},
                                                     {"101010","111111","111010","001100","110000","001101","110001","001111","110011","011111","110111","001110","110010","000010","010011","000111","000000","010101"}};
        this.symbolTable[0] = new ArrayList<String>(Arrays.asList("R0","R1","R2","R3","R4","R5","R6","R7","R8","R9","R10","R11","R12","R13","R14","R15"));
        this.symbolTable[1] = new ArrayList<String>(Arrays.asList("0","1","2","4","5","6","7","8","9","10","11","12","13","14","15"));
    }

    /*public String readNextLine() {
        // This will reference one line at a time.
        //String line = null;

        BufferedReader bufferedReader = null;
        String line = null;
        try {
            // FileReader reads text files in the default encoding...
            FileReader fileReader = new FileReader(this.inputFile);

            // Always wrap FileReader in BufferReader.
            bufferedReader = new BufferedReader(fileReader);
            line = bufferedReader.readLine();


        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + this.inputFile + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + this.inputFile + "'");
        }
        return line; */
}