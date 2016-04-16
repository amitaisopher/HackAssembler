import java.io.*;
import java.util.*;

/**
 * Created by Amitai Sopher on 12/04/2016.
 */
public class HackAssembler {
    String inputFile;
    String tempFile;
    String[][] jumpTable;
    String[][] destTable;
    String[][] computationTableForA;
    String[][] computationTableForM;
    HashMap symbolTable = new HashMap<String,String>();

    public HackAssembler (String inputFileName) {
        //this.inputFile = "shit.txt";
        this.inputFile = inputFileName;
        this.tempFile = "C:\\Users\\Amitai Sopher\\IdeaProjects\\HackAssembler\\src\\temporaryFile.txt";
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
        this.symbolTable.put("SP","0");
        this.symbolTable.put("LCL","1");
        this.symbolTable.put("ARG","2");
        this.symbolTable.put("THIS","3");
        this.symbolTable.put("THAT","4");

        this.deleteTempfile();
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

    private void deleteTempfile() {
        try{

            File file = new File(this.tempFile);

            if(file.delete()){
                System.out.println(file.getName() + " is deleted!");
            }else{
                System.out.println("Delete operation is failed.");
            }

        }catch(Exception e){

            e.printStackTrace();

        }
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

    public void writeLineToFile (String line) {
        String outputFileName = "C:\\Users\\Amitai Sopher\\IdeaProjects\\HackAssembler\\src\\temporaryFile.txt";
        BufferedWriter bufferedWriter = null;

        try {
            // Assume default encoding and configuring FileWriter to append lines to output file.
            FileWriter fileWriter = new FileWriter(outputFileName, true);

            // Always wrap FileWriter in BufferedWriter.
            bufferedWriter = new BufferedWriter(fileWriter);

            // Note that write() does not automatically
            // append a new line character.
            bufferedWriter.write(line);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException ex) {
            System.out.println("Error writing to file '" + outputFileName + "'");
        } finally {
            // Always close files.
            try {
                if (bufferedWriter !=null ){
                    bufferedWriter.close();
                }
            } catch (IOException e) {

            }
        }
    }

    public void firstPass() {
        int realCommandCounter = 0;
        int variableCounter = 16;
        boolean isLastLine = false;
        for (int i = 0; i < this.numOfLinesInInputFile(); i++) {
            String line = this.readLineNumber(i+1);

            // Filtering empty lines and comments and writing only pure instructions to a temporary file.
            if (!line.startsWith("//") &&(line.trim().length() > 0)) {
                if (line.contains("//")) {
                    // Removing inline comments and spaces.
                    line = line.substring(0,line.indexOf("/"));
                    line = line.trim();
                }
                //System.out.println(line);
                this.writeLineToFile(line);
            }

            if (!line.startsWith("//") && !(line.startsWith("(") && line.endsWith(")")) && (line.trim().length() > 0)) {
                realCommandCounter++;
            }
            // Looking for new lables.
            if (line.startsWith("(") && line.endsWith(")")){
                line = line.substring(1,line.length()-1);

                // Checking if lable already exist in Symbol Table - if this is the first time we see this symbol then add it - otherwise ignore it.
                if (!this.symbolTable.containsKey(line)) {
                    //System.out.println("The current line is " + line + " and the current realCommandCounter value is: " + realCommandCounter);
                    this.symbolTable.put(line, realCommandCounter);
                }
            }
            // Looking for new variables.
            /*if (line.startsWith("@")) {
                line = line.substring(1,line.length());
                if (!this.symbolTable.containsKey(line)) {
                    System.out.println("Detected variable " + line + " and the current Variable Counter value is: " + variableCounter);
                    this.symbolTable.put(line, variableCounter);
                    variableCounter++;
                }
            }*/

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