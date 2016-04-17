/**
 * Created by sophea on 13/04/2016.
 */
public class test {
    public static void main(String[] args) {
        HackAssembler assembler = new HackAssembler("C:\\Users\\Amitai Sopher\\IdeaProjects\\HackAssembler\\src\\shit.txt");
        assembler.firstPass();
        //System.out.println(assembler.symbolTable.entrySet());
        //System.out.println(assembler.readLineNumber(2));
        //System.out.println("The number of lines that in input file are " + assembler.numOfLinesInFile());
        assembler.hackAssemblyParser();
       
    }
}
