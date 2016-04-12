import java.util.Dictionary;

/**
 * Created by Amitai Sopher on 12/04/2016.
 */
public class HackAssembler {
    ReadFile inputFile;
    String[][] jumpTable //{{"null","null","null","null","null","null","null","null"},
            //{"null","null","null","null","null","null","null","null",}};
    String[][] destTable
    String[][] computationTableForA
    String[][] computationTableForM
    public HackAssembler (String inputFileName) {
        this.inputFile = new ReadFile(inputFileName);
        this.jumpTable = new String [][] {{"null","JGT","JEQ","JGE","JLT","JNE","JLE","JMP"},
                            {"000","001","010","011","100","101","110","111",}};
        this.destTable = new String [][] {{"null","M","D","MD","A","AM","AD","AMD"},
                            {"000","001","010","011","100","101","110","111",}};
        this.computationTableForA = new String [][] {{"0","1","-1","D","A","!D","!A","-D","-A","D+1","A+1","D-1","A-1","D+A","D-A","A-D","D&A","D|A"},
                                    {"101010","111111","111010","001100","110000","001101","110001","001111","110011","011111","110111","001110","110010","000010","010011","000111","000000","010101"}};
        this.computationTableForM = new String [][] {{"0","1","-1","D","M","!D","!M","-D","-M","D+1","M+1","D-1","M-1","D+M","D-M","M-D","D&M","D|M"},
                {"101010","111111","111010","001100","110000","001101","110001","001111","110011","011111","110111","001110","110010","000010","010011","000111","000000","010101"}};

    }
}