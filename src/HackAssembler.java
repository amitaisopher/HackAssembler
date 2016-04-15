import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Amitai Sopher on 12/04/2016.
 */
public class HackAssembler {
    String inputFile;
    String[][] jumpTable;
    String[][] destTable;
    String[][] computationTableForA;
    String[][] computationTableForM;
    HashMap symbolTable = new HashMap<String,String>();

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
        this.symbolTable.put("R0","0");
        this.symbolTable.put("R1","1");
        this.symbolTable.put("R2","2");
        this.symbolTable.put("R3","3");
        this.symbolTable.put("R4","4");
        this.symbolTable.put("R5","5");
        this.symbolTable.put("R6","6");
        this.symbolTable.put("R7","7");
        this.symbolTable.put("R8","8");
        this.symbolTable.put("R9","9");
        this.symbolTable.put("R10","10");
        this.symbolTable.put("R11","11");
        this.symbolTable.put("R12","12");
        this.symbolTable.put("R13","13");
        this.symbolTable.put("R14","14");
        this.symbolTable.put("R15","15");
        this.symbolTable.put("SCREEN","16384");
        this.symbolTable.put("KBD","24576");

    }

    public int numOfLinesInInputFile () {
        String fileName = this.inputFile;
        String line = null;
        int n = 0;

        BufferedReader bufferedReader = null;
        try {
            // FileReader reads text files in the default encoding...
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferReader.
            bufferedReader = new BufferedReader(fileReader);

            line = bufferedReader.readLine();
            while (line != null) {
                n++;
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + this.inputFile + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + this.inputFile + "'");
        } finally {
            try {
                // Always close files.
                if (bufferedReader != null){
                    bufferedReader.close();
                }
            } catch (IOException e) {

            }
        }
        return n;
    }

    public String readLineNumber (int n) {
        String fileName = this.inputFile;
        String line = "";
        if (n > this.numOfLinesInInputFile()) {
            System.out.println("The input file contains less rows than the requested row number");
        } else {
            BufferedReader bufferedReader = null;
            try {
                // FileReader reads text files in the default encoding...
                FileReader fileReader = new FileReader(fileName);

                // Always wrap FileReader in BufferReader.
                bufferedReader = new BufferedReader(fileReader);
                for (int i = 0; i < n ; i++) {
                    line = bufferedReader.readLine();
                }
            } catch (FileNotFoundException ex) {
                System.out.println("Unable to open file '" + this.inputFile + "'");
            } catch (IOException ex) {
                System.out.println("Error reading file '" + this.inputFile + "'");
            } finally {
                try {
                    // Always close files.
                    if (bufferedReader != null){
                        bufferedReader.close();
                    }
                } catch (IOException e) {

                }
            }
        }

        return line;
    }

    public void symbolTableUpdater() {
        int realCommandCounter = 0;
        for (int i = 0; i < this.numOfLinesInInputFile(); i++) {
            String line = this.readLineNumber(i+1);
            if (!line.startsWith("//") && !(line.startsWith("(") && line.endsWith(")")) && (line.trim().length() > 0)) {
                realCommandCounter++;
            }
            if (line.startsWith("(") && line.endsWith(")")){
                line = line.substring(1,line.length()-1);
                System.out.println("The current line is " + line + " and the current realCommandCounter value is: " + realCommandCounter);
                this.symbolTable.put(line, realCommandCounter);
            }

        }
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