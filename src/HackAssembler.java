import java.io.*;
import java.util.*;

/**
 * Created by Amitai Sopher on 12/04/2016.
 */
public class HackAssembler {
    String inputFile;
    String tempFile;
    String outputFile;
    String[][] jumpTable;
    String[][] destTable;
    String[][] computationTableForA;
    String[][] computationTableForM;
    HashMap symbolTable = new HashMap<String,String>();

    public HackAssembler (String inputFileName) {
        this.inputFile = inputFileName;
        this.tempFile = "C:\\Users\\Amitai Sopher\\IdeaProjects\\HackAssembler\\src\\temporaryFile.txt";
        this.outputFile = "C:\\Users\\Amitai Sopher\\IdeaProjects\\HackAssembler\\src\\MachineCodeFile.txt";
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

        this.deleteTempfile(this.tempFile);
        this.deleteTempfile(this.outputFile);
    }

    public int numOfLinesInFile(String fileName) {
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

    private void deleteTempfile(String fileName) {
        try{

            File file = new File(fileName);

            if(file.delete()){
                System.out.println(file.getName() + " is deleted!");
            }else{
                System.out.println("Delete operation is failed.");
            }

        }catch(Exception e){

            e.printStackTrace();

        }
    }

    public String readLineNumber (int n, String fileName) {
        String line = "";
        if (n > this.numOfLinesInFile(this.inputFile)) {
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

    public void writeLineToFile (String line, String outputFileName) {
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
        boolean isLastLine = false;
        for (int i = 0; i < this.numOfLinesInFile(this.inputFile); i++) {
            String line = this.readLineNumber(i+1, this.inputFile);

            // Filtering empty lines and comments and writing only pure instructions to a temporary file.
            if (!line.startsWith("//") &&(line.trim().length() > 0)) {
                if (line.contains("//")) {
                    // Removing inline comments and spaces.
                    line = line.substring(0,line.indexOf("/"));
                    line = line.trim();
                }
                //System.out.println(line);
                this.writeLineToFile(line,this.tempFile);
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

    // This method takes a line of hack assembly language and convert it into Hack Machine Code using the object internal symbol tables after they were updated by the firstPass method.
    public void hackAssemblyParser () {
        boolean isAInstruction = false;
        boolean isCInstruction = false;
        int variableCounter = 16;
        String line;
        for (int i = 0; i < this.numOfLinesInFile(this.tempFile); i++) {
            // Reading a line from temporary file.
            line = readLineNumber(i + 1, tempFile);

            // If the line read is an A instruction than parse it and write to output file.
            variableCounter = parseAInstruction(variableCounter, line);
            // If the line read is an C instruction than parse it and write to output file.
            // Function to parse C instruction should go below.
            parseCInstruction(line);
        }
    }

    private void parseCInstruction(String line) {
        if (line.contains(";") && !line.contains("=")) {
            String computation = line.substring(0, line.indexOf(";"));
            String jumpToCommand = line.substring(line.indexOf(";") + 1, line.length());
            for (int j = 0; j < this.jumpTable[0].length; j++) {
                if (this.jumpTable[0][j].equals(jumpToCommand)) {
                    jumpToCommand = this.jumpTable[1][j];
                }
            }
            if (computation.contains("M")) {
                for (int j = 0; j < this.computationTableForM[0].length; j++) {
                    if (this.computationTableForM[0][j].equals(computation)) {
                        computation = this.computationTableForM[1][j];
                        computation = "0" + computation; // Adding the "A" bit value
                    }
                }
            } else if (!computation.contains("M")) {
                for (int j = 0; j < this.computationTableForA[0].length; j++) {
                    if (this.computationTableForA[0][j].equals(computation)) {
                        computation = this.computationTableForA[1][j];
                        computation = "1" + computation; // Adding the "A" bit value
                    }
                }
            }
            line = "111" + computation + "000" + jumpToCommand;
            writeLineToFile(line,this.outputFile);
        } else if (line.contains(";") && line.contains("=")) {
            String destination = line.substring(0, line.indexOf("="));
            String computation = line.substring(line.indexOf("=")+1, line.indexOf(";"));
            String jumptocommand = line.substring(line.indexOf(";") + 1, line.length());
            for (int j = 0; j < this.destTable[0].length; j++) {
                if (this.destTable[0][j].equals(destination)) {
                    destination = this.destTable[1][j];
                }
            }

            for (int j = 0; j < this.jumpTable[0].length; j++) {
                if (this.jumpTable[0][j].equals(jumptocommand)) {
                    jumptocommand = this.jumpTable[1][j];
                }
            }
            if (computation.contains("M")) {
                for (int j = 0; j < this.computationTableForM[0].length; j++) {
                    if (this.computationTableForM[0][j].equals(computation)) {
                        computation = this.computationTableForM[1][j];
                        computation = "0" + computation; // Adding the "A" bit value
                    }
                }
            } else if (!computation.contains("M")) {
                for (int j = 0; j < this.computationTableForA[0].length; j++) {
                    if (this.computationTableForA[0][j].equals(computation)) {
                        computation = this.computationTableForA[1][j];
                        computation = "1" + computation; // Adding the "A" bit value
                    }
                }
            }
            line = "111" + computation + destination + jumptocommand;
            writeLineToFile(line,this.outputFile);
        } else if (!line.contains(";") && line.contains("=")) { // Parsing in case there are only desination and computation.
            String destination = line.substring(0, line.indexOf("="));
            String computation = line.substring(line.indexOf("=")+1, line.length());
            for (int j = 0; j < this.destTable[0].length; j++) {
                if (this.destTable[0][j].equals(destination)) {
                    destination = this.destTable[1][j];
                }
            }
            if (computation.contains("M")) {
                for (int j = 0; j < this.computationTableForM[0].length; j++) {
                    if (this.computationTableForM[0][j].equals(computation)) {
                        computation = this.computationTableForM[1][j];
                        computation = "0" + computation; // Adding the "A" bit value
                    }
                }
            } else if (!computation.contains("M")) {
                for (int j = 0; j < this.computationTableForA[0].length; j++) {
                    if (this.computationTableForA[0][j].equals(computation)) {
                        computation = this.computationTableForA[1][j];
                        computation = "1" + computation; // Adding the "A" bit value
                    }
                }
            }
            line = "111" + computation + destination + "000";
            writeLineToFile(line, this.outputFile);
        }
    }

    private int parseAInstruction(int variableCounter, String line) {
        // Handling variables
        if (line.startsWith("@")) {
            line = line.substring(1,line.length());

            // If it is a new variable add it to symbol table, assign address to it and replace the variable name with address in the line.
            boolean isString = false;
            try {
                Integer.parseInt(line);
            } catch (java.lang.NumberFormatException ex) {
                isString = true;
            }
            if (isString) {
                if (!this.symbolTable.containsKey(line)) {
                    //System.out.println("Detected variable " + line + " and the current Variable Counter value is: " + variableCounter);
                    this.symbolTable.put(line, variableCounter);
                    line = convertAInstructionToBinaryCode(variableCounter);
                    writeLineToFile(line,this.outputFile);
                    variableCounter++;
                } else {
                    System.out.println(this.symbolTable.entrySet());
                    line = (String) this.symbolTable.get(line);
                    line = convertAInstructionToBinaryCode(Integer.parseInt(line));
                    writeLineToFile(line,this.outputFile);
                }
            } else {
                line = convertAInstructionToBinaryCode(Integer.parseInt(line));
                writeLineToFile(line,this.outputFile);
            }
        }

        // Check if this is a label
        if ((line.startsWith("(") && line.endsWith(")"))) {
            line = line.substring(1,line.length()-1);
            line = "@" + this.symbolTable.get(line);
            writeLineToFile(line,this.outputFile);
        }
        return variableCounter;
    }

    private String convertAInstructionToBinaryCode(int variableCounter) {
        String line;
        line = Integer.toBinaryString(variableCounter);
        int lineLength = 16 - line.length();
        if (lineLength < 16) {
            for (int j = 0; j < lineLength ; j++) {
                line = "0" + line;
            }
        }
        return line;
    }
}